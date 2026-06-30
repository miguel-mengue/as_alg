package structures;

import java.util.ArrayList;
import java.util.List;

public class Fila<T> {

    private static class Node<T> {
        T dado;
        Node<T> proximo;

        Node(T dado) {
            this.dado = dado;
        }
    }

    private Node<T> inicio;
    private Node<T> fim;
    private int tamanho;
    private final int capacidade;

    public Fila() {
        this.capacidade = -1; // Sem limite de capacidade
    }

    public Fila(int capacidade) {
        this.capacidade = capacidade;
    }

    public void enfileirar(T elemento) {
        if (capacidade > 0 && tamanho >= capacidade) {
            desenfileirar(); // Remove o mais antigo para manter o limite de capacidade
        }

        Node<T> novo = new Node<>(elemento);
        if (estaVazia()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.proximo = novo;
            fim = novo;
        }
        tamanho++;
    }

    public T desenfileirar() {
        if (estaVazia()) {
            return null;
        }
        T valor = inicio.dado;
        inicio = inicio.proximo;
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return valor;
    }

    public T espiar() {
        if (estaVazia()) {
            return null;
        }
        return inicio.dado;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void remover(T elemento) {
        if (estaVazia()) {
            return;
        }

        if (inicio.dado.equals(elemento)) {
            desenfileirar();
            return;
        }

        Node<T> atual = inicio;
        while (atual.proximo != null) {
            if (atual.proximo.dado.equals(elemento)) {
                atual.proximo = atual.proximo.proximo;
                if (atual.proximo == null) {
                    fim = atual;
                }
                tamanho--;
                return;
            }
            atual = atual.proximo;
        }
    }

    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        Node<T> atual = inicio;
        while (atual != null) {
            lista.add(atual.dado);
            atual = atual.proximo;
        }
        return lista;
    }
}
