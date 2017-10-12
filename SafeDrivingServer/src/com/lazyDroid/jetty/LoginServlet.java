package com.lazyDroid.jetty;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{
	Map<String, User> users;
	/**
	 * The constructor of LoginServlet.
	 */
	LoginServlet(Map<String, User> users) {
		// TODO may add more things here
		this.users = users;
	}
	
	/**
	 * Handle the HTTP GET method.
	 * 
	 * @param request - The HTTP request from a client.
	 * 
	 * @param response - The HTTP response that will be send to the client.
	 * 
	 * @throws IOException When a servlet IO exception occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		// TODO needs more things here
		Map<String, String> parsedRequest = SafeDrivingUtils.parseRequest(request);
		
		if (parsedRequest == null || parsedRequest.size() != 2) {
			loginFail(response);
			return;
		}
		
		String username = parsedRequest.get("username");
		String password = parsedRequest.get("password");
		
		if (username == null || password == null) {
			loginFail(response);
			return;
		}
		
		if (BCrypt.checkpw(password, users.get(username).hashedPW)) {
			response.getWriter().write("status:success");
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			loginFail(response);
		}
	}
	
	private void loginFail(HttpServletResponse response) throws IOException {
		response.getWriter().write("status:fail");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
