package com.saucedemo.pages;

import org.openqa.selenium.By;

/** Social icons in the footer, with the link each one must point to. */
public enum SocialNetwork {

    // SauceDemo rebranded the Twitter icon to X (data-test="social-x"); either domain is accepted for the opened tab
    TWITTER("social-x", "https://x.com/saucelabs", ".*(twitter|x)\\.com/saucelabs.*"),
    FACEBOOK("social-facebook", "https://www.facebook.com/saucelabs", ".*facebook\\.com/saucelabs.*");

    private final String dataTest;
    private final String expectedHref;
    private final String expectedUrlPattern;

    SocialNetwork(String dataTest, String expectedHref, String expectedUrlPattern) {
        this.dataTest = dataTest;
        this.expectedHref = expectedHref;
        this.expectedUrlPattern = expectedUrlPattern;
    }

    public By locator() {
        return By.cssSelector("[data-test='" + dataTest + "']");
    }

    public String expectedHref() {
        return expectedHref;
    }

    public String expectedUrlPattern() {
        return expectedUrlPattern;
    }
}
