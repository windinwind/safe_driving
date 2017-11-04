package com.lazyDroid.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.lazyDroid.jetty.SafeDrivingUtils;
import com.lazyDroid.sql.SQLConnector;

public class AuthenticationUnitTest {
	private Connection dbConnection;
	private String username = "kyrin";
	private String userPass123456 = "MWxhenlreXJpbjYxMjM0NTZsYXp5RHJvaWQ=";
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		dbConnection = connector.getDBConnection();
	}
	
	@Test
	public void normalAuthentication() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		parsedReq.put("username", username);
		parsedReq.put("password", userPass123456);
		
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertEquals("kyrin", result.get("username"));
		assertEquals(100, result.get("safepoint"));
	}
}
