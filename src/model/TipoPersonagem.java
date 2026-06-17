package model;

public enum TipoPersonagem {
    ESPECULADOR {
        @Override
        public double ajustarSalario(double salarioOriginal) {
            return salarioOriginal * 1.20;
        }
    },
    NEGOCIANTE,
    ADVOGADO {
        @Override
        public double ajustarImposto(double impostoOriginal) {
            return impostoOriginal * 0.80;
        }
    },
    CONSTRUTOR;

    public double ajustarSalario(double salarioOriginal) {
        return salarioOriginal;
    }

    public double ajustarImposto(double impostoOriginal) {
        return impostoOriginal;
    }
}