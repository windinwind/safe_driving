package com.lazyDroid.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnector {
	private final Properties sqlProperties;
	private final String URI;
	private final static String USERNAME = "root";
	private final static String PASSWORD = "safedriving";
	private final static String HOSTNAME = "127.0.0.1";
	private final static String DATABASE = "user";

	/**
	 * Constructor of SQLConnector. Create a connector for the safe driving
	 * database.
	 */
	public SQLConnector() {
		// Setup the necessary parameters
		sqlProperties = new Properties();
		sqlProperties.put("user", USERNAME);
		sqlProperties.put("password", PASSWORD);
		URI = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE;
	}

	/**
	 * Get the connection to the safe driving database.
	 * 
	 * @return The connection to the safe driving database.
	 * @throws SQLException
	 *             When failed to access the database.
	 */
	public Connection getDBConnection() throws SQLException {
		Connection c = DriverManager.getConnection(URI, sqlProperties);
		c.setAutoCommit(true);
		return c;
	}
}
