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
