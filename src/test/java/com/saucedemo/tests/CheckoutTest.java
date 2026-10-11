package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutOverviewPage;

public class CheckoutTest extends BaseTest {

    /** Login, add the backpack and open checkout step one. */
    private CheckoutInfoPage startCheckout() {
        CheckoutInfoPage info = loginAsStandardUser()
                .addToCart(BACKPACK)
                .openCart()
                .checkout();
        assertTrue(info.isLoaded(), "Checkout information page should be shown");
        return info;
    }

    @Test(groups = {SMOKE, CHECKOUT}, description = "Complete checkout with valid customer details")
    public void completeCheckout() {
        CheckoutOverviewPage overview = startCheckout()
                .fillInformation("ABC", "DEF", "123456")
                .continueToOverview();
        assertTrue(overview.isLoaded(), "Overview page should be shown");
        assertEquals(overview.getItemNames(), List.of(BACKPACK), "Items in order summary");

        CheckoutCompletePage complete = overview.finish();
        assertTrue(complete.isLoaded(), "Complete page should be shown");
        assertEquals(complete.getConfirmationMessage(), "Thank you for your order!");
        assertEquals(complete.getCartCount(), 0, "Cart should be empty after the order");
    }

    @Test(groups = {CHECKOUT, NEGATIVE}, description = "Checkout is blocked when First Name is empty")
    public void checkoutWithoutFirstNameShowsError() {
        CheckoutInfoPage info = startCheckout()
                .fillInformation("", "DEF", "123456")
                .continueExpectingError();

        assertEquals(info.getError(), "Error: First Name is required");
        assertTrue(info.isLoaded(), "Should stay on the information page");
    }
}
