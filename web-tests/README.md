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

O enunciado pede pra levantar pelo menos 2 cenários relevantes de teste pra busca do blog. Escolhi 3:

1. **Busca com termo existente retorna resultados** — digita um termo válido e confere que aparece pelo menos um post na lista. É o caminho feliz, mas validando de verdade que veio resultado, não só que a página carregou.
2. **Busca sem correspondência exibe mensagem de erro** — digita um termo que não bate com nada e confere a mensagem exata de "nada encontrado". É onde mais aparece bug de UX em busca (mensagem genérica, tela quebrada, etc.), então vale a pena cobrir.
3. **Busca com campo vazio não quebra a página** — submete a busca sem digitar nada. O site cai num fallback (lista geral de posts) em vez de dar erro, e o teste confirma isso.

## Rodando headless (CI)

```
mvn test -pl web-tests -Dheadless=true
```

## Considerações

Durante o desenvolvimento, o blog do Agi chegou a bloquear os testes algumas vezes no GitHub Actions com `429 Too Many Requests`. O IP dos runners do GitHub é compartilhado por muita gente ao mesmo tempo, então o site aplica rate limit nele com mais frequência do que aplicaria numa máquina local rodando os mesmos testes. Já foi adicionado um pequeno intervalo entre os testes pra reduzir isso, mas o bloqueio pode voltar a acontecer — não é bug do código, é o próprio site limitando o tráfego. Se acontecer, rodar de novo geralmente resolve.

