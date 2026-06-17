package model;

public class Imovel {

    private String nome;
    private double valorCompra;
    private double aluguelBase;
    private Jogador dono;
    private double multiplicadorDemanda;
    private double maiorAluguelCobrado;

    public Imovel(String nome, double valorCompra, double aluguelBase) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.aluguelBase = aluguelBase;
        this.multiplicadorDemanda = 1.0;
        this.maiorAluguelCobrado = 0.0;
    }

    public double calcularAluguelAtual() {
        return aluguelBase * multiplicadorDemanda;
    }

    public void registrarVisita() {
        this.multiplicadorDemanda = Math.min(2.0, this.multiplicadorDemanda + 0.1);
    }

    public void registrarAluguelCobrado(double valor) {
        this.maiorAluguelCobrado = Math.max(this.maiorAluguelCobrado, valor);
    }

    public boolean possuiDono() {
        return dono != null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public double getAluguelBase() {
        return aluguelBase;
    }

    public void setAluguelBase(double aluguelBase) {
        this.aluguelBase = aluguelBase;
    }

    public Jogador getDono() {
        return dono;
    }

    public void setDono(Jogador dono) {
        this.dono = dono;
    }

    public double getMultiplicadorDemanda() {
        return multiplicadorDemanda;
    }

    public double getMaiorAluguelCobrado() {
        return maiorAluguelCobrado;
    }

    @Override
    public String toString() {
        return """
                
                Nome: %s
                Valor Compra: R$ %.2f
                Aluguel Base: R$ %.2f
                Multiplicador: %.1f
                Dono: %s""".formatted(
                nome,
                valorCompra,
                aluguelBase,
                multiplicadorDemanda,
                dono != null ? dono.getNome() : "Sem dono"
        );
    }
}