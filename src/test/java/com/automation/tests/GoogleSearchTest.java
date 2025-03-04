package com.automation.tests;

import com.automation.pages.GoogleSearchPage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.ExtentManager;
import java.io.IOException;
import java.net.URL;

@Feature("Google Search Test")
public class GoogleSearchTest {
    //WebDriver driver;
    RemoteWebDriver driver;
    GoogleSearchPage searchPage;
    ExtentTest test;
    ExtentReports extent;

    @Parameters("browser")
    @BeforeClass
    public void setup(String browser) throws IOException, InterruptedException {
        extent = ExtentManager.getInstance();
        test = extent.createTest("Google Test", "Validates Google Search");

        String seleniumGridURL = "http://localhost:4444/wd/hub";  // Change if using a different grid URL
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (browser.equalsIgnoreCase("chrome")) {
            System.out.println("Running code in chrome");
            capabilities.setBrowserName("chrome");
            WebDriverManager.chromedriver().clearDriverCache().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
            chromeOptions.addArguments("--remote-allow-origins=*","--headless","--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--start-maximized");
            chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            driver = new RemoteWebDriver(new URL(seleniumGridURL), chromeOptions);
            //driver = new ChromeDriver(chromeOptions);
        }else {
            capabilities.setBrowserName("firefox");
            System.out.println("Running code in firefox");
            //WebDriverManager.firefoxdriver().clearDriverCache().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
            firefoxOptions.addPreference("general.useragent.override", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            firefoxOptions.addPreference("dom.webdriver.enabled", false);
            firefoxOptions.addPreference("useAutomationExtension", false);
            firefoxOptions.addArguments("--headless","--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--start-maximized","--disable-extensions");
            //Set profile path manually
//            File profileDir = new File("/home/seluser/.mozilla/firefox/myprofile");
//            if (profileDir.exists()) {
//                System.out.println("Firefox profile found, using it.");
//                firefoxOptions.setProfile(new FirefoxProfile(profileDir));
//            } else {
//                System.out.println("Firefox profile not found, using default settings.");
//            }
            driver = new RemoteWebDriver(new URL(seleniumGridURL), firefoxOptions);
            //driver = new FirefoxDriver(firefoxOptions);
        }
        test.log(Status.INFO, browser + " Driver initialized");
        searchPage = new GoogleSearchPage(driver);
    }

    @Test(description = "Open Google and verify title")
    @Description("Test to open Google.com and validate the page title")
    public void testGoogleSearch() throws InterruptedException {
        try {
            driver.get("https://www.google.com");
            //String screenshotPath = searchPage.captureScreenshot("Google_Homepage");
            //test.addScreenCaptureFromPath(screenshotPath, "Google Homepage");
            test.log(Status.INFO, "Navigated to Google");
            test.addScreenCaptureFromBase64String(((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));
            searchPage.enterSearchQuery("Maven Selenium Automation");
            searchPage.randomDelay();
            searchPage.clickSearchButton();
            searchPage.randomDelay();
            //String screenshotPath_Search = searchPage.captureScreenshot("Google_Search");
            //test.addScreenCaptureFromPath(screenshotPath_Search, "Google Search");
            test.addScreenCaptureFromBase64String(((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));
            saveScreenshot();
            System.out.println(driver.getTitle());
            Assert.assertTrue(driver.getTitle().contains("Maven Selenium Automation"));
            test.log(Status.PASS, "Google title verified");
        } catch (AssertionError e){
            searchPage.captureScreenshot("AssertionFailure");
            test.log(Status.FAIL, "Google title mismatch");
            throw e;
        }
    }

    @AfterTest
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] saveScreenshot() {
        return new byte[0]; // Implement screenshot capture logic
    }

    @AfterClass
    public void teardown() {
        driver.quit();
        extent.flush();
    }
}
