package arvorebinaria;


public class NoArvore<T> {
    private T valor;
    private NoArvore<T> filhoEsquerda;
    private NoArvore<T> filhoDireita;

    public NoArvore(T valor) {
        this.valor = valor;
        this.filhoEsquerda = null;
        this.filhoDireita = null;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public NoArvore<T> getFilhoEsquerda() {
        return filhoEsquerda;
    }

    public void setFilhoEsquerda(NoArvore<T> filhoEsquerda) {
        this.filhoEsquerda = filhoEsquerda;
    }

    public NoArvore<T> getFilhoDireita() {
        return filhoDireita;
    }

    public void setFilhoDireita(NoArvore<T> filhoDireita) {
        this.filhoDireita = filhoDireita;
    }
}
