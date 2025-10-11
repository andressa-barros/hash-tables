package src;

import java.util.Random;

public class Dados {
    public static int[] gerarDados(int quantidade, long seed) {
        Random rand = new Random(seed);
        int[] dados = new int[quantidade];
        for (int i = 0; i < quantidade; i++) {
            // Gera um número entre 100_000_000 e 999_999_999 (inclusive)
            dados[i] = 100_000_000 + rand.nextInt(900_000_000);
        }
        return dados;
    }
}
