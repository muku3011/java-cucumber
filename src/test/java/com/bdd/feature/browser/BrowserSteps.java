package com.bdd.feature.browser;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class BrowserSteps {

    private WebDriver driver;

    @Given("Setup driver path")
    public void setupDriverPath() {
        // Setting system properties of ChromeDriver (optional, can be added in PATH also)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\muku3\\tools\\web-drivers\\chromedriver.exe");
    }

    @Given("Browser driver is configured")
    public void browserDriverIsConfigured() {
        driver = new ChromeDriver();
    }

    @When("Open {string}")
    public void openBrowserWithUrl(String url) {
        driver.get(url);
    }

    @Then("Close browser")
    public void closeBrowser() {
        driver.close();
    }

    @Then("Search for element {string} and verify it's latest phone is listed")
    public void searchForElementAndVerify(String searchItem) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        WebElement searchBox = wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"topsearch-text\"]")));
        searchBox.sendKeys(searchItem);
        searchBox.click();
        Thread.sleep(1000);

        WebElement selectItem = wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"topsearch\"]/div[1]/div[2]/ul/li[1]/a")));
        String selectedItemTest = selectItem.getText();
        selectItem.click();

        WebElement openedItem = wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"body\"]/div/div[1]/div/div[1]/h1")));
        System.out.println("Latest " + searchItem + " phone is : " + selectedItemTest);
        Assert.assertEquals(openedItem.getText(), selectedItemTest);
    }
}
