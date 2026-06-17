package structures;

import model.Casa;

public class ListaDuplamenteLigadaCircular {

    private NoCasa inicio;
    private int tamanho;

    public void adicionar(Casa casa) {

        NoCasa novo = new NoCasa(casa);

        if (inicio == null) {

            inicio = novo;

            inicio.setProximo(inicio);
            inicio.setAnterior(inicio);

        } else {

            NoCasa ultimo = inicio.getAnterior();

            ultimo.setProximo(novo);
            novo.setAnterior(ultimo);

            novo.setProximo(inicio);
            inicio.setAnterior(novo);
        }

        tamanho++;
    }

    public NoCasa getInicio() {
        return inicio;
    }

    public int getTamanho() {
        return tamanho;
    }

    public NoCasa obterPorIndice(int indice) {

        if (indice < 0 || indice >= tamanho) {
            return null;
        }

        NoCasa atual = inicio;

        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }

        return atual;
    }

    public void exibirTabuleiro() {

        if (inicio == null) {

            System.out.println("Tabuleiro vazio.");
            return;
        }

        NoCasa atual = inicio;

        int indice = 0;

        do {

            System.out.println(
                    "[" + indice + "] "
                            + atual.getCasa().getNome()
                            + " - "
                            + atual.getCasa().getTipo()
            );

            atual = atual.getProximo();

            indice++;

        } while (atual != inicio);

        System.out.println(
                "\nÚltima casa conecta novamente ao início."
        );
    }
}