package com.qa.api;

import com.qa.api.base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Epic("Dog API")
@Feature("Imagens por raça")
public class BreedImagesTest extends BaseApiTest {

    @Test
    @DisplayName("Deve retornar imagens de uma raça válida")
    @Description("GET /breed/{breed}/images com uma raça existente deve responder 200 com uma lista de URLs de imagens.")
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
    @DisplayName("Deve retornar status 404 com mensagem de raça não encontrada")
    @Description("GET /breed/{breed}/images com uma raça inexistente deve responder 404 com status de erro e mensagem explicando que a raça não existe.")
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
