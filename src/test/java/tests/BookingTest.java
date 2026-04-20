package tests;

import java.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomePage;
import pages.HotelDetailsPage;
import pages.SearchResultsPage;
import utils.ScreenShotUtils;

/**
 * BookingTest Class
 * Orchestrates the end-to-end functional test for the Booking.com platform.
 * Inherits WebDriver setup and teardown from BaseTest.
 */
public class BookingTest extends BaseTest {

    @Test(description = "Verify the complete booking flow from search to hotel details validation.")
    public void testBookingFlow() {

        // --- PHASE 1: SEARCH ---
        HomePage home = new HomePage(driver);
        
        // Handle environmental noise (popups) before starting the flow
        home.dismissGeniusPopup();
        
        // Define destination
        home.enterDestination("Goa, India");

        // Dynamic Date Generation: 
        // Ensures the test is 'future-proof' by always selecting dates relative to the current execution day.
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = LocalDate.now().plusDays(13);
        
        home.selectDates(checkIn.toString(), checkOut.toString());
        home.clickSearch();

        // --- PHASE 2: FILTER & RESULTS ---
        SearchResultsPage results = new SearchResultsPage(driver);
        
        // Apply business-specific filters (e.g., Breakfast, Star ratings)
        results.applyFilters();
        
        // Extract and print property data to the console for traceability
        results.printHotelListing();

        // Evidence Collection: Capture state of UI after filters are applied
        ScreenShotUtils.captureScreenshot(driver, "AfterFilters");

        // Functional Assertion: Ensure the filtered inventory meets minimum requirements
        Assert.assertTrue(results.getFilteredCount() >= 3,
                "Inventory Check Failed: Less than 3 hotels found for the selected criteria.");

        // Select the top-ranked property to view details
        results.clickFirstHotel();
     // Check the count of open windows
       

        // --- PHASE 3: PROPERTY DETAILS ---
        HotelDetailsPage details = new HotelDetailsPage(driver);
        
        // Handle multi-window logic: Switching to the property detail tab
        details.switchToNewTab();
        
        // Evidence Collection: Capture the specific property page
        ScreenShotUtils.captureScreenshot(driver, "HotelDetailsTab");

        // Log room availability data
        details.printRoomDetails();
        
        // Verify UI elements that load only on scroll (Lazy Loading)
        details.scrollToBottom();

        // Final Assertion: Verify that social proof (Reviews) is visible to the user
        Assert.assertTrue(details.isReviewVisible(),
                "Validation Failed: Reviews section not visible on the details page.");

        // --- PHASE 4: SUMMARY LOGGING ---
        // Prints a professional execution summary for Jenkins/Console output
        System.out.println("\n===============================================");
        System.out.println("          BOOKING.COM AUTOMATION SUMMARY        ");
        System.out.println("===============================================");
        System.out.println("  Status:     PASSED ✅");
        System.out.println("  Location:   Goa, India");
        System.out.println("  Dates:      " + checkIn + " to " + checkOut);
        System.out.println("  Execution:  Successful");
        System.out.println("===============================================\n");
    }
}