package service;

import model.Imovel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImovelService {

    private final List<Imovel> imoveis;

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