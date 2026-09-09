package com.qa.api;

import com.qa.api.base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Dog API")
@Feature("Imagem aleatória")
public class BreedImagesRandomTest extends BaseApiTest {

    @Test
    @DisplayName("Deve retornar status 200 ao buscar imagem aleatória")
    @Description("GET /breeds/image/random deve responder 200 com a URL de uma única imagem aleatória.")
    void deveRetornarStatus200AoBuscarImagemAleatoria() {
        given()
        .when()
            .get("/breeds/image/random")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/breed-images-random-schema.json"));
    }
}
