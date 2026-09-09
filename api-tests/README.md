# API Tests — Dog API

Automação dos endpoints da [Dog API](https://dog.ceo/dog-api/documentation):

- `GET /breeds/list/all`
- `GET /breed/{breed}/images`
- `GET /breeds/image/random`

## Stack

Java 21 · RestAssured · JUnit 5 · json-schema-validator (contrato) · Allure (relatório)

## Como rodar

```
mvn test -pl api-tests
```

## Relatório

```
mvn -pl api-tests allure:report
mvn -pl api-tests allure:serve
```

O relatório inclui request/response de cada chamada (via filtro `AllureRestAssured`), não só o resultado passa/falha.

## Cenários cobertos

- `GET /breeds/list/all` — 200 e o corpo bate com o schema esperado (objeto de raça → lista de sub-raças).
- `GET /breed/{breed}/images` com raça válida — 200 e lista de imagens no formato certo.
- `GET /breed/{breed}/images` com raça inexistente — 404, confere também a mensagem de erro, não só o status code.
- `GET /breeds/image/random` — 200 com uma URL de imagem válida.

A validação usa JSON Schema em vez de assert campo a campo, pra cobrir o formato inteiro da resposta de uma vez.

## Pré-requisitos

Java 21 e Maven — nada além disso, a `baseURI` da Dog API já vem configurada no código.

