package com.qa.performance;

import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

import java.time.Duration;

import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class PurchaseFlightPerformanceTest {

    private static final String FROM_PORT = "Paris";
    private static final String TO_PORT = "London";
    private static final int TARGET_RPS = 250;
    private static final Duration MAX_P90 = Duration.ofSeconds(2);

    @Test
    void deveSuportarCargaDe250FluxosPorSegundo() throws Exception {
        var stats = testPlan(
                httpDefaults().url("https://www.blazedemo.com"),
                rpsThreadGroup()
                        .maxThreads(300)
                        .rampToAndHold(TARGET_RPS, Duration.ofSeconds(30), Duration.ofMinutes(10))
                        .children(purchaseFlowSamplers()),
                htmlReporter("target/reports/load-test")
        ).run();

        Duration p90 = stats.overall().sampleTime().perc90();
        double throughput = stats.overall().samples().perSecond();

        assertEquals(0, stats.overall().errorsCount(), "nao deveria ter erros dentro do alvo de carga");
        assertTrue(p90.compareTo(MAX_P90) < 0, "p90 acima de " + MAX_P90.toMillis() + "ms: " + p90.toMillis() + "ms");
        assertTrue(throughput >= TARGET_RPS, "throughput abaixo do alvo de " + TARGET_RPS + " req/s: " + throughput);
    }

    @Test
    void deveSuportarPicoAcimaDoAlvo() throws Exception {
        // teste exploratorio: observa o comportamento acima da capacidade,
        // sem assert de criterio de aceitacao (nao e o que ele mede)
        testPlan(
                httpDefaults().url("https://www.blazedemo.com"),
                rpsThreadGroup()
                        .maxThreads(800)
                        .rampToAndHold(600, Duration.ofSeconds(10), Duration.ofMinutes(1))
                        .children(purchaseFlowSamplers()),
                htmlReporter("target/reports/spike-test")
        ).run();
    }

    private DslHttpSampler[] purchaseFlowSamplers() {
        return new DslHttpSampler[]{
                httpSampler("home", "/"),

                httpSampler("reserve-flight", "/reserve.php")
                        .method("POST")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .rawParam("fromPort", FROM_PORT)
                        .rawParam("toPort", TO_PORT)
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
                        .rawParam("fromPort", FROM_PORT)
                        .rawParam("toPort", TO_PORT)
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
