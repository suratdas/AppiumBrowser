package surat;

import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumAppTest {

    public AppiumDriver driver;

    @Before
    public void setUp() throws Exception{

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "");
        capabilities.setCapability("deviceName", "Android Emulator");
        //capabilities.setCapability("automationName", "Selendroid");

        capabilities.setCapability("platformVersion", "4.4");
        capabilities.setCapability("platformName", "Android");
        //capabilities.setCapability("appPackage", "biz.tekeye.dice");
        capabilities.setCapability("appPackage", "com.octo.android.robodemo.sample");
        capabilities.setCapability("appActivity", ".MainActivity");
        capabilities.setCapability("appWaitActivity", ".MainActivity");
        capabilities.setJavascriptEnabled(true);

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }

    @Test
    public void FirstTest() throws IOException, InterruptedException{
        //driver.findElement(By.id("biz.tekeye.dice:id/imageView1")).click();
        driver.findElement(By.id("com.octo.android.robodemo.sample:id/button_demo_finish")).click();
        //We can use method chaining as in the next line or we can just use one method e.g. description. Description relates to cont-Desc in UIautomator.
        driver.findElementByAndroidUIAutomator("UiSelector().description(\"More options\").index(1)").click();
        System.out.println(driver.currentActivity());
        Thread.sleep(2000);
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        File f  = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(f, new File("c://tmp//screenshot.jpg"));
        System.out.println("Test hello in branch.");

        String context = driver.getContext();
        driver.context("NATIVE_APP");
        int height = driver.manage().window().getSize().height;
        driver.swipe(160, (int) (height*0.8), 160, (int) (height*0.25), 1000);
        driver.context(context);
    }

    @After
    public void teardown(){
        driver.quit();
    }

}