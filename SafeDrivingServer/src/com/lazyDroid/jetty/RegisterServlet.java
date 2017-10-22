package com.lazyDroid.jetty;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	Map<String, User> users;

	/**
	 * The constructor of RegisterServlet.
	 * 
	 * @param users
	 *            - The database for safe driving project.
	 */
	RegisterServlet(Map<String, User> users) {
		// TODO may add more things here
		this.users = users;
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

		if (users.containsKey(username)) {
			// User already exists
			SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		// Registration succeeded
		users.put(username, new User(username, BCrypt.hashpw(password, BCrypt.gensalt())));
		response.getWriter().write("status:success");
		response.setStatus(HttpServletResponse.SC_OK);
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
