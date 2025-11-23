package view;

import model.Alunos;
import repository.AlunoRepository;

import java.util.List;
import java.util.Scanner;

import controller.AlunoController;

public class AlunoView {
    private final Scanner scanner = new Scanner(System.in);
    private final AlunoController alunoController = new AlunoController(AlunoRepository.getInstance(), this);

    public void menuAlunos () {
          String escolha;
        do {
            System.out.println("\n>>> ALUNOS");
            System.out.println("a) Cadastrar aluno");
            System.out.println("b) Listar alunos");
            System.out.println("c) Matricular aluno em disciplina");
            System.out.println("d) Desmatricular aluno de disciplina");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> this.cadastrarAluno();
                case "b" -> this.listarAlunos();
                case "c", "d" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento...");
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    public void cadastrarAluno() {
        this.exibirTitulo("CADASTRAR ALUNO");
        String nome = this.lerNome();

        this.alunoController.cadastrarAluno(nome);
    }

    public void listarAlunos() {
        this.alunoController.listarAlunos();
    }

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

    public void sucesso(String mensagem) {
        System.out.println("SUCESSO: " + mensagem);
    }

    public void erro(String mensagem) {
        System.out.println("ERRO: " + mensagem);
    }

    public void info(String mensagem) {
        System.out.println("INFO: " + mensagem);
    }

    public void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}