package com.lazyDroid.jetty;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet{
	/**
	 * The constructor of loginServlet.
	 */
	UserServlet() {
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
		response.getWriter().write("User Servlet");
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
