package com.lazyDroid.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.lazyDroid.sql.SQLConnector;

/**
 * Utility class for safe driving server.
 * 
 * @author Kyrin Feng
 *
 */
public class SafeDrivingUtils {
	public static final int DEFAULT_SAFE_POINT = 100;

	/**
	 * Parse the content of the HTTP request based on the simple protocol
	 * ("tag:content").
	 * 
	 * @param request
	 *            - An HTTP servlet request that contains the content for parsing.
	 * 
	 * @return The map which contains the parsed request. The keys of this map
	 *         correspond to the tags, and the values of this map correspond to the
	 *         contents. Return null if the format of the request is not valid.
	 * 
	 * @throws IOException
	 *             when the request is null or an error occurs when reading the
	 *             request content.
	 */
	public static Map<String, String> parseRequest(HttpServletRequest request) throws IOException {
		// Get the reader for reading the request contents
		BufferedReader br = getRequestReader(request);

		Map<String, String> result = new HashMap<String, String>();

		// Parse each line of the request contents
		String thisLine = br.readLine();
		while (thisLine != null) {
			String[] requestLine = parseRequestLine(thisLine);

			if (requestLine == null) return null; // Format checking
			
			if ("".equals(requestLine[0]) || "".equals(requestLine[1]))
				return null; // Content and Key cannot be empty
			
			if (result.containsKey(requestLine[0])) {
				return null; // Duplication checking
			}

			result.put(requestLine[0], requestLine[1]);
			thisLine = br.readLine();
		}

		return result;
	}

	/**
	 * Check whether a specific line of the request content is following the "simple
	 * protocol"
	 * 
	 * @param requestLine
	 *            - The target line of the request content
	 * @return Splitted string based on the "simple protocol".
	 */
	private static String[] parseRequestLine(String requestLine) {
		if (requestLine == null)
			return null;

		String[] result = requestLine.split(":");
		if (result.length != 2)
			return null;

		return result;
	}

	/**
	 * Helper method that creates a reader for reading the request content.
	 * 
	 * @param request
	 *            - An HTTP request.
	 * @return A reader for reading the request content.
	 * @throws IOException
	 *             The HTTP request is null or failed to create the reader.
	 */
	private static BufferedReader getRequestReader(HttpServletRequest request) throws IOException {
		if (request == null)
			throw new IOException();

		return new BufferedReader(new InputStreamReader(request.getInputStream()));
	}

	/**
	 * Authenticate the user information based on the input parsed request.
	 * 
	 * @param parsedRequest
	 *            - Parsed HTTP servlet request.
	 * @param dbConnection
	 *            - Connection to the database that contains all user information
	 * 
	 * @return The authenticated user's information stored in the database. Return
	 *         null if the user is unauthorized or the user authentication
	 *         information cannot be found in the parsed request.
	 * @throws SQLException
	 */
	public static Map<String, String> userAuthentication(Map<String, String> parsedRequest, Connection dbConnection)
			throws SQLException {
		// Check if the user authentication information exists in the request.
		String username = parsedRequest.get("username");
		String password = parsedRequest.get("password");

		if (username == null || password == null) {
			System.out.println("username or password is null");
			return null;
		}

		Map<String, String> targetUserInfo = getUserInfo(username, dbConnection);

		// Check if the user exists in the database
		if (targetUserInfo == null) {
			System.out.println("targetUserPass is null");
			return null;
		}

		// Check whether the password in the request matches the user name.
		if (!BCrypt.checkpw(password, targetUserInfo.get("userpass"))) {
			System.out.println("incorrect password");
			return null;
		}

		return targetUserInfo;
	}

	/**
	 * Obtain the target user information from the database.
	 * 
	 * @param username
	 *            - The user name of the target user.
	 * @param dbConnection
	 *            - The connection to the database.
	 * @return The map that contains all user information of the target user. Return
	 *         null if the user is not found.
	 * @throws SQLException
	 *             An error occurs when doing SQL operations.
	 */
	static Map<String, String> getUserInfo(String username, Connection dbConnection) throws SQLException {
		// Prepare SQL query
		String sqlQuery = "SELECT * FROM user.user_info WHERE user_info.username = ?";
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);
		statement.setString(1, username);

		// Check connection to database
		if (dbConnection.isClosed()) {
			dbConnection = new SQLConnector().getDBConnection();
		}

		ResultSet results = statement.executeQuery();
		ResultSetMetaData metadata = results.getMetaData();
		int numCols = metadata.getColumnCount();

		Map<String, String> retVal = new HashMap<String, String>();
		if (!results.next()) return null; // Result is empty

		// Put user information in the map
		for (int i = 1; i <= numCols; i++) {
			String tag = metadata.getColumnName(i);
			String value = results.getString(tag);

			retVal.put(tag, value);
		}
		return retVal;
	}

	/**
	 * Set the HTTP servlet response for a bad HTTP servlet request.
	 * 
	 * @param response
	 *            - The HTTP servlet response. The content of the response will be
	 *            set to indicate the request is bad.
	 * @param statusCode
	 *            - The HTTP status code that corresponds to the response.
	 * 
	 * @throws IOException
	 *             When the input response is null, or an error occurs when writing
	 *             the response content.
	 */
	static void responseToBadRequest(HttpServletResponse response, int statusCode) throws IOException {
		if (response == null)
			throw new IOException();

		// Indicate the request is bad
		response.getWriter().write("status:fail");
		response.setStatus(statusCode);
	}

	/**
	 * Update the safe point for the target user.
	 * 
	 * @param username
	 *            - The user name of the target user
	 * @param newPoint
	 *            - The updated safe point
	 * @param dbConnection
	 *            - The connection to the database
	 * @return True if the safe point is successfully updated, and false otherwise.
	 * @throws SQLException
	 *             If an error occurs when accessing the database.
	 */
	static boolean updateSafePoint(String username, int newPoint, Connection dbConnection) throws SQLException {
		// Setup SQL query
		String sqlQuery = "UPDATE user.user_info SET safepoint = ? WHERE username = ?";
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);
		statement.setInt(1, newPoint);
		statement.setString(2, username);

		// Execute the query
		return statement.execute();
	}
}
