package com.montanha.gerenciador.core;

import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class HealthCheck {
    @Test
    public void testApiHealthCheck(){
        given()
        .when()
                .get("http://localhost:8089/")
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
        ;
    }
}
