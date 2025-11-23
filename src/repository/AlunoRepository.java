package repository;

import model.Alunos;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepository {
    private List<Alunos> alunos = new ArrayList<>();
    private int sequenceId = 1;
    private static AlunoRepository instance;

    public static AlunoRepository getInstance() {
        if (instance == null) {
            instance = new AlunoRepository();
        }
        return instance;
    }

    public void cadastrarAluno(Alunos aluno) {
        if (aluno != null && buscarPorMatricula(aluno.getMatricula()) == null) {
            alunos.add(aluno);
            this.sequenceId++;
            System.out.println("Aluno cadastrado: " + aluno);
        } else {
            System.out.println("Erro: Matrícula já existe ou aluno inválido.");
        }
    }

    public void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        System.out.println("\n=== LISTA DE ALUNOS ===");
        for (Alunos a : alunos) {
            System.out.printf("Matrícula: %s | Nome: %s | Disciplinas: %d%n",
                    a.getMatricula(), a.getNome(), a.getDisciplinasMatriculadas().size());
        }
        System.out.println("Total: " + alunos.size() + "\n");
    }

    public int getNewId() {
        return this.sequenceId;
    }

    public Alunos buscarPorMatricula(String matricula) {
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public List<Alunos> getTodos() {
        return new ArrayList<>(alunos);
    }
}