package src;
public class Main {
    public static void main(String[] args) {

        long seed = 42;
        int[] tamanhosTabelas = {1000, 10000, 100000};
        int[] quantidadesDados = {100_000, 1_000_000, 10_000_000};

        for (int tamanhoTabela : tamanhosTabelas) {
            for (int quantidadeDados : quantidadesDados) {

                System.out.println("=======================================");
                System.out.println("Tabela de tamanho " + tamanhoTabela + " | Dados: " + quantidadeDados);

                int[] dados = Dados.gerarDados(quantidadeDados, seed);

                // Encadeamento Divisão
                TabelaHash tabelaDiv = new TabelaHash(tamanhoTabela);
                long start = System.nanoTime();
                for (int c : dados) tabelaDiv.inserirEncadeamentoDivisao(new Registro(c));
                long end = System.nanoTime();
                System.out.println("Encadeamento Divisão – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaDiv.getColisoesHashDivisao());

                // Encadeamento Multiplicação
                TabelaHash tabelaMul = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaMul.inserirEncadeamentoMultiplicacao(new Registro(c));
                end = System.nanoTime();
                System.out.println("Encadeamento Multiplicação – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaMul.getColisoesHashMultiplicacao());

                // Rehashing Linear
                TabelaHash tabelaRehash = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaRehash.inserirRehashing(new Registro(c));
                end = System.nanoTime();
                System.out.println("Rehashing Linear – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaRehash.getColisoesHashLinear());

                //Double Hash
                TabelaHash tabelaDouble = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaDouble.inserirDoubleHash(new Registro(c));
                end = System.nanoTime();
                System.out.println("Double Hash – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaDouble.getColisoesDoubleHash());

                // =========================
                // Busca de teste
                // =========================
                int codigoTeste = dados[0];
                boolean encontradoDiv = buscarEncadeamentoDivisao(tabelaDiv, codigoTeste);
                boolean encontradoMul = buscarEncadeamentoMultiplicacao(tabelaMul, codigoTeste);
                boolean encontradoRehash = buscarRehashing(tabelaRehash, codigoTeste);
                boolean encontradoDouble = buscarDoubleHash(tabelaDouble, codigoTeste);

                System.out.println("\nBusca do código " + codigoTeste);
                System.out.println("Divisão: " + encontradoDiv);
                System.out.println("Multiplicação: " + encontradoMul);
                System.out.println("Rehashing: " + encontradoRehash);
                System.out.println("Double Hash: " + encontradoDouble);

                System.out.println("---------------------------------------\n");
            }
        }
    }

    // =========================
    // Funções de busca
    // =========================
    public static boolean buscarEncadeamentoDivisao(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int idx = codigo % tabela.getTamanhoTabela();
        Node atual = vetor[idx];
        while (atual != null) {
            if (atual.getInformacao().getCodigo() == codigo) return true;
            atual = atual.getProximo();
        }
        return false;
    }

    public static boolean buscarEncadeamentoMultiplicacao(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        double A = 0.6180339887;
        double prod = codigo * A;
        double frac = prod - Math.floor(prod);
        int idx = (int) Math.floor(frac * tabela.getTamanhoTabela());
        Node atual = vetor[idx];
        while (atual != null) {
            if (atual.getInformacao().getCodigo() == codigo) return true;
            atual = atual.getProximo();
        }
        return false;
    }

    public static boolean buscarRehashing(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int hashBase = codigo % tabela.getTamanhoTabela();
        int tentativa = 0;
        while (tentativa < tabela.getTamanhoTabela()) {
            int indice = (hashBase + tentativa) % tabela.getTamanhoTabela();
            Node n = vetor[indice];
            if (n != null && n.getInformacao().getCodigo() == codigo) return true;
            if (n == null) return false;
            tentativa++;
        }
        return false;
    }

    public static boolean buscarDoubleHash(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int hash1 = codigo % tabela.getTamanhoTabela();
        int hash2 = 1 + (codigo % (tabela.getTamanhoTabela()-1));
        int tentativa = 0;

        while (tentativa < tabela.getTamanhoTabela()) {
            int idx = (hash1 + tentativa * hash2) % tabela.getTamanhoTabela();
            Node n = vetor[idx];
            if (n != null && n.getInformacao().getCodigo() == codigo) return true;
            if (n == null) return false;
            tentativa++;
        }
        return false;
    }
}
