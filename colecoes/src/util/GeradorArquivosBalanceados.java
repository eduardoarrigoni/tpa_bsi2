package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Gera um arquivo de contatos ("nome;telefone") em uma ordem tal que, lido
 * para uma árvore binária indexada por telefone, a árvore resultante fica
 * perfeitamente balanceada: o primeiro telefone é o do meio do intervalo,
 * depois vêm os meios das duas metades, e assim por diante.
 *
 * Adaptado de GeradorArquivosBalanceados, do professor
 * (https://github.com/victoriocarvalho/GeradorArquivos).
 *
 * Uso: java -cp bin util.GeradorArquivosBalanceados <quantidade> <arquivo_saida> [seed]
 */
public class GeradorArquivosBalanceados {

    private static int[] chaves;
    private static int proximaPosicao;
    private static int maiorProfundidade;
    private static int posicaoMaisProfunda;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Uso: java util.GeradorArquivosBalanceados <quantidade> <arquivo_saida> [seed]");
            return;
        }
        int quantidade = Integer.parseInt(args[0]);
        String arquivoSaida = args[1];
        long seed = args.length > 2 ? Long.parseLong(args[2]) : 42L;
        Random random = new Random(seed);

        chaves = new int[quantidade];
        proximaPosicao = 0;
        maiorProfundidade = -1;
        posicaoMaisProfunda = -1;
        gerarChavesBalanceadas(1, quantidade, 0);

        String piorNome = null;
        String piorTelefone = null;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoSaida))) {
            for (int i = 0; i < quantidade; i++) {
                String nome = GeradorNomes.sortear(random, i + 1);
                String telefone = GeradorNomes.telefone(chaves[i]);
                escritor.write(nome + ";" + telefone);
                escritor.newLine();
                if (i == posicaoMaisProfunda) {
                    piorNome = nome;
                    piorTelefone = telefone;
                }
            }
        }

        // O pior caso de busca por telefone é uma folha do último nível da
        // árvore. A profundidade de cada chave é a profundidade da recursão
        // que a gerou; guardamos a primeira chave que atinge a maior delas.
        System.out.println("Arquivo '" + arquivoSaida + "' gerado com " + quantidade + " contato(s).");
        System.out.println("PIOR_CASO_NOME=" + piorNome);
        System.out.println("PIOR_CASO_TELEFONE=" + piorTelefone);
        System.out.println("PIOR_CASO_PROFUNDIDADE=" + maiorProfundidade);
    }

    /**
     * Divide o intervalo [inicio, fim] ao meio, emite a chave do meio e repete
     * para as duas metades. A profundidade da recursão é log2(n), então não há
     * risco de estouro de pilha.
     */
    private static void gerarChavesBalanceadas(int inicio, int fim, int profundidade) {
        if (inicio > fim) {
            return;
        }
        int meio = (inicio + fim) / 2;
        if (profundidade > maiorProfundidade) {
            maiorProfundidade = profundidade;
            posicaoMaisProfunda = proximaPosicao;
        }
        chaves[proximaPosicao++] = meio;
        gerarChavesBalanceadas(inicio, meio - 1, profundidade + 1);
        gerarChavesBalanceadas(meio + 1, fim, profundidade + 1);
    }
}
