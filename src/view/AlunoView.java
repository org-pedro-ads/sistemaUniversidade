// src/view/AlunoView.java
package view;

import model.Alunos;

import java.util.List;
import java.util.Scanner;

public class AlunoView {
    private final Scanner scanner = new Scanner(System.in);

    // ====================== EXIBIÇÃO ======================
    public void exibirTitulo(String titulo) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" ".repeat((60 - titulo.length()) / 2) + titulo);
        System.out.println("=".repeat(60));
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

    // ====================== LEITURA DE DADOS (SÓ LÊ!) ======================
    public String lerNome() {
        System.out.print("Nome completo: ");
        return scanner.nextLine().trim();
    }

    public String lerMatricula() {
        System.out.print("Matrícula (ex: 2023001): ");
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

    // ====================== MENSAGENS (só exibe o que mandar) ======================
    public void sucesso(String mensagem) {
        System.out.println("SUCESSO: " + mensagem);
    }

    public void erro(String mensagem) {
        System.out.println("ERRO: " + mensagem);
    }

    public void info(String mensagem) {
        System.out.println("INFO: " + mensagem);
    }

    // ====================== PAUSA ======================
    public void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}