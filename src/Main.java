package src;

public class Main {

    // =========================================================
    // seção: parâmetros de execução
    // =========================================================

    public static void main(String[] args) {

        // - seed fixa para gerar os mesmos dados em todos os testes
        long seed = 42;

        // - rehashing usa um tamanho próprio calculado a partir do nº de dados (≈70% de ocupação) e ajustado para primo
        // - tamanhos de tabela (para encadeamento)
        int[] tamanhosTabelas = {1000, 10000, 100000};              // tamanhos base (encadeamento)
        // - quantidades de dados (3 conjuntos (enunciado pediu :p))
        int[] quantidadesDados = {100_000, 1_000_000, 10_000_000};  // 100k, 1m, 10m


        // =====================================================
        // laço principal: combina cada tamanho de tabela com cada conjunto de dados
        // =====================================================
        for (int tamanhoTabela : tamanhosTabelas) {
            for (int quantidadeDados : quantidadesDados) {

                System.out.println("=======================================");
                System.out.println("Tabela de Tamanho " + tamanhoTabela + " | Dados: " + quantidadeDados);

                // gera dados com seed fixa (todos os testes usam o mesmo vetor)
                int[] dados = Dados.gerarDados(quantidadeDados, seed);

                // dimensiona tabela para rehashing (≈70% ocupação) e ajusta para primo (necessário para double hash)
                int bruto = (int) Math.ceil(quantidadeDados / 0.7);
                int tamanhoTabelaRehash = proximoPrimo(bruto);
                System.out.println("Tamanho de Rehashing Usado: " + tamanhoTabelaRehash);

                // =================================================
                // seção: inserções (4 variações)
                // =================================================

                // 1) encadeamento por divisão
                TabelaHash tabelaDiv = new TabelaHash(tamanhoTabela);
                long start = System.nanoTime();
                for (int c : dados) tabelaDiv.inserirEncadeamentoDivisao(new Registro(c));
                long end = System.nanoTime();
                System.out.println("Encadeamento (Divisão) – Tempo: " + (end - start) / 1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaDiv.getColisoesHashDivisao());

                // 2) encadeamento por multiplicação
                TabelaHash tabelaMul = new TabelaHash(tamanhoTabela);
                start = System.nanoTime();
                for (int c : dados) tabelaMul.inserirEncadeamentoMultiplicacao(new Registro(c));
                end = System.nanoTime();
                System.out.println("Encadeamento (Multiplicação) – Tempo: " + (end - start) / 1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaMul.getColisoesHashMultiplicacao());

                // 3) rehashing linear (sondagem linear)
                TabelaHash tabelaRehash = new TabelaHash(tamanhoTabelaRehash);
                start = System.nanoTime();
                for (int c : dados) tabelaRehash.inserirRehashing(new Registro(c));
                end = System.nanoTime();
                System.out.println("Rehashing Linear – Tempo: " + (end - start) / 1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaRehash.getColisoesHashLinear());

                // 4) double hash (dupla dispersão)
                TabelaHash tabelaDouble = new TabelaHash(tamanhoTabelaRehash);
                start = System.nanoTime();
                for (int c : dados) tabelaDouble.inserirDoubleHash(new Registro(c));
                end = System.nanoTime();
                System.out.println("Double Hash – Tempo: " + (end - start) / 1_000_000_000.0 + " s");
                System.out.println("Colisões: " + tabelaDouble.getColisoesDoubleHash());

                // =================================================
                // seção: buscas
                // =================================================

                // busca de checagem (1 elemento de exemplo)
                int codigoTeste = dados[0];
                boolean encontradoDiv = buscarEncadeamentoDivisao(tabelaDiv, codigoTeste);
                boolean encontradoMul = buscarEncadeamentoMultiplicacao(tabelaMul, codigoTeste);
                boolean encontradoRehash = buscarRehashing(tabelaRehash, codigoTeste);
                boolean encontradoDouble = buscarDoubleHash(tabelaDouble, codigoTeste);

                System.out.println("\nBusca do Código " + codigoTeste);
                System.out.println("Divisão: " + encontradoDiv);
                System.out.println("Multiplicação: " + encontradoMul);
                System.out.println("Rehashing: " + encontradoRehash);
                System.out.println("Double Hash: " + encontradoDouble);

                // tempo de busca dos 3 conjuntos completos (exigência do enunciado)
                long tbDiv = tempoBuscaTodosEncDiv(tabelaDiv, dados);
                long tbMul = tempoBuscaTodosEncMul(tabelaMul, dados);
                long tbReh = tempoBuscaTodosRehash(tabelaRehash, dados);
                long tbDbl = tempoBuscaTodosDouble(tabelaDouble, dados);

                System.out.println("Tempo de Busca (todos) – Divisão: " + tbDiv / 1_000_000.0 + " ms");
                System.out.println("Tempo de Busca (todos) – Multiplicação: " + tbMul / 1_000_000.0 + " ms");
                System.out.println("Tempo de Busca (todos) – Rehashing: " + tbReh / 1_000_000.0 + " ms");
                System.out.println("Tempo de Busca (todos) – Double Hash: " + tbDbl / 1_000_000.0 + " ms");

                // =================================================
                // seção: análise estrutural exigida no enunciado
                // - top 3 buckets (apenas encadeamento)
                // - gaps min/máx/médio (todas as tabelas)
                // =================================================

                // top 3 maiores listas encadeadas
                int[] topDiv = top3Buckets(tabelaDiv.getTabela());
                int[] topMul2 = top3Buckets(tabelaMul.getTabela());
                System.out.println("Top 3 (Divisão):  #1 índice " + topDiv[0] + " tamanho " + topDiv[1] +
                        " | #2 índice " + topDiv[2] + " tamanho " + topDiv[3] +
                        " | #3 índice " + topDiv[4] + " tamanho " + topDiv[5]);
                System.out.println("Top 3 (Multiplicação):  #1 índice " + topMul2[0] + " tamanho " + topMul2[1] +
                        " | #2 índice " + topMul2[2] + " tamanho " + topMul2[3] +
                        " | #3 índice " + topMul2[4] + " tamanho " + topMul2[5]);

                // gaps (mín, máx, média)
                GapStats gDiv = calcularGaps(tabelaDiv.getTabela());
                GapStats gMul = calcularGaps(tabelaMul.getTabela());
                GapStats gReh = calcularGaps(tabelaRehash.getTabela());
                GapStats gDbl = calcularGaps(tabelaDouble.getTabela());

                System.out.println("Gaps – Divisão: mín=" + gDiv.min + " máx=" + gDiv.max + " média=" + gDiv.media);
                System.out.println("Gaps – Multiplicação: mín=" + gMul.min + " máx=" + gMul.max + " média=" + gMul.media);
                System.out.println("Gaps – Rehashing: mín=" + gReh.min + " máx=" + gReh.max + " média=" + gReh.media);
                System.out.println("Gaps – Double Hash: mín=" + gDbl.min + " máx=" + gDbl.max + " média=" + gDbl.media);

                System.out.println("---------------------------------------\n");
            }
        }
    }

