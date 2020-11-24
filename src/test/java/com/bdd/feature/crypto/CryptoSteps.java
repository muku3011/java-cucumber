package com.bdd.feature.crypto;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.nio.charset.StandardCharsets;

public class CryptoSteps {

    private String encryptedString;

    @Given("Steps to be performed before each scenario")
    public void stepsToBePerformedBeforeEachScenario() {
        System.out.println("Background: MISSING");
    }

    @Given("Nothing is given")
    public void nothing_is_given() {
        System.out.println("Given: MISSING");
    }

    @When("Encrypt {string} with password {string}")
    public void encryptPlainText(String plainText, String password) throws Exception {
        encryptedString = EncryptorAesGcm.encrypt(plainText.getBytes(StandardCharsets.UTF_8), password);
    }

    @Then("Validate text encoded and not equal to {string}")
    public void encryptedValue(String expectedEncryptedString) {
        Assert.assertNotEquals(expectedEncryptedString, encryptedString);
    }

    @Then("Decrypt with password {string} and verify plain text equal to {string}")
    public void decryptAndVerify(String password, String plainText) throws Exception {
        String decryptedText = DecryptAesGcm.decrypt(encryptedString, password);
        Assert.assertEquals(plainText, decryptedText);
    }

}