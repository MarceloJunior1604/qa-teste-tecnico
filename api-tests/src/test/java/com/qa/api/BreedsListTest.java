package com.qa.api;

import com.qa.api.base.BaseApiTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BreedsListTest extends BaseApiTest {

    @Test
    void deveRetornarStatus200AoListarTodasAsRacas() {
        given()
        .when()
            .get("/breeds/list/all")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/breeds-list-schema.json"));
    }
}
