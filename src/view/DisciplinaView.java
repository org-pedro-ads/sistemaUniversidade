package view;

import model.Alunos;
import model.Disciplina;

import java.util.List;
import java.util.Scanner;

public class DisciplinaView {

    private final Scanner scanner = new Scanner(System.in);

    // -------------------- PRINTS DE DADOS -------------------------------

    public void print(String s) {
        System.out.println(s);
    }

    public void showDisciplinas(List<Disciplina> disciplinas) {
        print("\n--- LISTA DE DISCIPLINAS ---");

        for (Disciplina d : disciplinas) {
            String profNome = (d.getProfessorResponsavel() != null)
                    ? d.getProfessorResponsavel().getNome()
                    : "N/A";

            print(d.getId() +
                    " | Nome: " + d.getNome() +
                    " | Carga Horária: " + d.getCargaHoraria() + "h" +
                    " | Professor: " + profNome);
        }
        print("----------------------------\n");
    }

    public void showDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            print("Disciplina não encontrada.");
            return;
        }

        String profNome = (disciplina.getProfessorResponsavel() != null)
                ? disciplina.getProfessorResponsavel().getNome()
                : "N/A";

        print("\n--- DETALHES DA DISCIPLINA " + disciplina.getNome() + " ---");
        print("Código: " + disciplina.getId());
        print("Carga Horária: " + disciplina.getCargaHoraria() + "h");
        print("Professor Responsável: " + profNome);
        print("\nAlunos Matriculados:");

        if (disciplina.getAlunos() != null && disciplina.getAlunos().size() > 0) {
            for (Alunos a : disciplina.getAlunos()) {
                print("  -> Matrícula: " + a.getMatricula() + " | Nome: " + a.getNome());
            }
        } else {
            print("  Nenhum aluno matriculado.");
        }
        print("---------------------------------------------------\n");
    }

    // -------------------- LEITURA DE DADOS -------------------------------

    public String getInfo(String message) {
        print(message);
        return scanner.nextLine().trim();
    }
}
