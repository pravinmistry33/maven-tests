package com.automation.tests;

import com.automation.pages.GoogleSearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class GoogleSearchTest {
    //WebDriver driver;
    RemoteWebDriver driver;
    GoogleSearchPage searchPage;

    @Parameters("browser")
    @BeforeClass
    public void setup(String browser) throws IOException, InterruptedException {
        String seleniumGridURL = "http://localhost:4444/wd/hub";  // Change if using a different grid URL
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (browser.equalsIgnoreCase("chrome")) {
            capabilities.setBrowserName("chrome");
            WebDriverManager.chromedriver().clearDriverCache().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
            chromeOptions.addArguments("--remote-allow-origins=*","--headless=new","--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--start-maximized");
            chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            //driver = new RemoteWebDriver(new URL(seleniumGridURL), chromeOptions);
            driver = new ChromeDriver(chromeOptions);
        }else {
            capabilities.setBrowserName("firefox");
            WebDriverManager.firefoxdriver().clearDriverCache().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
            firefoxOptions.addArguments("--headless","--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--start-maximized","--disable-extensions");
            //firefoxOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            //firefoxOptions.addPreference("general.useragent.override","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            firefoxOptions.setProfile(new FirefoxProfile(new File("/Users/pravinmistry/Library/Application Support/Firefox/Profiles/7s2j4yfa.default-release-1737955747675")));
            //driver = new RemoteWebDriver(new URL(seleniumGridURL), firefoxOptions);
            driver = new FirefoxDriver(firefoxOptions);
        }
        searchPage = new GoogleSearchPage(driver);
    }

    @Test
    public void testGoogleSearch() throws InterruptedException {
        try {
            driver.get("https://www.google.com");
            searchPage.enterSearchQuery("Maven Selenium Automation");
            searchPage.randomDelay();
            searchPage.clickSearchButton();
            searchPage.randomDelay();
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
