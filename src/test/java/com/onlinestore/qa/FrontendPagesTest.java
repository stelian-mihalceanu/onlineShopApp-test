package com.onlinestore.qa;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrontendPagesTest {

    private static WebDriver driver;
    private static String baseUrl;

    @BeforeAll
    static void setUp() {
        baseUrl = System.getProperty("baseUrl", "http://127.0.0.1:8080");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1440,1000");
        driver = new ChromeDriver(options);
        new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void publicCatalogAndProductPagesRender() {
        driver.get(baseUrl + "/products");
        assertTrue(driver.getTitle() != null && !driver.getTitle().isBlank());
        assertTrue(driver.findElements(By.cssSelector("a[href*='/product/']")).size() > 0,
                "Product catalog should expose at least one product link");

        driver.findElement(By.cssSelector("a[href*='/product/']")).click();
        assertTrue(driver.getCurrentUrl().contains("/product/"));
        assertTrue(driver.findElements(By.cssSelector("form[action*='/cart']")).size() > 0,
                "Product detail should expose add-to-cart form");
    }

    @Test
    void protectedPagesRedirectAnonymousUserToLogin() {
        driver.get(baseUrl + "/cart");
        assertEquals(baseUrl + "/login", driver.getCurrentUrl());

        driver.get(baseUrl + "/checkout");
        assertEquals(baseUrl + "/login", driver.getCurrentUrl());
    }
}
