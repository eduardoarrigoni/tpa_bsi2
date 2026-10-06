package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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

        String ultimoNome = null;
        String ultimoTelefone = null;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoSaida))) {
            for (int i = 1; i <= quantidade; i++) {
                ultimoNome = GeradorNomes.sortear(random, i);
                ultimoTelefone = GeradorNomes.telefone(i);
                escritor.write(ultimoNome + ";" + ultimoTelefone);
                escritor.newLine();
            }
        }

        // O pior caso de busca por telefone é a única folha da árvore: o maior
        // telefone, que é o último registro do arquivo (profundidade n - 1).
        System.out.println("Arquivo '" + arquivoSaida + "' gerado com " + quantidade + " contato(s).");
        System.out.println("PIOR_CASO_NOME=" + ultimoNome);
        System.out.println("PIOR_CASO_TELEFONE=" + ultimoTelefone);
        System.out.println("PIOR_CASO_PROFUNDIDADE=" + (quantidade - 1));
    }
}
