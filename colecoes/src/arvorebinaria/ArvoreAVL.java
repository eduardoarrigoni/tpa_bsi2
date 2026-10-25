package arvorebinaria;

import java.util.Comparator;


public class ArvoreAVL<T> extends ArvoreBinaria<T> {

    public ArvoreAVL(Comparator<T> comparador) {
        super(comparador);
    }

    @Override
    public boolean adicionar(T novoValor) {
        if (novoValor == null) {
            return false;
        }
        raiz = adicionar(raiz, novoValor);
        quantidade++;
        return true;
    }

    private NoArvore<T> adicionar(NoArvore<T> no, T novoValor) {
        if (no == null) {
            return new NoArvore<>(novoValor);
        }
        if (comparador.compare(novoValor, no.getValor()) < 0) {
            no.setFilhoEsquerda(adicionar(no.getFilhoEsquerda(), novoValor));
        } else {
            no.setFilhoDireita(adicionar(no.getFilhoDireita(), novoValor));
        }
        return balancear(no);
    }

    @Override
    public boolean remover(T valor) {
        int quantidadeAnterior = quantidade;
        raiz = remover(raiz, valor);
        return quantidade < quantidadeAnterior;
    }

    private NoArvore<T> remover(NoArvore<T> no, T valor) {
        if (no == null) {
            return null;
        }
        int cmp = comparador.compare(valor, no.getValor());
        if (cmp < 0) {
            no.setFilhoEsquerda(remover(no.getFilhoEsquerda(), valor));
        } else if (cmp > 0) {
            no.setFilhoDireita(remover(no.getFilhoDireita(), valor));
        } else {
            quantidade--;
            if (no.getFilhoEsquerda() == null) {
                return no.getFilhoDireita();
            }
            if (no.getFilhoDireita() == null) {
                return no.getFilhoEsquerda();
            }
            NoArvore<T> sucessor = no.getFilhoDireita();
            while (sucessor.getFilhoEsquerda() != null) {
                sucessor = sucessor.getFilhoEsquerda();
            }
            no.setValor(sucessor.getValor());
            no.setFilhoDireita(removerMenor(no.getFilhoDireita()));
        }
        return balancear(no);
    }

    // Desliga o nó mais à esquerda da subárvore (o sucessor) e rebalanceia o caminho até ele.
    private NoArvore<T> removerMenor(NoArvore<T> no) {
        if (no.getFilhoEsquerda() == null) {
            return no.getFilhoDireita();
        }
        no.setFilhoEsquerda(removerMenor(no.getFilhoEsquerda()));
        return balancear(no);
    }

    @Override
    public int altura() {
        return alturaNo(raiz);
    }

    private int alturaNo(NoArvore<T> no) {
        return no == null ? -1 : no.getAltura();
    }

    private void atualizarAltura(NoArvore<T> no) {
        no.setAltura(1 + Math.max(alturaNo(no.getFilhoEsquerda()), alturaNo(no.getFilhoDireita())));
    }

    private int fatorBalanceamento(NoArvore<T> no) {
        return alturaNo(no.getFilhoEsquerda()) - alturaNo(no.getFilhoDireita());
    }

    private NoArvore<T> balancear(NoArvore<T> no) {
        atualizarAltura(no);
        int fator = fatorBalanceamento(no);
        if (fator > 1) {
            if (fatorBalanceamento(no.getFilhoEsquerda()) < 0) {
                return rotacaoDuplaDireita(no);
            }
            return rotacaoDireita(no);
        }
        if (fator < -1) {
            if (fatorBalanceamento(no.getFilhoDireita()) > 0) {
                return rotacaoDuplaEsquerda(no);
            }
            return rotacaoEsquerda(no);
        }
        return no;
    }

    private NoArvore<T> rotacaoDireita(NoArvore<T> no) {
        NoArvore<T> novaRaiz = no.getFilhoEsquerda();
        no.setFilhoEsquerda(novaRaiz.getFilhoDireita());
        novaRaiz.setFilhoDireita(no);
        atualizarAltura(no);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }

    private NoArvore<T> rotacaoEsquerda(NoArvore<T> no) {
        NoArvore<T> novaRaiz = no.getFilhoDireita();
        no.setFilhoDireita(novaRaiz.getFilhoEsquerda());
        novaRaiz.setFilhoEsquerda(no);
        atualizarAltura(no);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }

    private NoArvore<T> rotacaoDuplaDireita(NoArvore<T> no) {
        no.setFilhoEsquerda(rotacaoEsquerda(no.getFilhoEsquerda()));
        return rotacaoDireita(no);
    }

    private NoArvore<T> rotacaoDuplaEsquerda(NoArvore<T> no) {
        no.setFilhoDireita(rotacaoDireita(no.getFilhoDireita()));
        return rotacaoEsquerda(no);
    }
}
