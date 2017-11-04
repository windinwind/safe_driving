package com.lazyDroid.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;

import com.lazyDroid.jetty.SafeDrivingUtils;

public class ServerTestUtils {
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
