package com.saucedemo.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.Config;
import com.saucedemo.utils.DriverFactory;

/** Opens a fresh Chrome on the login page before each test and closes it afterwards. */
public abstract class BaseTest {

    /** TestNG group names, kept in one place to avoid typos. */
    protected static final String SMOKE = "smoke";
    protected static final String SORTING = "sorting";
    protected static final String CART = "cart";
    protected static final String CHECKOUT = "checkout";
    protected static final String NEGATIVE = "negative";
    protected static final String SOCIAL = "social";

    protected static final String BACKPACK = "Sauce Labs Backpack";

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.start();
        driver.get(Config.get("baseUrl"));
        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quit();
    }

    /** Shared step for every test that starts on the products page. */
    protected ProductsPage loginAsStandardUser() {
        return loginPage.loginAs(Config.get("username"), Config.get("password"));
    }
}
