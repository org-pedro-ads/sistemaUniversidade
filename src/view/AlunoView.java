package view;

import model.Alunos;
import repository.AlunoRepository;

import java.util.List;

import controller.AlunoController;

public class AlunoView extends BaseView {
    private final AlunoController alunoController = new AlunoController(AlunoRepository.getInstance(), this);

    public void listarAlunos() {
        this.alunoController.listarAlunos();
    }

    public void exibirListaAlunos(List<Alunos> alunos) {
        System.out.printf("%-15s %-35s %s%n", "MATRÍCULA", "NOME", "DISCIPLINAS");
        System.out.println("─".repeat(70));

        for (Alunos a : alunos) {
            System.out.printf("%-15s %-35s %d%n",
                    a.getMatricula(),
                    a.getNome().length() > 33 ? a.getNome().substring(0, 32) + "..." : a.getNome(),
                    a.getDisciplinasMatriculadas().size());
        }
        System.out.println("─".repeat(70));
        System.out.println("Total de alunos: " + alunos.size());
    }

    public String lerNome() {
        System.out.print("Nome completo: ");
        return scanner.nextLine().trim();
    }

    public String lerMatricula() {
        System.out.print("Matrícula (ex: BT2023001): ");
        return scanner.nextLine().trim();
    }

    public String lerCodigoDisciplina() {
        System.out.print("Código da disciplina: ");
        return scanner.nextLine().trim();
    }

    public String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
}