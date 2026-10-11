package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.saucedemo.pages.ProductsPage;

public class LoginTest extends BaseTest {

    @Test(groups = SMOKE, description = "Standard user logs in and lands on the products page")
    public void successfulLogin() {
        assertTrue(loginPage.isLoaded(), "Login page should be shown");

        ProductsPage products = loginAsStandardUser();

        assertTrue(products.isLoaded(), "Products page should be shown after login");
        assertEquals(products.getProductNames().size(), 6, "Product count");
        assertEquals(products.getCartCount(), 0, "Cart should start empty");
    }
}
