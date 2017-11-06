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

import com.lazyDroid.jetty.LoginServlet;
import com.lazyDroid.sql.SQLConnector;

public class TestLoginServlet {
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	private LoginServletForTesting loginServlet;
	private String username = "lazykyrin";
	private String userPass123456 = "MWxhenlreXJpbjYxMjM0NTZsYXp5RHJvaWQ=";
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		loginServlet = new LoginServletForTesting(connector.getDBConnection());
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void normalPostRequest() throws IOException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:success", responseContent);
	}
	
	@Test
	public void requestWithOtherActions() throws IOException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		postReq = postReq + "\nother:actions" + "\nlazy:droid";
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:success", responseContent);
	}
	
	@Test
	public void requestWithEmptyContent() throws IOException {
		String postReq = "";
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithoutUsername() throws IOException {
		String postReq = "password:" + userPass123456;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithoutPassword() throws IOException {
		String postReq = "username:" + username;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidContent() throws IOException {
		String postReq = "username:" + username + "\npassword::" + userPass123456;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidUsername() throws IOException {
		String postReq = "username:" + "aefbsd\n" + "password:" + userPass123456;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidPassword() throws IOException {
		String postReq = "username:" + username + "\npassword:" + userPass123456 + "abc";
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	@Test
	public void requestWithInvalidActions() throws IOException {
		String postReq = "usernam:" + username + "\npasword:" + userPass123456;
		String responseContent = loginServletDoPostHelper(postReq);
		assertEquals("status:fail", responseContent);
	}
	
	private String loginServletDoPostHelper(String requestContent) throws IOException {
		ServerTestUtils.generateRequestInputStream(requestContent, request);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		loginServlet.doPost(request, response);
		
		return outstream.toString();
	}
	
	private class LoginServletForTesting extends LoginServlet {
		private static final long serialVersionUID = -6087043763837917178L;

		public LoginServletForTesting(Connection dbConnection) {
			super(dbConnection);
		}
		
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
			super.doPost(request, response);
		}
	}
}
