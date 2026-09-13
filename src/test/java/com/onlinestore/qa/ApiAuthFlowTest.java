package com.onlinestore.qa;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class ApiAuthFlowTest {

    @BeforeAll
    static void configure() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://127.0.0.1:8080");
    }

    @Test
    void registerLoginAndAccessProtectedCart() {
        String username = "qa-" + UUID.randomUUID().toString().substring(0, 8);
        String password = "QaPassword123!";

        given()
            .contentType("application/json")
            .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(200)
            .body("message", notNullValue());

        Response login = given()
            .contentType("application/json")
            .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .extract().response();

        String token = login.path("token");

        given()
            .auth().oauth2(token)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200);
    }

    @Test
    void weakRegistrationIsRejected() {
        String username = "qa-" + UUID.randomUUID().toString().substring(0, 8);

        given()
            .contentType("application/json")
            .body("{\"username\":\"" + username + "\",\"password\":\"123\"}")
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(400)
            .body("error", equalTo("Validation failed"));
    }
}
