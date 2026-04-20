package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.WaitUtils;

public class HomePage {
    WebDriver driver;

    By logo = By.xpath("//a[@data-testid = 'header-logo-link']");

    By searchBox = By.name("ss");
    By searchBtn = By.xpath("//button[@type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void dismissGeniusPopup() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            // Try multiple possible selectors for the 'X' button
            By[] closeButtons = {
                By.cssSelector("button[aria-label='Dismiss sign-in info.']"),
                By.xpath("//button[contains(@aria-label, 'Dismiss')]"),
                By.xpath("//button[@role='button' and contains(., 'Close')]"),
                By.cssSelector(".modal-mask-closeBtn"), // Common class name for overlays
                By.xpath("//span[@class='fc63351294' and @aria-hidden='true']") // Specific to current Booking.com UI
            };

            WebElement element = null;
            for (By selector : closeButtons) {
                try {
                    element = wait.until(ExpectedConditions.elementToBeClickable(selector));
                    if (element != null) break;
                } catch (Exception next) {
                    // Keep trying the next selector
                }
            }

            if (element != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("Popup dismissed successfully.");
            }
        } catch (Exception e) {
            System.out.println("Could not find popup close button: " + e.getMessage());
        }
        System.out.println("[INIT] Genius Popup: Dismissed ✅");
    }

    
    public void enterDestination(String place) {
        WaitUtils.waitForElement(driver, searchBox).sendKeys(place);
    }

    public void selectDates(String checkInDate, String checkOutDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Using a more 'forgiving' selector in case data-testid changed
        // This looks for any element containing the 'dates-container' attribute
        By dateSelector = By.cssSelector("[data-testid*='dates-container']");
        
        try {
            // Wait for it to be clickable, not just visible
            WebElement dateContainer = wait.until(ExpectedConditions.elementToBeClickable(dateSelector));
            
            // Scroll into view just in case it's below the fold
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateContainer);
            
            dateContainer.click();
        } catch (Exception e) {
            // Fallback: Try clicking by the placeholder text if the ID failed
            WebElement fallback = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Check-in')] | //div[contains(., 'Check-in')]")));
            fallback.click();
        }
     // 1. Click the date picker to open the calendar (if not already open)
        // driver.findElement(By.xpath("//button[@data-testid='date-display-field-start']")).click();

        try {
            // 2. Select Check-in Date
            // We use an XPath that looks for the specific date string (YYYY-MM-DD)
            String checkInXpath = String.format("//span[@data-date='%s']", checkInDate);
            driver.findElement(By.xpath(checkInXpath)).click();
            
            Thread.sleep(500); // Small pause for UI stability

            // 3. Select Check-out Date
            String checkOutXpath = String.format("//span[@data-date='%s']", checkOutDate);
            driver.findElement(By.xpath(checkOutXpath)).click();
            
            System.out.println("[STEP] Dates Selected: " + checkInDate + " to " + checkOutDate + " ✅");
            
        } catch (Exception e) {
            System.out.println("❌ Could not select dates: " + e.getMessage());
        }
        System.out.println("[STEP] Search: Dates Selected ✅");
    }
    

    public void clickSearch() {
        driver.findElement(searchBtn).click();
    }
}