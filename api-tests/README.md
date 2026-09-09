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

- `GET /breeds/list/all` — retorna 200 e o corpo bate com o schema esperado (objeto de raça → lista de sub-raças).
- `GET /breed/{breed}/images` com raça válida — retorna 200 e lista de imagens no formato esperado.
- `GET /breed/{breed}/images` com raça inexistente — retorna 404 com a mensagem de erro correta, não só o status code.
- `GET /breeds/image/random` — retorna 200 com uma URL de imagem válida.

Validação de contrato feita com JSON Schema em vez de asserts soltos campo a campo — garante o formato inteiro da resposta, não só alguns valores pontuais.

## Pré-requisitos

Java 21 e Maven — nada além disso, a `baseURI` da Dog API já vem configurada no código.

