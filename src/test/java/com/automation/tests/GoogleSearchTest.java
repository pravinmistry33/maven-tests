package com.automation.tests;

import com.automation.pages.GoogleSearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GoogleSearchTest {
    WebDriver driver;
    GoogleSearchPage searchPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        //chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        searchPage = new GoogleSearchPage(driver);
    }

    @Test
    public void testGoogleSearch() throws InterruptedException {
        driver.get("https://www.google.com");
        searchPage.enterSearchQuery("Maven Selenium Automation");
        searchPage.clickSearchButton();
        Assert.assertTrue(driver.getTitle().contains("Maven Selenium Automation"));
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}