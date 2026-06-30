package service;

import structures.ListaDuplamenteLigadaCircular;
import structures.NoCasa;
import model.*;
import java.util.List;

public class TabuleiroService {

    private final ListaDuplamenteLigadaCircular tabuleiro;
    private final ConfiguracaoJogo configuracao;

    public TabuleiroService(ConfiguracaoJogo configuracao) {
        this.configuracao = configuracao;
        this.tabuleiro = new ListaDuplamenteLigadaCircular();
    }

    public void criarTabuleiro(List<Imovel> imoveis) {
        tabuleiro.adicionar(new Casa("INÍCIO", TipoCasa.INICIO));

        int contador = 0;
        for (Imovel imovel : imoveis) {
            contador++;
            tabuleiro.adicionar(new Casa(imovel.getNome(), TipoCasa.IMOVEL, imovel));

            if (contador % 3 == 0) {
                tabuleiro.adicionar(new Casa("SORTE/REVÉS", TipoCasa.SORTE_REVES));
            }

            if (contador % 4 == 0) {
                tabuleiro.adicionar(new Casa("IMPOSTO", TipoCasa.IMPOSTO));
            }

            if (contador % 5 == 0) {
                tabuleiro.adicionar(new Casa("RESTITUIÇÃO", TipoCasa.RESTITUICAO));
            }

            if (contador % 6 == 0) {
                tabuleiro.adicionar(new Casa("PRISÃO", TipoCasa.PRISAO));
            }

            if (contador % 7 == 0) {
                tabuleiro.adicionar(new Casa("LEILÃO", TipoCasa.LEILAO));
            }
        }
    }

    public void posicionarJogadores(List<Jogador> jogadores) {
        NoCasa inicio = tabuleiro.getInicio();
        for (Jogador jogador : jogadores) {
            jogador.setPosicaoAtual(inicio);
            jogador.setPosicaoAnterior(inicio);
        }
    }

    public void moverJogador(Jogador jogador, int quantidadeCasas) {
        jogador.setPosicaoAnterior(jogador.getPosicaoAtual());

        NoCasa atual = jogador.getPosicaoAtual();
        NoCasa inicio = tabuleiro.getInicio();

        for (int i = 0; i < quantidadeCasas; i++) {
            atual = atual.getProximo();
            if (atual == inicio) {
                pagarSalario(jogador);
            }
        }
        jogador.setPosicaoAtual(atual);
    }

    public void retrocederJogador(Jogador jogador, int quantidadeCasas) {
        jogador.setPosicaoAnterior(jogador.getPosicaoAtual());

        NoCasa atual = jogador.getPosicaoAtual();
        for (int i = 0; i < quantidadeCasas; i++) {
            atual = atual.getAnterior();
        }
        jogador.setPosicaoAtual(atual);
    }

    public void enviarParaInicio(Jogador jogador) {
        jogador.setPosicaoAnterior(jogador.getPosicaoAtual());
        jogador.setPosicaoAtual(tabuleiro.getInicio());
        pagarSalario(jogador);
    }

    private void pagarSalario(Jogador jogador) {
        double salarioBase = configuracao.getSalarioPorVolta();
        double salarioFinal = jogador.getPersonagem().ajustarSalario(salarioBase);

        jogador.adicionarSaldo(salarioFinal);
        jogador.incrementarVolta();

        System.out.printf("%n>>> %s passou pelo INÍCIO e recebeu salário de R$ %.2f%n",
                jogador.getNome(), salarioFinal);

        if (jogador.getPersonagem() == TipoPersonagem.ESPECULADOR) {
            System.out.println("    (Especulador: +20% de bônus no salário)");
        }
    }

    public void exibirTabuleiro() {
        tabuleiro.exibirTabuleiro();
    }

    public ListaDuplamenteLigadaCircular getTabuleiro() {
        return tabuleiro;
    }

    public NoCasa getInicio() {
        return tabuleiro.getInicio();
    }
}