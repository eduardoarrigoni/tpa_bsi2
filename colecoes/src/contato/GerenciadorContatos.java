package contato;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import arvorebinaria.ArvoreBinaria;
import colecao.IColecao;
import listaencadeada.ListaEncadeada;


public class GerenciadorContatos {

    private static final String ARQUIVO_ENTRADA_PADRAO = "entrada.txt";

    private static IColecao<Contato> estruturaPorNome;
    private static IColecao<Contato> estruturaPorTelefone;
    private static String arquivoEntrada = ARQUIVO_ENTRADA_PADRAO;

    public static void main(String[] args) {
        if (args.length > 0) {
            arquivoEntrada = args[0];
        }

        Scanner scanner = new Scanner(System.in);

        int estrutura = perguntarEstrutura(scanner);
        if (estrutura == 3) {
            estruturaPorNome = new ArvoreBinaria<Contato>(new ComparadorContatoPorNome());
            estruturaPorTelefone = new ArvoreBinaria<Contato>(new ComparadorContatoPorTelefone());
        } else {
            boolean ehOrdenada = estrutura == 1;
            estruturaPorNome = new ListaEncadeada<Contato>(new ComparadorContatoPorNome(), ehOrdenada);
            estruturaPorTelefone = new ListaEncadeada<Contato>(new ComparadorContatoPorTelefone(), ehOrdenada);
        }

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcaoMenu(scanner);
            switch (opcao) {
                case 1:
                    carregarDadosDeArquivo();
                    break;
                case 2:
                    adicionarContato(scanner);
                    break;
                case 3:
                    pesquisarPorNome(scanner);
                    break;
                case 4:
                    pesquisarPorTelefone(scanner);
                    break;
                case 5:
                    removerPorTelefone(scanner);
                    break;
                case 6:
                    alterarContato(scanner);
                    break;
                case 7:
                    System.out.println("Quantidade total de contatos: " + estruturaPorTelefone.quantidadeNos());
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 7);

        scanner.close();
    }

    

    private static int perguntarEstrutura(Scanner scanner) {
        int opcao;
        do {
            System.out.println("Em qual estrutura os contatos devem ser armazenados?");
            System.out.println("1 - Lista ordenada");
            System.out.println("2 - Lista não-ordenada");
            System.out.println("3 - Árvore binária");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcaoMenu(scanner);
            if (opcao < 1 || opcao > 3) {
                System.out.println("Opção inválida!");
            }
        } while (opcao < 1 || opcao > 3);
        return opcao;
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("===== AGENDA DE CONTATOS =====");
        System.out.println("1 - Carregar dados de arquivo");
        System.out.println("2 - Adicionar contato");
        System.out.println("3 - Pesquisar contato por nome");
        System.out.println("4 - Pesquisar contato por telefone");
        System.out.println("5 - Remover contato por telefone");
        System.out.println("6 - Alterar dados de contato");
        System.out.println("7 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcaoMenu(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

   
    private static boolean inserirContato(Contato contato) {
        Contato existente = estruturaPorTelefone.pesquisar(new Contato(null, contato.getTelefone()));
        if (existente != null) {
            return false;
        }
        estruturaPorNome.adicionar(contato);
        estruturaPorTelefone.adicionar(contato);
        return true;
    }

    private static void carregarDadosDeArquivo() {
        long inicio = System.nanoTime();
        int totalLidos = 0;
        int totalIgnorados = 0;

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoEntrada))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] partes = linha.split(";", 2);
                if (partes.length < 2) {
                    totalIgnorados++;
                    continue;
                }
                String nome = partes[0].trim();
                String telefone = partes[1].trim();
                if (inserirContato(new Contato(nome, telefone))) {
                    totalLidos++;
                } else {
                    totalIgnorados++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo '" + arquivoEntrada + "': " + e.getMessage());
            return;
        }

        long fim = System.nanoTime();
        System.out.println(totalLidos + " contato(s) carregado(s) com sucesso.");
        if (totalIgnorados > 0) {
            System.out.println(totalIgnorados + " linha(s) ignorada(s) (telefone duplicado ou formato inválido).");
        }
        System.out.printf("Tempo total de leitura do arquivo e montagem das estruturas: %.3f ms%n", (fim - inicio) / 1_000_000.0);
    }

    private static void adicionarContato(Scanner scanner) {
        System.out.print("Nome do contato: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone do contato: ");
        String telefone = scanner.nextLine().trim();

        if (inserirContato(new Contato(nome, telefone))) {
            System.out.println("Contato adicionado com sucesso!");
        } else {
            System.out.println("Já existe um contato com o telefone " + telefone + ". Contato não adicionado.");
        }
    }

    private static void pesquisarPorNome(Scanner scanner) {
        System.out.print("Nome do contato a ser pesquisado: ");
        String nome = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = estruturaPorNome.pesquisar(new Contato(nome, null));
        long fim = System.nanoTime();

        if (encontrado == null) {
            System.out.println("Contato não existe.");
        } else {
            System.out.println("Telefone: " + encontrado.getTelefone());
        }
        imprimirTempo("Tempo de busca", fim - inicio);
    }

    private static void pesquisarPorTelefone(Scanner scanner) {
        System.out.print("Telefone do contato a ser pesquisado: ");
        String telefone = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = estruturaPorTelefone.pesquisar(new Contato(null, telefone));
        long fim = System.nanoTime();

        if (encontrado == null) {
            System.out.println("Contato não existe.");
        } else {
            System.out.println("Nome: " + encontrado.getNome());
        }
        imprimirTempo("Tempo de busca", fim - inicio);
    }

    private static void removerPorTelefone(Scanner scanner) {
        System.out.print("Telefone do contato a ser removido: ");
        String telefone = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = estruturaPorTelefone.pesquisar(new Contato(null, telefone));
        boolean removido = false;
        if (encontrado != null) {
            estruturaPorTelefone.remover(encontrado);
            estruturaPorNome.remover(encontrado);
            removido = true;
        }
        long fim = System.nanoTime();

        System.out.println(removido ? "Contato removido com sucesso." : "Contato não existe.");
        imprimirTempo("Tempo de remoção", fim - inicio);
    }

    private static void alterarContato(Scanner scanner) {
        System.out.print("Nome do contato a ser alterado: ");
        String nome = scanner.nextLine().trim();

        Contato encontrado = estruturaPorNome.pesquisar(new Contato(nome, null));
        if (encontrado == null) {
            System.out.println("Contato não existe.");
            return;
        }
        System.out.println("Telefone atual: " + encontrado.getTelefone());

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine().trim();
        System.out.print("Novo telefone: ");
        String novoTelefone = scanner.nextLine().trim();

        boolean telefoneMudou = !novoTelefone.equals(encontrado.getTelefone());
        if (telefoneMudou && estruturaPorTelefone.pesquisar(new Contato(null, novoTelefone)) != null) {
            System.out.println("Já existe outro contato com o telefone " + novoTelefone + ". Alteração cancelada.");
            return;
        }

        estruturaPorNome.remover(encontrado);
        estruturaPorTelefone.remover(encontrado);

        encontrado.setNome(novoNome);
        encontrado.setTelefone(novoTelefone);

        estruturaPorNome.adicionar(encontrado);
        estruturaPorTelefone.adicionar(encontrado);

        System.out.println("Contato atualizado com sucesso!");
    }

    private static void imprimirTempo(String rotulo, long nanos) {
        System.out.printf("%s: %.3f ms (%d ns)%n", rotulo, nanos / 1_000_000.0, nanos);
    }
}
