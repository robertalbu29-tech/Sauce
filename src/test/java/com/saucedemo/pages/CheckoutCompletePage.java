package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends AppPage {

    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        waits.urlContains("checkout-complete.html");
        return hasTitle("Checkout: Complete!");
    }

    public String getConfirmationMessage() {
        return textOf(COMPLETE_HEADER);
    }
}
