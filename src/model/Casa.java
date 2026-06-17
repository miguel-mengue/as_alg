package model;

public class Casa {

    private String nome;
    private TipoCasa tipo;
    private Imovel imovel;

    public Casa(String nome,
                TipoCasa tipo) {

        this.nome = nome;
        this.tipo = tipo;
    }

    public Casa(String nome,
                TipoCasa tipo,
                Imovel imovel) {

        this.nome = nome;
        this.tipo = tipo;
        this.imovel = imovel;
    }

    public boolean possuiImovel() {
        return imovel != null;
    }

    public String getNome() {
        return nome;
    }

    public TipoCasa getTipo() {
        return tipo;
    }

    public Imovel getImovel() {
        return imovel;
    }

    @Override
    public String toString() {

        return "Casa{" +
                "nome='" + nome + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}