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
    private boolean preso;
    private int tentativasPrisao;
    private boolean usouIsencaoAdvogado;

    public Jogador(String nome, double saldo, TipoPersonagem personagem) {
        this.nome = nome;
        this.saldo = saldo;
        this.personagem = personagem;
        this.propriedades = new ArrayList<>();
        this.voltasCompletas = 0;
        this.falido = false;
        this.preso = false;
        this.tentativasPrisao = 0;
        this.usouIsencaoAdvogado = false;
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

    public boolean isPreso() {
        return preso;
    }

    public void setPreso(boolean preso) {
        this.preso = preso;
    }

    public int getTentativasPrisao() {
        return tentativasPrisao;
    }

    public void setTentativasPrisao(int tentativasPrisao) {
        this.tentativasPrisao = tentativasPrisao;
    }

    public boolean isUsouIsencaoAdvogado() {
        return usouIsencaoAdvogado;
    }

    public void setUsouIsencaoAdvogado(boolean usouIsencaoAdvogado) {
        this.usouIsencaoAdvogado = usouIsencaoAdvogado;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Saldo: R$ %.2f | Patrimônio: R$ %.2f | Imóveis: %d | Voltas: %d",
                nome, personagem, saldo, calcularPatrimonio(), propriedades.size(), voltasCompletas);
    }
}