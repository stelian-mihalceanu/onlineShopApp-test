package com.onlinestore.qa;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
    void productsEndpointIsReachable() {
        given()
            .when().get("/api/products")
            .then().statusCode(200);
    }

    @Test
    void protectedCartEndpointRejectsAnonymousUser() {
        given()
            .when().get("/api/cart")
            .then().statusCode(401);
    }

    @Test
    void invalidRegistrationIsRejected() {
        given()
            .contentType("application/json")
            .body("{\"username\":\"\",\"password\":\"\"}")
            .when().post("/api/auth/register")
            .then().statusCode(400)
            .body("error", notNullValue());
    }
}
