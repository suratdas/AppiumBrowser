package com.surat.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SecondPage extends BasePage {

    public SecondPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(id = "cpar1")
    WebElement firstTextbox;

    @CacheLookup
    @FindBy(id = "cpar2")
    WebElement secondTextbox;

    @CacheLookup
    @FindBy(xpath = ".//*[@id='content']/table/tbody/tr/td[2]/input")
    WebElement calculateButton;

    @CacheLookup
    @FindBy(xpath = ".//*[@id='content']/p[2]/span/font/b")
    public WebElement resultText;

    /**
     * @deprecated use {@link #uf_GetResult(String, String)} instead.
     */
    @Deprecated
    public SecondPage uf_EnterFirstNumber(String number){
        firstTextbox.sendKeys(number);
        return this;
    }

    @Deprecated
    public SecondPage uf_EnterSecondNumber(String number){
        secondTextbox.sendKeys(number);
        return this;
    }

    public SecondPage uf_ClickSubmitButton(){
        calculateButton.click();
        return this;
    }

    /**
     * Enters the two numbers passed on two text boxes and then clicks on the calculate button.
     * @param number1 - number to be put in the first textbox.
     * @param number2 - number to be put in the second textbox.
     * @return SecondPage - the next page.
     */
    public SecondPage uf_GetResult(String number1, String number2){
        uf_EnterFirstNumber(number1);
        uf_EnterSecondNumber(number2);
        calculateButton.click();
        return this;
    }

    public void uf_ClearText() {
        firstTextbox.clear();
        secondTextbox.clear();
    }

}