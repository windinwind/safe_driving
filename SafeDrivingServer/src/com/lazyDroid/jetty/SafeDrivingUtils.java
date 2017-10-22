package com.lazyDroid.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for safe driving server.
 * 
 * @author Kyrin Feng
 *
 */
public class SafeDrivingUtils {
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

		String thisLine = br.readLine();
		while (thisLine != null) {
			String[] requestLine = parseRequestLine(thisLine);

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
	 * @param users
	 *            - The database that contains all user information
	 * 
	 * @return The authenticated user's information stored in the database. Return
	 *         null if the user is unauthorized or the user authentication
	 *         information cannot be found in the parsed request.
	 */
	public static User userAuthentication(Map<String, String> parsedRequest, Map<String, User> users) {
		// Check if the user authentication information exists in the request.
		String username = parsedRequest.get("username");
		String password = parsedRequest.get("password");

		if (username == null || password == null) {
			System.out.println("username or password is null");
			return null;
		}

		User targetUser = users.get(username);

		// Check if the user exists in the database
		if (targetUser == null) {
			System.out.println("targetUser is null");
			return null;
		}

		// Check whether the password in the request matches the user name.
		if (!BCrypt.checkpw(password, targetUser.hashedPW)) {
			System.out.println("incorrect password");
			return null;
		}

		return targetUser;
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
}
