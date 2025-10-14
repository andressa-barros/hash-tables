# Tabelas Hash (Hash Tables)

Projeto desenvolvido para a disciplina de Resolução de Problemas Estruturados em Computação. 4 Período, Turma U.

### Estudantes:
- Lissa Deguti
- Andressa de Oliveira
- Melissa Weiss

## Estrutura do projeto:
```
src/
|--Main.java        -> executa os testes e imprime resultados
|--TabelaHash.java  -> implementação das funções hash e inser
cões
|--Node.java        -> estrutura usada no encadeamento
|--Registro.java    -> classe que armazena o código (chave)
|--Dados.java       -> gera dados aleatórios com seed fixa
```

## Métodos implementados
### Encadeamento (Chaining)
#### 1. Divisão:
```
h(k) = k % m
```
#### 2. Multiplicação:
```
h(k) = floor(m * frac(k * A)), com A = 0.6180339887
```
### Endereçamento Aberto (Open Adressing)
#### 3. Sondagem Linear:
```
h_i(k) = (h(k) + i) % m
```
#### 4. Hash Duplo:
```
h_i(k) = (h1(k) + i * h2(k)) % m
onde
h1(k) = k % m e h2(k) = 1 + (k % (m - 1))
```
O tamanho das tabelas de rehashing é ajustado para 70% de ocupação e arredondado para o próximo número primo, que melhora a dispersão de dados.

### Parâmetros de Teste
- Tamanhos de tabela (encadeamento): 1000, 10000 e 100000
- Quantidade de dados: 100000, 1000000, 10000000
- Seed: 42 (gera os mesmos dados em todas as execuções)

Cada combinação (tabela x dados) foi testado com as 4 estratégias.

### Como executar
#### IntelliJ IDEA
1. Abra o projeto
2. Verifique o SDK (Java 21+)
3. Vá em Build -> Rebuild Project
4. Rode a classe src.Main

### Métricas coletadas
#### - Tempo de inserção(s): tempo total para inserir o conjunto
#### - Colisões:
    - Encadeamento: quantos links percorridos até inserir (somando por inserção) 
    - Sondagem Linear / Hash Duplo: número de tentativas até encontrar posição livre (somando por inserção)
#### - Tempo de busca (todos) (ms): buscar cada item gerado na sua tabela
#### - Top 3 listas (encadeamento): índices e tamanhos das maiores listas (pior concentração)
#### - Gaps (endereçamento aberto e também encadeamento, por simetria do relatório): Sequências de posições nulas contíguas no vetor base: min/máx/média

### Resultados
#### 1. Tempo de inserção (segundos) (aproximado)
```
Tabela/Dados  | Divisão (Encad.) | Multipl. (Encad.) | Linear (OA) | Duplo (OA)
1000 x 100k   | 0,091            | 0,0975            | 0,008       | 0,0095
1000 x 1M     | 10,44            | 19,97             | 0,042       | 0,040
1000 x 10M    | 560,24           | 1099,58           | 0,9954      | 0,708
10000 x 100k  | 0,0079           | 0,0108            | 0,0039      | 0,0036
10000 x 1M    | 4,79             | 4,86              | 0,0466      | 0,048
10000 x 10M   | 270,32           | 713,54            | 1,356       | 0,883
100000 x 100k | 0,0036           | 0,0033            | 0,0039      | 0,0036
100000 x 1M   | 0,357            | 0,54              | 0,053       | 0,052
100000 x 10M  | 34,23            | 27,24             | 1,062       | 0,793
```
Conclusão: Técnicas de endereçamento aberto são muito mais rápidas em todos os casos.

#### 2. Colisões (contagem acumulada)
```
Tabela/Dados  | Divisão (Encad.) | Multipl. (Encad.) | Linear (OA) | Duplo (OA)
1000 x 100k   | 5.004.743        | 5.003.075         | 117.881     | 72.281
1000 x 1M     | 499.994.493      | 499.986.753       | 1.169.612   | 718.739
1000 x 10M    | 49.999.678.152   | 50.000,046.755    | 11.663.989  | 7.213.499
10000 x 100k  | 500.086          | 499.846           | 117.881     | 72.281
10000 x 1M    | 50.007.626       | 49.996.551        | 1.169.612   | 718.739
10000 x 10M   | 5.000.083.275    | 5.000.081.035     | 11.663.989  | 7.213.499
100000 x 100k | 50.227           | 49.982            | 117.881     | 72.281
100000 x 1M   | 5.000.438        | 4.997.105         | 1.169.612   | 718.739
100000 x 10M  | 500.011.335      | 500.019.167       | 11.663.989  | 7.213.499
```
Conclusão: Hash Duplo apresenta menos colisões e melhor dispersão.

#### 3. Tempo de Busca (milissegundos)
```
Tabela/Dados  | Divisão (Encad.) | Multipl. (Encad.) | Linear (OA) | Duplo (OA)
1000 x 100k   | 89,22            | 79,06             | 6,24        | 6,25
1000 x 1M     | 8.333,16         | 19.101,44         | 40,63       | 43,56
1000 x 10M    | 238.040,07       | 223.478,85        | 1.305,60    | 1.013,80
10000 x 100k  | 6,93             | 7,13              | 2,89        | 2,85
10000 x 1M    | 3.065,43         | 4.122,69          | 46,05       | 42,98
10000 x 10M   | 38.846,67        | 39.324,25         | 1.435,33    | 917,28
100000 x 100k | 1,61             | 2,43              | 3,29        | 2,99
100000 x 1M   | 410,72           | 248,24            | 55,45       | 44,93
100000 x 10M  | 8.391,48         | 9.446,27          | 1.151,89    | 731,03
```
Conclusão: Hash Duplo é o melhor em busca, com tempos menores que encadeamento.

### Top 3 Listas (encadeamento)
Exemplo (1000 x 10M):
1. Índice 990 -> 10324 elementos
2. Índice 494 -> 10303 elementos
3. Índice 851 -> 10278 elementos

O encadeamento sofre alta concentração em poucos índices.

### Gaps (endereçamento aberto)
Exemplo (10M elementos):
- Linear: mín = 1, máx = 25, média +-= 1.98
- Duplo: mín = 1, máx = 12, média +-= 1.43

Hash Duplo distribui melhor os dados (menores gaps)

### Conclusões
#### - Encadeamento:
Simples, mas pior desempenho com tabelas pequenas.
#### - Rehashing Linear:
Rápido, porém mais colisões.
#### - Hash Duplo:
Melhor desempenho geral (inserção, busca e distribuição).
#### - Tabelas maiores e número primo:
Reduzem colisões e melhoram dispersão

#### Melhor método geral:
Hash Duplo (Double Hashing)
#### Melhor custo-benefício:
Rehashing Linear
