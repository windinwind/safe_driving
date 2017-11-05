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

import com.lazyDroid.jetty.UserServlet;
import com.lazyDroid.sql.SQLConnector;

public class TestUserServlet {
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	private UserServletForTesting userServlet;
	private String username = "lazykyrin";
	private String userPass123456 = "MWxhenlreXJpbjYxMjM0NTZsYXp5RHJvaWQ=";
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		userServlet = new UserServletForTesting(connector.getDBConnection());
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void normalPostRequest() throws IOException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		String responseContent = userServletDoGetHelper(postReq);
		assertEquals("status:success", responseContent);
	}
	
	private String userServletDoGetHelper(String requestContent) throws IOException {
		ServerTestUtils.generateRequestInputStream(requestContent, request);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doGet(request, response);
		
		return outstream.toString();
	}
	
	private class UserServletForTesting extends UserServlet {
		private static final long serialVersionUID = -1334124236529885789L;

		protected UserServletForTesting(Connection dbConnection) {
			super(dbConnection);
		}
		
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			super.doGet(request, response);
		}
		
		public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
			super.doPut(request, response);
		}
	}
}
