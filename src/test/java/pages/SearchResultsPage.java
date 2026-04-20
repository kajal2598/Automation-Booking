package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public void printHotelListings() {
        List<WebElement> hotels = driver.findElements(hotelCards);

        for (WebElement hotel : hotels) {
            try {
                System.out.println("Name: " + hotel.findElement(hotelName).getText());
                System.out.println("Price: " + hotel.findElement(price).getText());
                System.out.println("Rating: " + hotel.findElement(rating).getText());
                System.out.println("Image: " + hotel.findElement(image).getAttribute("src"));
                System.out.println("----------------------");
            } catch (Exception e) {
                System.out.println("Skipping incomplete data");
            }
        }
    }

    public void applyFilters() {
        driver.findElement(By.xpath("//div[text()='4 stars']")).click();
        driver.findElement(By.xpath("//div[text()='Breakfast included']")).click();
        driver.findElement(By.xpath("//div[text()='Free cancellation']")).click();
    }

    public int getFilteredCount(){
        return driver.findElements(hotelCards).size();
    }

    public void clickFirstHotel() {
        driver.findElements(hotelCards).get(0).click();
    }
}