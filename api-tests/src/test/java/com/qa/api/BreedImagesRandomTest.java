package com.qa.api;

import com.qa.api.base.BaseApiTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BreedImagesRandomTest extends BaseApiTest {

    @Test
    void deveRetornarStatus200AoBuscarImagemAleatoria() {
        given()
        .when()
            .get("/breeds/image/random")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/breed-images-random-schema.json"));
    }
}
