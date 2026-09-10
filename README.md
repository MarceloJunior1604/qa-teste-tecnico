# Teste Técnico QA: Web, API e Performance

Repositório com a automação dos 3 desafios técnicos de QA. Relatórios sempre atualizados aqui: https://marcelojunior1604.github.io/qa-teste-tecnico/

Clica no nome de cada módulo na tabela abaixo pra ir direto no README detalhado dele (cenários, como rodar, relatório):

| Módulo | Escopo | Stack |
|---|---|---|
| [`api-tests/`](api-tests/README.md) | Testes de API da [Dog API](https://dog.ceo/dog-api/documentation) | Java + RestAssured + JUnit5 |
| [`web-tests/`](web-tests/README.md) | Testes de busca do [blog do Agi](https://blogdoagi.com.br/) | Java + Selenium + JUnit5 |
| [`performance-tests/`](performance-tests/README.md) | Teste de carga/pico do fluxo de compra do [BlazeDemo](https://blazedemo.com) | JMeter Java DSL |

## Estrutura do projeto

```
qa-teste-tecnico/
├── pom.xml                    # pom pai, so agrega os 3 módulos
├── api-tests/                 # RestAssured + JUnit5
├── web-tests/                 # Selenium + JUnit5
├── performance-tests/         # JMeter Java DSL + JUnit5
└── .github/workflows/         # api.yml, web.yml, performance.yml, pages.yml
```

É um monorepo Maven multi-módulo: o `pom.xml` da raiz só agrega, cada módulo tem seu próprio `pom.xml` com as dependências que usa (RestAssured só no api-tests, Selenium só no web-tests, etc). Dá pra rodar cada um isolado (`mvn test -pl <módulo>`) ou todos juntos.

## Padrões usados

- Um pacote por módulo (`com.qa.api`, `com.qa.web`, `com.qa.performance`), sem dependência cruzada entre eles.
- Classes e métodos em inglês (convenção normal de Java/Selenium/RestAssured); nomes de `@Test` e das anotações do Allure (`@DisplayName`, `@Description`) em português, porque descrevem os cenários do próprio enunciado.
- `web-tests` usa Page Object Model (`HomePage`, `SearchResultsPage`), separando a interação com a página da lógica do teste.
- Allure como relatório em API e Web; no Performance é o dashboard nativo do JMeter, que já vem pronto na lib.

## Pré-requisitos

- Java 21 (JDK)
- Maven 3.9+
- Google Chrome instalado (para os testes web)

Nada de instalar JMeter separado: o módulo de performance usa a versão Java da lib, que já vem como dependência Maven.

## Como rodar

```
mvn test -pl api-tests         # só a API
mvn test -pl web-tests         # só o Web
mvn test -pl performance-tests # carga + pico, ~12-14min
mvn test                       # roda os 3 módulos, incluindo o de performance
```

Instruções de execução e relatório de cada módulo estão no README específico de cada pasta.

### Exemplo completo: API

```
$ mvn test -pl api-tests
...
[INFO] Running com.qa.api.BreedsListTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.qa.api.BreedImagesTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.qa.api.BreedImagesRandomTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

$ mvn -pl api-tests allure:serve
# abre o relatório no navegador automaticamente
```

### Exemplo completo: Web

```
$ mvn test -pl web-tests
...
[INFO] Running com.qa.web.tests.SearchTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

$ mvn -pl web-tests allure:serve
```

Pra rodar sem abrir o navegador de verdade (modo usado no CI): `mvn test -pl web-tests -Dheadless=true`.

### Exemplo completo: Performance

```
$ mvn test -pl performance-tests
...
[INFO] Running com.qa.performance.PurchaseFlightPerformanceTest
 =  152181 in 00:10:35 =  241,2/s Avg: 378 Min: 249 Max: 5147 Err: 0 (0,00%)
 =   20545 in 00:00:56 =  261,1/s Avg: 2373 Min: 187 Max: 8837 Err: 65 (0,32%)
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Aqui não tem `allure:serve`. O relatório é o dashboard nativo do JMeter, gerado direto em `performance-tests/target/reports/<load-test|spike-test>/<timestamp>/index.html`, que já abre puro no navegador sem precisar de servidor.

## CI/CD

O repositório tem GitHub Actions configurado em `.github/workflows/`: `api.yml` e `web.yml` rodam automaticamente a cada push/PR nos respectivos módulos (e também manualmente), `performance.yml` é só manual. Há ainda `pages.yml`, que publica os relatórios de execução como um site estático.

## Melhorias futuras

Coisas que eu faria se isso fosse um projeto real, não um desafio técnico:

- Notificação em Slack/Teams/Discord via webhook ao final da execução (passou/falhou, link do relatório), em vez de depender de alguém ir checar a aba Actions.
- Integrar com a pipeline de deploy de verdade da aplicação, como um step/gate antes ou depois de subir uma versão nova, em vez de rodar isolado só nesse repositório.
- Tags nos testes (`@Tag` do JUnit5, tipo `smoke` e `regressivo`) pra escolher o quanto rodar dependendo do contexto: smoke rápido no deploy, regressão completa em outro momento.
- Execução agendada, tipo de 8h às 18h de 2 em 2 horas (`schedule`/`cron` no Actions). Em banco os sistemas de times diferentes se comunicam o tempo todo, então isso ajuda a pegar rápido quando o deploy de outro time quebrou alguma integração que afeta a sua aplicação, mesmo sem ninguém ter mexido no seu próprio código naquele dia.
