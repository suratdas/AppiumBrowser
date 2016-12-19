package com.surat.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public SecondPage uf_GoToPercentCalculator(){
        MathCalculator.click();
        PercentCalculator.click();
        return PageFactory.initElements(driver, SecondPage.class);
    }
}