package com.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step two: order summary. */
public class CheckoutOverviewPage extends AppPage {

    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By FINISH = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        waits.urlContains("checkout-step-two.html");
        return hasTitle("Checkout: Overview");
    }

    public List<String> getItemNames() {
        return textsOf(ITEM_NAMES);
    }

    public CheckoutCompletePage finish() {
        click(FINISH);
        return waitFor(new CheckoutCompletePage(driver));
    }
}
