package com.bdd.feature.browser;

import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.bdd.feature.browser.UiCommonSteps.driver;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class GsmArenaTest {

    @Then("Search for element {string} and verify it's latest phone is listed")
    public void searchForElementAndVerify(String searchItem) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ZERO);

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
