package structures;

import model.Carta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PilhaCartas {

    private NoCarta topo;
    private int tamanho;
    private final List<Carta> cartasOriginais = new ArrayList<>();

    public void adicionarCartaOriginal(Carta carta) {
        cartasOriginais.add(carta);
        push(carta);
    }

    public void push(Carta carta) {
        NoCarta novo = new NoCarta(carta);
        novo.setProximo(topo);
        topo = novo;
        tamanho++;
    }

    public Carta pop() {
        if (estaVazia()) {
            reabastecerBaralho();
        }
        if (estaVazia()) {
            return null;
        }
        Carta carta = topo.getCarta();
        topo = topo.getProximo();
        tamanho--;
        return carta;
    }

    public Carta peek() {
        if (estaVazia()) {
            return null;
        }
        return topo.getCarta();
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void limpar() {
        topo = null;
        tamanho = 0;
    }

    public void reabastecerBaralho() {
        if (cartasOriginais.isEmpty()) {
            return;
        }

        System.out.println("\nBaralho esgotado! Remontando e embaralhando...\n");

        limpar();
        List<Carta> embaralhadas = new ArrayList<>(cartasOriginais);
        Collections.shuffle(embaralhadas);

        for (Carta carta : embaralhadas) {
            push(carta);
        }
    }

    public void exibirBaralho() {
        if (estaVazia()) {
            System.out.println("Baralho vazio.");
            return;
        }

        NoCarta atual = topo;
        while (atual != null) {
            System.out.println(atual.getCarta().getDescricao());
            atual = atual.getProximo();
        }
    }

    private static class NoCarta {
        private Carta carta;
        private NoCarta proximo;

        public NoCarta(Carta carta) {
            this.carta = carta;
        }

        public Carta getCarta() {
            return carta;
        }

        public void setCarta(Carta carta) {
            this.carta = carta;
        }

        public NoCarta getProximo() {
            return proximo;
        }

        public void setProximo(NoCarta proximo) {
            this.proximo = proximo;
        }
    }
}