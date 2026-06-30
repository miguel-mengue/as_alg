package model;

public class ConfiguracaoJogo {

    private double saldoInicial;
    private double salarioPorVolta;
    private int maxRodadas;
    private double valorFianca;
    private int capacidadeHistorico;

    public ConfiguracaoJogo(double saldoInicial,
                            double salarioPorVolta,
                            int maxRodadas) {
        this.saldoInicial = saldoInicial;
        this.salarioPorVolta = salarioPorVolta;
        this.maxRodadas = maxRodadas;
        // Fianca padrao: 10% do salario por volta
        this.valorFianca = salarioPorVolta * 0.10;
        this.capacidadeHistorico = 10;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public double getSalarioPorVolta() {
        return salarioPorVolta;
    }

    public void setSalarioPorVolta(double salarioPorVolta) {
        this.salarioPorVolta = salarioPorVolta;
        // Recalcular fianca ao mudar o salario
        this.valorFianca = salarioPorVolta * 0.10;
    }

    public int getMaxRodadas() {
        return maxRodadas;
    }

    public void setMaxRodadas(int maxRodadas) {
        this.maxRodadas = maxRodadas;
    }

    public double getValorFianca() {
        return valorFianca;
    }

    public void setValorFianca(double valorFianca) {
        this.valorFianca = valorFianca;
    }

    public int getCapacidadeHistorico() {
        return capacidadeHistorico;
    }

    public void setCapacidadeHistorico(int capacidadeHistorico) {
        this.capacidadeHistorico = capacidadeHistorico;
    }
}