package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.DriverFactory;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = DriverFactory.createDriver();
        driver.get("https://www.booking.com");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}