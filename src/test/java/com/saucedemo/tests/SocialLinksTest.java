package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.saucedemo.pages.ProductsPage;
import com.saucedemo.pages.SocialNetwork;

public class SocialLinksTest extends BaseTest {

    @DataProvider(name = "networks")
    public Object[][] networks() {
        return new Object[][] {{SocialNetwork.TWITTER}, {SocialNetwork.FACEBOOK}};
    }

    @Test(groups = SOCIAL, dataProvider = "networks", description = "Footer social icons open the correct page")
    public void socialIconOpensCorrectLink(SocialNetwork network) {
        ProductsPage products = loginAsStandardUser();

        assertEquals(products.getSocialLinkHref(network), network.expectedHref(), "Link target of " + network);

        String openedUrl = products.openSocialLink(network);
        assertTrue(openedUrl.matches(network.expectedUrlPattern()), network + " opened unexpected URL: " + openedUrl);
        assertTrue(products.isLoaded(), "Should be back on the products page");
    }
}
