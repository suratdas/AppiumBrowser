package com.surat.pages;

import com.surat.common.BeforeTest;
import com.surat.common.CommonTasks;
import com.surat.common.Initialization;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver = BeforeTest.Driver;
    protected WebDriverWait wait = new WebDriverWait(driver, 120);

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage() {
        if (Initialization.getIncludeDebugInfoInScreenshot().toLowerCase().contains("yes"))
            CommonTasks.injectJavaScript();
    }
}