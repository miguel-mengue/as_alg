package model;

public final class Carta {

    private final String descricao;
    private final TipoCarta tipo;
    private final double valor;
    private final int quantidadeCasas;

    public Carta(String descricao, TipoCarta tipo, double valor, int quantidadeCasas) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.quantidadeCasas = quantidadeCasas;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public int getQuantidadeCasas() {
        return quantidadeCasas;
    }

    @Override
    public String toString() {
        return """
                
                ========================
                CARTA SORTE / REVÉS
                Descrição: %s
                Tipo: %s
                ========================""".formatted(descricao, tipo);
    }
}