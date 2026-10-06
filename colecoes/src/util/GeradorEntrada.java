package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Gera um arquivo de contatos aleatórios para os testes empíricos de
 * complexidade da Etapa 3 do trabalho.
 *
 * Uso: java -cp bin util.GeradorEntrada <quantidade> <arquivo_saida> [seed]
 *
 * Cada linha gerada tem o formato "nome;telefone". Os telefones são únicos
 * (regra de negócio do programa de contatos) e a ordem das linhas no arquivo
 * é aleatória em relação à ordem alfabética de nome e à ordem numérica de
 * telefone — ou seja, simula contatos chegando "fora de ordem", que é o
 * cenário relevante para medir o custo de manter uma lista ordenada durante
 * a inserção.
 *
 * Usar sempre a mesma seed para um dado tamanho garante que o mesmo arquivo
 * seja usado tanto na execução com lista ordenada quanto na execução com
 * lista não-ordenada, permitindo comparação justa entre as duas.
 */
public class GeradorEntrada {

    private static final String[] PRIMEIROS_NOMES = {
        "Ana", "Bruno", "Carla", "Daniel", "Eduarda", "Felipe", "Gabriela",
        "Henrique", "Isabela", "Joao", "Karina", "Lucas", "Mariana", "Nicolas",
        "Olivia", "Pedro", "Quesia", "Rafael", "Sofia", "Thiago", "Ursula",
        "Vitor", "Wesley", "Ximena", "Yasmin", "Zeca"
    };

    private static final String[] SOBRENOMES = {
        "Silva", "Souza", "Oliveira", "Santos", "Pereira", "Costa", "Rodrigues",
        "Almeida", "Nascimento", "Lima", "Araujo", "Fernandes", "Carvalho",
        "Gomes", "Martins", "Rocha", "Ribeiro", "Alves", "Monteiro", "Mendes",
        "Barros", "Freitas", "Barbosa", "Pinto", "Moura", "Cavalcanti"
    };

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Uso: java util.GeradorEntrada <quantidade> <arquivo_saida> [seed]");
            return;
        }

        int quantidade = Integer.parseInt(args[0]);
        String arquivoSaida = args[1];
        long seed = args.length > 2 ? Long.parseLong(args[2]) : 42L;

        Random random = new Random(seed);

        // Telefones únicos: embaralha os números de 0 a quantidade-1 (Fisher-Yates)
        // e usa cada um como sufixo numérico de 8 dígitos.
        int[] chavesTelefone = new int[quantidade];
        for (int i = 0; i < quantidade; i++) {
            chavesTelefone[i] = i;
        }
        for (int i = quantidade - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = chavesTelefone[i];
            chavesTelefone[i] = chavesTelefone[j];
            chavesTelefone[j] = tmp;
        }

        String ultimoNome = null;
        String ultimoTelefone = null;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoSaida))) {
            for (int i = 0; i < quantidade; i++) {
                String primeiro = PRIMEIROS_NOMES[random.nextInt(PRIMEIROS_NOMES.length)];
                String sobrenome = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
                // Sufixo numérico garante nomes únicos, mesmo com poucas
                // combinações de nome/sobrenome (evita ambiguidade nos testes).
                String nome = primeiro + " " + sobrenome + " " + (i + 1);
                String telefone = String.format("9%08d", chavesTelefone[i]);

                escritor.write(nome);
                escritor.write(";");
                escritor.write(telefone);
                escritor.newLine();

                ultimoNome = nome;
                ultimoTelefone = telefone;
            }
        }

        System.out.println("Arquivo '" + arquivoSaida + "' gerado com " + quantidade + " contato(s).");
        System.out.println("ULTIMO_NOME=" + ultimoNome);
        System.out.println("ULTIMO_TELEFONE=" + ultimoTelefone);
    }
}
