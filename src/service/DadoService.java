package service;

import java.util.Random;

public class DadoService {

    private final Random random;

    public DadoService() {
        this.random = new Random();
    }

    public int lancarDado() {
        return random.nextInt(6) + 1;
    }

    public int[] lancarDoisDados() {

        int dado1 = lancarDado();
        int dado2 = lancarDado();

        return new int[]{dado1, dado2};
    }

    public int somaDados() {

        int[] dados = lancarDoisDados();

        return dados[0] + dados[1];
    }
}