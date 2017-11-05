package com.lazyDroid.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestSafeDrivingUtils.class,
	TestLoginServlet.class,
	TestRegistrationServlet.class,
	TestUserServlet.class
})
public class TestAll {

}
