package testSafeDrivingApp;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class UserRegisterActivityTest {

private static AppiumDriver<MobileElement> driver;
	
	public UserRegisterActivityTest(AppiumDriver<MobileElement> main_driver){
		driver = main_driver;
	}
	
	public void userRegisterActivityTest()throws Exception{
		registerEmptyString();
		registerDuplicate();
	}
	
	public void registerEmptyString()throws Exception{
		//register with empty string
		WebElement button = driver.findElementById("register_button");
		button.click();
		
		WebElement feedback = driver.findElementById("feedback_box");
		System.out.println("---------------------------------------\n");
		System.out.println("Check register with empty strings: \n");
		System.out.println("feedback should be an empty string");
		System.out.println("feedback = " + feedback.getText());
	}
	
	
	public void registerDuplicate() throws Exception{
		//register with existing username
		WebElement username = driver.findElementById("user_name");
    	WebElement password = driver.findElementById("password");
    	
    	username.clear();
    	password.clear();
    	
    	username.sendKeys("helen");
    	password.sendKeys("123");
    	
    	driver.hideKeyboard(); 
    	
    	WebElement button = driver.findElementById("register_button");
		button.click();
    	
		WebElement feedback = driver.findElementById("feedback_box");
		System.out.println("---------------------------------------\n");
		System.out.println("Check register with existing username: \n");
		System.out.println("feedback should be 'registration failed'");
		System.out.println("feedback = " + feedback.getText());
	}
}
