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
@Feature("Listagem de raças")
public class BreedsListTest extends BaseApiTest {

    @Test
    @DisplayName("Deve retornar status 200 ao listar todas as raças")
    @Description("GET /breeds/list/all deve responder 200 com o corpo no formato esperado: objeto de raça mapeando para lista de sub-raças.")
    void deveRetornarStatus200AoListarTodasAsRacas() {
        given()
        .when()
            .get("/breeds/list/all")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/breeds-list-schema.json"));
    }
}
