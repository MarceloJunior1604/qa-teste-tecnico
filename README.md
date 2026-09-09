# Teste Técnico QA — Web, API e Performance

Repositório com a automação dos 3 desafios técnicos de QA. Relatórios sempre atualizados aqui: https://marcelojunior1604.github.io/qa-teste-tecnico/

| Módulo | Escopo | Stack |
|---|---|---|
| [`api-tests/`](api-tests/README.md) | Testes de API da [Dog API](https://dog.ceo/dog-api/documentation) | Java + RestAssured + JUnit5 |
| [`web-tests/`](web-tests/README.md) | Testes de busca do [blog do Agi](https://blogdoagi.com.br/) | Java + Selenium + JUnit5 |
| [`performance-tests/`](performance-tests/README.md) | Teste de carga/pico do fluxo de compra do [BlazeDemo](https://blazedemo.com) | JMeter Java DSL |

## Pré-requisitos

- Java 21 (JDK)
- Maven 3.9+
- Google Chrome instalado (para os testes web)

Nada de instalar JMeter separado — o módulo de performance usa a versão Java da lib, que já vem como dependência Maven.

## Como rodar

```
mvn test -pl api-tests         # só a API
mvn test -pl web-tests         # só o Web
mvn test -pl performance-tests # carga + pico, ~12-14min
mvn test                       # roda os 3 módulos, incluindo o de performance
```

Instruções de execução e relatório de cada módulo estão no README específico de cada pasta.

## CI/CD

O repositório tem GitHub Actions configurado em `.github/workflows/`: `api.yml` e `web.yml` rodam automaticamente a cada push/PR nos respectivos módulos (e também manualmente), `performance.yml` é só manual. Há ainda `pages.yml`, que publica os relatórios de execução como um site estático.
