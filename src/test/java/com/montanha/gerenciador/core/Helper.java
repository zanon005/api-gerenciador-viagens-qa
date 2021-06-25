package com.montanha.gerenciador.core;

import com.montanha.gerenciador.auth.LoginTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Helper extends GerenciadorViagensMontanhaBaseTest{

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

    public static Response getResponseFromCreatingNewTrip(){
        String token_adm = getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken();
        JSONObject new_trip = new JSONObject();
        try {
            new_trip.put("acompanhante", "Matheus");
            new_trip.put("dataPartida", "2021-06-24"); // YYYY-MM-DD
            new_trip.put("dataRetorno", "2021-06-28");
            new_trip.put("localDeDestino", "Bahia");
            new_trip.put("regiao", "Norte");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return given()
                .header("Authorization", token_adm)
                .body(new_trip.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/viagens")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("data.acompanhante", is("Matheus"))
                .body("data.localDeDestino", is("Bahia"))
                .extract().response()
        ;
    }

    public static ArrayList<Integer> getListOfAllTripIDs(){
        String token = getGivenUserLoginAndPasswordWhenLoginThenReturnsAuthToken();

        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/viagens")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().path("data.id")
        ;
    }
}
