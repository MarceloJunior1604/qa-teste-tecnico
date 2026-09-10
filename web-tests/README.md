# Web Tests: Blog do Agi

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

O enunciado pede pelo menos 2 cenários relevantes. Escolhi 3:

1. Busca com termo existente retorna resultados. Digita um termo válido e confere que veio pelo menos um post na lista.
2. Busca sem correspondência mostra a mensagem certa. Termo aleatório, confere o texto exato de "nada encontrado". É onde geralmente aparece bug de UX em busca (mensagem genérica, tela quebrada), por isso entrou.
3. Campo vazio não quebra a página. Submete sem digitar nada, o site cai num fallback listando tudo, sem erro.

## Rodando headless (CI)

```
mvn test -pl web-tests -Dheadless=true
```

## Considerações

Durante o desenvolvimento o blog do Agi chegou a bloquear os testes com `429 Too Many Requests`, tanto no GitHub Actions quanto rodando local. O rate limit do site é agressivo o bastante pra travar com só as 3 buscas em sequência que os testes fazem, independente de vir do IP dos runners ou da sua própria máquina. Coloquei um intervalo entre os testes que ajuda um pouco, mas não elimina — o limite deles parece ser mesmo bem baixo. Não é bug do código, é o site limitando o tráfego. Se rolar, roda de novo.

