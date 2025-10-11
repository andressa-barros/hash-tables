package src;

import java.util.Random;

public class Dados {
    public static int[] gerarDados(int n, long seed) {
        Random rand = new Random(seed);
        int[] dados = new int[n];
        for (int i = 0; i < n; i++) {
            dados[i] = rand.nextInt(Integer.MAX_VALUE);
        }
        return dados;
    }
}
