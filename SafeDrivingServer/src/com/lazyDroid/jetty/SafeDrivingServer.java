package com.lazyDroid.jetty;

import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.lazyDroid.sql.SQLConnector;

/**
 * The server class for safe driving project. This class contains the main
 * function for the server.
 * 
 * @author lazyDroid
 */
public class SafeDrivingServer {
	final static int PORT_NUMBER = 80;

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
		SQLConnector connector = new SQLConnector();
		Connection dbConnection = connector.getDBConnection();

		// Setup the servlet handler
		ServletHandler handler = new ServletHandler();
		// Add all the necessary servlets to the handler
		handler.addServletWithMapping(new ServletHolder(new LoginServlet(dbConnection)), "/login");
		handler.addServletWithMapping(new ServletHolder(new RegisterServlet(dbConnection)), "/register");
		handler.addServletWithMapping(new ServletHolder(new UserServlet(dbConnection)), "/user");
		handler.addServletWithMapping(new ServletHolder(new ShopServlet()), "/shop");

		// Run the server
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
