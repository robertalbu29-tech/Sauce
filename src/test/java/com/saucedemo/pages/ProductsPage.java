package com.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class ProductsPage extends AppPage {

    private static final By INVENTORY_LIST = By.cssSelector("[data-test='inventory-list']");
    private static final By PRODUCT_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By SORT_DROPDOWN = By.cssSelector("[data-test='product-sort-container']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        waits.urlContains("inventory.html");
        return hasTitle("Products") && isDisplayed(INVENTORY_LIST);
    }

    public ProductsPage sortBy(SortOrder order) {
        new Select(waits.visible(SORT_DROPDOWN)).selectByValue(order.optionValue());
        return this;
    }

    public List<String> getProductNames() {
        waits.visible(PRODUCT_NAMES);
        return textsOf(PRODUCT_NAMES);
    }

    public ProductsPage addToCart(String productName) {
        click(productButton(productName, "Add to cart"));
        waits.visible(productButton(productName, "Remove"));
        return this;
    }

    public ProductsPage removeFromCart(String productName) {
        click(productButton(productName, "Remove"));
        waits.visible(productButton(productName, "Add to cart"));
        return this;
    }

    /** Text of the product's button: "Add to cart" or "Remove". */
    public String getButtonText(String productName) {
        return textOf(By.xpath(productCard(productName) + "//button"));
    }

    private static By productButton(String productName, String buttonText) {
        return By.xpath(productCard(productName) + "//button[text()='" + buttonText + "']");
    }

    private static String productCard(String productName) {
        return "//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name' and text()='"
                + productName + "']]";
    }
}
