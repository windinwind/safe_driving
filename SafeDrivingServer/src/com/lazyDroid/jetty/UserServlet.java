package com.lazyDroid.jetty;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet{
	Map<String, User> users;
	/**
	 * The constructor of UserServlet.
	 */
	UserServlet(Map<String, User> users) {
		this.users = users;
		// TODO may add more things here
	}
	
	/**
	 * Handle the HTTP PUT method.
	 * 
	 * @param request - The HTTP request from a client.
	 * 
	 * @param response - The HTTP response that will be send to the client.
	 * 
	 * @throws IOException When a servlet IO exception occurs.
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		// TODO needs more things here
		Map<String, String> parsedRequest = SafeDrivingUtils.parseRequest(request);
		
		User targetUser = SafeDrivingUtils.userAuthentication(parsedRequest, users);
		if (targetUser == null) {
			invalidUserResponse(response);
			return;
		}
		
		String action = parsedRequest.get("update");
		if (action == null) {
			invalidActionResponse(response);
			return;
		}
		
		int newSafePoint = Integer.parseInt(parsedRequest.get("update"));
		targetUser.updateSafePoint(newSafePoint);
		
		response.getWriter().write("status:success");
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String username = request.getParameter("username");
		String password = request.getHeader("Authorization");
		
		if (username == null || password == null) {
			invalidUserResponse(response);
			return;
		}
		
		User targetUser = users.get(username);
		if (targetUser == null) {
			invalidUserResponse(response);
			return;
		}
		
		System.out.println(username + " is getting point");
		response.getWriter().write("point:" + targetUser.getSafePoint());
		System.out.println("user servlet response: point:" + targetUser.getSafePoint());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	private void invalidUserResponse(HttpServletResponse response) throws IOException {
		response.getWriter().write("status:fail");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	private void invalidActionResponse(HttpServletResponse response) throws IOException {
		response.getWriter().write("status:fail");
		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}
}
