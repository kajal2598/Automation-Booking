package pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * SearchResultsPage Class
 * Manages the interaction with the property listing page.
 * Includes advanced data extraction from dynamic lists and defensive filtering logic.
 */
public class SearchResultsPage {
    WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    // --- Locators (Global to the class for reusability) ---
    By hotelCards = By.xpath("//div[@data-testid='property-card']");
    By hotelName = By.xpath(".//div[@data-testid='title']");
    By price = By.xpath(".//span[@data-testid='price-and-discounted-price']");
    By rating = By.xpath(".//div[@data-testid='review-score']");

    /**
     * Iterates through all visible property cards and extracts key data points.
     * Uses Relative XPaths (starting with ".") to search specifically inside each card.
     */
    public void printHotelListing() { 
        System.out.println("\n--- EXTRACTING HOTEL DATA ---");
        
        // Capture all individual property containers
        List<WebElement> cards = driver.findElements(hotelCards);
        
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hotel listings found on the page.");
            return;
        }

        for (WebElement card : cards) {
            try {
                // 1. Fetch Name: Standard extraction
                String name = card.findElement(hotelName).getText();
                
                // 2. Fetch Price: Implements a multi-selector check (OR logic) to handle 
                // different UI variations like discounted vs. regular pricing.
                String priceText = card.findElements(By.xpath(".//span[@data-testid='price-and-discounted-price'] | .//div[contains(@class, 'f6431b446c')]")).size() > 0 
                               ? card.findElement(By.xpath(".//span[@data-testid='price-and-discounted-price'] | .//div[contains(@class, 'f6431b446c')]")).getText() 
                               : "Show Prices to view";

                // 3. Fetch Rating: Uses split() to isolate the numeric score from the descriptive text
                String ratingText = card.findElements(rating).size() > 0 
                                 ? card.findElement(rating).getText().split("\n")[0] 
                                 : "No Rating";

                // 4. Fetch Image URL: Extracts the 'src' attribute for visual verification
                String imageUrl = card.findElement(By.xpath(".//img[@data-testid='image']")).getAttribute("src");

                // Log the structured data to the console
                System.out.println(String.format("🏨 %s", name));
                System.out.println(String.format("   💰 Price: %s | ⭐ Rating: %s", priceText, ratingText));
                System.out.println("   🖼️ Image: " + imageUrl);
                System.out.println("----------------------------------------------");
                
            } catch (Exception e) {
                // Defensive logic: If one card fails (e.g., ad listing), skip it and move to the next
                System.out.println("⏩ Skipping a listing due to incomplete data.");
            }
        }
    }

    /**
     * Applies search filters (Breakfast, Star rating, etc.) to the results page.
     * Uses JavascriptExecutor to bypass 'Element Click Intercepted' errors caused by sticky headers.
     */
    public void applyFilters() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Target the 'Breakfast included' filter specifically
            By breakfastFilter = By.xpath("//div[contains(text(), 'Breakfast included')]");

            try {
                WebElement filterElement = wait.until(ExpectedConditions.presenceOfElementLocated(breakfastFilter));
                
                // Scroll the filter into the center of the viewport to ensure it is clickable
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", filterElement);
                
                Thread.sleep(500); // Visual stability pause

                // Execute a JS click to force interaction even if hidden under an overlay
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", filterElement);
                
                System.out.println("Breakfast filter applied successfully.");
                
                // Wait for the AJAX refresh to finish before proceeding
                Thread.sleep(2000); 
                
            } catch (Exception e) {
                System.out.println("Could not apply breakfast filter: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("[STEP] Filter: Breakfast filter skipped (Not found) ⚠️");
        }
    }

    /**
     * Returns the total count of properties currently visible on the page.
     */
    public int getFilteredCount(){
        return driver.findElements(hotelCards).size();
    }

    /**
     * Selects the first property from the results list.
     * Uses Action chains to clear potential overlays before clicking.
     */
    public void clickFirstHotel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Global defensive action: Hit ESC to close any random popups or tooltips
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        
        try {
            WebElement firstHotel = wait.until(ExpectedConditions.elementToBeClickable(hotelCards));
            
            // Re-centering the element to avoid interaction with sticky site headers
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstHotel);
            
            // Final JS click to trigger the hotel details in a new tab
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstHotel);
            
            System.out.println("First hotel clicked successfully via JS.");
        } catch (Exception e) {
            System.out.println("Failed to click hotel: " + e.getMessage());
        }
    }
}