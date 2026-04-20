package pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class HotelDetailsPage {
    WebDriver driver;

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToNewTab() {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }
    }

    public void printRoomDetails() {
        try {
            System.out.println(driver.findElement(By.tagName("body")).getText());
        } catch (Exception e) {
            System.out.println("Room data not found");
        }
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public boolean isReviewVisible() {
        return driver.getPageSource().contains("Reviews");
    }
}