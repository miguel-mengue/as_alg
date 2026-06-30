package service;

import structures.Fila;
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

    // Fila para histórico de rodadas (FIFO)
    private final Fila<RegistroHistorico> historicoRodadas;

    // Fila para controle dos presos (FIFO)
    private final Fila<Jogador> filaPrisao;

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

        // Histórico de rodadas com limite
        this.historicoRodadas = new Fila<>(configuracao.getCapacidadeHistorico());

        // Fila da prisão
        this.filaPrisao = new Fila<>();
    }

    // Inicia a simulação e o fluxo das rodadas
    public void iniciarPartida() {
        tabuleiroService.posicionarJogadores(jogadorService.getJogadores());

        System.out.println("\nTabuleiro:");
        tabuleiroService.exibirTabuleiro();

        while (!fimDeJogo()) {
            System.out.printf("""

╔══════════════════════════════════════╗
║           RODADA  %-3d                ║
╚══════════════════════════════════════╝""", rodadaAtual);

            System.out.println("\n[H] Ver histórico de rodadas  |  [ENTER] Continuar");
            String escolha = scanner.nextLine().trim();
            if (escolha.equalsIgnoreCase("H")) {
                exibirHistorico();
            }

            executarRodada();
            rodadaAtual++;
        }

        gerarRelatorioFinal();
    }

    // Processa a rodada (primeiro os presos, depois os turnos dos jogadores livres)
    private void executarRodada() {
        processarFilaPrisao();

        List<Jogador> jogadores = new ArrayList<>(jogadorService.getJogadores());

        for (Jogador jogador : jogadores) {
            if (jogador.isFalido() || jogador.isPreso()) continue;

            executarTurno(jogador);

            if (fimDeJogo()) break;
        }
    }

    // Tenta libertar os jogadores na ordem em que foram presos
    private void processarFilaPrisao() {
        if (filaPrisao.estaVazia()) return;

        System.out.println("\n┌─── Processando jogadores na PRISÃO ───┐");

        List<Jogador> presosNestaRodada = new ArrayList<>(filaPrisao.toList());

        for (Jogador preso : presosNestaRodada) {
            if (preso.isFalido()) {
                filaPrisao.remover(preso);
                continue;
            }
            System.out.printf("%n%s está na PRISÃO (tentativa %d de 3)%n",
                    preso.getNome(), preso.getTentativasPrisao() + 1);
            tentarSairDaPrisao(preso);
        }
        System.out.println("└───────────────────────────────────────┘");
    }

    // Envia o jogador para a prisão e o adiciona na fila FIFO
    private void enviarParaPrisao(Jogador jogador) {
        jogador.setPreso(true);
        jogador.setTentativasPrisao(0);
        filaPrisao.enfileirar(jogador);
        System.out.printf("%n>>> %s foi enviado para a PRISÃO!%n", jogador.getNome());
        System.out.printf("    Posição na fila: %d° lugar%n", filaPrisao.getTamanho());
    }

    // Lógica para sair da prisão (fiança, dados duplos ou habilidade do Advogado)
    private void tentarSairDaPrisao(Jogador preso) {
        // Habilidade do Advogado: isenção de fiança uma vez por jogo
        if (preso.getPersonagem() == TipoPersonagem.ADVOGADO && !preso.isUsouIsencaoAdvogado()) {
            System.out.println("⚖  Você é ADVOGADO e tem direito a 1 isenção de fiança.");
            System.out.print("   Deseja usar agora? (S/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                preso.setUsouIsencaoAdvogado(true);
                soltarDaPrisao(preso);
                System.out.println("   Habilidade ativa: Advogado saiu da prisão de graça!");
                executarTurno(preso);
                return;
            }
        }

        double fianca = configuracao.getValorFianca();
        System.out.printf("  Escolha: [1] Pagar fiança (R$ %,.2f)  |  [2] Tentar dados duplos%n", fianca);
        int opcao = lerOpcaoPrisao();

        if (opcao == 1) {
            if (preso.getSaldo() >= fianca) {
                preso.removerSaldo(fianca);
                soltarDaPrisao(preso);
                System.out.printf("  Fiança paga! Saldo: R$ %,.2f%n", preso.getSaldo());
                executarTurno(preso);
            } else {
                System.out.println("  Saldo insuficiente para fiança. Indo para os dados...");
                tentarDadosDuplos(preso);
            }
        } else {
            tentarDadosDuplos(preso);
        }
    }

    // Sair da prisão lançando dados duplos
    private void tentarDadosDuplos(Jogador preso) {
        int[] dados = dadoService.lancarDoisDados();
        System.out.printf("  Dados: %d e %d%n", dados[0], dados[1]);

        preso.setTentativasPrisao(preso.getTentativasPrisao() + 1);

        if (dados[0] == dados[1]) {
            soltarDaPrisao(preso);
            System.out.println("  Dados iguais! " + preso.getNome() + " está livre!");
            tabuleiroService.moverJogador(preso, dados[0] + dados[1]);
            Casa casaDestino = preso.getPosicaoAtual().getCasa();
            System.out.println("  Parou em: " + casaDestino.getNome());
            processarCasa(preso, casaDestino, dados[0], dados[1]);

        } else if (preso.getTentativasPrisao() >= 3) {
            soltarDaPrisao(preso);
            System.out.println("  Esgotou as 3 tentativas. " + preso.getNome() +
                    " saiu da prisão, mas perde a vez nesta rodada.");
        } else {
            System.out.printf("  Continua preso. Tentativas restantes: %d%n",
                    3 - preso.getTentativasPrisao());
        }
    }

    private void soltarDaPrisao(Jogador jogador) {
        jogador.setPreso(false);
        jogador.setTentativasPrisao(0);
        filaPrisao.remover(jogador);
    }

    private int lerOpcaoPrisao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    // Executa o turno do jogador
    private void executarTurno(Jogador jogador) {
        System.out.printf("%n┌── Vez de %-20s (Saldo: R$ %,.2f)%n", jogador.getNome(), jogador.getSaldo());
        System.out.println("└── Pressione ENTER para lançar os dados...");
        scanner.nextLine();

        int[] dados = dadoService.lancarDoisDados();
        int totalMovimento = dados[0] + dados[1];
        System.out.printf("    Dados: %d + %d = %d casas%n", dados[0], dados[1], totalMovimento);

        tabuleiroService.moverJogador(jogador, totalMovimento);

        Casa casaAtual = jogador.getPosicaoAtual().getCasa();
        System.out.printf("    Parou em: [%s]%n", casaAtual.getNome());

        String efeito = processarCasa(jogador, casaAtual, dados[0], dados[1]);

        // Grava no histórico
        RegistroHistorico registro = new RegistroHistorico(
                rodadaAtual,
                jogador.getNome(),
                dados[0] + " e " + dados[1],
                casaAtual.getNome(),
                efeito
        );
        historicoRodadas.enfileirar(registro);

        verificarFalencia(jogador);
    }

    // Processa a casa correspondente
    private String processarCasa(Jogador jogador, Casa casa, int dado1, int dado2) {
        return switch (casa.getTipo()) {
            case INICIO      -> "Passou pelo Início e recebeu salário";
            case IMOVEL      -> processarImovel(jogador, casa.getImovel());
            case IMPOSTO     -> cobrarImposto(jogador);
            case RESTITUICAO -> pagarRestituicao(jogador);
            case SORTE_REVES -> processarCarta(jogador);
            case PRISAO      -> { enviarParaPrisao(jogador); yield "Enviado para a PRISÃO"; }
            case LEILAO      -> processarLeilao(jogador);
        };
    }

    // Compra de imóvel ou pagamento de aluguel
    private String processarImovel(Jogador jogador, Imovel imovel) {
        imovel.registrarVisita();

        if (!imovel.possuiDono()) {
            return oferecerCompra(jogador, imovel);
        }

        if (imovel.getDono() == jogador) {
            System.out.println("    Este imóvel já é seu. Nenhuma ação necessária.");
            return "Parou no próprio imóvel";
        }

        return cobrarAluguel(jogador, imovel);
    }

    // Oferece a compra do imóvel livre
    private String oferecerCompra(Jogador jogador, Imovel imovel) {
        System.out.println("\n    Imóvel disponível para compra:");
        System.out.printf("    %s%n", imovel);

        if (jogador.getSaldo() < imovel.getValorCompra()) {
            System.out.println("    Saldo insuficiente para comprar este imóvel.");
            return "Imóvel disponível, sem saldo para comprar";
        }

        System.out.print("    Deseja comprar? (S/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
            return "Optou por não comprar";
        }

        // Bônus do Construtor: +15% no aluguel base
        if (jogador.getPersonagem() == TipoPersonagem.CONSTRUTOR) {
            double novoAluguel = imovel.getAluguelBase() * 1.15;
            imovel.setAluguelBase(novoAluguel);
            System.out.printf("    Habilidade Construtor: Aluguel base aumentado para R$ %,.2f%n", novoAluguel);
        }

        jogador.removerSaldo(imovel.getValorCompra());
        jogador.adicionarImovel(imovel);
        imovel.setDono(jogador);

        System.out.printf("    Compra realizada! Saldo restante: R$ %,.2f%n", jogador.getSaldo());
        return "Comprou " + imovel.getNome() + " por R$ " + imovel.getValorCompra();
    }

    // Cobra aluguel
    private String cobrarAluguel(Jogador visitante, Imovel imovel) {
        double aluguel = imovel.calcularAluguelAtual();
        Jogador dono = imovel.getDono();

        // Bônus do Negociante: 10% de desconto no aluguel
        if (visitante.getPersonagem() == TipoPersonagem.NEGOCIANTE) {
            double desconto = aluguel * 0.10;
            aluguel -= desconto;
            System.out.printf("    Habilidade Negociante: 10%% de desconto! Economizou R$ %,.2f%n", desconto);
        }

        visitante.removerSaldo(aluguel);
        dono.adicionarSaldo(aluguel);
        imovel.registrarAluguelCobrado(aluguel);

        System.out.printf("    %s pagou R$ %,.2f de aluguel para %s%n", visitante.getNome(), aluguel, dono.getNome());
        System.out.printf("    (Multiplicador de demanda atual: %.1fx)%n", imovel.getMultiplicadorDemanda());

        return String.format("Pagou aluguel R$ %.2f para %s", aluguel, dono.getNome());
    }

    // Cobra 5% do patrimônio
    private String cobrarImposto(Jogador jogador) {
        double patrimonioTotal = jogador.calcularPatrimonio();
        double impostoBase     = patrimonioTotal * 0.05;
        double impostoFinal    = jogador.getPersonagem().ajustarImposto(impostoBase);

        jogador.removerSaldo(impostoFinal);

        System.out.printf("    IMPOSTO: 5%% do patrimônio (R$ %,.2f) = R$ %,.2f cobrado%n",
                patrimonioTotal, impostoFinal);

        if (jogador.getPersonagem() == TipoPersonagem.ESPECULADOR) {
            System.out.println("    Habilidade Especulador: pagou +10% de taxa sobre o imposto.");
        }

        return String.format("Pagou imposto R$ %.2f", impostoFinal);
    }

    // Paga 10% do salário como restituição
    private String pagarRestituicao(Jogador jogador) {
        double valorRestituicao = configuracao.getSalarioPorVolta() * 0.10;
        jogador.adicionarSaldo(valorRestituicao);
        System.out.printf("    RESTITUIÇÃO: Recebeu R$ %,.2f%n", valorRestituicao);
        return String.format("Recebeu restituição R$ %.2f", valorRestituicao);
    }

    // Saca e processa a carta do baralho
    private String processarCarta(Jogador jogador) {
        Carta carta = cartaService.sacarCarta();
        System.out.println("\n    " + carta);

        double saldoAntes = jogador.getSaldo();

        switch (carta.getTipo()) {
            case GANHO -> {
                jogador.adicionarSaldo(carta.getValor());
                System.out.printf("    Saldo: R$ %,.2f → R$ %,.2f%n",
                        saldoAntes, jogador.getSaldo());
            }
            case PERDA -> {
                jogador.removerSaldo(carta.getValor());
                System.out.printf("    Saldo: R$ %,.2f → R$ %,.2f%n",
                        saldoAntes, jogador.getSaldo());
            }
            case AVANCAR -> {
                String posAntes = jogador.getPosicaoAtual().getCasa().getNome();
                tabuleiroService.moverJogador(jogador, carta.getQuantidadeCasas());
                String posDepois = jogador.getPosicaoAtual().getCasa().getNome();
                System.out.printf("    Avançou %d casas: [%s] → [%s]%n",
                        carta.getQuantidadeCasas(), posAntes, posDepois);
            }
            case RETROCEDER -> {
                String posAntes = jogador.getPosicaoAtual().getCasa().getNome();
                tabuleiroService.retrocederJogador(jogador, carta.getQuantidadeCasas());
                String posDepois = jogador.getPosicaoAtual().getCasa().getNome();
                System.out.printf("    Retrocedeu %d casas: [%s] → [%s]%n",
                        carta.getQuantidadeCasas(), posAntes, posDepois);
            }
            case IR_INICIO -> {
                tabuleiroService.enviarParaInicio(jogador);
                System.out.println("    Foi para o INÍCIO e coletou o salário!");
            }
            case VOLTAR_POSICAO_ANTERIOR -> {
                NoCasa posAnterior = jogador.getPosicaoAnterior();
                System.out.printf("    Voltou para a posição anterior: [%s]%n",
                        posAnterior.getCasa().getNome());
                jogador.setPosicaoAtual(posAnterior);
            }
            case IR_PRISAO -> {
                enviarParaPrisao(jogador);
            }
        }

        return "Carta: " + carta.getDescricao();
    }

    // Leilão de imóvel com lances interativos entre todos os jogadores
    private String processarLeilao(Jogador jogadorAtual) {
        System.out.println("\n    ┌──────────────────────────────────────────────┐");
        System.out.println("    │             LEILÃO INICIADO!                 │");
        System.out.println("    └──────────────────────────────────────────────┘");

        Imovel imovel = imovelService.obterImovelSemDono();
        if (imovel == null) {
            System.out.println("    Não há imóveis sem dono disponíveis.");
            return "Leilão: nenhum imóvel disponível";
        }

        double precoOriginal = imovel.getValorCompra();
        double lanceMinimo = precoOriginal * 0.50;

        System.out.printf("    Imóvel sorteado: %s%n", imovel.getNome());
        System.out.printf("    Preço original: R$ %,.2f%n", precoOriginal);
        System.out.printf("    Lance mínimo exigido (50%%): R$ %,.2f%n", lanceMinimo);

        List<Jogador> ativos = new ArrayList<>(jogadorService.jogadoresAtivos());
        if (ativos.size() < 2) {
            System.out.println("    Falta jogadores para disputar o leilão.");
            return "Leilão: jogadores insuficientes";
        }

        int idxAtual = ativos.indexOf(jogadorAtual);
        List<Jogador> ordemLances = new ArrayList<>();
        for (int i = 1; i < ativos.size(); i++) {
            ordemLances.add(ativos.get((idxAtual + i) % ativos.size()));
        }
        ordemLances.add(jogadorAtual);

        Jogador vencedorLeilao = null;
        double maiorLance = 0;

        System.out.println("\n    --- Rodada de Lances ---");
        for (Jogador j : ordemLances) {
            if (j.isFalido()) continue;

            System.out.printf("    %s, digite seu lance (0 para passar | Saldo: R$ %,.2f): ",
                    j.getNome(), j.getSaldo());
            
            double lance = 0;
            try {
                lance = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                lance = 0;
            }

            if (lance > 0) {
                if (lance < lanceMinimo) {
                    System.out.printf("    Lance recusado: menor que o mínimo de R$ %,.2f.%n", lanceMinimo);
                } else if (lance <= maiorLance) {
                    System.out.printf("    Lance recusado: menor ou igual ao maior lance de R$ %,.2f.%n", maiorLance);
                } else if (lance > j.getSaldo()) {
                    System.out.println("    Lance recusado: saldo insuficiente.");
                } else {
                    maiorLance = lance;
                    vencedorLeilao = j;
                    System.out.printf("    Maior lance atual: R$ %,.2f por %s%n", maiorLance, j.getNome());
                }
            } else {
                System.out.printf("    %s passou.%n", j.getNome());
            }
        }

        System.out.println("    ----------------------------------");
        if (vencedorLeilao != null && maiorLance >= lanceMinimo) {
            vencedorLeilao.removerSaldo(maiorLance);
            vencedorLeilao.adicionarImovel(imovel);
            imovel.setDono(vencedorLeilao);

            if (vencedorLeilao.getPersonagem() == TipoPersonagem.CONSTRUTOR) {
                double novoAluguel = imovel.getAluguelBase() * 1.15;
                imovel.setAluguelBase(novoAluguel);
                System.out.printf("    Habilidade Construtor: Aluguel base aumentado para R$ %,.2f%n", novoAluguel);
            }

            System.out.printf("    🏆 %s comprou %s por R$ %,.2f no leilão!%n",
                    vencedorLeilao.getNome(), imovel.getNome(), maiorLance);
            
            return String.format("Leilão: %s comprou %s por R$ %.2f", vencedorLeilao.getNome(), imovel.getNome(), maiorLance);
        } else {
            System.out.println("    Sem lances válidos. Imóvel continua livre.");
            return "Leilão: nenhum imóvel arrematado";
        }
    }

    // Verifica falência
    private void verificarFalencia(Jogador jogador) {
        if (jogador.getSaldo() >= 0) return;

        System.out.printf("%n    ⚠  %s está com saldo negativo (R$ %,.2f).%n",
                jogador.getNome(), jogador.getSaldo());

        jogador.setFalido(true);
        filaPrisao.remover(jogador);

        for (Imovel imovel : jogador.getPropriedades()) {
            imovel.setDono(null);
        }
        jogador.limparPropriedades();

        System.out.printf("    ✗  %s faliu e foi eliminado! Propriedades liberadas.%n",
                jogador.getNome());
    }

    // Fim de jogo
    private boolean fimDeJogo() {
        if (rodadaAtual > configuracao.getMaxRodadas()) return true;
        return jogadorService.jogadoresAtivos().size() <= 1;
    }

    // Relatório final
    private void gerarRelatorioFinal() {
        System.out.println("""

╔══════════════════════════════════════╗
║          RELATÓRIO FINAL             ║
╚══════════════════════════════════════╝""");

        System.out.println("\n── Classificação ──────────────────");
        jogadorService.getJogadores()
                .stream()
                .sorted(Comparator.comparingDouble(Jogador::calcularPatrimonio).reversed())
                .forEach(j -> System.out.printf(
                        "  %-20s | Patrimônio: R$ %,12.2f | Imóveis: %2d | Voltas: %2d | %s%n",
                        j.getNome(),
                        j.calcularPatrimonio(),
                        j.getPropriedades().size(),
                        j.getVoltasCompletas(),
                        j.isFalido() ? "FALIDO" : "Ativo"
                ));

        Imovel destaque = imovelService.obterMaiorAluguelDaPartida();
        if (destaque != null && destaque.getMaiorAluguelCobrado() > 0) {
            System.out.println("\n── Imóvel Destaque ────────────────────");
            System.out.printf("  %s%n", destaque.getNome());
            System.out.printf("  Maior aluguel cobrado: R$ %,.2f%n", destaque.getMaiorAluguelCobrado());
        }

        System.out.println("\n── Histórico Final (últimas rodadas) ──────────");
        exibirHistorico();

        List<Jogador> ranking = jogadorService.getJogadores()
                .stream()
                .filter(j -> !j.isFalido())
                .sorted(Comparator.comparingDouble(Jogador::calcularPatrimonio).reversed())
                .toList();

        System.out.println();
        if (!ranking.isEmpty()) {
            System.out.printf("  🏆  VENCEDOR: %s com R$ %,.2f!%n",
                    ranking.get(0).getNome(), ranking.get(0).calcularPatrimonio());
        } else {
            System.out.println("  Sem vencedores.");
        }
    }

    // Exibe o histórico salvo
    public void exibirHistorico() {
        if (historicoRodadas.estaVazia()) {
            System.out.println("  Nenhum registro ainda.");
            return;
        }
        System.out.printf("  (Histórico de rodadas)%n");
        for (RegistroHistorico r : historicoRodadas.toList()) {
            System.out.println("  " + r);
        }
    }
}