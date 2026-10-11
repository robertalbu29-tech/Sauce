package com.saucedemo.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Explicit-wait helpers so page objects never need Thread.sleep. */
public class Waits {

    private final WebDriverWait wait;

    public Waits(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getInt("timeoutSeconds")));
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** True once the element shows the expected text, false if it never does within the timeout. */
    public boolean textIs(By locator, String expected) {
        try {
            return wait.until(ExpectedConditions.textToBe(locator, expected));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void gone(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void urlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    public void urlMatches(String regex) {
        wait.until(ExpectedConditions.urlMatches(regex));
    }

    public void windowCount(int count) {
        wait.until(ExpectedConditions.numberOfWindowsToBe(count));
    }
}
