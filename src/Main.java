import model.ConfiguracaoJogo;
import service.*;
import util.ConsoleUtil;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                ====================================
                        BANCO IMOBILIÁRIO
                ====================================""");

        ConfiguracaoJogo configuracao = new ConfiguracaoJogo(
                1_500_000,
                200_000,
                50
        );

        JogadorService jogadorService = new JogadorService();
        ImovelService imovelService = new ImovelService();
        CartaService cartaService = new CartaService();
        DadoService dadoService = new DadoService();

        imovelService.carregarDadosPadrao();

        cadastrarJogadores(
                scanner,
                jogadorService,
                configuracao.getSaldoInicial()
        );

        if (!jogadorService.possuiMinimoJogadores()) {
            System.out.println("\nERRO: São necessários pelo menos 2 jogadores.");
            scanner.close();
            return;
        }

        if (!imovelService.possuiMinimoImoveis()) {
            System.out.println("\nERRO: São necessários pelo menos 10 imóveis.");
            scanner.close();
            return;
        }

        TabuleiroService tabuleiroService = new TabuleiroService(configuracao);
        tabuleiroService.criarTabuleiro(imovelService.getImoveis());

        System.out.println("\nTabuleiro criado com sucesso.");
        System.out.println("\nJogadores cadastrados:");
        jogadorService.listarJogadores();

        System.out.println("\nPressione ENTER para iniciar a partida...");
        scanner.nextLine();

        JogoService jogoService = new JogoService(
                configuracao,
                jogadorService,
                imovelService,
                tabuleiroService,
                cartaService,
                dadoService,
                scanner
        );

        jogoService.iniciarPartida();

        System.out.println("""
                
                ====================================
                PARTIDA ENCERRADA
                ====================================""");

        scanner.close();
    }

    private static void cadastrarJogadores(Scanner scanner,
                                           JogadorService jogadorService,
                                           double saldoInicial) {
        int quantidade = ConsoleUtil.lerInteiroNoIntervalo(
                scanner,
                "\nQuantidade de jogadores (2 a 6): ",
                2,
                6
        );

        for (int i = 1; i <= quantidade; i++) {
            System.out.printf("%n====================================%n");
            System.out.printf("Cadastro do Jogador %d%n", i);
            System.out.printf("====================================%n");

            jogadorService.cadastrarJogador(scanner, saldoInicial);
        }
    }
}