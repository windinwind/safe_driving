package com.lazyDroid.jetty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	private Connection dbConnection;

	/**
	 * The constructor of RegisterServlet.
	 * 
	 * @param users
	 *            - The database for safe driving project.
	 */
	RegisterServlet(Connection dbConnection) {
		// TODO may add more things here
		this.dbConnection = dbConnection;
	}

	/**
	 * Handle the HTTP POST method regarding user registration events.
	 * 
	 * @param request
	 *            - The HTTP request from a client.
	 * 
	 * @param response
	 *            - The HTTP response that will be send to the client.
	 * 
	 * @throws IOException
	 *             When a servlet IO exception occurs.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> parsedRequest = SafeDrivingUtils.parseRequest(request);

		String username = parsedRequest.get("username");
		String password = parsedRequest.get("password");

		if (username == null || password == null) {
			// Bad request
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		// For debug
		System.out.println("User Registration");
		System.out.println("username: " + username);
		System.out.println("password: " + password);

		try {
			Map<String, String> userInfo = SafeDrivingUtils.getUserInfo(username, dbConnection);

			if (userInfo != null) {
				// User already exists
				SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}

			if (!registerUser(username, BCrypt.hashpw(password, BCrypt.gensalt()), dbConnection)) {
				// Insertion failed
				SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Registration succeeded
		response.getWriter().write("status:success");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Store the user information to the database.
	 * 
	 * @param username
	 *            - The user name of the target user.
	 * @param hashedPW
	 *            - The hashed password of the target user.
	 * @param dbConnection
	 *            - The connection to the database.
	 * @return True when the insertion is successful, false otherwise.
	 * @throws SQLException
	 *             When doing operations on the database, an error occurs.
	 */
	private static boolean registerUser(String username, String hashedPW, Connection dbConnection) throws SQLException {
		// Setup the SQL query for inserting new user information to the database
		String sqlQuery = "INSERT INTO user.user_info VALUES(?, ?, ?);";
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);
		statement.setString(1, username);
		statement.setString(2, hashedPW);
		statement.setInt(3, SafeDrivingUtils.DEFAULT_SAFE_POINT);

		// Execute the query
		return statement.execute();
	}

	/**
	 * For testing connection.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("<html><h1>This is the Register Servlet.</h1></html>");
	}
}
