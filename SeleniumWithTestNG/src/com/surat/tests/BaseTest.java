package com.surat.tests;

import com.surat.pages.FirstPage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    //Source : http://www.tutorialspoint.com/selenium/selenium_grids.htm
    //Source : https://www.packtpub.com/sites/default/files/downloads/Distributed_Testing_with_Selenium_Grid.pdf

		/*To run a node with chrome as a parameter, use below command. For Mac use chromedriver instead of chromedriver.exe
            C:\Users\surat_das\Downloads>java -jar selenium-server-standalone-2.43.1.jar -role node -hub http://localhost:4444/grid/register -maxSession 15 -browser browserName="chrome",version=ANY,platform=VISTA,maxInstances=5 -Dwebdriver.chrome.driver=chromedriver.exe -port 5555
		 */

    protected WebDriver driver;
    protected String URL, Node;
    protected ThreadLocal<RemoteWebDriver> threadDriver = null;
    protected FirstPage page;
    protected Logger log;

    @Parameters({"browser", "platform", "version"})
    @BeforeTest
    public void setUp(String browser, String platform, @Optional String version) throws MalformedURLException {
        DOMConfigurator.configure("log4j.xml");

        String URL = "http://www.calculator.net";

        DesiredCapabilities cap = new DesiredCapabilities();
        System.out.println(" " + this.getClass().toString().split(" ")[1] + " Executing on " + platform.toString() + " with " + browser.toString() + (version == null ? "" : "(v" + version + ")"));

        if (platform.equalsIgnoreCase("MAC")) {
            cap.setPlatform(Platform.MAC);
        } else if (platform.equalsIgnoreCase("VISTA")) {
            cap.setPlatform(Platform.VISTA);
        } else if (platform.equalsIgnoreCase("Windows")) {
            cap.setPlatform(Platform.WINDOWS);
            //Node = "http://10.112.66.52:5557/wd/hub";
            //DesiredCapabilities cap = DesiredCapabilities.chrome();
        } else {
            throw new IllegalArgumentException("The Browser Type is Undefined");
        }

			/*To run on a particular node.
				driver = new RemoteWebDriver(new URL(Node),cap);*/

        cap.setBrowserName(browser);
        cap.setVersion(version);
        //driver = new RemoteWebDriver(cap);
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Launch website
        driver.navigate().to(URL);
        //driver.manage().window().maximize();
        page = PageFactory.initElements(driver, FirstPage.class);
    }

    @DataProvider(name = "testDataProvider")
    //inputString1, inputString2, expectedString
    public static Object[][] data() {
        return new Object[][]{{"10", "50", "5"}, {"1", "5", "0.05"}, {"3", "9", "0.27"}};
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        if (!result.isSuccess()) {
            File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File failureImageFile = new File(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + result.getMethod().getMethodName() + ".png");
            FileUtils.moveFile(imageFile, failureImageFile);
            System.out.println("File copied at : " + failureImageFile.getAbsoluteFile());
        }
    }

    @AfterTest
    public void tear() {
        driver.quit();
    }

}