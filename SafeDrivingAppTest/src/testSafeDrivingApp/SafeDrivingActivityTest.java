package testSafeDrivingApp;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidKeyCode;

public class SafeDrivingActivityTest {
	
	private static AppiumDriver<MobileElement> driver;

	public SafeDrivingActivityTest(AppiumDriver<MobileElement> main_driver){
		driver = main_driver;
	}
	
	public void safeDrivingActivityTest()throws Exception {
		
		LoginActivityTest t = new LoginActivityTest(driver);
		UserRegisterActivityTest r = new UserRegisterActivityTest(driver);
		
		//before login
		System.out.println("---------------------------------------\n");
		System.out.println("Check user info before login: \n");
		System.out.println("user and point should both be empty strings");
		checkUserInfo();
		
		//login
		loginButtonTest();
		//test login activity
		t.loginActivityTest();
		
		//after login
		System.out.println("---------------------------------------\n");
		System.out.println("Check user info after login: \n");
		System.out.println("user should be helen, point should be a number");
		checkUserInfo();
		
		//logout
		logoutTest();
		
		//after logout
		System.out.println("---------------------------------------\n");
		System.out.println("Check user info after logout: \n");
		System.out.println("user and point should both be empty strings");
		checkUserInfo();
		
		//check remember info
		loginButtonTest();
		t.rememberInfo();
		System.out.println("---------------------------------------\n");
		System.out.println("Check user info with remembered info login: \n");
		System.out.println("user should be helen, point should be a number");
		checkUserInfo();
		
		//test registration
		logoutTest();
		loginButtonTest();
		t.registerButtonTest();
		r.registerDuplicate();
		driver.navigate().back();
		t.rememberInfo();
	}
	
	
    public void loginButtonTest() throws Exception {
		//go to the login page
		WebElement button = driver.findElementById("login_button");
		button.click();
		
	}
    
    public void logoutTest()throws Exception{
    	//go to the login page
    	WebElement button = driver.findElementById("logout_button");
    	button.click();
    }
    
    public static void checkUserInfo()throws Exception{
    	//print username and safepoint shown on screen
    	WebElement username = driver.findElementById("after_login_username");
    	WebElement safepoint = driver.findElementById("after_login_point");
		System.out.println("user = " + username.getText() + "\n" + "point = " + safepoint.getText());
    }

}
