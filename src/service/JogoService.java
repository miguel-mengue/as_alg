package service;

import structures.NoCasa;
import model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class JogoService {

    private final ConfiguracaoJogo configuracao;
    private final JogadorService jogadorService;
    private final ImovelService imovelService;
    private final TabuleiroService tabuleiroService;
    private final CartaService cartaService;
    private final DadoService dadoService;
    private final Scanner scanner;
    private int rodadaAtual;

    public JogoService(ConfiguracaoJogo configuracao,
                       JogadorService jogadorService,
                       ImovelService imovelService,
                       TabuleiroService tabuleiroService,
                       CartaService cartaService,
                       DadoService dadoService,
                       Scanner scanner) {
        this.configuracao = configuracao;
        this.jogadorService = jogadorService;
        this.imovelService = imovelService;
        this.tabuleiroService = tabuleiroService;
        this.cartaService = cartaService;
        this.dadoService = dadoService;
        this.scanner = scanner;
        this.rodadaAtual = 1;
    }

    public void iniciarPartida() {
        tabuleiroService.posicionarJogadores(jogadorService.getJogadores());

        while (!fimDeJogo()) {
            System.out.println("""
                    
                    ====================================
                    RODADA %d
                    ====================================""".formatted(rodadaAtual));

            executarRodada();
            rodadaAtual++;
        }

        gerarRelatorioFinal();
    }

    private void executarRodada() {
        List<Jogador> jogadores = new ArrayList<>(jogadorService.getJogadores());

        for (Jogador jogador : jogadores) {
            if (jogador.isFalido()) {
                continue;
            }

            executarTurno(jogador);

            if (fimDeJogo()) {
                break;
            }
        }
    }

    private void executarTurno(Jogador jogador) {
        System.out.printf("%nVez de %s%n", jogador.getNome());
        System.out.println("Pressione ENTER para lançar os dados...");
        scanner.nextLine();

        int[] dados = dadoService.lancarDoisDados();
        int movimento = dados[0] + dados[1];

        System.out.printf("Dados: %d e %d (Movimento: %d)%n", dados[0], dados[1], movimento);

        tabuleiroService.moverJogador(jogador, movimento);

        NoCasa posicao = jogador.getPosicaoAtual();
        Casa casa = posicao.getCasa();

        System.out.printf("Parou em: %s%n", casa.getNome());

        processarCasa(jogador, casa);
        verificarFalencia(jogador);
    }

    private void processarCasa(Jogador jogador, Casa casa) {
        switch (casa.getTipo()) {
            case IMOVEL -> processarImovel(jogador, casa.getImovel());
            case IMPOSTO -> cobrarImposto(jogador);
            case RESTITUICAO -> pagarRestituicao(jogador);
            case SORTE_REVES -> processarCarta(jogador);
            default -> {
            }
        }
    }

    private void processarImovel(Jogador jogador, Imovel imovel) {
        imovel.registrarVisita();

        if (!imovel.possuiDono()) {
            oferecerCompra(jogador, imovel);
            return;
        }

        if (imovel.getDono() == jogador) {
            System.out.println("Você já é dono deste imóvel.");
            return;
        }

        cobrarAluguel(jogador, imovel);
    }

    private void oferecerCompra(Jogador jogador, Imovel imovel) {
        System.out.println("\nImóvel disponível:");
        System.out.println(imovel);

        if (jogador.getSaldo() < imovel.getValorCompra()) {
            System.out.println("Saldo insuficiente.");
            return;
        }

        System.out.print("Deseja comprar? (S/N): ");
        String resposta = scanner.nextLine();

        if (!resposta.equalsIgnoreCase("S")) {
            return;
        }

        jogador.removerSaldo(imovel.getValorCompra());
        jogador.adicionarImovel(imovel);
        imovel.setDono(jogador);

        System.out.println("Compra realizada.");
    }

    private void cobrarAluguel(Jogador visitante, Imovel imovel) {
        double aluguel = imovel.calcularAluguelAtual();
        Jogador dono = imovel.getDono();

        visitante.removerSaldo(aluguel);
        dono.adicionarSaldo(aluguel);
        imovel.registrarAluguelCobrado(aluguel);

        System.out.printf("%s pagou R$ %.2f para %s%n", visitante.getNome(), aluguel, dono.getNome());
    }

    private void cobrarImposto(Jogador jogador) {
        double impostoBase = jogador.calcularPatrimonio() * 0.05;
        double impostoFinal = jogador.getPersonagem().ajustarImposto(impostoBase);

        jogador.removerSaldo(impostoFinal);
        System.out.printf("Imposto pago: R$ %.2f%n", impostoFinal);
    }

    private void pagarRestituicao(Jogador jogador) {
        double valor = 1000.0;
        jogador.adicionarSaldo(valor);
        System.out.printf("Restituição recebida: R$ %.2f%n", valor);
    }

    private void processarCarta(Jogador jogador) {
        Carta carta = cartaService.sacarCarta();
        System.out.println(carta);

        switch (carta.getTipo()) {
            case GANHO -> jogador.adicionarSaldo(carta.getValor());
            case PERDA -> jogador.removerSaldo(carta.getValor());
            case AVANCAR -> tabuleiroService.moverJogador(jogador, carta.getQuantidadeCasas());
            case RETROCEDER -> tabuleiroService.retrocederJogador(jogador, carta.getQuantidadeCasas());
            case IR_INICIO -> tabuleiroService.enviarParaInicio(jogador);
            case VOLTAR_POSICAO_ANTERIOR -> jogador.setPosicaoAtual(jogador.getPosicaoAnterior());
        }
    }

    private void verificarFalencia(Jogador jogador) {
        if (jogador.getSaldo() >= 0) {
            return;
        }

        jogador.setFalido(true);

        for (Imovel imovel : jogador.getPropriedades()) {
            imovel.setDono(null);
        }

        jogador.limparPropriedades();
        System.out.printf("%n%s FALIU!%n", jogador.getNome());
    }

    private boolean fimDeJogo() {
        if (rodadaAtual > configuracao.getMaxRodadas()) {
            return true;
        }
        return jogadorService.jogadoresAtivos().size() <= 1;
    }

    private void gerarRelatorioFinal() {
        System.out.println("""
                
                ====================================
                RELATÓRIO FINAL
                ====================================""");

        jogadorService.getJogadores()
                .stream()
                .sorted(Comparator.comparingDouble(Jogador::calcularPatrimonio).reversed())
                .forEach(jogador -> {
                    System.out.printf("%nJogador: %s%n", jogador.getNome());
                    System.out.printf("Patrimônio: R$ %.2f%n", jogador.calcularPatrimonio());
                    System.out.printf("Imóveis: %d%n", jogador.getPropriedades().size());
                    System.out.printf("Voltas: %d%n", jogador.getVoltasCompletas());
                });

        Imovel destaque = imovelService.obterMaiorAluguelDaPartida();
        if (destaque != null) {
            System.out.println("\nImóvel com maior aluguel:");
            System.out.println(destaque.getNome());
            System.out.printf("Maior aluguel cobrado: R$ %.2f%n", destaque.getMaiorAluguelCobrado());
        }

        List<Jogador> ranking = jogadorService.getJogadores()
                .stream()
                .filter(j -> !j.isFalido())
                .sorted(Comparator.comparingDouble(Jogador::calcularPatrimonio).reversed())
                .toList();

        if (!ranking.isEmpty()) {
            System.out.printf("%nVENCEDOR: %s%n", ranking.get(0).getNome());
        }
    }
}