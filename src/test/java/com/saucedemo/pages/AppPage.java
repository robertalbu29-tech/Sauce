package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Any page shown after login. They all share the same header (cart icon)
 * and footer (social links), so those actions live here once.
 */
public abstract class AppPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    protected AppPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return textOf(PAGE_TITLE);
    }

    /**
     * Waits for the page heading to show the expected text. The app is a single-page React app,
     * so the URL changes before the new page renders; waiting on the heading avoids stale reads.
     */
    protected boolean hasTitle(String expected) {
        return waits.textIs(PAGE_TITLE, expected);
    }

    /** Number shown on the cart icon; 0 when the badge is hidden. */
    public int getCartCount() {
        return isDisplayed(CART_BADGE) ? Integer.parseInt(textOf(CART_BADGE)) : 0;
    }

    public CartPage openCart() {
        click(CART_LINK);
        return waitFor(new CartPage(driver));
    }

    public String getSocialLinkHref(SocialNetwork network) {
        return waits.visible(network.locator()).getAttribute("href");
    }

    /**
     * Clicks a footer social icon, waits for the new tab to load the expected site,
     * then closes that tab and returns the URL it reached.
     */
    public String openSocialLink(SocialNetwork network) {
        String originalTab = driver.getWindowHandle();
        click(network.locator());
        waits.windowCount(2);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalTab)) {
                driver.switchTo().window(handle);
            }
        }
        try {
            waits.urlMatches(network.expectedUrlPattern());
            return driver.getCurrentUrl();
        } finally {
            driver.close();
            driver.switchTo().window(originalTab);
        }
    }
}
