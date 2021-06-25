package com.montanha.gerenciador.viagens;

import com.montanha.gerenciador.auth.LoginTest;
import com.montanha.gerenciador.core.GerenciadorViagensMontanhaBaseTest;
import com.montanha.gerenciador.core.Helper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ViagensTest extends GerenciadorViagensMontanhaBaseTest{

    @Test
    public void testGivenImAdministratorWhenCreateNewTripThenGetStatusCode201(){
        String token = LoginTest.getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken();
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

        given()
                .header("Authorization", token)
                .body(new_trip.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/viagens")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("data.acompanhante", is("Matheus"))
                .body("data.localDeDestino", is("Bahia"))
        ;
    }

    @Test
    public void testGivenImUserWhenGetTripsThenReturnListOfTrips(){
        String token = LoginTest.getGivenUserLoginAndPasswordWhenLoginThenReturnsAuthToken();

        given()
                .header("Authorization", token)
                .params("regiao", "Norte")
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/viagens")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
        ;
    }

    // PROBLEMA NA API TEMPO
    @Test
    public void testGivenImAdministratorWhenGetTripWithIDThenReturnTripWithThatID(){
        JsonPath new_trip = Helper.getResponseFromCreatingNewTrip().jsonPath();
        int new_trip_id = new_trip.get("data.id");
        String token_user = LoginTest.getGivenUserLoginAndPasswordWhenLoginThenReturnsAuthToken();

        given()
                .header("Authorization", token_user)
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/viagens/{trip_id}",new_trip_id)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all()
        ;
    }

    //BUG, API SEMPRE RETORNA 204 AO INVES DE 200 ou 201!!
    @Test
    public void testGivenImAdministratorWhenUpdateOneTripThenReturnTheUpdatedTrip(){
        ArrayList<Integer> trip_ids = Helper.getListOfAllTripIDs();
        Random rand = new Random();
        int old_trip_id = trip_ids.get( rand.nextInt(trip_ids.size()) );

        JSONObject updated_trip = new JSONObject();
        try {
            updated_trip.put("acompanhante", "NovoAcompanhante");
            updated_trip.put("dataPartida", "2021-06-24"); // YYYY-MM-DD
            updated_trip.put("dataRetorno", "2021-07-24");
            updated_trip.put("localDeDestino", "Arambare");
            updated_trip.put("regiao", "Sul");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token_adm = LoginTest.getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken();
        given()
                .header("Authorization", token_adm)
                .body(updated_trip.toString())
                .contentType(ContentType.JSON)
        .when()
                .put("/api/v1/viagens/{old_trip_id}", old_trip_id)
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id", is(equalTo(old_trip_id)))
                .body("data.acompanhante", is(equalTo("NovoAcompanhante")))
                .body("data.localDeDestino", is(equalTo("Arambare")))
        ;
    }

    @Test
    public void test(){
        ArrayList<Integer> trip_ids = Helper.getListOfAllTripIDs();
        Random rand = new Random();
        int old_trip_id = trip_ids.get( rand.nextInt(trip_ids.size()) );
        System.out.println(old_trip_id);
    }

    // API RETORNANDO 500 QUANDO SE TENTA DELETAR OQ NAO EXISTE!
    @Test
    public void testGivenImAdministratorWhenDeleteTripThenGetStatusCode204(){
        ArrayList<Integer> trip_ids = Helper.getListOfAllTripIDs();
        Random rand = new Random();
        int trip_id_to_delete = trip_ids.get( rand.nextInt(trip_ids.size()) );
        String token_adm = LoginTest.getGivenAdministratorLoginAndPasswordWhenLoginThenReturnsAuthToken();

        given()
                .header("Authorization", token_adm)
        .when()
                .delete("/api/v1/viagens/{trip_id_to_delete}", trip_id_to_delete)
        .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;

        /*given()
                .header("Authorization", token_adm)
        .when()
                .delete("/api/v1/viagens/{trip_id_to_delete}", trip_id_to_delete)
        .then()
                .statusCode(HttpStatus.SC_OK)
        ;*/
    }
}
