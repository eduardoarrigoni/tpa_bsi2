package listaencadeada;

import colecao.IColecao;
import java.util.Comparator;

public class ListaEncadeada<T> implements IColecao<T>{
    private No<T> prim;
    private No<T> ultimo;
    private int quantidade;
    private final Comparator<T> comparador;
    private final boolean ehOrdenada;

    public ListaEncadeada() {
        this(null, false);
    }

    public ListaEncadeada(Comparator<T> comparador, boolean ehOrdenada) {
        this.comparador = comparador;
        this.ehOrdenada = ehOrdenada;
    }

    
    private int comparar(T a, T b) {
        if (comparador != null) {
            return comparador.compare(a, b);
        }
        return a.equals(b) ? 0 : 1;
    }

    @Override
    public boolean adicionar(T novoValor) {
        No<T> novoNo = new No<>(novoValor);

        if (!ehOrdenada) {
            // Lista não-ordenada: insere sempre no final, preservando a ordem
            // de chegada. O ponteiro "ultimo" evita percorrer a lista -> O(1)
            if (prim == null) {
                prim = novoNo;
            } else {
                ultimo.setProx(novoNo);
            }
            ultimo = novoNo;
        } else if (prim == null || comparar(novoValor, prim.getValor()) < 0) {
            // Lista ordenada, vazia ou o novo valor é o menor -> insere no início
            novoNo.setProx(prim);
            prim = novoNo;
            if (ultimo == null) {
                ultimo = novoNo;
            }
        } else {
            // Lista ordenada: percorre até encontrar a posição correta -> O(n)
            No<T> atual = prim;
            while (atual.getProx() != null && comparar(novoValor, atual.getProx().getValor()) >= 0) {
                atual = atual.getProx();
            }
            novoNo.setProx(atual.getProx());
            atual.setProx(novoNo);
            if (atual == ultimo) {
                ultimo = novoNo;
            }
        }

        quantidade++;
        return true;
    }

    @Override
    public T pesquisar(T valor) {
        No<T> atual = prim;
        while (atual != null) {
            int cmp = comparar(atual.getValor(), valor);
            if (cmp == 0) {
                return atual.getValor();
            }
            if (ehOrdenada && cmp > 0) {
                break;
            }
            atual = atual.getProx();
        }
        return null;
    }

    @Override
    public boolean remover(T valor) {
        No<T> atual = prim;
        No<T> anterior = null;

        while (atual != null) {
            int cmp = comparar(atual.getValor(), valor);
            if (cmp == 0) {
                if (anterior == null) {
                    prim = atual.getProx();
                } else {
                    anterior.setProx(atual.getProx());
                }
                if (atual == ultimo) {
                    ultimo = anterior;
                }
                quantidade--;
                return true;
            }
            if (ehOrdenada && cmp > 0) {
                break;
            }
            anterior = atual;
            atual = atual.getProx();
        }
        return false;
    }

    @Override
    public int quantidadeNos() {
        return quantidade;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        No<T> atual = prim;
        while (atual != null) {
            sb.append(atual.getValor());
            if (atual.getProx() != null) {
                sb.append(",");
            }
            atual = atual.getProx();
        }
        sb.append("]");
        return sb.toString();
    }

}
