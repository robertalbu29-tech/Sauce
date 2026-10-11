package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: customer information form. */
public class CheckoutInfoPage extends AppPage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        waits.urlContains("checkout-step-one.html");
        return hasTitle("Checkout: Your Information");
    }

    public CheckoutInfoPage fillInformation(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    /** Continue with valid data. */
    public CheckoutOverviewPage continueToOverview() {
        click(CONTINUE);
        return waitFor(new CheckoutOverviewPage(driver));
    }

    /** Continue with invalid data: the form stays on this page and shows an error. */
    public CheckoutInfoPage continueExpectingError() {
        click(CONTINUE);
        return this;
    }

    public String getError() {
        return textOf(ERROR);
    }
}
