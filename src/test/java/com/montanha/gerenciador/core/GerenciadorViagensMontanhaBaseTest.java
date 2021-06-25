package com.montanha.gerenciador.core;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class GerenciadorViagensMontanhaBaseTest {

	@BeforeClass
	public static void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8089;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

}
