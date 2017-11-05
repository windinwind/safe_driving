package com.lazyDroid.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lazyDroid.jetty.RegisterServlet;
import com.lazyDroid.sql.SQLConnector;

public class TestRegistrationServlet {
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	private RegisterServletForTesting registerServlet;
	private String username = "urararararara";
	private String userPass123456 = "passsssssssss";
	Connection dbConnection;
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		dbConnection = connector.getDBConnection();
		registerServlet = new RegisterServletForTesting(dbConnection);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void normalPostRequest() throws IOException, SQLException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:success", responseContent);
	}
	
	@Test
	public void requestWithOtherActions() throws IOException, SQLException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		postReq = postReq + "\nother:actions" + "\nlazy:droid";
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:success", responseContent);
	}
	
	@Test
	public void requestWithEmptyContent() throws IOException, SQLException {
		String postReq = "";
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithoutUsername() throws IOException, SQLException {
		String postReq = "password:" + userPass123456;
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithoutPassword() throws IOException, SQLException {
		String postReq = "username:" + username;
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidContent() throws IOException, SQLException {
		String postReq = "username:" + username + "\npassword::" + userPass123456;
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithExistingUsername() throws IOException, SQLException {
		String postReq = "username:" + username + "password:" + userPass123456;
		registerServletDoPostHelper(postReq);
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidActions() throws IOException, SQLException {
		String postReq = "usernam:" + username + "\npasword:" + userPass123456;
		String responseContent = registerServletDoPostHelper(postReq);
		ServerTestUtils.deleteUserFromDatabase(dbConnection, username);
		assertEquals("status:fail", responseContent);
	}
	
	private String registerServletDoPostHelper(String requestContent) throws IOException {
		ServerTestUtils.generateRequestInputStream(requestContent, request);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		registerServlet.doPost(request, response);
		
		return outstream.toString();
	}
	
	private class RegisterServletForTesting extends RegisterServlet {
		private static final long serialVersionUID = -826543815654757197L;

		RegisterServletForTesting(Connection dbConnection) {
			super(dbConnection);
		}
	
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
			super.doPost(request, response);
		}
	}
}
