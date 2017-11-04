package com.lazyDroid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	private String username = "lazykyrin";
	private String userPass123456 = "MWxhenlreXJpbjYxMjM0NTZsYXp5RHJvaWQ=";
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		dbConnection = connector.getDBConnection();
	}
	
	@Test
	public void testNormalAuthentication() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		parsedReq.put("username", username);
		parsedReq.put("password", userPass123456);
		
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertNotNull(result);
		assertEquals("lazykyrin", result.get("username"));
		assertEquals("98", result.get("safepoint"));
	}
	
	@Test
	public void authenticationWithOtherRequests() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		parsedReq.put("username", username);
		parsedReq.put("password", userPass123456);
		parsedReq.put("action", "aodlfhbui");
		parsedReq.put("lazy", "Droid");
		parsedReq.put("save", "driving");
		parsedReq.put("old", "driver");
		
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertNotNull(result);
		assertEquals("lazykyrin", result.get("username"));
		assertEquals("98", result.get("safepoint"));
	}
	
	@Test
	public void testEmptyRequest() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertEquals(null, result);
	}
	
	@Test
	public void testInvalidRequest() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		
		parsedReq.put("boawekd", "aodlfhbui");
		parsedReq.put("lazy", "Droid");
		parsedReq.put("save", "driving");
		parsedReq.put("old", "driver");
		
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertEquals(null, result);
	}
	
	@Test
	public void testInvalidPassword() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		parsedReq.put("username", "lazykyrin");
		parsedReq.put("password", "123456");
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertEquals(null, result);
	}
	
	@Test
	public void testInvalidUsername() throws SQLException {
		Map<String, String> parsedReq = new HashMap<String, String>();
		parsedReq.put("username", "la2ykyrin");
		parsedReq.put("password", userPass123456);
		Map<String, String> result = SafeDrivingUtils.userAuthentication(parsedReq, dbConnection);
		assertEquals(null, result);
	}
	
	@Test
	public void testNullRequest() throws SQLException {
		Map<String, String> result = SafeDrivingUtils.userAuthentication(null, dbConnection);
		assertEquals(null, result);
	}
}
