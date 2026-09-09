package com.qa.web.tests;

import com.qa.web.base.BaseTest;
import com.qa.web.pages.HomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Blog do Agi")
@Feature("Busca de artigos")
class SearchTest extends BaseTest {

    @Test
    @DisplayName("Busca com termo existente retorna resultados relevantes")
    @Description("Buscar por um termo com correspondência deve exibir o heading correto e ao menos um post na lista de resultados.")
    void buscaComTermoExistenteRetornaResultados() {
        var results = new HomePage(driver)
                .open()
                .searchFor("como");

        assertTrue(results.getHeading().contains("como"));
        assertTrue(results.hasResults());
        assertFalse(results.getResultTitles().isEmpty());
    }

    @Test
    @DisplayName("Busca sem correspondência exibe mensagem de nada encontrado")
    @Description("Buscar por um termo sem correspondência não deve listar posts e deve exibir a mensagem exata de erro amigável.")
    void buscaSemCorrespondenciaExibeMensagem() {
        var results = new HomePage(driver)
                .open()
                .searchFor("zxqwnaoexisteresultado999");

        assertFalse(results.hasResults());
        assertTrue(results.getNoResultsMessage()
                .contains("Lamentamos, mas nada foi encontrado para sua pesquisa"));
    }

    @Test
    @DisplayName("Busca com campo vazio não quebra a página")
    @Description("Submeter a busca sem digitar nenhum termo deve cair no fallback do WordPress (lista geral de posts), sem erro ou tela quebrada.")
    void buscaComCampoVazioNaoQuebra() {
        var results = new HomePage(driver)
                .open()
                .searchFor("");

        assertTrue(results.getHeading().contains("Resultados encontrados para"));
        assertTrue(results.hasResults());
    }
}
