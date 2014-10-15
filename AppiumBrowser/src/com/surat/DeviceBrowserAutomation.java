package com.surat;

//import io.appium.java_client.AppiumDriver;
//import java.io.File;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DeviceBrowserAutomation {
	public RemoteWebDriver driver;

	@Before
	public void setUp() throws Exception{
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability ("newCommandTimeout", "120");  

		//Android
		capabilities.setCapability("browserName", "Browser");
		capabilities.setCapability("deviceName", "emulator-5554");
		//capabilities.setCapability("androidDeviceSerial", "emulator-5554");
		//capabilities.setCapability("platformVersion", "4.4");
		capabilities.setCapability("platformName", "Android");
		//driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		//*/
		
		/*//iOS
		capabilities.setCapability("browserName", "Safari");
		capabilities.setCapability("deviceName", "iphone"); //Change the name here to iphone or ipad.
		//capabilities.setCapability("androidDeviceSerial", "emulator-5554");
		//capabilities.setCapability("platformVersion", "4.4");
		capabilities.setCapability("platformName", "iOS");
		//driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver = new RemoteWebDriver(new URL("http://10.147.11.248:4723/wd/hub"), capabilities);
		 //*/
		
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
	}

	@Test
	public void testSir() throws Exception{

		/* Screenshot not working
		 WebDriver augmentedDriver = new Augmenter().augment(driver);
		 File f  = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(f, new File("screenshot.jpg"));
		 */

		driver.get("http://www.google.com");
		driver.findElement(By.id("lst-ib")).sendKeys("cnn");
		driver.findElement(By.id("tsbb")).click();
		Thread.sleep(2000);
	}

	@After
	public void tearDown(){

		driver.quit();
		System.out.println("In tearDown function.");
	}
}