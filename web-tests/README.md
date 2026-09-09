# Web Tests — Blog do Agi

Automação da busca de artigos do [blog do Agi](https://blogdoagi.com.br/) (lupa no canto superior direito).

## Stack

Java 21 · Selenium · WebDriverManager · JUnit 5 · Allure (relatório)

## Como rodar

```
mvn test -pl web-tests
```

## Relatório

```
mvn -pl web-tests allure:report
mvn -pl web-tests allure:serve
```

## Cenários cobertos

