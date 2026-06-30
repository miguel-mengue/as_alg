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
    private static final int MAX_JOGADORES = 6;

    public JogadorService() {
        this.jogadores = new ArrayList<>();
    }

    public List<Jogador> getJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    public void cadastrarJogador(Scanner scanner, double saldoInicial) {
        if (jogadores.size() >= MAX_JOGADORES) {
            System.out.println("Máximo de " + MAX_JOGADORES + " jogadores atingido.");
            return;
        }

        String nome = ConsoleUtil.lerString(scanner, "Nome: ");

        System.out.println("""
                Escolha o Personagem:
                1 - ESPECULADOR   (+20% salário, +10% imposto)
                2 - NEGOCIANTE    (paga -10% de aluguel)
                3 - ADVOGADO      (sai da prisão de graça 1x)
                4 - CONSTRUTOR    (+15% no aluguel base dos imóveis que compra)""");

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
        for (int i = 0; i < jogadores.size(); i++) {
            Jogador j = jogadores.get(i);
            System.out.printf("[%d] %-20s | Personagem: %-12s | Saldo: R$ %,.2f%n",
                    i + 1, j.getNome(), j.getPersonagem(), j.getSaldo());
        }
    }

    public void atualizarJogador(Scanner scanner, double saldoInicial) {
        listarJogadores();
        if (jogadores.isEmpty()) return;
        int idx = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Número do jogador a atualizar: ", 1, jogadores.size()) - 1;
        Jogador j = jogadores.get(idx);
        System.out.println("Editando: " + j.getNome());

        System.out.print("Novo nome [" + j.getNome() + "]: ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) j.setNome(novoNome);

        System.out.println("""
                Novo personagem:
                1 - ESPECULADOR | 2 - NEGOCIANTE | 3 - ADVOGADO | 4 - CONSTRUTOR | (ENTER para manter)""");
        System.out.print("Opção: ");
        String opcaoStr = scanner.nextLine().trim();
        if (!opcaoStr.isEmpty()) {
            try {
                int op = Integer.parseInt(opcaoStr);
                TipoPersonagem novo = switch (op) {
                    case 1 -> TipoPersonagem.ESPECULADOR;
                    case 2 -> TipoPersonagem.NEGOCIANTE;
                    case 3 -> TipoPersonagem.ADVOGADO;
                    default -> TipoPersonagem.CONSTRUTOR;
                };
                j.setPersonagem(novo);
            } catch (NumberFormatException ignored) {}
        }
        System.out.println("Jogador atualizado.");
    }

    public void removerJogador(Scanner scanner) {
        listarJogadores();
        if (jogadores.isEmpty()) return;
        int idx = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Número do jogador a remover: ", 1, jogadores.size()) - 1;
        Jogador removido = jogadores.remove(idx);
        System.out.println("Jogador '" + removido.getNome() + "' removido.");
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