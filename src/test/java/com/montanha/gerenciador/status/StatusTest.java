package com.montanha.gerenciador.status;

import com.montanha.gerenciador.core.GerenciadorViagensMontanhaBaseTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class StatusTest extends GerenciadorViagensMontanhaBaseTest {

    @Test
    public void testGivenApplicationIsRunningWhenGetStatusThenReturnOK(){
        given()
        .when()
                .get("/api/v1/status")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("funcionando corretamente"))
        ;
    }
}
