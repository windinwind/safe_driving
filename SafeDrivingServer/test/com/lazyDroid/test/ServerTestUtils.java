package com.lazyDroid.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;

import com.lazyDroid.jetty.SafeDrivingUtils;

public class ServerTestUtils {
	static void setRequestMethod(String method, HttpServletRequest request) {
		Mockito.when(request.getMethod()).thenReturn(method);
	}
	
	static ByteArrayOutputStream setResponseWriter(HttpServletResponse response) throws IOException {
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		Mockito.when(response.getWriter()).thenReturn(new PrintWriter(outputstream));
		Mockito.doNothing().when(response).setStatus(Mockito.anyInt());
		return outputstream;
	}
	
	static Map<String, String> parseRequestHelper(String content, HttpServletRequest request) throws IOException {
		ServletInputStream requestInputStream = generateServletInputStream(content);
		Mockito.when(request.getInputStream()).thenReturn(requestInputStream);
		return SafeDrivingUtils.parseRequest(request);
	}
	
	private static ServletInputStream generateServletInputStream(String content) {
		if (content == null) content = "";
		
		ByteArrayInputStream tempInputStream = new ByteArrayInputStream(content.getBytes());
		return new ServletInputStream() {
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
	}
}
