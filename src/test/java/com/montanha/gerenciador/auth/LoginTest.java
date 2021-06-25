package com.montanha.gerenciador.auth;

import com.montanha.gerenciador.core.GerenciadorViagensMontanhaBaseTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginTest extends GerenciadorViagensMontanhaBaseTest {

    @Test
    public void testGivenValidLoginAndPasswordWhenLoginThenReturnsAuthToken() {
        JSONObject auth = new JSONObject();
        auth.put("email", "admin@email.com");
        auth.put("senha", "654321");

        given()
                .body(auth.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/auth")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .log().body()
                .body("data", hasKey("token"))
                .body("data.token", instanceOf(String.class))
                .body("errors.size()", is(0))
        ;
    }
}
