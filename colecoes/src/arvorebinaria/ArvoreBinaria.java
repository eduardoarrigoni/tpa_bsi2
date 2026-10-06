package arvorebinaria;

import java.util.Comparator;


public class ArvoreBinaria<T> extends ArvoreBinariaBase<T> {

    protected NoArvore<T> raiz;
    protected int quantidade;

    public ArvoreBinaria(Comparator<T> comparador) {
        super(comparador);
    }

    @Override
    public boolean adicionar(T novoValor) {
        if (novoValor == null) {
            return false;
        }
        NoArvore<T> novoNo = new NoArvore<>(novoValor);

        if (raiz == null) {
            raiz = novoNo;
            quantidade++;
            return true;
        }

        NoArvore<T> atual = raiz;
        while (true) {
            if (comparador.compare(novoValor, atual.getValor()) < 0) {
                if (atual.getFilhoEsquerda() == null) {
                    atual.setFilhoEsquerda(novoNo);
                    break;
                }
                atual = atual.getFilhoEsquerda();
            } else {
                if (atual.getFilhoDireita() == null) {
                    atual.setFilhoDireita(novoNo);
                    break;
                }
                atual = atual.getFilhoDireita();
            }
        }

        quantidade++;
        return true;
    }

    @Override
    public T pesquisar(T valor) {
        NoArvore<T> atual = raiz;
        while (atual != null) {
            int cmp = comparador.compare(valor, atual.getValor());
            if (cmp == 0) {
                return atual.getValor();
            }
            if (cmp < 0) {
                atual = atual.getFilhoEsquerda();
            } else {
                atual = atual.getFilhoDireita();
            }
        }
        return null;
    }

    @Override
    public boolean remover(T valor) {
        NoArvore<T> pai = null;
        NoArvore<T> atual = raiz;

        while (atual != null) {
            int cmp = comparador.compare(valor, atual.getValor());
            if (cmp == 0) {
                break;
            }
            pai = atual;
            if (cmp < 0) {
                atual = atual.getFilhoEsquerda();
            } else {
                atual = atual.getFilhoDireita();
            }
        }

        if (atual == null) {
            return false;
        }

        if (atual.getFilhoEsquerda() != null && atual.getFilhoDireita() != null) {
            NoArvore<T> paiSucessor = atual;
            NoArvore<T> sucessor = atual.getFilhoDireita();
            while (sucessor.getFilhoEsquerda() != null) {
                paiSucessor = sucessor;
                sucessor = sucessor.getFilhoEsquerda();
            }
            atual.setValor(sucessor.getValor());
            pai = paiSucessor;
            atual = sucessor;
        }

        NoArvore<T> filho = atual.getFilhoEsquerda() != null ? atual.getFilhoEsquerda() : atual.getFilhoDireita();

        if (pai == null) {
            raiz = filho;
        } else if (pai.getFilhoEsquerda() == atual) {
            pai.setFilhoEsquerda(filho);
        } else {
            pai.setFilhoDireita(filho);
        }

        quantidade--;
        return true;
    }

    @Override
    public int quantidadeNos() {
        return quantidade;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    private int altura(NoArvore<T> no) {
        if (no == null) {
            return -1;
        }
        return 1 + Math.max(altura(no.getFilhoEsquerda()), altura(no.getFilhoDireita()));
    }

    @Override
    public String caminharEmNivel() {
        String resultado = "[";
        int altura = altura();
        for (int nivel = 0; nivel <= altura; nivel++) {
            if (nivel > 0) {
                resultado += "\n";
            }
            resultado += caminharNoNivel(raiz, nivel);
        }
        return resultado + "]";
    }

    
    private String caminharNoNivel(NoArvore<T> no, int nivel) {
        if (no == null) {
            return "";
        }
        if (nivel == 0) {
            return String.valueOf(no.getValor());
        }
        String esquerda = caminharNoNivel(no.getFilhoEsquerda(), nivel - 1);
        String direita = caminharNoNivel(no.getFilhoDireita(), nivel - 1);
        if (esquerda.isEmpty()) {
            return direita;
        }
        if (direita.isEmpty()) {
            return esquerda;
        }
        return esquerda + "," + direita;
    }

    @Override
    public String caminharEmOrdem() {
        return "[" + caminharEmOrdem(raiz) + "]";
    }

    private String caminharEmOrdem(NoArvore<T> no) {
        if (no == null) {
            return "";
        }
        String esquerda = caminharEmOrdem(no.getFilhoEsquerda());
        String direita = caminharEmOrdem(no.getFilhoDireita());
        String resultado = esquerda;
        if (!resultado.isEmpty()) {
            resultado += ",";
        }
        resultado += no.getValor();
        if (!direita.isEmpty()) {
            resultado += "," + direita;
        }
        return resultado;
    }
}
