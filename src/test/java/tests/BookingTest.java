package tests;

import java.time.LocalDate;

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
     // 1. Handle the popup first
     // Destination
        home.dismissGeniusPopup();
        home.enterDestination("Goa, India");
     // 2. GENERATE DYNAMIC DATES (Assignment Requirement)
        // This ensures your test always looks 10 and 13 days ahead from 'today'
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = LocalDate.now().plusDays(13);
        home.selectDates(checkIn.toString(), checkOut.toString());
        home.clickSearch();

        SearchResultsPage results = new SearchResultsPage(driver);
        

        results.applyFilters();
        results.printHotelListing();

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
        System.out.println("\n===============================================");
        System.out.println("          BOOKING.COM AUTOMATION SUMMARY        ");
        System.out.println("===============================================");
        System.out.println("  Status:    PASSED ✅");
        System.out.println("  Location:  Goa, India"); // Update this line!
        System.out.println("  Dates:     " + checkIn + " to " + checkOut);
        System.out.println("  Execution: Successful");
        System.out.println("===============================================\n");
    }
    
}