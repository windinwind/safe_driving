package com.lazyDroid.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.lazyDroid.jetty.SafeDrivingUtils;

public class ServerTestUtils {
	static void setRequestMethod(String method, HttpServletRequest request) {
		Mockito.when(request.getMethod()).thenReturn(method);
	}
	
	static ByteArrayOutputStream setResponseWriter(HttpServletResponse response, OutputStream os) throws IOException {
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		PrintWriter pw = Mockito.mock(PrintWriter.class);
		
		Answer<Void> answer = new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				String strToWrite = (String)args[0];
				System.out.println("Writing " + strToWrite);
				outputstream.write(strToWrite.getBytes());
				return null;
			}
		};
		
		Mockito.doAnswer(answer).when(pw).write(Mockito.anyString());
		
		Mockito.when(response.getWriter()).thenReturn(pw);
		Mockito.doNothing().when(response).setStatus(Mockito.anyInt());
		return outputstream;
	}
	
	static Map<String, String> parseRequestHelper(String content, HttpServletRequest request) throws IOException {
		generateRequestInputStream(content, request);
		return SafeDrivingUtils.parseRequest(request);
	}
	
	static void generateRequestInputStream(String content, HttpServletRequest request) throws IOException {
		if (content == null) content = "";
		
		ByteArrayInputStream tempInputStream = new ByteArrayInputStream(content.getBytes());
		ServletInputStream requestInputStream = new ServletInputStream() {
			@Override
			public boolean isFinished() {
				// Do nothing
				return false;
			}
			@Override
			public boolean isReady() {
				// Do nothing
				return true;
			}
			@Override
			public void setReadListener(ReadListener arg0) {
				// Do nothing
			}

			@Override
			public int read() throws IOException {
				return tempInputStream.read();
			}
		};
		
		Mockito.when(request.getInputStream()).thenReturn(requestInputStream);
	}
}
