package com.onlinestore.qa;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

class ApiSmokeTest {

    @BeforeAll
    static void configure() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://127.0.0.1:8080");
    }

    @Test
    void healthIsUp() {
        given()
            .when().get("/actuator/health")
            .then().statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    void protectedCartPageRedirectsAnonymousUserToLogin() {
        given()
            .redirects().follow(false)
            .when().get("/cart")
            .then().statusCode(302)
            .header("Location", containsString("/login"));
    }
}
