package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductsPage;

public class CartTest extends BaseTest {

    @Test(groups = CART, description = "Add a product, remove it, then add it again")
    public void addRemoveAndReAddProduct() {
        ProductsPage products = loginAsStandardUser();

        // Add
        products.addToCart(BACKPACK);
        assertEquals(products.getCartCount(), 1, "Badge after adding");
        assertEquals(products.getButtonText(BACKPACK), "Remove");

        CartPage cart = products.openCart();
        assertTrue(cart.isLoaded(), "Cart page should be shown");
        assertEquals(cart.getItemNames(), List.of(BACKPACK), "Cart after adding");

        // Remove
        cart.removeItem(BACKPACK);
        assertTrue(cart.getItemNames().isEmpty(), "Cart should be empty after removing");
        assertEquals(cart.getCartCount(), 0, "Badge after removing");

        // Re-add
        products = cart.continueShopping();
        assertEquals(products.getButtonText(BACKPACK), "Add to cart");
        products.addToCart(BACKPACK);
        assertEquals(products.getCartCount(), 1, "Badge after re-adding");
        assertEquals(products.openCart().getItemNames(), List.of(BACKPACK), "Cart after re-adding");
    }
}
