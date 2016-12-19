package com.surat.tests;

import com.surat.pages.SecondPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCase1 extends com.surat.tests.BaseTest {

    @Test(dataProvider = "testDataProvider")
    /*	@Test(dataProviderClass=tests.FileDataProvider.class,dataProvider="getDataFromFile")
	@DataProviderArguments(value = { "inputA" }, value1 = { "inputB" }, value2 = { "expected" })*/
    public void calculatepercent(String inputA, String inputB, String expectedResult) {
        log = Logger.getLogger(this.getClass().getName());

        SecondPage resultPage;
        try {
            resultPage = page.uf_GoToPercentCalculator();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            resultPage = PageFactory.initElements(driver, SecondPage.class);
        }

        //resultPage.uf_EnterFirstNumber("10").uf_EnterSecondNumber("50").uf_ClickSubmitButton();
        //OR
        resultPage.uf_GetResult(inputA, inputB);

        String result = resultPage.resultText.getText();
        resultPage = PageFactory.initElements(driver, SecondPage.class);
        resultPage.uf_ClearText();

        if (result.equals(expectedResult)) {
            log.info("This is pass.");
            System.out.println(" The Result is Pass");
        } else {
            System.out.println(" The Result is Fail");
            Assert.fail("The result is not correct.");
        }

    }
}