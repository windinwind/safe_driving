package com.lazyDroid.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class SafeDrivingUtils {
	public static Map<String, String> parseRequest(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		Map<String, String> result = new HashMap<String, String>();
		
		String temp = br.readLine();
		if (temp == null) return null;
		while (temp != null) {
			String[] temp2 = temp.split(":");
			if (temp2.length != 2) return null;
			if (result.containsKey(temp2[0])) return null;
			
			result.put(temp2[0], temp2[1]);
		}
		
		return result;
	}
}
