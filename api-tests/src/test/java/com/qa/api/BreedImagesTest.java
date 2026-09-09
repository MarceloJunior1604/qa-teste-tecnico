package com.qa.api;

import com.qa.api.base.BaseApiTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class BreedImagesTest extends BaseApiTest {

    @Test
    void deveRetornarImagensDeUmaRacaValida() {
        given()
            .pathParam("breed", "labrador")
        .when()
            .get("/breed/{breed}/images")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/breed-images-schema.json"));
    }

    @Test
    void deveRetornarStatus404ComMensagemDeRacaNaoEncontrada() {
        given()
            .pathParam("breed", "racaInexistente")
        .when()
            .get("/breed/{breed}/images")
        .then()
            .statusCode(404)
            .body(matchesJsonSchemaInClasspath("schemas/breed-not-found-schema.json"))
            .body("message", equalTo("Breed not found (main breed does not exist)"));
    }
}
