package model;

import structures.NoCasa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogador {

    private String nome;
    private double saldo;
    private TipoPersonagem personagem;
    private NoCasa posicaoAtual;
    private NoCasa posicaoAnterior;
    private final List<Imovel> propriedades;
    private int voltasCompletas;
    private boolean falido;

    public Jogador(String nome, double saldo, TipoPersonagem personagem) {
        this.nome = nome;
        this.saldo = saldo;
        this.personagem = personagem;
        this.propriedades = new ArrayList<>();
        this.voltasCompletas = 0;
        this.falido = false;
    }

    public void adicionarSaldo(double valor) {
        saldo += valor;
    }

    public void removerSaldo(double valor) {
        saldo -= valor;
    }

    public void adicionarImovel(Imovel imovel) {
        propriedades.add(imovel);
    }

    public void removerImovel(Imovel imovel) {
        propriedades.remove(imovel);
    }

    public double calcularPatrimonio() {
        double patrimonio = saldo;
        for (Imovel imovel : propriedades) {
            patrimonio += imovel.getValorCompra();
        }
        return patrimonio;
    }

    public void limparPropriedades() {
        propriedades.clear();
    }

    public boolean possuiImoveis() {
        return !propriedades.isEmpty();
    }

    public void incrementarVolta() {
        voltasCompletas++;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public TipoPersonagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(TipoPersonagem personagem) {
        this.personagem = personagem;
    }

    public NoCasa getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(NoCasa posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
    }

    public NoCasa getPosicaoAnterior() {
        return posicaoAnterior;
    }

    public void setPosicaoAnterior(NoCasa posicaoAnterior) {
        this.posicaoAnterior = posicaoAnterior;
    }

    public List<Imovel> getPropriedades() {
        return Collections.unmodifiableList(propriedades);
    }

    public int getVoltasCompletas() {
        return voltasCompletas;
    }

    public boolean isFalido() {
        return falido;
    }

    public void setFalido(boolean falido) {
        this.falido = falido;
    }

    @Override
    public String toString() {
        return """
                
                ========================
                Nome: %s
                Saldo: R$ %.2f
                Personagem: %s
                Patrimônio: R$ %.2f
                Imóveis: %d
                Voltas: %d
                ========================""".formatted(
                nome,
                saldo,
                personagem,
                calcularPatrimonio(),
                propriedades.size(),
                voltasCompletas
        );
    }
}