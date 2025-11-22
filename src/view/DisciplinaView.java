package view;

import model.Alunos;
import model.Disciplina;
import repository.IDisciplinaRepository;

import java.util.List;
import java.util.Scanner;

/**
 * Caracteres de Desenho de Caixa (Box-Drawing Characters):
 * Linha Dupla: ╔ ╗ ╚ ╝ ═ ║ ╦ ╩ ╠ ╣ ╬
 * Linha Simples: ┌ ┐ └ ┘ ─ │ ┬ ┴ ├ ┤ ┼
 * Blocos e Sombras: █ ▀ ▄ ░ ▒ ▓
 */

public class DisciplinaView implements IDisciplinaRepository {

    private final Scanner scanner = new Scanner(System.in);

    // ----------------- Funcoes genericas ---------------------
    public void print(String s) {
        System.out.println(s);
    }

    public String getInfo(String message) {
        this.print(message);
        return scanner.nextLine().trim();
    }

    // ----------------- Prints especificos ---------------------
    public void printDisciplina(Disciplina disciplina) {

        int idDisciplina = disciplina.getId();
        String nomeDisciplina = disciplina.getNome();
        String tipoDisciplina = disciplina.getTipo();
        int cargaHoraria = disciplina.getCargaHoraria();
        String nomeProfessor = disciplina.getProfessorResponsavel().getNome();
        int qtdeAlunos = disciplina.getAlunos().size();

        this.print("╔════════════════════════════════════════════════════════════════╗");
        this.print("║                  DADOS DA DISCIPLINA                         ║");
        this.print("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ ID: %-60s ║%n", idDisciplina);
        System.out.printf("║ Nome: %-58s ║%n", nomeDisciplina);
        System.out.printf("║ Tipo: %-58s ║%n", tipoDisciplina);
        this.print("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Carga Horária: %d horas%-47s ║%n", cargaHoraria, "");
        System.out.printf("║ Professor Responsável: %-49s ║%n", nomeProfessor);
        this.print("╠════════════════════════════════════════════════════════════════╣");
        this.print("║ Status de Popularidade: [%s] %s (%.0f%% de interesse) ║%n");
        System.out.printf("║ Alunos Matriculados: %-47s ║%n", qtdeAlunos);
        this.print("╚════════════════════════════════════════════════════════════════╝");
    }

    public void printAlunos(Disciplina disciplina, List<Alunos> alunos) {

        int idDisciplina = disciplina.getId();
        String nomeDisciplina = disciplina.getNome();

        this.print("╔════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ Disciplina: %-58s ║%n", nomeDisciplina);
        this.print("╠════════════════════════════════════════════════════════════════╣");

        // Cabeçalho da tabela de alunos
        this.print("║ Matrícula  │ Nome Completo                                     ║");
        this.print("╠════════════╪═══════════════════════════════════════════════════╣");

        // Loop 'for' para mostrar os alunos
        if (alunos != null && !alunos.isEmpty()) {
            for (Alunos aluno : alunos) {
                // Formata a matrícula e o nome, garantindo que o total de colunas se encaixe na borda
                String matricula = aluno.getMatricula();
                String nome = aluno.getNome();

                // Formato: ║ Matrícula (10 chars) │ Nome (49 chars) ║
                System.out.printf("║ %-10s │ %-49s ║%n", matricula, nome);
            }
        } else {
            this.print("║ Nenhuma aluno matriculado nesta disciplina.                     ║");
        }

        this.print("╚════════════╧═══════════════════════════════════════════════════╝");
    }

    // ----------------- Relatorios ---------------------
    public void printRelatorios(List<Disciplina> disciplina) {
        this.print("Implementar relatorio");
    }
}
