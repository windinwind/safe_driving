package com.lazyDroid.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
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
	
	private LoginServlet loginServlet;
	private String username = "lazykyrin";
	private String userPass123456 = "MWxhenlreXJpbjYxMjM0NTZsYXp5RHJvaWQ=";
	
	@Before
	public void setupUnitTest() throws SQLException {
		SQLConnector connector = new SQLConnector();
		loginServlet = new LoginServlet(connector.getDBConnection());
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void normalPostRequest() throws ServletException, IOException {
		String postReq = "username:" + username + "\npassword:" + userPass123456;
		ServerTestUtils.parseRequestHelper(postReq, request);
		ByteArrayOutputStream outstream = ServerTestUtils.setResponseWriter(response);
		loginServlet.service(request, response);
		assertEquals("status:success", outstream.toString());
	}
}
