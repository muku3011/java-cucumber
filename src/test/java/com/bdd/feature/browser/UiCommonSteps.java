package com.bdd.feature.browser;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class UiCommonSteps {

    public static WebDriver driver;

    @Given("Setup driver path")
    public void setupDriverPath() {
        // Setting system properties of ChromeDriver (optional, can be added in PATH also)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\muku3\\tools\\web-drivers\\chromedriver.exe");
    }

    @Given("Setup driver configuration")
    public void browserDriverIsConfigured() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--window-size=1920,1200");
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to Windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        driver = new ChromeDriver(options);
    }

    @When("Open {string}")
    public void openBrowserWithUrl(String url) {
        driver.get(url);
    }

    @Then("Close browser")
    public void closeBrowser() {
        try {
            driver.close();
            driver.quit();
            driver = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
