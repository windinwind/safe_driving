package testSafeDrivingApp;
 
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
 
public class TestSafeDriving {
	
	public static void main(String[] args) {
		
		//Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "Nexus_6_API_27");
		caps.setCapability("udid", "emulator-5554"); //Give Device ID of your mobile phone
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "6.0");
		caps.setCapability("appPackage", "lazydroid.safedriving");
		caps.setCapability("appActivity", "lazydroid.safedriving.SafeDrivingActivity");
		caps.setCapability("noReset", "true");
		
		
		//Instantiate Appium Driver
		try {
				AppiumDriver<MobileElement> driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
				//set timeout
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				
				SafeDrivingActivityTest t = new SafeDrivingActivityTest(driver);
				t.safeDrivingActivityTest();
				
				
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	
	
}

