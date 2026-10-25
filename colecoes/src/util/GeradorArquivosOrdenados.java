package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

import arvorebinaria.ArvoreAVL;

/**
 * Gera um arquivo de contatos ("nome;telefone") com os telefones em ordem
 * CRESCENTE. Lido para uma árvore binária indexada por telefone, cada contato
 * vira filho direito do anterior: a árvore fica totalmente degenerada.
 *
 * Adaptado de GeradorArquivosOrdenados, do professor
 * (https://github.com/victoriocarvalho/GeradorArquivos).
 *
 * Uso: java -cp bin util.GeradorArquivosOrdenados <quantidade> <arquivo_saida> [seed]
 */
public class GeradorArquivosOrdenados {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Uso: java util.GeradorArquivosOrdenados <quantidade> <arquivo_saida> [seed]");
            return;
        }
        int quantidade = Integer.parseInt(args[0]);
        String arquivoSaida = args[1];
        long seed = args.length > 2 ? Long.parseLong(args[2]) : 42L;
        Random random = new Random(seed);

        String[] nomes = new String[quantidade + 1];

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoSaida))) {
            for (int i = 1; i <= quantidade; i++) {
                nomes[i] = GeradorNomes.sortear(random, i);
                escritor.write(nomes[i] + ";" + GeradorNomes.telefone(i));
                escritor.newLine();
            }
        }

        // O pior caso de busca por telefone é a única folha da árvore: o maior
        // telefone, que é o último registro do arquivo (profundidade n - 1).
        System.out.println("Arquivo '" + arquivoSaida + "' gerado com " + quantidade + " contato(s).");
        System.out.println("PIOR_CASO_NOME=" + nomes[quantidade]);
        System.out.println("PIOR_CASO_TELEFONE=" + GeradorNomes.telefone(quantidade));
        System.out.println("PIOR_CASO_PROFUNDIDADE=" + (quantidade - 1));

        // Na árvore AVL o mesmo arquivo gera uma árvore balanceada, e o pior caso
        // passa a ser uma folha do último nível. Montamos a AVL com as mesmas
        // chaves (1..n, na ordem do arquivo) e pegamos o primeiro valor da última
        // linha do caminhamento em nível.
        int chaveAVL = folhaMaisProfundaAVL(quantidade);
        System.out.println("PIOR_CASO_AVL_NOME=" + nomes[chaveAVL]);
        System.out.println("PIOR_CASO_AVL_TELEFONE=" + GeradorNomes.telefone(chaveAVL));
    }

    private static int folhaMaisProfundaAVL(int quantidade) {
        ArvoreAVL<Integer> avl = new ArvoreAVL<Integer>(Comparator.<Integer>naturalOrder());
        for (int i = 1; i <= quantidade; i++) {
            avl.adicionar(i);
        }
        String niveis = avl.caminharEmNivel();
        String ultimoNivel = niveis.substring(niveis.lastIndexOf('\n') + 1, niveis.length() - 1);
        System.out.println("PIOR_CASO_AVL_PROFUNDIDADE=" + avl.altura());
        return Integer.parseInt(ultimoNivel.split(",")[0]);
    }
}
