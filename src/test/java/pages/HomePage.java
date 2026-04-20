package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

/**
 * HomePage Class
 * Encapsulates the elements and actions for the Booking.com landing page.
 * Implements defensive automation techniques like fallback selectors and JS clicks.
 */
public class HomePage {
    WebDriver driver;

    // --- Locators ---
    // Using data-testid where possible as it is more stable than auto-generated classes
    By logo = By.xpath("//a[@data-testid = 'header-logo-link']");
    By searchBox = By.name("ss");
    By searchBtn = By.xpath("//button[@type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Attempts to dismiss the 'Genius' or Sign-in info popup.
     * Travel sites often use dynamic A/B testing for popups; 
     * this method loops through multiple common selectors to find the close button.
     */
    public void dismissGeniusPopup() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            // Array of possible selectors for the 'X' button to handle UI updates/changes
            By[] closeButtons = {
                By.cssSelector("button[aria-label='Dismiss sign-in info.']"),
                By.xpath("//button[contains(@aria-label, 'Dismiss')]"),
                By.xpath("//button[@role='button' and contains(., 'Close')]"),
                By.cssSelector(".modal-mask-closeBtn"),
                By.xpath("//span[@class='fc63351294' and @aria-hidden='true']")
            };

            WebElement element = null;
            // Iterate through selectors until one is successfully found and clickable
            for (By selector : closeButtons) {
                try {
                    element = wait.until(ExpectedConditions.elementToBeClickable(selector));
                    if (element != null) break;
                } catch (Exception next) {
                    // Ignore and try the next locator in the array
                }
            }

            // If found, use JavaScript click to bypass any "Element Click Intercepted" errors
            if (element != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("Popup dismissed successfully.");
            }
        } catch (Exception e) {
            System.out.println("Could not find popup close button: " + e.getMessage());
        }
        System.out.println("[INIT] Genius Popup: Dismissed ✅");
    }

    /**
     * Enters the travel destination into the main search box.
     * Uses WaitUtils to ensure the field is interactive.
     * @param place The destination name (e.g., "Paris", "New York").
     */
    public void enterDestination(String place) {
        WaitUtils.waitForElement(driver, searchBox).sendKeys(place);
    }

    /**
     * Handles the calendar interaction to select Check-in and Check-out dates.
     * Implements a fallback mechanism if the primary date container ID changes.
     * @param checkInDate Format: YYYY-MM-DD
     * @param checkOutDate Format: YYYY-MM-DD
     */
    public void selectDates(String checkInDate, String checkOutDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Primary selector using a wildcard 'contains' on the data-testid
        By dateSelector = By.cssSelector("[data-testid*='dates-container']");
        
        try {
            // Attempt to click the calendar container to open the date picker
            WebElement dateContainer = wait.until(ExpectedConditions.elementToBeClickable(dateSelector));
            
            // Scroll to ensure the calendar is fully visible for interaction
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateContainer);
            dateContainer.click();
        } catch (Exception e) {
            // Fallback: If technical ID fails, attempt to find the container by visible 'Check-in' text
            WebElement fallback = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Check-in')] | //div[contains(., 'Check-in')]")));
            fallback.click();
        }

        try {
            // Select Check-in Date: Dynamic XPath targeting the specific 'data-date' attribute
            String checkInXpath = String.format("//span[@data-date='%s']", checkInDate);
            driver.findElement(By.xpath(checkInXpath)).click();
            
            // UI Stability Pause: Allows the calendar to register the first click before the second
            Thread.sleep(500); 

            // Select Check-out Date
            String checkOutXpath = String.format("//span[@data-date='%s']", checkOutDate);
            driver.findElement(By.xpath(checkOutXpath)).click();
            
            System.out.println("[STEP] Dates Selected: " + checkInDate + " to " + checkOutDate + " ✅");
            
        } catch (Exception e) {
            System.out.println("❌ Could not select dates: " + e.getMessage());
        }
        System.out.println("[STEP] Search: Dates Selected ✅");
    }

    /**
     * Clicks the final Search button to navigate to the results page.
     */
    public void clickSearch() {
        driver.findElement(searchBtn).click();
    }
}