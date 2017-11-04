package com.lazyDroid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParseRequestUnitTest {
	@Mock
	HttpServletRequest request;

	@Before
	public void setupTest() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void parseRequestNormalCases() throws IOException {
		String requestContent = "a:123\n" + "3:dog\n" + "helen:friend\n" + "lazyDroid:fantastic";
		
		Map<String, String> result = ServerTestUtils.parseRequestHelper(requestContent, request);
		
		assertEquals("123", result.get("a"));
		assertEquals("dog", result.get("3"));
		assertEquals("friend", result.get("helen"));
		assertEquals("fantastic", result.get("lazyDroid"));
	}
	
	@Test
	public void parseRequestEmptyContent() throws IOException {
		Map<String, String> result = ServerTestUtils.parseRequestHelper("", request);
		
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	public void parseRequestInvalidContent() throws IOException {
		Map<String, String> result = ServerTestUtils.parseRequestHelper("abcdef", request);
		assertEquals("message without colon", null, result);
		
		result = ServerTestUtils.parseRequestHelper("abcdef:", request);
		assertEquals("message without content", null, result);
		
		result = ServerTestUtils.parseRequestHelper(":abcdef", request);
		assertEquals("message without key", null, result);
		
		result = ServerTestUtils.parseRequestHelper("ab:cd:ef", request);
		assertEquals("message with more than one colon", null, result);
		
		result = ServerTestUtils.parseRequestHelper("ab:cd\n" + "ab:de", request);
		assertEquals("message with duplicate key", null, result);
	}
	
	@Test
	public void parseRequestInvalidContentWithValidContent() throws IOException {
		String validContent = "ab:cd\n" + "cd:ef\n" + "jiol:defg\n";
		Map<String, String> result = ServerTestUtils.parseRequestHelper(validContent + "iob", request);
		assertEquals("message without colon", null, result);
		
		result = ServerTestUtils.parseRequestHelper(validContent + "iob:", request);
		assertEquals("message without content", null, result);
		
		result = ServerTestUtils.parseRequestHelper(validContent + ":iob", request);
		assertEquals("message without key", null, result);
		
		result = ServerTestUtils.parseRequestHelper(validContent + "i:o:b", request);
		assertEquals("message with more than one colon", null, result);
		
		result = ServerTestUtils.parseRequestHelper(validContent + "iob:aw\n" + "iob:ewwrg", request);
		assertEquals("message with duplicate key", null, result);
	}
}
