package util;

import java.util.Random;

/**
 * Sorteia nomes para os geradores de arquivos de teste das árvores.
 * O sufixo numérico garante que não existam dois contatos com o mesmo nome.
 */
final class GeradorNomes {

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

    private GeradorNomes() {
    }

    static String sortear(Random random, int sufixo) {
        String primeiro = PRIMEIROS_NOMES[random.nextInt(PRIMEIROS_NOMES.length)];
        String sobrenome = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
        return primeiro + " " + sobrenome + " " + sufixo;
    }

    /** Telefone de largura fixa: a ordem alfabética coincide com a numérica. */
    static String telefone(int chave) {
        return String.format("9%08d", chave);
    }
}
