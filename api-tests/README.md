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

