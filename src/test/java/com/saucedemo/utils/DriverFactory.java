package com.saucedemo.utils;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Creates and holds one ChromeDriver per thread.
 * Selenium Manager (built into Selenium 4) downloads the matching chromedriver automatically.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver start() {
        ChromeOptions options = new ChromeOptions();
        // Don't wait for every image/script of heavy pages (e.g. x.com); explicit waits handle the rest
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (Config.isHeadless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        options.addArguments("--start-maximized", "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage");
        // Stop Chrome's "change your password" pop-up for the public demo credentials
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        DRIVER.set(driver);
        Log.info("Started Chrome (headless=" + Config.isHeadless() + ")");
        return driver;
    }

    public static WebDriver get() {
        return DRIVER.get();
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
