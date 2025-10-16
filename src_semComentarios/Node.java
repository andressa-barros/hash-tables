package src_semComentarios;

import src_semComentarios.Registro;

public class Node {
    private Registro informacao;
    private Node proximo;

    public Node() {
        this.informacao = null;
        this.proximo = null;
    }

    public Registro getInformacao() {
        return informacao;
    }

    public void setInformacao(Registro informacao) {
        this.informacao = informacao;
    }

    public Node getProximo() {
        return proximo;
    }

    public void setProximo(Node proximo) {
        this.proximo = proximo;
    }
}