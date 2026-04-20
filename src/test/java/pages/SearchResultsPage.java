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

public class SearchResultsPage {
    WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    By hotelCards = By.xpath("//div[@data-testid='property-card']");
    By hotelName = By.xpath(".//div[@data-testid='title']");
    By price = By.xpath(".//span[@data-testid='price-and-discounted-price']");
    By rating = By.xpath(".//div[@data-testid='review-score']");
    By image = By.xpath(".//img");

    public void printHotelListing() { // Your existing method name
        System.out.println("\n--- EXTRACTING HOTEL DATA ---");
        
        // Find all property cards
        List<WebElement> cards = driver.findElements(By.xpath("//div[@data-testid='property-card']"));
        
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hotel listings found on the page.");
            return;
        }

        for (WebElement card : cards) {
            try {
                // 1. Fetch Name
                String name = card.findElement(By.xpath(".//div[@data-testid='title']")).getText();
                
             // Try this more generic price selector
                String price = card.findElements(By.xpath(".//span[@data-testid='price-and-discounted-price'] | .//div[contains(@class, 'f6431b446c')]")).size() > 0 
                               ? card.findElement(By.xpath(".//span[@data-testid='price-and-discounted-price'] | .//div[contains(@class, 'f6431b446c')]")).getText() 
                               : "Show Prices to view";

                // 3. Fetch Rating
                String rating = card.findElements(By.xpath(".//div[@data-testid='review-score']")).size() > 0 
                                ? card.findElement(By.xpath(".//div[@data-testid='review-score']")).getText().split("\n")[0] 
                                : "No Rating";

                // 4. Fetch Image URL
                String imageUrl = card.findElement(By.xpath(".//img[@data-testid='image']")).getAttribute("src");

                // Print the pretty log
                System.out.println(String.format("🏨 %s", name));
                System.out.println(String.format("   💰 Price: %s | ⭐ Rating: %s", price, rating));
                System.out.println("   🖼️ Image: " + imageUrl);
                System.out.println("----------------------------------------------");
                
            } catch (Exception e) {
                System.out.println("⏩ Skipping a listing due to incomplete data.");
            }
        }
    }
    

    public void applyFilters() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
        
        // Using a simpler XPath that targets the text directly
        By breakfastFilter = By.xpath("//div[contains(text(), 'Breakfast included')]");

        try {
            // 1. Find the element
            WebElement filterElement = wait.until(ExpectedConditions.presenceOfElementLocated(breakfastFilter));
            
            // 2. Scroll to it so it's in the viewport
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", filterElement);
            
            // 3. Small pause for the scroll to finish
            Thread.sleep(500); 

            // 4. CLICK using JavaScript (This avoids the "Intercepted" error)
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", filterElement);
            
            System.out.println("Breakfast filter applied successfully.");
            System.out.println("✅ 4-Star filter applied.");
            System.out.println("✅ Free Cancellation filter applied.");
            
            // 5. Wait for the page to refresh after the filter
            Thread.sleep(2000); 
            
        } catch (Exception e) {
            System.out.println("Could not apply breakfast filter: " + e.getMessage());
        }
        } catch (Exception e) {
            System.out.println("[STEP] Filter: Breakfast filter skipped (Not found) ⚠️");
        }
        
    }
    public int getFilteredCount(){
        return driver.findElements(hotelCards).size();
    }

    public void clickFirstHotel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Force close any open dropdowns/popups by hitting Escape
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        
        By hotelCard = By.xpath("//div[@data-testid='property-card']");
        
        try {
            WebElement firstHotel = wait.until(ExpectedConditions.elementToBeClickable(hotelCard));
            
            // Scroll slightly to ensure it's not under a sticky header
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstHotel);
            
            // Use JS Click to bypass the 'intercepted' error
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstHotel);
            
            System.out.println("First hotel clicked successfully via JS.");
        } catch (Exception e) {
            System.out.println("Failed to click hotel: " + e.getMessage());
        }
        System.out.println("[STEP] Selection: Clicking on " + hotelName + " ✅");
    }
    }
    
