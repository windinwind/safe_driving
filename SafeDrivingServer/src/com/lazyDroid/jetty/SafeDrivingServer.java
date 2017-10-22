package com.lazyDroid.jetty;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * The server class for safe driving project. This class contains the main
 * function for the server.
 * 
 * @author lazyDroid
 */
public class SafeDrivingServer {
	final static int PORT_NUMBER = 80;
	private static Map<String, User> users;

	/**
	 * The main function of the server. Run this function to run the server.
	 * 
	 * @param args
	 *            - Not used
	 * @throws Exception
	 *             When a Jetty server exception occurs.
	 */
	public static void main(String[] args) throws Exception {
		// Create the server
		Server server = new Server(PORT_NUMBER);
		users = new HashMap<String, User>();

		// Setup the servlet handler
		ServletHandler handler = new ServletHandler();
		// Add all the necessary servlets to the handler
		handler.addServletWithMapping(new ServletHolder(new LoginServlet(users)), "/login");
		handler.addServletWithMapping(new ServletHolder(new RegisterServlet(users)), "/register");
		handler.addServletWithMapping(new ServletHolder(new UserServlet(users)), "/user");
		handler.addServletWithMapping(new ServletHolder(new ShopServlet()), "/shop");

		// Run the server
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
