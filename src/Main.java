package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {

        // Tamanhos da tabela
        int[] tamanhosTabela = {1000, 10000, 100000};
        // Quantidade de registros
        int[] tamanhosDados = {100_000, 1_000_000, 10_000_000};
        long seed = 42;

        // CSV para salvar resultados
        FileWriter csv = new FileWriter("resultados.csv");
        csv.write("TipoTabela,TamanhoTabela,TamanhoDados,TempoInsercao(s),TempoBusca(s),Colisoes,MenorGap,MaiorGap,MediaGap\n");

        for (int tamTabela : tamanhosTabela) {
            for (int tamDados : tamanhosDados) {

                System.out.println("=== Teste: Tabela " + tamTabela + " | Dados " + tamDados + " ===");
                int[] dados = gerarDados(tamDados, seed);

                // ====================
                // Encadeamento Divisão
                // ====================
                TabelaHash tabelaDiv = new TabelaHash(tamTabela);

                long start = System.nanoTime();
                for (int c : dados) {
                    tabelaDiv.inserirEncadeamentoDivisao(new Registro(c));
                }
                long end = System.nanoTime();
                double tempoInsercao = (end - start) / 1_000_000_000.0;

                // Busca
                start = System.nanoTime();
                for (int c : dados) {
                    buscarEncadeamento(tabelaDiv, c);
                }
                end = System.nanoTime();
                double tempoBusca = (end - start) / 1_000_000_000.0;

                csv.write("EncadeamentoDivisao," + tamTabela + "," + tamDados + "," +
                        tempoInsercao + "," + tempoBusca + "," +
                        tabelaDiv.getColisoesHashDivisao() + ",-, -, -\n"); // gaps não aplicam para encadeamento

                // ====================
                // Encadeamento Multiplicação
                // ====================
                TabelaHash tabelaMul = new TabelaHash(tamTabela);

                start = System.nanoTime();
                for (int c : dados) {
                    tabelaMul.inserirEncadeamentoMultiplicacao(new Registro(c));
                }
                end = System.nanoTime();
                tempoInsercao = (end - start) / 1_000_000_000.0;

                // Busca
                start = System.nanoTime();
                for (int c : dados) {
                    buscarEncadeamento(tabelaMul, c);
                }
                end = System.nanoTime();
                tempoBusca = (end - start) / 1_000_000_000.0;

                csv.write("EncadeamentoMultiplicacao," + tamTabela + "," + tamDados + "," +
                        tempoInsercao + "," + tempoBusca + "," +
                        tabelaMul.getColisoesHashMultiplicacao() + ",-, -, -\n");

                // ====================
                // Rehashing Linear
                // ====================
                TabelaHash tabelaRehash = new TabelaHash(tamTabela);

                start = System.nanoTime();
                for (int c : dados) {
                    tabelaRehash.inserirRehashing(new Registro(c));
                }
                end = System.nanoTime();
                tempoInsercao = (end - start) / 1_000_000_000.0;

                // Busca
                start = System.nanoTime();
                for (int c : dados) {
                    buscarRehashing(tabelaRehash, c);
                }
                end = System.nanoTime();
                tempoBusca = (end - start) / 1_000_000_000.0;

                // Calcular gaps
                int menorGap = Integer.MAX_VALUE;
                int maiorGap = Integer.MIN_VALUE;
                int somaGaps = 0;
                int contador = 0;
                for (int g : tabelaRehash.getGapsLinear()) {
                    if (g > 0) {
                        menorGap = Math.min(menorGap, g);
                        maiorGap = Math.max(maiorGap, g);
                        somaGaps += g;
                        contador++;
                    }
                }
                double mediaGap = contador > 0 ? (double) somaGaps / contador : 0;

                csv.write("RehashingLinear," + tamTabela + "," + tamDados + "," +
                        tempoInsercao + "," + tempoBusca + "," +
                        tabelaRehash.getColisoesHashLinear() + "," +
                        menorGap + "," + maiorGap + "," + mediaGap + "\n");

            }
        }

        csv.close();
        System.out.println("Resultados salvos em resultados.csv");
    }

    // ==============================
    // Funções auxiliares
    // ==============================
    public static int[] gerarDados(int n, long seed) {
        Random rand = new Random(seed);
        int[] dados = new int[n];
        for (int i = 0; i < n; i++) {
            dados[i] = rand.nextInt(1_000_000_000); // 9 dígitos
        }
        return dados;
    }

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
