# Performance Tests — BlazeDemo

Teste de carga e teste de pico do fluxo de compra de passagem aérea em [blazedemo.com](https://www.blazedemo.com).

## Critério de aceitação

250 requisições por segundo com p90 (percentil 90 do tempo de resposta) abaixo de 2 segundos.

## Por que JMeter Java DSL

Em vez de montar o teste na interface do JMeter e versionar um `.jmx`, escrevi a configuração como código Java usando a lib [`jmeter-java-dsl`](https://github.com/abstracta/jmeter-java-dsl). Continua sendo JMeter rodando por baixo, só que escrito como teste JUnit normal. Na prática isso significa que `mvn test` já baixa tudo sozinho — não precisa instalar JMeter separado pra rodar.

## Fluxo testado

1. `GET /` — home
2. `POST /reserve.php` — busca voos de Paris pra London
3. `POST /purchase.php` — compra o primeiro voo retornado (flight/price/airline vêm da resposta anterior, não são fixos)
4. `POST /confirmation.php` — confirma com dados de pagamento fictícios

A validação de sucesso checa o texto `"Thank you for your purchase today!"` na resposta final, não só o status 200.

Sobre o "250 req/s": configurei o `rpsThreadGroup` mirando 250 fluxos completos por segundo. Na prática, como cada fluxo tem 4 requests, o número que a lib realmente controla acabou se comportando como requisições HTTP totais por segundo — dá pra ver isso nos resultados abaixo.

## Como executar

```
mvn test -pl performance-tests
```

Roda os dois testes seguidos — carga (~10-11min) e pico (~1min), uns 12-14min no total.

## Como ler o relatório

Cada execução gera um dashboard HTML em `performance-tests/target/reports/<load-test|spike-test>/<timestamp>/index.html`. Os relatórios das execuções usadas nos resultados abaixo já estão commitados em `performance-tests/reports/load-test/` e `performance-tests/reports/spike-test/`.

Pontos principais pra olhar quando abrir:
- **APDEX / tabela no topo**: throughput, p90, taxa de erro — o resumo mais rápido pra comparar com o critério de aceitação.
- **Aba "Over Time" → "Response Times Over Time"**: mostra a latência ao longo da execução. No teste de carga essa linha fica bem estável; no de pico dá pra ver ela subindo conforme a carga aumenta além do que o servidor aguenta.
- **Aba "Over Time" → "Active Threads Over Time"**: mostra quantos "usuários simulados" estavam ativos a cada momento — útil pra confirmar que a rampa de subida aconteceu do jeito configurado.
- **Aba "Charts" → "Response Times Percentiles"**: onde dá pra ver o p90 de forma visual, não só o número na tabela.

## Resultados

| | Carga (alvo: 250) | Pico (alvo: 600) |
|---|---|---|
| Requisições totais | 152.181 | 20.545 |
| Throughput real | 241,2 req/s | 261,1 req/s |
| Erros | 0% | 0,32% |
| p90 | 464 ms | 5.875 ms |
| Tempo médio | 378 ms | 2.373 ms |

## Conclusão

O critério não foi atingido: tanto mirando 250 quanto 600 req/s, o throughput real ficou parecido (~240-260 req/s) — parece ser um teto real do BlazeDemo (site de demonstração público, não feito pra aguentar esse volume) ou da rede até ele, não do script em si.

Dentro desse teto, a latência é ótima (p90 de 464ms, bem abaixo dos 2s). O problema aparece quando se força além dele: no pico o p90 passa de 5s e começam a aparecer erros — o sistema degrada rápido assim que passa da capacidade real dele.

## Considerações

- O fluxo sempre compra o primeiro voo retornado, não um aleatório — mais simples de debugar, sem afetar o resultado do teste.
- Dados de cartão são fictícios (o BlazeDemo não valida isso de verdade).
- Às vezes a geração do relatório HTML falha silenciosamente numa execução (o teste passa normal, mas a pasta do relatório fica incompleta). Rodar de novo resolve — parece ser uma instabilidade da própria lib com volumes grandes de amostras.
