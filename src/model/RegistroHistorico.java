package model;

public class RegistroHistorico {

    private final int rodada;
    private final String jogador;
    private final String dadosLancados;
    private final String casaDestino;
    private final String efeito;

    public RegistroHistorico(int rodada, String jogador, String dadosLancados, String casaDestino, String efeito) {
        this.rodada = rodada;
        this.jogador = jogador;
        this.dadosLancados = dadosLancados;
        this.casaDestino = casaDestino;
        this.efeito = efeito;
    }

    public int getRodada() {
        return rodada;
    }

    public String getJogador() {
        return jogador;
    }

    public String getDadosLancados() {
        return dadosLancados;
    }

    public String getCasaDestino() {
        return casaDestino;
    }

    public String getEfeito() {
        return efeito;
    }

    @Override
    public String toString() {
        return String.format("Rodada %d | Jogador: %s | Dados: %s | Parou em: %s | Efeito: %s",
                rodada, jogador, dadosLancados, casaDestino, efeito);
    }
}
