package service;

import model.Imovel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import util.ConsoleUtil;

public class ImovelService {

    private final List<Imovel> imoveis;
    private static final int MAX_IMOVEIS = 40;

    public ImovelService() {
        this.imoveis = new ArrayList<>();
    }

    public List<Imovel> getImoveis() {
        return Collections.unmodifiableList(imoveis);
    }

    public boolean possuiMinimoImoveis() {
        return imoveis.size() >= 10;
    }

    public void carregarDadosPadrao() {
        imoveis.add(new Imovel("Chalé da Serra Gaúcha", 200_000, 1_000));
        imoveis.add(new Imovel("Flat Paulista", 350_000, 1_750));
        imoveis.add(new Imovel("Sobrado de Ouro Preto", 400_000, 2_000));
        imoveis.add(new Imovel("Pousada do Pantanal", 500_000, 2_500));
        imoveis.add(new Imovel("Mansão de Gramado", 600_000, 3_000));
        imoveis.add(new Imovel("Cobertura de Florianópolis", 750_000, 3_750));
        imoveis.add(new Imovel("Fazenda do Cerrado", 280_000, 1_400));
        imoveis.add(new Imovel("Bangalô de Búzios", 450_000, 2_250));
        imoveis.add(new Imovel("Penthouse de Salvador", 850_000, 4_250));
        imoveis.add(new Imovel("Casa de Bonito", 220_000, 1_100));
        imoveis.add(new Imovel("Palacete de Petrópolis", 1_000_000, 5_000));
        imoveis.add(new Imovel("Rancho do Vale do São Francisco", 310_000, 1_550));
    }

    // --- CRUD ---

    public void cadastrar(Scanner scanner) {
        if (imoveis.size() >= MAX_IMOVEIS) {
            System.out.println("Limite de " + MAX_IMOVEIS + " imóveis atingido.");
            return;
        }
        System.out.println("\n--- Cadastrar Imóvel ---");
        String nome = ConsoleUtil.lerString(scanner, "Nome do imóvel: ");
        double valorCompra = ConsoleUtil.lerDoublePositivo(scanner, "Valor de compra (R$): ");
        double aluguelBase = ConsoleUtil.lerDoublePositivo(scanner, "Aluguel base (R$): ");
        imoveis.add(new Imovel(nome, valorCompra, aluguelBase));
        System.out.println("Imóvel cadastrado com sucesso.");
    }

    public void listar() {
        System.out.println("\n--- Lista de Imóveis ---");
        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imóvel cadastrado.");
            return;
        }
        for (int i = 0; i < imoveis.size(); i++) {
            Imovel im = imoveis.get(i);
            System.out.printf("[%d] %-35s | Compra: R$ %,.2f | Aluguel: R$ %,.2f%n",
                    i + 1, im.getNome(), im.getValorCompra(), im.getAluguelBase());
        }
    }

    public void atualizar(Scanner scanner) {
        listar();
        if (imoveis.isEmpty()) return;
        int idx = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Número do imóvel a atualizar: ", 1, imoveis.size()) - 1;
        Imovel im = imoveis.get(idx);
        System.out.println("Editando: " + im.getNome());
        System.out.println("(Pressione ENTER para manter o valor atual)");

        System.out.print("Novo nome [" + im.getNome() + "]: ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) im.setNome(novoNome);

        System.out.print("Novo valor de compra [R$ " + im.getValorCompra() + "]: ");
        String novoValorStr = scanner.nextLine().trim();
        if (!novoValorStr.isEmpty()) {
            try { im.setValorCompra(Double.parseDouble(novoValorStr)); } catch (NumberFormatException ignored) {}
        }

        System.out.print("Novo aluguel base [R$ " + im.getAluguelBase() + "]: ");
        String novoAluguelStr = scanner.nextLine().trim();
        if (!novoAluguelStr.isEmpty()) {
            try { im.setAluguelBase(Double.parseDouble(novoAluguelStr)); } catch (NumberFormatException ignored) {}
        }

        System.out.println("Imóvel atualizado.");
    }

    public void remover(Scanner scanner) {
        listar();
        if (imoveis.isEmpty()) return;
        int idx = ConsoleUtil.lerInteiroNoIntervalo(scanner, "Número do imóvel a remover: ", 1, imoveis.size()) - 1;
        Imovel removido = imoveis.remove(idx);
        System.out.println("Imóvel '" + removido.getNome() + "' removido.");
    }

    public Imovel obterImovelSemDono() {
        List<Imovel> semDono = new ArrayList<>();
        for (Imovel im : imoveis) {
            if (!im.possuiDono()) semDono.add(im);
        }
        if (semDono.isEmpty()) return null;
        int idx = (int) (Math.random() * semDono.size());
        return semDono.get(idx);
    }

    public Imovel obterMaiorAluguelDaPartida() {
        Imovel maior = null;
        for (Imovel imovel : imoveis) {
            if (maior == null || imovel.getMaiorAluguelCobrado() > maior.getMaiorAluguelCobrado()) {
                maior = imovel;
            }
        }
        return maior;
    }
}