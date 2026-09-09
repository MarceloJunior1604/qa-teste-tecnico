package com.qa.api.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    @BeforeAll
    static void configureRestAssured() {
        RestAssured.baseURI = "https://dog.ceo/api";
        RestAssured.filters(new AllureRestAssured());
    }
}
