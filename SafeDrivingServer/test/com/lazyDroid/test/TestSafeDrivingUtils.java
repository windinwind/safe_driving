package com.lazyDroid.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	AuthenticationUnitTest.class,
	ParseRequestUnitTest.class
})
public class TestSafeDrivingUtils {

}
