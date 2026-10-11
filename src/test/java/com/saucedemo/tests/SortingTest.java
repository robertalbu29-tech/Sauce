package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.saucedemo.pages.ProductsPage;
import com.saucedemo.pages.SortOrder;

public class SortingTest extends BaseTest {

    @DataProvider(name = "sortOrders")
    public Object[][] sortOrders() {
        return new Object[][] {{SortOrder.NAME_A_TO_Z}, {SortOrder.NAME_Z_TO_A}};
    }

    @Test(groups = SORTING, dataProvider = "sortOrders", description = "Products are sorted by name A-Z and Z-A")
    public void productsAreSortedByName(SortOrder order) {
        ProductsPage products = loginAsStandardUser();

        List<String> actual = products.sortBy(order).getProductNames();

        // Each SortOrder supplies its own comparator, so this line works for every option
        assertEquals(actual, order.sort(actual), "Products not sorted " + order);
    }
}
