/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Scanner;

import colecao.IColecao;
import listaencadeada.*;

/**
 *
 * @author victoriocarvalho
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Instanciando uma lista com Object
        IColecao<Aluno> l;
        l = new ListaEncadeada<Aluno>(new ComparadorAlunoPorMatricula(), false);
        Aluno a;
        int mat, nota, resp;
        String nome;
        Scanner scanner = new Scanner(System.in);
        try {            
            do{
                System.out.println("Digite a matricula do aluno");
                mat = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Digite o nome do aluno");
                nome = scanner.nextLine();
                System.out.println("Digite a nota do aluno");
                nota = scanner.nextInt();
                a = new Aluno(mat, nome, nota);
                l.adicionar(a);
                System.out.println("Digite 1 para adicionar mais alunos ou outro numero para parar");
                resp = scanner.nextInt();
            }while(resp==1);

            do{
                System.out.println("Digite a matricula do aluno a ser procurado");
                mat = scanner.nextInt();
                a = l.pesquisar(new Aluno(mat,"",0));
                if (a==null)
                    System.out.println("Aluno não existe");
                else
                    System.out.println("Aluno encontrado " + a);
                System.out.println("Digite 1 para adicionar mais alunos ou outro numero para parar");
                resp = scanner.nextInt();
            }while(resp==1);
            scanner.close();
        } catch (Exception e) {
            scanner.close();
            System.out.println("ERRO! " + e.getMessage());
        }

    }
}
