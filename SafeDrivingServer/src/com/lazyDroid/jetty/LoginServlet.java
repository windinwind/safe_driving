package com.lazyDroid.jetty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	private Connection dbConnection;

	/**
	 * The constructor of LoginServlet.
	 * 
	 * @param users
	 *            - The database for safe driving project.
	 */
	public LoginServlet(Connection dbConnection) {
		// TODO may add more things here
		this.dbConnection = dbConnection;
	}

	/**
	 * Handle the HTTP POST method regarding user login events.
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
		System.out.println("Parsed Request:");
		System.out.println(parsedRequest);

		Map<String, String> targetUser;

		try {
			targetUser = SafeDrivingUtils.userAuthentication(parsedRequest, dbConnection);

			if (targetUser == null) {
				// Login failed
				SafeDrivingUtils.responseToBadRequest(response, HttpServletResponse.SC_UNAUTHORIZED);
				return;
			} else {
				// Login succeeded
				response.getWriter().write("status:success");
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For testing connection
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Test the network connection
		response.getWriter().write("<html><h1>This is the Login Servlet.</h1></html>");
	}
}
