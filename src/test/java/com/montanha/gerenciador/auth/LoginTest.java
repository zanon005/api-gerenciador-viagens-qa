package com.montanha.gerenciador.auth;

import com.montanha.gerenciador.core.GerenciadorViagensMontanhaBaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginTest extends GerenciadorViagensMontanhaBaseTest {

    @Test
    public void testGivenValidLoginAndPasswordWhenLoginThenReturnsAuthToken() {
        JSONObject auth = new JSONObject();
        try {
            auth.put("email", "admin@email.com");
            auth.put("senha", "654321");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestAssured.basePath = "/api";

        given()
                .body(auth.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/v1/auth")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .log().body()
                .body("data", hasKey("token"))
                .body("data.token", instanceOf(String.class))
                .body("errors.size()", is(0))
        ;
    }

    public static String getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken() {
        JSONObject auth = new JSONObject();
        try {
            auth.put("email", "admin@email.com");
            auth.put("senha", "654321");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return given()
                .body(auth.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/auth")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data", hasKey("token"))
                .body("data.token", instanceOf(String.class))
                .body("errors.size()", is(0))
                .extract().path("data.token")
        ;
    }

    public static String getGivenUserLoginAndPasswordWhenLoginThenReturnsAuthToken() {
        JSONObject auth = new JSONObject();
        try {
            auth.put("email", "usuario@email.com");
            auth.put("senha", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return given()
                .body(auth.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/auth")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data", hasKey("token"))
                .body("data.token", instanceOf(String.class))
                .body("errors.size()", is(0))
                .extract().path("data.token")
        ;
    }
}
