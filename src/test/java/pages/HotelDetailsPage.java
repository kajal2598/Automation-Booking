package pages;

import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * HotelDetailsPage Class
 * Handles interactions on the specific hotel information page.
 * This page typically opens in a new browser tab/window upon selection.
 */
public class HotelDetailsPage {
    WebDriver driver;

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Switches the WebDriver focus to the most recently opened window or tab.
     * Essential for Booking.com as property details are launched in a separate tab
     * to keep the search results active in the primary window.
     */
    public void switchToNewTab() {
        // Capture all currently open window handles (unique IDs)
        Set<String> handles = driver.getWindowHandles();
        
        // Iterate through handles and switch to the last one in the set
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }
        System.out.println("[NAV] Switched to Hotel Details Tab ✅");
    }

    /**
     * Extracts and prints the visible text of the entire page body.
     * Used as a fallback to capture room types, amenities, and pricing 
     * when specific element locators are highly dynamic or obfuscated.
     */
    public void printRoomDetails() {
        try {
            // Capturing the full body text for debugging or data verification
            System.out.println(driver.findElement(By.tagName("body")).getText());
        } catch (Exception e) {
            System.out.println("Room data not found: " + e.getMessage());
        }
    }

    /**
     * Executes a JavaScript command to scroll to the very bottom of the page.
     * This is often required to trigger "Lazy Loading" elements or to reach 
     * footer-based components like Reviews or Policies.
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        System.out.println("[ACTION] Scrolled to bottom of page ✅");
    }

    /**
     * Checks if the keyword "Reviews" exists within the page's HTML source.
     * A lightweight way to verify that the review section has loaded without 
     * relying on complex XPath/CSS selectors.
     * @return boolean true if 'Reviews' text is present.
     */
    public boolean isReviewVisible() {
        boolean isVisible = driver.getPageSource().contains("Reviews");
        System.out.println("[CHECK] Reviews visible: " + isVisible);
        return isVisible;
    }
}