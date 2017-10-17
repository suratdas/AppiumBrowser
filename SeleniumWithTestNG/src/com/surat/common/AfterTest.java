package com.surat.common;

import com.surat.actions.BaseActions;

public class AfterTest {
    public AfterTest() {
        try {
            //Reset the test name for the next test case, this will also stop the repeat screenshot thread
            BaseActions.testName = "";
            if (!Initialization.isAlwaysKeepScreenshots())
                if (!BaseActions.stepFailed && BaseActions.testName.length() > 1)
                    CommonTasks.deleteContinuousScreenshotFolder();
        } catch (Exception e) {
        }
        try {
            BeforeTest.Driver.quit();
        } catch (Exception e) {
        }
    }

}
