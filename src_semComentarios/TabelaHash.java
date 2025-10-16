package src_semComentarios;
import src.Node;
import src.Registro;

public class TabelaHash {
    private src.Node[] tabela;
    private int tamanhoTabela;

    private long colisoesHashDivisao;
    private long colisoesHashMultiplicacao;
    private long colisoesHashLinear;
    private long colisoesDoubleHash;

    public TabelaHash(int tamanhoTabela){
        this.tabela = new src.Node[tamanhoTabela];
        this.tamanhoTabela = tamanhoTabela;
    }


    public src.Node[] getTabela() {
        return tabela;
    }

    public int getTamanhoTabela() { // pq o duplo hash vai usar
        return tamanhoTabela;
    }

    public long getColisoesHashDivisao() {
        return colisoesHashDivisao;
    }

    public long getColisoesHashMultiplicacao() {
        return colisoesHashMultiplicacao;
    }

    public long getColisoesHashLinear() {
        return colisoesHashLinear;
    }

    public long getColisoesDoubleHash() {
        return colisoesDoubleHash;
    }

    public int hashDivisao(int codigo){
        return codigo % tamanhoTabela;
    }

    public int hashMultiplicacao(int codigo){
        double A = 0.6180339887;
        double frac = (codigo * A) - Math.floor(codigo * A);
        return (int) Math.floor(frac * tamanhoTabela);
    }

    public int hashLinear(int codigo, int tentativa){
        return (codigo % tamanhoTabela + tentativa) % tamanhoTabela;
    }

    public int hashDouble(int codigo) {
        return 1 + (codigo % (tamanhoTabela - 1));//garantir que não seja zero usando 1+
    }


    public void inserirEncadeamentoDivisao(src.Registro reg){
        int idx = hashDivisao(reg.getCodigo());
        src.Node novo = new src.Node();
        novo.setInformacao(reg);
        src.Node atual = tabela[idx];

        long colisaoLocal = 0;
        if (atual == null) {
            tabela[idx] = novo;
        } else {
            colisaoLocal++;
            while (atual.getProximo() != null){
                atual = atual.getProximo();
                colisaoLocal++;
            }
            atual.setProximo(novo);
        }
        colisoesHashDivisao += colisaoLocal;
    }

    public void inserirEncadeamentoMultiplicacao(src.Registro reg){
        int idx = hashMultiplicacao(reg.getCodigo());
        src.Node novo = new src.Node();
        novo.setInformacao(reg);
        src.Node atual = tabela[idx];

        long colisaoLocal = 0;
        if (atual == null) {
            tabela[idx] = novo;
        } else {
            colisaoLocal++;
            while (atual.getProximo() != null){
                atual = atual.getProximo();
                colisaoLocal++;
            }
            atual.setProximo(novo);
        }
        colisoesHashMultiplicacao += colisaoLocal;
    }

    public void inserirRehashing(src.Registro reg){
        int tentativa = 0;
        int idx;
        while (tentativa < tamanhoTabela){
            idx = hashLinear(reg.getCodigo(), tentativa);
            if (tabela[idx] == null){
                src.Node novo = new src.Node();
                novo.setInformacao(reg);
                tabela[idx] = novo;
                colisoesHashLinear += tentativa;
                return;
            }
            tentativa++;
        }
        colisoesHashLinear += tentativa;
    }

    public void inserirDoubleHash(Registro reg) {
        int tentativa = 0;
        int idx;
        int hash1 = reg.getCodigo() % tamanhoTabela;
        int hash2 = hashDouble(reg.getCodigo());

        while (tentativa < tamanhoTabela) {
            idx = (hash1 + tentativa * hash2) % tamanhoTabela;
            if (tabela[idx] == null) {
                src.Node novo = new Node();
                novo.setInformacao(reg);
                tabela[idx] = novo;
                colisoesDoubleHash += tentativa;
                return;
            }
            tentativa++;
        }
        colisoesDoubleHash += tentativa;
    }
}
