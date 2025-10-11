package src;

public class TabelaHash {
    private Node[] tabela;
    private int tamanhoTabela;
    int colisoesHashDivisao;
    int colisoesHashMultiplicacao;
    int colisoesHashLinear;
    int tamanhoListas[];
    int gapsLinear[];

    public TabelaHash(int tamanhoTabela){
        this.tabela = new Node[tamanhoTabela];
        this.tamanhoTabela = tamanhoTabela;
        this.gapsLinear = new int[tamanhoTabela];
        this.tamanhoListas = new int[tamanhoTabela];
    }

    public int hashDivisao(int codigo){

        //Faz o cálculo de modulo e retorna o indice sendo o resto da divisão
        int indice = codigo % tamanhoTabela;
        return indice;
    }

    public int hashMultiplicacao(int codigo){

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

    public int hashLinear (int codigo){
        //número da tentativa de inserção (int, começando em 0)
        int tentativa = 0;

        //Calcular o índice base (neste caso vamos usar divisão)
        int hashBase = codigo % tamanhoTabela;

        //Calcular novo indice com linear probing
        int indice = (hashBase + tentativa) % tamanhoTabela;

        //Iremos realizar este loop enquanto não acharmos uma posição livre na tabela
        while (tentativa < tamanhoTabela) {
            if (tabela[indice] == null) {
                gapsLinear[indice] = tentativa;
                return indice;

            } else {
                colisoesHashLinear++;
                tentativa++;
                indice = (hashBase + tentativa) % tamanhoTabela;

            }
        }

        //Caso a tabela esteja cheia
        return -1;
    }

    //Inserir com Encademaneto com a função de hash de divisão
    public void inserirEncadeamentoDivisao(Registro r){
        //Calculo para saber o indice que sera inserido o código
        int indice = hashDivisao(r.getCodigo());

        //se o indice estiver vazio irei inserir o novo nó com o código de registro
        if(tabela[indice] == null){
            Node novoNo = new Node();
            novoNo.setInformacao(r);
            tabela[indice] = novoNo;

            //o valor do tamanho da lista do indice é 1, pois somente temos um registro na lista deste indice
            tamanhoListas[indice] = 1;
        }
        //se não estiver vazio irei inserir no final da lista do indice o novo nó com o código de registro
        else{

            Node atual = tabela[indice];

            int colisoes = 0;

            while(atual.getProximo() != null){
                atual = atual.getProximo();
                // cada nó que se passa é uma colisão
                colisoes++;
            }
            Node novoNo = new Node();
            novoNo.setInformacao(r);
            atual.setProximo(novoNo);

            //acumulamos todas as colisões da tabela
            colisoesHashDivisao += colisoes;
            //Quantidade total de nós no indice
            tamanhoListas[indice] = colisoes + 1;
        }
    }

    //Inserir com Encademaneto com a função de hash de multiplicação
    public void inserirEncadeamentoMultiplicacao(Registro r){
        //Calculo para saber o indice que sera inserido o código
        int indice = hashMultiplicacao(r.getCodigo());

        //se o indice estiver vazio irei inserir o novo nó com o código de registro
        if(tabela[indice] == null){
            Node novoNo = new Node();
            novoNo.setInformacao(r);
            tabela[indice] = novoNo;

            tamanhoListas[indice] = 1;
        }
        //se não estiver vazio irei inserir no final da lista do indice o novo nó com o código de registro
        else{
            Node atual = tabela[indice];

            int colisoes = 0;
            while(atual.getProximo() != null){
                atual = atual.getProximo();
                // cada nó que se passa é uma colisão
                colisoes++;
            }
            Node novoNo = new Node();
            novoNo.setInformacao(r);
            atual.setProximo(novoNo);

            //acumulamos todas as colisões da tabela
            colisoesHashMultiplicacao += colisoes;
            //Quantidade total de nós no indice
            tamanhoListas[indice] = colisoes + 1;
        }
    }

    public void inserirRehashing(Registro r){
        int indice = hashLinear(r.getCodigo());

        if (indice == -1) return;

        Node registro = new Node();
        registro.setInformacao(r);
        tabela[indice] = registro;

    }

    public int getColisoesHashDivisao(){
        return colisoesHashDivisao;
    }

    public int getColisoesHashMultiplicacao(){
        return colisoesHashMultiplicacao;
    }

    public int getColisoesHashLinear(){
        return  colisoesHashLinear;
    }

    public int[] getTamanhoListas(){
        return tamanhoListas;
    }

    public int[] getGapsLinear() {
        return gapsLinear;
    }

    public Node[] getTabela(){
        return tabela;
    }







}
