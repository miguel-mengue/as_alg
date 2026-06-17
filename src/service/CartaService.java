package service;

import structures.PilhaCartas;
import model.Carta;
import model.TipoCarta;

public class CartaService {

    private final PilhaCartas baralho;

    public CartaService() {

        this.baralho =
                new PilhaCartas();

        carregarCartas();
    }

    private void carregarCartas() {

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Receba R$ 500",
                        TipoCarta.GANHO,
                        500,
                        0
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Receba R$ 1000",
                        TipoCarta.GANHO,
                        1000,
                        0
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Pague R$ 300",
                        TipoCarta.PERDA,
                        300,
                        0
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Pague R$ 700",
                        TipoCarta.PERDA,
                        700,
                        0
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Avance 3 casas",
                        TipoCarta.AVANCAR,
                        0,
                        3
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Avance 5 casas",
                        TipoCarta.AVANCAR,
                        0,
                        5
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Volte 2 casas",
                        TipoCarta.RETROCEDER,
                        0,
                        2
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Volte 4 casas",
                        TipoCarta.RETROCEDER,
                        0,
                        4
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Ir para o início",
                        TipoCarta.IR_INICIO,
                        0,
                        0
                )
        );

        baralho.adicionarCartaOriginal(
                new Carta(
                        "Voltar posição anterior",
                        TipoCarta.VOLTAR_POSICAO_ANTERIOR,
                        0,
                        0
                )
        );
    }

    public Carta sacarCarta() {
        return baralho.pop();
    }

    public PilhaCartas getBaralho() {
        return baralho;
    }
}