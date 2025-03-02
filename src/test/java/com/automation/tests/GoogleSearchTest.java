package com.automation.tests;

import com.automation.pages.GoogleSearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class GoogleSearchTest {
    //WebDriver driver;
    RemoteWebDriver driver;
    GoogleSearchPage searchPage;

    @BeforeClass
    public void setup() throws IOException {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        //Path tempDir = Files.createTempDirectory("chrome-profile");
        //chromeOptions.addArguments("--user-data-dir=" + tempDir.toAbsolutePath().toString());
        chromeOptions.addArguments("--remote-allow-origins=*,--headless=new","--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--start-maximized");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        chromeOptions.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
//        driver = new ChromeDriver(chromeOptions);
//        driver.manage().window().maximize();
//        searchPage = new GoogleSearchPage(driver);

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
        searchPage = new GoogleSearchPage(driver);
    }

    @Test
    public void testGoogleSearch() throws InterruptedException {
        try {
            driver.get("https://www.google.com");
            searchPage.enterSearchQuery("Maven Selenium Automation");
            searchPage.randomDelay();
            searchPage.clickSearchButton();
            Thread.sleep(3000);
            System.out.println(driver.getTitle());
            Assert.assertTrue(driver.getTitle().contains("Maven Selenium Automation"));
        } catch (AssertionError e){
            searchPage.captureScreenshot("AssertionFailure");
            throw e;
        }
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}