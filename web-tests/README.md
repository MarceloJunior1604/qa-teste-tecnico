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

O enunciado pede pelo menos 2 cenários relevantes. Escolhi 3:

1. Busca com termo existente retorna resultados — digita um termo válido e confere que veio pelo menos um post na lista.
2. Busca sem correspondência mostra a mensagem certa — termo aleatório, confere o texto exato de "nada encontrado". É onde geralmente aparece bug de UX em busca (mensagem genérica, tela quebrada), por isso entrou.
3. Campo vazio não quebra a página — submete sem digitar nada, o site cai num fallback listando tudo, sem erro.

## Rodando headless (CI)

```
mvn test -pl web-tests -Dheadless=true
```

## Considerações

Durante o desenvolvimento o blog do Agi chegou a bloquear os testes algumas vezes no GitHub Actions com `429 Too Many Requests`. O IP dos runners é compartilhado por muita gente ao mesmo tempo, então o site aplica rate limit nele com mais frequência do que numa máquina local. Já coloquei um pequeno intervalo entre os testes pra ajudar, mas pode voltar a acontecer — não é bug do código, é o site limitando o tráfego mesmo. Se rolar, roda de novo.

