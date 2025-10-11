package src;

public class Main {
    public static void main(String[] args) {

        // ==============================
        // Parâmetros
        // ==============================
        long seed = 42;                   // mesma seed para todos os testes
        int[] tamanhosTabelas = {1000, 10000, 100000};
        int[] quantidadesDados = {100_000, 1_000_000, 10_000_000};

        for (int tamanhoTabela : tamanhosTabelas) {
            for (int quantidadeDados : quantidadesDados) {

                System.out.println("=======================================");
                System.out.println("Tabela de tamanho " + tamanhoTabela + " | Dados: " + quantidadeDados);

                // ==============================
                // Gerar dados
                // ==============================
                int[] dados = Dados.gerarDados(quantidadeDados, seed);

                // ==============================
                // Encadeamento - Divisão
                // ==============================
                TabelaHash tabelaDiv = new TabelaHash(tamanhoTabela);
                long start = System.nanoTime();
                for (int c : dados) tabelaDiv.inserirEncadeamentoDivisao(new Registro(c));
                long end = System.nanoTime();
                System.out.println("Encadeamento Divisão – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaDiv.getColisoesHashDivisao());

                // ==============================
                // Encadeamento - Multiplicação
                // ==============================
                TabelaHash tabelaMul = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaMul.inserirEncadeamentoMultiplicacao(new Registro(c));
                end = System.nanoTime();
                System.out.println("Encadeamento Multiplicação – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaMul.getColisoesHashMultiplicacao());

                // ==============================
                // Rehashing Linear
                // ==============================
                TabelaHash tabelaRehash = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaRehash.inserirRehashing(new Registro(c));
                end = System.nanoTime();
                System.out.println("Rehashing Linear – Tempo: " + (end-start)/1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaRehash.getColisoesHashLinear());

                // ==============================
                // Busca de teste
                // ==============================
                int codigoTeste = dados[0];
                boolean encontradoDiv = buscarEncadeamento(tabelaDiv, codigoTeste);
                boolean encontradoMul = buscarEncadeamento(tabelaMul, codigoTeste);
                boolean encontradoRehash = buscarRehashing(tabelaRehash, codigoTeste);

                System.out.println("\nBusca do código " + codigoTeste);
                System.out.println("Divisão: " + encontradoDiv);
                System.out.println("Multiplicação: " + encontradoMul);
                System.out.println("Rehashing: " + encontradoRehash);

                System.out.println("---------------------------------------\n");
            }
        }
    }

    // ==============================
    // Funções de busca
    // ==============================
    public static boolean buscarEncadeamento(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int hashDiv = codigo % vetor.length;
        Node atual = vetor[hashDiv];
        while (atual != null) {
            if (atual.getInformacao().getCodigo() == codigo) return true;
            atual = atual.getProximo();
        }
        return false;
    }

    public static boolean buscarRehashing(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int hashBase = codigo % vetor.length;
        int tentativa = 0;

        while (tentativa < vetor.length) {
            int indice = (hashBase + tentativa) % vetor.length;
            Node n = vetor[indice];
            if (n != null && n.getInformacao().getCodigo() == codigo) return true;
            if (n == null) return false;
            tentativa++;
        }
        return false;
    }
}
