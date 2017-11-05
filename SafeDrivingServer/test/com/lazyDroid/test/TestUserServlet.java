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
import org.mockito.Mockito;
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
	public void normalGetRequest() throws IOException {
		String responseContent = userServletDoGetHelper();
		assertEquals("point:98", responseContent);
	}
	
	@Test
	public void getRequestWithoutParameter() throws IOException {
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(userPass123456);
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn(null);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doGet(request, response);
		
		assertEquals("status:fail", outstream.toString());
	}
	
	@Test
	public void getRequestWithoutHeader() throws IOException {
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(null);
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn(username);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doGet(request, response);
		
		assertEquals("status:fail", outstream.toString());
	}
	
	@Test
	public void getRequestWithInvalidUsername() throws IOException {
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(userPass123456);
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn(username + "nouser");
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doGet(request, response);
		
		assertEquals("status:fail", outstream.toString());
	}
	
	@Test
	public void getRequestWithInvalidPassword() throws IOException {
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(userPass123456 + "abc");
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn(username);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doGet(request, response);
		
		assertEquals("status:fail", outstream.toString());
	}
	
	@Test
	public void normalPutRequest() throws IOException {
		String postReq = "username:" + username + "\npasword:" + userPass123456 + "\nupdate:98";
		String responseContent = userServletDoPutHelper(postReq);
		assertEquals("status:success", responseContent);
	}
	
	private String userServletDoPutHelper(String requestContent) throws IOException {
		ServerTestUtils.generateRequestInputStream(requestContent, request);
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		ServerTestUtils.setResponseWriter(response, outstream);
		userServlet.doPut(request, response);
		
		return outstream.toString();
	}
	
	private String userServletDoGetHelper() throws IOException {
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(userPass123456);
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn(username);
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
