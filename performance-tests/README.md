# Performance Tests: BlazeDemo

Teste de carga e teste de pico do fluxo completo de compra de passagem aérea em [blazedemo.com](https://www.blazedemo.com). Da home até a confirmação da compra, não só um endpoint isolado.

## O que é testado aqui

Performance test não é sobre "o site funciona", é sobre "o site aguenta o volume de gente usando ao mesmo tempo, dentro de um tempo de resposta aceitável". Então em vez de rodar o fluxo de compra uma vez, ele roda o fluxo várias vezes por segundo, de forma sustentada, e mede como o BlazeDemo se comporta sob essa carga: quantas requisições ele processa por segundo (throughput), quanto tempo demora pra responder (latência) e se começa a dar erro.

## Critério de aceitação

250 requisições por segundo com p90 abaixo de 2 segundos.

**p90** é o percentil 90 do tempo de resposta: ordena todas as respostas da mais rápida pra mais lenta e olha onde fica a que está na posição 90%. Ou seja, 90% das requisições foram respondidas em até esse tempo, só os 10% mais lentos ficaram acima. É uma medida mais honesta que a média, porque a média esconde os casos ruins (um punhado de requisições bem lentas não move muito a média, mas o p90 mostra na cara).

Pra validar o critério, montei dois testes:

- Carga: sobe até 250 req/s e segura ali por uns 10 minutos, num regime estável. É o que dá um p90 confiável de verdade: medir durante a subida ainda misturaria momento de pouca carga com carga plena.
- Pico: sobe bem mais rápido pra bem acima do alvo (mirei 600, mais que o dobro) e segura por menos tempo. O objetivo aqui não é medir p90 limpo, é ver como o sistema se comporta quando passa da capacidade dele.

## Por que JMeter Java DSL

Em vez de montar o teste na interface do JMeter e versionar um `.jmx`, escrevi a configuração como código Java usando a lib [`jmeter-java-dsl`](https://github.com/abstracta/jmeter-java-dsl). Continua sendo JMeter rodando por baixo, só que escrito como teste JUnit normal. Na prática isso significa que `mvn test` já baixa tudo sozinho, não precisa instalar JMeter separado pra rodar.

## Fluxo testado

1. `GET /`: home
2. `POST /reserve.php`: busca voos de Paris pra London
3. `POST /purchase.php`: compra o primeiro voo retornado (flight/price/airline vêm da resposta anterior, não são fixos)
4. `POST /confirmation.php`: confirma com dados de pagamento fictícios

A validação de sucesso checa o texto `"Thank you for your purchase today!"` na resposta final. Status 200 sozinho não garante que a compra foi mesmo processada.

Sobre o "250 req/s": configurei o `rpsThreadGroup` mirando 250 fluxos completos por segundo. Na prática, como cada fluxo tem 4 requests, o número que a lib realmente controla acabou se comportando como requisições HTTP totais por segundo, dá pra ver isso nos resultados abaixo.

## Como executar

```
mvn test -Pperformance -pl performance-tests
```

Roda os dois testes seguidos: carga (~10-11min) e pico (~1min), uns 12-14min no total. O `-Pperformance` é necessário porque esse módulo fica fora do build padrão (`mvn test` sozinho não roda ele, pra não travar 12min sem ninguém esperar isso).

## Como ler o relatório

Cada execução gera um dashboard HTML em `performance-tests/target/reports/<load-test|spike-test>/<timestamp>/index.html`. Os relatórios da execução usada nos resultados abaixo estão commitados em `performance-tests/reports/load-test/` e `performance-tests/reports/spike-test/`, e também publicados aqui:

- Carga: https://marcelojunior1604.github.io/qa-teste-tecnico/performance/load-test/index.html
- Pico: https://marcelojunior1604.github.io/qa-teste-tecnico/performance/spike-test/index.html

A tabela no topo (APDEX) já traz throughput, p90 e taxa de erro. É o resumo mais rápido pra comparar com o critério de aceitação. Se quiser ver mais detalhe, "Over Time → Response Times Over Time" mostra a latência ao longo da execução (no teste de carga fica uma linha estável; no de pico dá pra ver ela subindo conforme passa da capacidade do servidor), e "Over Time → Active Threads Over Time" mostra quantos usuários simulados estavam ativos em cada momento, bom pra conferir se a rampa subiu do jeito configurado.

## Resultados

Números da execução no GitHub Actions (não local, de propósito: é o ambiente que qualquer um consegue reproduzir rodando o workflow, e onde o teste realmente falha o assert de critério):

| | Carga (alvo: 250) | Pico (alvo: 600) |
|---|---|---|
| Requisições totais | 150.104 | 18.493 |
| Throughput real | 235,8 req/s | 231,6 req/s |
| Erros | 0,72% | 1,18% |
| p90 | 2.860 ms | 7.543 ms |
| Tempo médio | 1.165 ms | 2.735 ms |

Rodando local (máquina própria, fora do CI) os números do teste de carga saem bem melhores: ~241 req/s, p90 de 464ms, 0% de erro. A diferença é grande o suficiente pra valer a pena registrar: o runner do GitHub Actions tem CPU/rede mais limitada que uma máquina de desenvolvedor, então o mesmo teste, contra o mesmo BlazeDemo, se comporta de forma bem diferente dependendo de onde roda. Por isso os números usados aqui como referência são os do CI, não os locais.

## Conclusão

O critério não foi atingido. O teste de carga (`deveSuportarCargaDe250FluxosPorSegundo`) falha de verdade no CI: o assert de erro zero pega primeiro (1088 erros), e mesmo se não pegasse, o p90 de 2,86s já estouraria o limite de 2s sozinho.

Rodando local o cenário é bem melhor (0% de erro, p90 de 464ms), o que sugere que boa parte da degradação vem da combinação BlazeDemo (site de demonstração público, não dimensionado pra esse volume) com um runner de CI mais limitado, não só do site sozinho. De qualquer forma, com o alvo de 250 req/s, o critério não fecha de forma confiável no ambiente onde o teste realmente vai rodar (CI).

No pico (600 req/s), o comportamento é ainda pior, como esperado: throughput não passa de ~230, p90 chega a 7,5s e a taxa de erro sobe pra 1,18%. Confirma que passar da capacidade real (seja do BlazeDemo, da rede, ou do runner) degrada rápido.

## Considerações

- O fluxo sempre compra o primeiro voo retornado, não um aleatório. Mais simples de debugar, sem afetar o resultado do teste.
- Dados de cartão são fictícios (o BlazeDemo não valida isso de verdade).
- Às vezes a geração do relatório HTML falha silenciosamente numa execução (o teste passa normal, mas a pasta do relatório fica incompleta). Rodar de novo resolve. Parece ser uma instabilidade da própria lib com volumes grandes de amostras.
- O teste de carga tem assert real (`assertEquals`/`assertTrue` sobre erro, p90 e throughput) e falha de propósito quando o critério não é atingido — não é só uma conclusão escrita aqui no README, o CI reporta isso de verdade. O teste de pico não tem esse assert porque seu objetivo é observar degradação além da capacidade, não validar um critério de aceitação.
