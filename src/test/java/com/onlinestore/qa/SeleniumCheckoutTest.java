package com.onlinestore.qa;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumCheckoutTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;

    @BeforeAll
    static void setUp() {
        baseUrl = System.getProperty("baseUrl", "http://127.0.0.1:8080");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1440,1000");
        if (Boolean.parseBoolean(System.getProperty("webdriverHeadless", "true"))) {
            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void registerLoginProductCartCheckout() {
        String username = "qa-" + UUID.randomUUID().toString().substring(0, 8);
        String password = "QaPassword123!";

        driver.get(baseUrl + "/register");
        assertTrue(driver.getTitle() != null);

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlMatches(".*/(products|)$"));
        driver.get(baseUrl + "/products");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href*='/product/']")));
        driver.findElement(By.cssSelector("a[href*='/product/']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[action*='/cart']")));
        driver.findElement(By.cssSelector("form[action*='/cart'] button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/cart"));

        driver.get(baseUrl + "/checkout");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[action='/checkout']")));
        driver.findElement(By.cssSelector("form[action='/checkout'] button[type='submit']")).click();

        wait.until(ExpectedConditions.urlMatches(".*/orders/\\d+"));
        assertTrue(driver.getCurrentUrl().contains("/orders/"));
    }
}
