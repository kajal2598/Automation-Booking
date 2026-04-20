package pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class HomePage {
    WebDriver driver;

    By logo = By.xpath("//a[@data-testid = 'header-logo-link']");

    By searchBox = By.name("ss");
    By searchBtn = By.xpath("//button[@type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterDestination(String place) {
        WaitUtils.waitForElement(driver, searchBox).sendKeys(place);
    }

    public void selectDates() {
        LocalDate today = LocalDate.now();
        LocalDate checkIn = today.plusDays(10);
        LocalDate checkOut = today.plusDays(13);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        driver.findElement(By.cssSelector("span[data-testid='searchbox-dates-container']")).click();

        driver.findElement(By.cssSelector("span[data-date='" + checkIn.format(formatter) + "']")).click();
        driver.findElement(By.cssSelector("span[data-date='" + checkOut.format(formatter) + "']")).click();
    }

    public void clickSearch() {
        driver.findElement(searchBtn).click();
    }
}