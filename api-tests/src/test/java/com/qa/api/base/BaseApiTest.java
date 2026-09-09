package com.qa.api.base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    @BeforeAll
    static void configurarBaseUri() {
        RestAssured.baseURI = "https://dog.ceo/api";
    }
}
