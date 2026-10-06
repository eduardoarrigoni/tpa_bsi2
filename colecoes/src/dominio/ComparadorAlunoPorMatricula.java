package dominio;

import java.util.Comparator;

public class ComparadorAlunoPorMatricula implements Comparator<Aluno> {

    @Override
    public int compare(Aluno aluno1, Aluno aluno2) {
        return Integer.compare(aluno1.getMatricula(), aluno2.getMatricula());
    }
}
