package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.HotelDetailsPage;
import pages.SearchResultsPage;
import utils.ScreenShotUtils;

public class BookingTest extends BaseTest {

    @Test
    public void testBookingFlow() {

        HomePage home = new HomePage(driver);
        home.enterDestination("Goa, India");
        home.selectDates();
        home.clickSearch();

        SearchResultsPage results = new SearchResultsPage(driver);
        results.printHotelListings();

        results.applyFilters();

        ScreenShotUtils.captureScreenshot(driver, "AfterFilters");

        Assert.assertTrue(results.getFilteredCount() >= 3,
                "Less than 3 hotels found");

        results.clickFirstHotel();

        HotelDetailsPage details = new HotelDetailsPage(driver);
        details.switchToNewTab();
        ScreenShotUtils.captureScreenshot(driver, "AfterFilters");

        details.printRoomDetails();
        details.scrollToBottom();

        Assert.assertTrue(details.isReviewVisible(),
                "Reviews not visible");
    }
}