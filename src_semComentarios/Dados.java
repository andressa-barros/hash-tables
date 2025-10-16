package src_semComentarios;
import java.util.Random;

public class Dados {
    public static int[] gerarDados(int quantidade, long seed) {
        Random rand = new Random(seed);
        int[] dados = new int[quantidade];

        for (int i = 0; i < quantidade; i++) {
            dados[i] = 100_000_000 + rand.nextInt(900_000_000);
        }

        return dados;
    }
}