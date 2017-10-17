package com.surat.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.surat.common.Initialization.secondPage;

public class FirstPage extends BasePage {

    public FirstPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(xpath = ".//*[@id='menu']/div[3]/a")
    WebElement MathCalculator;

    @CacheLookup
    @FindBy(xpath = ".//*[@id='menu']/div[4]/div[3]/a")
    WebElement PercentCalculator;

    public SecondPage uf_GoToPercentCalculator() {
        MathCalculator.click();
        PercentCalculator.click();
        secondPage = PageFactory.initElements(driver, SecondPage.class);
        return secondPage;
    }

    public boolean GoToPercentCalculator() {
        MathCalculator.click();
        PercentCalculator.click();
        secondPage = PageFactory.initElements(driver, SecondPage.class);
        return true;
    }
}