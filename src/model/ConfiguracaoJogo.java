package model;

public class ConfiguracaoJogo {

    private double saldoInicial;
    private double salarioPorVolta;
    private int maxRodadas;

    public ConfiguracaoJogo(double saldoInicial,
                            double salarioPorVolta,
                            int maxRodadas) {

        this.saldoInicial = saldoInicial;
        this.salarioPorVolta = salarioPorVolta;
        this.maxRodadas = maxRodadas;
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
    }

    public int getMaxRodadas() {
        return maxRodadas;
    }

    public void setMaxRodadas(int maxRodadas) {
        this.maxRodadas = maxRodadas;
    }
}