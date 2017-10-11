package com.lazyDroid.jetty;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{
	static Map<String, User> users;
	/**
	 * The constructor of LoginServlet.
	 */
	LoginServlet() {
		// TODO may add more things here
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
		
		if (parsedRequest == null) {
			response.getWriter().write("status:fail");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		response.getWriter().write("status:success");
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
