package com.bdd.feature.browser;

import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.MessagingException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bdd.feature.browser.UiCommonSteps.driver;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class AppointmentTest {

    @Then("Fill form using provided phone number {string}, emailId {string}")
    public void fillForm(String phoneNumber, String emailId) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ZERO);

        wait.until(presenceOfElementLocated(By.id("ppt")));
        Select issuePlace = new Select(driver.findElement(By.name("ppt")));
        issuePlace.selectByVisibleText("Delhi");

        wait.until(presenceOfElementLocated(By.id("cr")));
        Select currentResidency = new Select(driver.findElement(By.name("cr")));
        currentResidency.selectByVisibleText("Haryana");

        wait.until(presenceOfElementLocated(By.id("duration")));
        Select durationOfStayAtCurrentResidence = new Select(driver.findElement(By.name("duration")));
        durationOfStayAtCurrentResidence.selectByVisibleText("More than 6 months");
        Thread.sleep(1000);

        wait.until(presenceOfElementLocated(By.id("centre")));
        Select centre = new Select(driver.findElement(By.name("centre")));
        centre.selectByVisibleText("New Delhi");
        Thread.sleep(1000);

        wait.until(presenceOfElementLocated(By.id("category")));
        Select category = new Select(driver.findElement(By.name("category")));
        category.selectByVisibleText("Normal");
        Thread.sleep(1000);

        wait.until(presenceOfElementLocated(By.id("phone")));
        driver.findElement(By.id("phone")).sendKeys(phoneNumber);

        wait.until(presenceOfElementLocated(By.id("email")));
        driver.findElement(By.id("email")).sendKeys(emailId);
        Thread.sleep(1000);

        driver.findElement(By.id("verification_code")).click();
    }

    @Then("Access gmail with id {string}, and password {string} to fetch OTP url and open it in a new tab")
    public void getOtp(String emailId, String emailIdPassword) throws MessagingException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ZERO);

        String otpUrl = EmailUtil.extractUrls(emailId, emailIdPassword);
        System.out.println("OTP URL: " + otpUrl);

        // Keep reference of main appointment tab
        String mainAppointmentTab = driver.getWindowHandle();

        // Open a new tab for OTP
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(otpUrl);

        wait.until(presenceOfElementLocated(By.xpath("/html/body/div[2]/form/input[1]")));
        driver.findElement(By.xpath("/html/body/div[2]/form/input[1]")).sendKeys(emailId);

        wait.until(presenceOfElementLocated(By.xpath("/html/body/div[2]/form/input[3]")));
        driver.findElement(By.xpath("/html/body/div[2]/form/input[3]")).click();

        wait.until(presenceOfElementLocated(By.xpath("/html/body/div[2]")));
        WebElement otpElement = driver.findElement(By.xpath("/html/body/div[2]"));

        String otp = otpElement.getText().substring(12);
        System.out.println("OTP for session: " + otp);

        Thread.sleep(2000);

        // Change focus back to main appointment ab tab
        //  driver.close();
        driver.switchTo().window(mainAppointmentTab);

        wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"otp\"]")));
        driver.findElement(By.xpath("//*[@id=\"otp\"]")).sendKeys(otp);

        wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"em_tr\"]/div[3]/input")));
        driver.findElement(By.xpath("//*[@id=\"em_tr\"]/div[3]/input")).click();

        wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"Booking\"]/section/div/div/div/div[3]/div[1]/button")));
        driver.findElement(By.xpath("//*[@id=\"Booking\"]/section/div/div/div/div[3]/div[1]/button")).click();

        wait.until(presenceOfElementLocated(By.xpath("//*[@id=\"app_date\"]")));
        driver.findElement(By.xpath("//*[@id=\"app_date\"]")).click();

        List<WebElement> numberOfWeeks = driver.findElements(By.xpath("/html/body/div[7]/div[1]/table/tbody/tr"));

        List<DAILY_STATUS> availableStatus = Arrays.asList(DAILY_STATUS.values());
        List<String> listOfAvailableDays = new ArrayList<>();

        for (int i = 1; i <= numberOfWeeks.size(); i++) {
            for (int j = 1; j <= 7; j++) {
                String date = driver.findElement(By.xpath("/html/body/div[7]/div[1]/table/tbody/tr[" + i + "]/td[" + j + "]")).getText();
                String dateInfo = driver.findElement(By.xpath("/html/body/div[7]/div[1]/table/tbody/tr[" + i + "]/td[" + j + "]")).getAttribute("title");
                String dateAndStatus = "Date: [ " + date + " ], status: [ " + dateInfo + " ]";
                if (availableStatus.contains(DAILY_STATUS.fromString(dateInfo))) {
                    System.out.println(dateAndStatus);
                } else {
                    // Slot(s) is/are available
                    System.out.println("Available slot: " + dateAndStatus);
                    listOfAvailableDays.add(dateAndStatus);
                }
            }
        }

        if (!listOfAvailableDays.isEmpty()) {
            String subject = "Spain Visa Slot Available";
            StringBuilder body = new StringBuilder("Slot available for following date(s):");
            for (String availableSlotDayInfo : listOfAvailableDays) {
                body.append("\n").append(availableSlotDayInfo);
            }
            EmailUtil.sendEmail(emailId, emailIdPassword, subject, body.toString());
        } else {
            EmailUtil.sendEmail(emailId, emailIdPassword, "No Visa Appointment Available",
                    "No Visa Appointment Available. \nKeep Trying" +
                            "\n\nThank you");
        }
        Thread.sleep(3000);
    }

    public enum DAILY_STATUS {
        NOT_ALLOWED("Not Allowed"),
        HOLIDAY("Holiday"),
        SLOT_FULL("Slots Full"),
        OFF_DAY("Off Day");

        public final String dailyStatus;

        DAILY_STATUS(String dailyStatus) {
            this.dailyStatus = dailyStatus;
        }

        public static DAILY_STATUS fromString(String dailyStatusText) {
            for (DAILY_STATUS dailyStatus : DAILY_STATUS.values()) {
                if (dailyStatus.dailyStatus.equalsIgnoreCase(dailyStatusText)) {
                    return dailyStatus;
                }
            }
            return null;
        }
    }

}
