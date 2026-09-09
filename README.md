# Teste Técnico QA — Web, API e Performance

Repositório com a automação dos 3 desafios técnicos de QA:

| Módulo | Escopo | Stack |
|---|---|---|
| [`api-tests/`](api-tests/README.md) | Testes de API da [Dog API](https://dog.ceo/dog-api/documentation) | Java + RestAssured + JUnit5 |
| [`web-tests/`](web-tests/README.md) | Testes de busca do [blog do Agi](https://blogdoagi.com.br/) | Java + Selenium + JUnit5 |
| [`performance-tests/`](performance-tests/README.md) | Teste de carga/pico do fluxo de compra do [BlazeDemo](https://blazedemo.com) | JMeter |

## Pré-requisitos

- Java 21 (JDK)
- Maven 3.9+
- Google Chrome instalado (para os testes web)
- Apache JMeter (apenas para o módulo de performance)

## Como rodar

```
mvn test                    # roda api-tests e web-tests
mvn test -pl api-tests      # roda só a API
mvn test -pl web-tests      # roda só o Web
```

Instruções de execução e relatório de cada módulo estão no README específico de cada pasta.
