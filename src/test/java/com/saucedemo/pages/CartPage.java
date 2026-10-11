package com.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends AppPage {

    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");
    private static final By CHECKOUT = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        waits.urlContains("cart.html");
        return hasTitle("Your Cart");
    }

    public List<String> getItemNames() {
        return textsOf(ITEM_NAMES);
    }

    public CartPage removeItem(String productName) {
        String item = "//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name' and text()='"
                + productName + "']]";
        click(By.xpath(item + "//button[text()='Remove']"));
        waits.gone(By.xpath(item));
        return this;
    }

    public ProductsPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return waitFor(new ProductsPage(driver));
    }

    public CheckoutInfoPage checkout() {
        click(CHECKOUT);
        return waitFor(new CheckoutInfoPage(driver));
    }
}
