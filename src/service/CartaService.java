package service;

import structures.PilhaCartas;
import model.Carta;
import model.TipoCarta;

public class CartaService {

    private final PilhaCartas baralho;

    public CartaService() {
        this.baralho = new PilhaCartas();
        carregarCartas();
    }

    private void carregarCartas() {
        baralho.adicionarCartaOriginal(new Carta("Receba R$ 500 do banco", TipoCarta.GANHO, 500, 0));
        baralho.adicionarCartaOriginal(new Carta("Receba R$ 1.000 do banco", TipoCarta.GANHO, 1_000, 0));
        baralho.adicionarCartaOriginal(new Carta("Receba R$ 2.500 do banco", TipoCarta.GANHO, 2_500, 0));
        baralho.adicionarCartaOriginal(new Carta("Avance 3 casas", TipoCarta.AVANCAR, 0, 3));
        baralho.adicionarCartaOriginal(new Carta("Avance 5 casas", TipoCarta.AVANCAR, 0, 5));
        baralho.adicionarCartaOriginal(new Carta("Ir para o INÍCIO (recebe salário)", TipoCarta.IR_INICIO, 0, 0));

        baralho.adicionarCartaOriginal(new Carta("Pague R$ 300 ao banco", TipoCarta.PERDA, 300, 0));
        baralho.adicionarCartaOriginal(new Carta("Pague R$ 700 ao banco", TipoCarta.PERDA, 700, 0));
        baralho.adicionarCartaOriginal(new Carta("Pague R$ 1.200 ao banco", TipoCarta.PERDA, 1_200, 0));
        baralho.adicionarCartaOriginal(new Carta("Volte 2 casas", TipoCarta.RETROCEDER, 0, 2));
        baralho.adicionarCartaOriginal(new Carta("Volte 4 casas", TipoCarta.RETROCEDER, 0, 4));
        baralho.adicionarCartaOriginal(new Carta("Volte para a última posição", TipoCarta.VOLTAR_POSICAO_ANTERIOR, 0, 0));
        baralho.adicionarCartaOriginal(new Carta("Vá diretamente para a PRISÃO!", TipoCarta.IR_PRISAO, 0, 0));
    }

    public Carta sacarCarta() {
        return baralho.pop();
    }

    public PilhaCartas getBaralho() {
        return baralho;
    }
}