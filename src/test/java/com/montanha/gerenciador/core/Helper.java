package com.montanha.gerenciador.core;

import com.montanha.gerenciador.auth.LoginTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class Helper extends GerenciadorViagensMontanhaBaseTest{

    public static Response getResponseFromCreatingNewTrip(){
        String token_adm = LoginTest.getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken();
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
        String token = LoginTest.getGivenUserLoginAndPasswordWhenLoginThenReturnsAuthToken();

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
