package com.surat.common;

import com.surat.actions.BaseActions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonTasks {

    public static File screenshotDirectoryFile;

    public static void deleteContinuousScreenshotFolder() {
        screenshotDirectoryFile = new File(Initialization.getScreenshotDirectory() + "\\" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + "\\" + BaseActions.testName);
        try {
            if (screenshotDirectoryFile.exists()) {
                File[] files = screenshotDirectoryFile.listFiles();
                if (null != files) {
                    for (int i = 0; i < files.length; i++) {
                        files[i].delete();
                    }
                }
            }
            screenshotDirectoryFile.delete();
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }
        }
    }

    public static void takeScreenshot(String stringPassed, boolean repeat, String exceptionString) {
        //Create a temporary notification before taking screenshot.
        boolean shouldCreateDebugInfoInScreenshot = exceptionString != null && Initialization.getIncludeDebugInfoInScreenshot().toLowerCase().contains("yes") && !repeat;
        try {
            if (shouldCreateDebugInfoInScreenshot) {
                exceptionString = exceptionString.replace("\n", " ");
                exceptionString = exceptionString.replace("\"", "");
                exceptionString = exceptionString.replace("'", "");
                String executeErrorMessageString = "$.growl.error({ title: 'ERROR', message: '" + StringEscapeUtils.escapeJava(exceptionString) + "' });";
                JavascriptExecutor js = (JavascriptExecutor) BeforeTest.Driver;
                js.executeScript(executeErrorMessageString);
                WebDriverWait wait5Seconds = new WebDriverWait(BeforeTest.Driver, 5);
                wait5Seconds.until(ExpectedConditions.visibilityOfElementLocated(By.id("growls")));
            }
        } catch (Exception e) {
        }

        try {
            //Create the directory structure
            screenshotDirectoryFile = new File(Initialization.getScreenshotDirectory() + "\\" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()));
            if (!screenshotDirectoryFile.exists()) {
                try {
                    screenshotDirectoryFile.mkdir();
                } catch (Exception e) {
                    Thread.sleep(1000);
                }
            }
            //Create a separate directory for failed case
            if (repeat) {
                int lastIndex = stringPassed.lastIndexOf("_");
                String folderNameSplit = stringPassed.substring(0, lastIndex);
                screenshotDirectoryFile = new File(screenshotDirectoryFile + "\\" + folderNameSplit);

                if (!screenshotDirectoryFile.exists())
                    screenshotDirectoryFile.mkdir();
            }

            File tempFile = ((TakesScreenshot) BeforeTest.Driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(tempFile, new File(screenshotDirectoryFile + "\\" + stringPassed + ".png"));
            if (!repeat || exceptionString != null)
                try {
                    if (shouldCreateDebugInfoInScreenshot)
                        ((JavascriptExecutor) BeforeTest.Driver).executeScript("document.getElementById(\"growls\").remove();");
                } catch (Exception e) {
                }

        } catch (Exception e) {
            BufferedImage image = null;
            try {
                image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
            try {
                ImageIO.write(image, "png", new File(screenshotDirectoryFile + "\\" + stringPassed + ".png"));
            } catch (IOException e1) {
            }
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (!file.delete()) {
            //Flag error
        }
    }

    public static boolean confirmFileExists(String filename) {
        File f = new File(filename);
        if (f.exists() && !f.isDirectory())
            return true;
        else
            return false;
    }

    public static void takeScreenshot(String fileNamePassed) {
        takeScreenshot(fileNamePassed, false, null);
    }

    public static void takeScreenshot(String fileNamePassed, String exceptionString) {
        takeScreenshot(fileNamePassed, false, exceptionString);
    }


    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static void injectJavaScript() {
        try {
            final Runnable runner = new Runnable() {
                public void run() {
                    String checkJqueryString = "if(!window.jQuery){var jquery = document.createElement('script');jquery.type = 'text/javascript';jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';document.getElementsByTagName('head')[0].appendChild(jquery);}";
                    String getGrowlString = "$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js');";
                    String getGrowlCssString = "$('head').append('<link rel=\"stylesheet\" href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" type=\"text/css\" />');";
                    try {
                        JavascriptExecutor js = (JavascriptExecutor) BeforeTest.Driver;
                        js.executeScript(checkJqueryString);
                        Thread.sleep(2500);
                        js.executeScript(getGrowlString);
                        Thread.sleep(2500);
                        js.executeScript(getGrowlCssString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t = new Thread(runner, "Code Executor");
            t.start();
        } catch (Exception e) {
        }
    }


    /* To use autoIt in Java follow below steps -
        1. Download JACOB.
        2. Download and install AutoIt. (Or register AutoIt dll file)
        3. Add jacob.jar and autoitx4java.jar to your library path.
        4. Place the jacob-1.15-M4-x64.dll file in your library path.
        5. Start using AutoItX.


    //Below are example usages
    private static void handleFireFoxFileDownload() throws AWTException {
        Robot robot = new Robot();

        robot.delay(5000);

        AutoItX autoItX;
        try {
            autoItX = new AutoItX();
        } catch (Exception e) {
        }
        autoItX.winActivate("Opening");

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(1000);
        pressReleaseKey(robot, KeyEvent.VK_ENTER);
        robot.delay(5000);
    }

    private static void pressReleaseKey(Robot robot, int keyEvent) {
        robot.keyPress(keyEvent);
        robot.keyRelease(keyEvent);
        robot.delay(1000);
    }

    public static void handleADFSPopUpInIE(String username, String password) {
        AutoItX autoItX;
        try {
            autoItX = new AutoItX();
        } catch (Exception e) {

        }
        autoItX.winActivate("Windows Security");
        boolean isWindowFound = autoItX.winWaitActive("Windows Security", "", 120);
        autoItX.send(username + "\t");
        autoItX.send(password + "\n");
    }
    */

}