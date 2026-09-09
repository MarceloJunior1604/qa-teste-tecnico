package com.qa.performance;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class PurchaseFlightPerformanceTest {

    @Test
    void deveSuportarCargaDe250FluxosPorSegundo() throws Exception {
        testPlan(
                httpDefaults().url("https://www.blazedemo.com"),
                rpsThreadGroup()
                        .maxThreads(300)
                        .rampToAndHold(250, Duration.ofSeconds(30), Duration.ofMinutes(10))
                        .children(purchaseFlowSamplers()),
                htmlReporter("target/reports/load-test")
        ).run();
    }

    @Test
    void deveSuportarPicoAcimaDoAlvo() throws Exception {
        testPlan(
                httpDefaults().url("https://www.blazedemo.com"),
                rpsThreadGroup()
                        .maxThreads(800)
                        .rampToAndHold(600, Duration.ofSeconds(10), Duration.ofMinutes(1))
                        .children(purchaseFlowSamplers()),
                htmlReporter("target/reports/spike-test")
        ).run();
    }

    private us.abstracta.jmeter.javadsl.http.DslHttpSampler[] purchaseFlowSamplers() {
        return new us.abstracta.jmeter.javadsl.http.DslHttpSampler[]{
                httpSampler("home", "/"),

                httpSampler("reserve-flight", "/reserve.php")
                        .method("POST")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .rawParam("fromPort", "Paris")
                        .rawParam("toPort", "London")
                        .children(
                                responseAssertion().containsSubstrings("name=\"flight\""),
                                regexExtractor("flight", "value=\"([^\"]+)\" name=\"flight\""),
                                regexExtractor("price", "value=\"([^\"]+)\" name=\"price\""),
                                regexExtractor("airline", "value=\"([^\"]+)\" name=\"airline\"")
                        ),

                httpSampler("purchase-flight", "/purchase.php")
                        .method("POST")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .rawParam("flight", "${flight}")
                        .rawParam("price", "${price}")
                        .rawParam("airline", "${airline}")
                        .rawParam("fromPort", "Paris")
                        .rawParam("toPort", "London")
                        .children(
                                responseAssertion().containsSubstrings("name=\"inputName\"")
                        ),

                httpSampler("confirm-purchase", "/confirmation.php")
                        .method("POST")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .rawParam("_token", "")
                        .rawParam("inputName", "John Doe")
                        .rawParam("address", "123 Main St")
                        .rawParam("city", "Springfield")
                        .rawParam("state", "IL")
                        .rawParam("zipCode", "62701")
                        .rawParam("cardType", "visa")
                        .rawParam("creditCardNumber", "4111111111111111")
                        .rawParam("creditCardMonth", "12")
                        .rawParam("creditCardYear", "2027")
                        .rawParam("nameOnCard", "John Doe")
                        .children(
                                responseAssertion().containsSubstrings("Thank you for your purchase today!")
                        )
        };
    }
}
