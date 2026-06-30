import model.ConfiguracaoJogo;
import service.*;
import util.ConsoleUtil;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                ============================
                    BANCO IMOBILIÁRIO
                ============================""");

        ConfiguracaoJogo configuracao = new ConfiguracaoJogo(1_500_000, 200_000, 50);

        JogadorService jogadorService = new JogadorService();
        ImovelService imovelService = new ImovelService();
        CartaService cartaService = new CartaService();
        DadoService dadoService = new DadoService();

        imovelService.carregarDadosPadrao();

        menuPreJogo(scanner, configuracao, jogadorService, imovelService);

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

        System.out.println("\n--- Jogadores da Partida ---");
        jogadorService.listarJogadores();

        System.out.println("\nPressione ENTER para iniciar a partida...");
        scanner.nextLine();

        TabuleiroService tabuleiroService = new TabuleiroService(configuracao);
        tabuleiroService.criarTabuleiro(imovelService.getImoveis());

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

    private static void menuPreJogo(Scanner scanner,
                                    ConfiguracaoJogo configuracao,
                                    JogadorService jogadorService,
                                    ImovelService imovelService) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("""

============================================
           MENU DE CONFIGURAÇÃO
============================================
1. Iniciar partida
2. Configurações da partida
3. Gerenciar Jogadores (CRUD)
4. Gerenciar Imóveis (CRUD)
5. Sair
============================================""");

            int opcao = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Escolha: ", 1, 5);
            switch (opcao) {
                case 1 -> continuar = false;
                case 2 -> menuConfiguracoes(scanner, configuracao);
                case 3 -> menuJogadores(scanner, jogadorService, configuracao.getSaldoInicial());
                case 4 -> menuImoveis(scanner, imovelService);
                case 5 -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
            }
        }
    }

    private static void menuConfiguracoes(Scanner scanner, ConfiguracaoJogo configuracao) {
        System.out.println("""

--- Configurações da Partida ---
(Pressione ENTER para manter o valor atual)""");

        System.out.printf("Saldo inicial [R$ %.2f]: ", configuracao.getSaldoInicial());
        String s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            try { configuracao.setSaldoInicial(Double.parseDouble(s)); } catch (NumberFormatException ignored) {}
        }

        System.out.printf("Salário por volta [R$ %.2f]: ", configuracao.getSalarioPorVolta());
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            try { configuracao.setSalarioPorVolta(Double.parseDouble(s)); } catch (NumberFormatException ignored) {}
        }

        System.out.printf("Valor da fiança da prisão [R$ %.2f]: ", configuracao.getValorFianca());
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            try { configuracao.setValorFianca(Double.parseDouble(s)); } catch (NumberFormatException ignored) {}
        }

        System.out.printf("Máximo de rodadas [%d]: ", configuracao.getMaxRodadas());
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            try { configuracao.setMaxRodadas(Integer.parseInt(s)); } catch (NumberFormatException ignored) {}
        }

        System.out.printf("Capacidade do histórico N [%d]: ", configuracao.getCapacidadeHistorico());
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            try { configuracao.setCapacidadeHistorico(Integer.parseInt(s)); } catch (NumberFormatException ignored) {}
        }

        System.out.println("Configurações atualizadas.");
    }

    // --- CRUD Jogadores ---
    private static void menuJogadores(Scanner scanner, JogadorService jogadorService, double saldoInicial) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("""

--- Gerenciar Jogadores ---
1. Cadastrar jogador
2. Listar jogadores
3. Atualizar jogador
4. Remover jogador
5. Voltar""");

            int op = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Escolha: ", 1, 5);
            switch (op) {
                case 1 -> jogadorService.cadastrarJogador(scanner, saldoInicial);
                case 2 -> jogadorService.listarJogadores();
                case 3 -> jogadorService.atualizarJogador(scanner, saldoInicial);
                case 4 -> jogadorService.removerJogador(scanner);
                case 5 -> voltar = true;
            }
        }
    }

    // --- CRUD Imóveis ---
    private static void menuImoveis(Scanner scanner, ImovelService imovelService) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("""

--- Gerenciar Imóveis ---
1. Cadastrar imóvel
2. Listar imóveis
3. Atualizar imóvel
4. Remover imóvel
5. Voltar""");

            int op = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Escolha: ", 1, 5);
            switch (op) {
                case 1 -> imovelService.cadastrar(scanner);
                case 2 -> imovelService.listar();
                case 3 -> imovelService.atualizar(scanner);
                case 4 -> imovelService.remover(scanner);
                case 5 -> voltar = true;
            }
        }
    }
}