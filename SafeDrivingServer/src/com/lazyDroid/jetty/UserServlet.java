package com.lazyDroid.jetty;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	Map<String, User> users;

	/**
	 * The constructor of UserServlet.
	 */
	UserServlet(Map<String, User> users) {
		this.users = users;
		// TODO may add more things here
	}

	/**
	 * Handle the HTTP PUT method regarding safe point operations.
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
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO needs more things here
		Map<String, String> parsedRequest = SafeDrivingUtils.parseRequest(request);

		User targetUser = SafeDrivingUtils.userAuthentication(parsedRequest, users);
		if (targetUser == null) {
			// Incorrect user name or password
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Check if the operation is valid
		String action = parsedRequest.get("update");
		if (action == null) {
			// Invalid operation
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		// Update the safe point for this user
		int newSafePoint = Integer.parseInt(parsedRequest.get("update"));
		targetUser.updateSafePoint(newSafePoint);

		response.getWriter().write("status:success");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Handle the HTTP GET method regarding safe point operations.
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Check if the request contains the user authentication information
		String username = request.getParameter("username");
		String password = request.getHeader("Authorization");

		if (username == null || password == null) {
			// User authentication information not found
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		User targetUser = users.get(username);
		if (targetUser == null) {
			// User not found
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} else if (!BCrypt.checkpw(password, targetUser.hashedPW)) {
			// Invalid password
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		// Send the safe point to the user
		response.getWriter().write("point:" + targetUser.getSafePoint());
		response.setStatus(HttpServletResponse.SC_OK);
		// For debug
		System.out.println(username + " is getting point");
		System.out.println("user servlet response: point:" + targetUser.getSafePoint());
	}
}
