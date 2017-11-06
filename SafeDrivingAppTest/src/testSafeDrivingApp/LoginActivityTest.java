package testSafeDrivingApp;

import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class LoginActivityTest {
	
	private static AppiumDriver<MobileElement> driver;
	
	public LoginActivityTest(AppiumDriver<MobileElement> main_driver){
		driver = main_driver;
	}
	
	public void loginActivityTest() throws Exception{
		loginEmptyString();
		loginUnregistered();
		loginProper();
	}
	
    public void loginEmptyString() throws Exception {
		//try clicking the login button when we only have empty strings
    	//we should see no feedback and no exception
    	WebElement remember_info = driver.findElementById("remember_user_info");
    	remember_info.click();
    	
    	WebElement username = driver.findElementById("user_name");
    	WebElement password = driver.findElementById("password");
    	username.clear();
    	password.clear();
    	
    	driver.hideKeyboard(); 
    	
		WebElement button = driver.findElementById("login_button");
		button.click();
		
		WebElement feedback = driver.findElementById("feedback_box");
		System.out.println("---------------------------------------\n");
		System.out.println("Check login feedback with empty string: \n");
		System.out.println("feedback should be empty string");
		System.out.println("feedback = " + feedback.getText());
	}
    
    
    public void loginUnregistered() throws Exception {
		//try clicking the login button with unregistered username and password
    	WebElement username = driver.findElementById("user_name");
    	WebElement password = driver.findElementById("password");
    	username.sendKeys("test_nonexist");
    	password.sendKeys("123");
    	
    	driver.hideKeyboard(); 
    	
		WebElement button = driver.findElementById("login_button");
		button.click();	
		
		WebElement feedback = driver.findElementById("feedback_box");
		System.out.println("---------------------------------------\n");
		System.out.println("Check login feedback with unregistered account: \n");
		System.out.println("feedback should be 'invalid username or password'");
		System.out.println("feedback = " + feedback.getText());
	
	}
    
    
    public void loginProper() throws Exception {
		//try login properly 
    	WebElement remember_info = driver.findElementById("remember_user_info");
    	remember_info.click();
    	
    	WebElement username = driver.findElementById("user_name");
    	WebElement password = driver.findElementById("password");
    	
    	username.clear();
    	password.clear();
    	
    	username.sendKeys("helen");
    	password.sendKeys("123");
    	
    	driver.hideKeyboard(); 
    	
		WebElement button = driver.findElementById("login_button");
		button.click();
		
	}
    
    
    public void rememberInfo() throws Exception {
    	//the correct userinfo should have been remembered, click login directly
    	WebElement button = driver.findElementById("login_button");
		button.click();
    }
	
    public void registerButtonTest()throws Exception{
    	//click the register button
    	WebElement button = driver.findElementById("register_button");
		button.click();
    	
    }
}
