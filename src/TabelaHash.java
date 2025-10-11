package src;

import static java.lang.Math.floor;

public class TabelaHash {
    private Node[] tabela;
    private int tamanhoTabela;
    private int contadorDeColisoes;

    public TabelaHash(int tamanhoTabela){
        this.tabela = new Node[tamanhoTabela];
        this.tamanhoTabela = tamanhoTabela;
    }

    public int hashDivisao(int codigo, int tamanhoTabela){

        //Faz o cálculo de modulo e retorna o indice sendo o resto da divisão
        int indice = codigo % tamanhoTabela;
        return indice;
    }

    public int hashMultiplicacao(int codigo, int tamanhoTabela){

        //constante A (inverso da razão áurea)
        double A = 0.6180339887;

        //Multiplicar o código pela constante A
        double produto = codigo * A;

        //Isolar a parte fracionária (resultado será smepre entre 0 e 1)
        double parteFracionaria = produto - Math.floor(produto);

        //Multiplicar pelo tamanho da tabela
        double indiceReal = parteFracionaria * tamanhoTabela;

        //Transformar em um índice inteiro
        int indice = (int) Math.floor(indiceReal);

        return indice;
    }

    public int hashLinear (int codigo, int tamanhoTabela){
        int colisoes = 0;
        //número da tentativa de inserção (int, começando em 0)
        int tentativa = 0;

        //Calcular o índice base (neste caso vamos usar divisão)
        int hashBase = codigo % tamanhoTabela;

        //Calcular novo indice com linear probing
        int indice = (hashBase + tentativa) % tamanhoTabela;

        //Iremos realizar este loop enquanto não acharmos uma posição livre na tabela
        while (tentativa < tamanhoTabela) {
            if (tabela[indice] == null) {
                return indice;
            } else {
                System.out.println("Colisão!");
                colisoes++;
                tentativa++;

                indice = (hashBase + tentativa) % tamanhoTabela;

            }
        }

        if (tentativa == tamanhoTabela){
            System.out.println("Tabela cheia!");
            return -1;
        }
        return -1;
    }




}
