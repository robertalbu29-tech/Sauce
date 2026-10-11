package com.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.saucedemo.utils.Waits;

/**
 * Parent of every page object. Holds the driver and the shared helper methods.
 *
 * Polymorphism: every page overrides {@link #isLoaded()} with its own check, so tests can
 * call {@code page.isLoaded()} on any BasePage without knowing which page it is.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final Waits waits;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    /** Each page decides for itself what "loaded" means. */
    public abstract boolean isLoaded();

    /**
     * Used by navigation methods: waits until the destination page is loaded and returns it.
     * Works for any page because it relies only on the overridden isLoaded().
     */
    protected <T extends BasePage> T waitFor(T page) {
        if (!page.isLoaded()) {
            throw new IllegalStateException(page.getClass().getSimpleName() + " did not load");
        }
        return page;
    }

    protected void click(By locator) {
        waits.clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waits.visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waits.visible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> found = driver.findElements(locator);
        return !found.isEmpty() && found.get(0).isDisplayed();
    }

    protected List<String> textsOf(By locator) {
        return driver.findElements(locator).stream().map(WebElement::getText).toList();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
