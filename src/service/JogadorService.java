package service;

import model.Jogador;
import model.TipoPersonagem;
import util.ConsoleUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JogadorService {

    private final List<Jogador> jogadores;

    public JogadorService() {
        this.jogadores = new ArrayList<>();
    }

    public List<Jogador> getJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    public void cadastrarJogador(Scanner scanner, double saldoInicial) {
        if (jogadores.size() >= 6) {
            System.out.println("Máximo de 6 jogadores atingido.");
            return;
        }

        String nome = ConsoleUtil.lerString(scanner, "Nome: ");

        System.out.println("""
                Escolha o Personagem:
                1 - ESPECULADOR
                2 - NEGOCIANTE
                3 - ADVOGADO
                4 - CONSTRUTOR""");

        int opcao = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Opção (1-4): ", 1, 4);

        TipoPersonagem personagem = switch (opcao) {
            case 1 -> TipoPersonagem.ESPECULADOR;
            case 2 -> TipoPersonagem.NEGOCIANTE;
            case 3 -> TipoPersonagem.ADVOGADO;
            default -> TipoPersonagem.CONSTRUTOR;
        };

        jogadores.add(new Jogador(nome, saldoInicial, personagem));
        System.out.println("Jogador cadastrado com sucesso.");
    }

    public void listarJogadores() {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        jogadores.forEach(System.out::println);
    }

    public void removerJogador(Jogador jogador) {
        jogadores.remove(jogador);
    }

    public boolean possuiMinimoJogadores() {
        return jogadores.size() >= 2;
    }

    public List<Jogador> jogadoresAtivos() {
        return jogadores.stream()
                .filter(jogador -> !jogador.isFalido())
                .toList();
    }
}