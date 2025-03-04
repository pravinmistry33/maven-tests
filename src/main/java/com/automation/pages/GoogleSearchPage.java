package com.automation.pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GoogleSearchPage {
    WebDriver driver;

    public GoogleSearchPage(WebDriver driver) {
        this.driver = driver;
    }

    private By searchBox = By.name("q");
    private By searchButton = By.name("btnK");

    public void enterSearchQuery(String query) {
        driver.findElement(searchBox).sendKeys(query);
        //slowType(driver.findElement(searchBox), query);
    }

    public void clickSearchButton() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,300)");
        Thread.sleep(2000);  // 2s delay before clicking
        driver.findElement(searchButton).submit();
    }

    public void captureScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("screenshots/" + testName + ".png"));
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public void randomDelay() {
        try {
            int delay = ThreadLocalRandom.current().nextInt(2000, 5000); // 2-5 seconds
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void slowType(WebElement element, String text) {
        for (char ch : text.toCharArray()) {
            element.sendKeys(Character.toString(ch));
            try {
                Thread.sleep(new Random().nextInt(300) + 200); // Random delay between 200-500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}