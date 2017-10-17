package com.surat.common;

import com.surat.pages.FirstPage;
import com.surat.pages.SecondPage;

public class Initialization {

    private static int anyVariable;
    public static volatile Thread repeatScreenshotThread = null;
    private static String browser;
    private static String platform;
    private static String browserVersion;
    private static String seleniumGridUrl;
    private static String url;
    private static String logFilePath;
    private static boolean alwaysKeepScreenshots;
    private static String screenshotDirectory;
    private static String includeDebugInfoInScreenshot;

    public static int getAnyVariable() {
        return anyVariable;
    }

    public static void AnyVariable(int valuePassed) {
        anyVariable = valuePassed;
    }

    public static String getBrowser() {
        return browser;
    }

    public static void Browser(String browserName) {
        browser = browserName;
    }

    public static String getPlatform() {
        return platform;
    }

    public static void Platform(String platform) {
        Initialization.platform = platform;
    }

    public static String getBrowserVersion() {
        return browserVersion;
    }

    public static void BrowserVersion(String version) {
        browserVersion = version;
    }

    public static String getSeleniumGridUrl() {
        return seleniumGridUrl;
    }

    public static void SeleniumGridUrl(String seleniumGridUrl) {
        Initialization.seleniumGridUrl = seleniumGridUrl;
    }

    public static String getUrl() {
        return url;
    }

    public static void URL(String url) {
        Initialization.url = url;
    }

    public static boolean isAlwaysKeepScreenshots() {
        return alwaysKeepScreenshots;
    }

    public static void AlwaysKeepScreenshots(boolean alwaysKeepScreenshots) {
        Initialization.alwaysKeepScreenshots = alwaysKeepScreenshots;
    }


    //page variables
    public static FirstPage firstPage;
    public static SecondPage secondPage;

    public static String getScreenshotDirectory() {
        return screenshotDirectory;
    }

    public static void ScreenshotDirectory(String screenshotDirectory) {
        Initialization.screenshotDirectory = screenshotDirectory;
    }


    public static String getIncludeDebugInfoInScreenshot() {
        return includeDebugInfoInScreenshot;
    }

    public static void IncludeDebugInfoInScreenshot(String includeDebugInfoInScreenshot) {
        Initialization.includeDebugInfoInScreenshot = includeDebugInfoInScreenshot;
    }
}