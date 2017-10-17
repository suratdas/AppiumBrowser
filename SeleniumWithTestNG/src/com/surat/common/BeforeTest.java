package com.surat.common;

import com.surat.actions.AllActions;
import com.surat.pages.FirstPage;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import java.net.URL;
import java.util.Set;

public class BeforeTest {
    public static WebDriver Driver;
    public static String BrowserType;
    public static String BrowserVersion;
    public static DesiredCapabilities capabilities = new DesiredCapabilities();
    public static Set<Cookie> cookies = null;
    public static boolean retainCookie;

    public boolean LaunchBrowserWithCookies() throws Exception {
        retainCookie = true;
        return LaunchBrowser();
    }

    public boolean LaunchBrowser() throws Exception {
        try {
            AllActions.stopOnFailure = false;
            AllActions.stepFailed = false;
            AllActions.errorMessage = "";
            Initialization.repeatScreenshotThread = null;
        } catch (Exception e) {
            //Log"Errors during resetting values in SetUp method.\n" + e.getMessage()
        }

        //LogLib.logFilePath = Initialization.getLogFilePath();
        capabilities = new DesiredCapabilities();
        String browser = Initialization.getBrowser().toLowerCase();
        switch (browser) {
            case "firefox":
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "firefox");
                capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
                break;
            case "chrome":
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
                break;
            case "internet explorer":
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
                capabilities.setCapability("InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS", false);
                break;
            case "edge":
                //capabilities = DesiredCapabilities.edge();
                break;
        }

        String platform = Initialization.getPlatform().toUpperCase();
        switch (platform) {
            case "WINDOWS":
                capabilities.setCapability(CapabilityType.PLATFORM, "WINDOWS");
                break;
        }

        if (!(Initialization.getBrowserVersion().length() < 1)) {
            capabilities.setCapability(CapabilityType.VERSION, Initialization.getBrowserVersion());
        }

        try {
            Driver = new RemoteWebDriver(new URL(Initialization.getSeleniumGridUrl()), capabilities);
            Driver.get(Initialization.getUrl());
            if (retainCookie) {
                for (Cookie cookie : cookies)
                    Driver.manage().addCookie(cookie);
                Driver.navigate().refresh();
                retainCookie = false;
            }
            Driver.manage().window().maximize();
            BrowserType = capabilities.getBrowserName().toLowerCase();
            BrowserVersion = ((RemoteWebDriver) Driver).getCapabilities().getVersion();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        Initialization.firstPage = PageFactory.initElements(BeforeTest.Driver, FirstPage.class);

        return true;
    }
}