    // =====================================================================
    // seção: buscas (implementações de busca para cada técnica)
    // =====================================================================

    // encadeamento (divisão)
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

    // encadeamento (multiplicação)
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

    // rehashing linear
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

    // double hash
    public static boolean buscarDoubleHash(TabelaHash tabela, int codigo) {
        Node[] vetor = tabela.getTabela();
        int hash1 = codigo % tabela.getTamanhoTabela();
        int hash2 = 1 + (codigo % (tabela.getTamanhoTabela() - 1));
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

    // =====================================================================
    // seção: helpers numéricos (primos) para dimensionar rehashing
    // =====================================================================

    static int proximoPrimo(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++;
        while (true) {
            if (ehPrimo(n)) return n;
            n += 2;
        }
    }

    static boolean ehPrimo(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int d = 3; d * d <= n; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }

    // =====================================================================
    // seção: medições exigidas no passo 5 (tempos de busca, top3, gaps)
    // =====================================================================

    // tempos de busca (todos os elementos)
    static long tempoBuscaTodosEncDiv(TabelaHash t, int[] dados){
        long s = System.nanoTime();
        for (int c : dados) buscarEncadeamentoDivisao(t, c);
        return System.nanoTime() - s;
    }

    static long tempoBuscaTodosEncMul(TabelaHash t, int[] dados){
        long s = System.nanoTime();
        for (int c : dados) buscarEncadeamentoMultiplicacao(t, c);
        return System.nanoTime() - s;
    }

    static long tempoBuscaTodosRehash(TabelaHash t, int[] dados){
        long s = System.nanoTime();
        for (int c : dados) buscarRehashing(t, c);
        return System.nanoTime() - s;
    }

    static long tempoBuscaTodosDouble(TabelaHash t, int[] dados){
        long s = System.nanoTime();
        for (int c : dados) buscarDoubleHash(t, c);
        return System.nanoTime() - s;
    }

    // top 3 maiores listas encadeadas (retorna: idx1,tam1,idx2,tam2,idx3,tam3)
    static int[] top3Buckets(Node[] vet){
        int m1=0,i1=-1, m2=0,i2=-1, m3=0,i3=-1;
        for(int i=0;i<vet.length;i++){
            int len=0; Node a=vet[i];
            while(a!=null){ len++; a=a.getProximo(); }
            if(len>m1){ m3=m2;i3=i2; m2=m1;i2=i1; m1=len;i1=i; }
            else if(len>m2){ m3=m2;i3=i2; m2=len;i2=i; }
            else if(len>m3){ m3=len;i3=i; }
        }
        return new int[]{i1,m1,i2,m2,i3,m3};
    }

    // gaps (mín, máx, média) no vetor base
    static class GapStats { int min, max, qtd; double media; }

    static GapStats calcularGaps(Node[] vet){
        GapStats g = new GapStats();
        g.min = Integer.MAX_VALUE;
        int atual = 0, soma = 0, qtd = 0;

        for(int i=0;i<vet.length;i++){
            if(vet[i]==null) {
                atual++;
            } else {
                if(atual>0){
                    soma += atual; qtd++;
                    if(atual < g.min) g.min = atual;
                    if(atual > g.max) g.max = atual;
                    atual = 0;
                }
            }
        }
        // cauda de nulls (se terminar com espaço)
        if(atual>0){
            soma += atual; qtd++;
            if(atual < g.min) g.min = atual;
            if(atual > g.max) g.max = atual;
        }

        g.qtd = qtd;
        g.media = (qtd==0) ? 0.0 : (soma * 1.0) / qtd;
        if(qtd==0){ g.min=0; g.max=0; }
        return g;
    }
}