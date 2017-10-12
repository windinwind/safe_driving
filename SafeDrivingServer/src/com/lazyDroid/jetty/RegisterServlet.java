package com.lazyDroid.jetty;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet{
	/**
	 * The constructor of RegisterServlet.
	 */
	static Map<String, User> users;
	RegisterServlet() {
		// TODO may add more things here
	}
	
	/**
	 * Handle the HTTP POST method.
	 * 
	 * @param request - The HTTP request from a client.
	 * 
	 * @param response - The HTTP response that will be send to the client.
	 * 
	 * @throws IOException When a servlet IO exception occurs.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		Map<String, String> parsedRequest = SafeDrivingUtils.parseRequest(request);
		
		if (parsedRequest == null || parsedRequest.size() != 2) {
			registerFail(response);
			return;
		}
		
		String username = parsedRequest.get("username");
		String password = parsedRequest.get("password");
		
		if (username == null || password == null) {
			registerFail(response);
			return;
		}
		
		System.out.println("User Registration");
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		
		if (users.containsKey(username)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			response.getWriter().write("status:already_exists");
			return;
		}
		
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
		response.getWriter().write("<html><h1>This is Register Servlet.</h1></html>");
	}
	
	private void registerFail(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		response.getWriter().write("status:error");
	}
}
