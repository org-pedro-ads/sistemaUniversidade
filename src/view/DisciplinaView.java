package view;

import controller.AlunoController;
import controller.DisciplinaController;
import controller.ProfessorController;
import model.Alunos;
import model.Disciplina;
import repository.AlunoRepository;
import repository.DisciplinaRepository;
import repository.IDisciplinaRepository;

import java.util.List;
import java.util.Scanner;

/**
 * Caracteres de Desenho de Caixa (Box-Drawing Characters):
 * Linha Dupla: ╔ ╗ ╚ ╝ ═ ║ ╦ ╩ ╠ ╣ ╬
 * Linha Simples: ┌ ┐ └ ┘ ─ │ ┬ ┴ ├ ┤ ┼
 * Blocos e Sombras: █ ▀ ▄ ░ ▒ ▓
 */

public class DisciplinaView implements IDisciplinaView {

    private final DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
    private final AlunoRepository alunoRepository = AlunoRepository.getInstance();

    private final AlunoView alunoView =  new AlunoView();

    private final ProfessorController professorController = new  ProfessorController();
    private final AlunoController alunoController = new AlunoController(alunoRepository, alunoView);
    private final DisciplinaController disciplinaController = new DisciplinaController(disciplinaRepository, professorController, alunoController);


    private final Scanner scanner = new Scanner(System.in);

    // ----------------- Funcoes genericas ---------------------
    public void print(String s) {
        System.out.println(s);
    }

    public String getInfo(String message) {
        this.print(message);
        return scanner.nextLine().trim();
    }

    // ====================== MENU DISCIPLINAS ======================
    public void menuDisciplinas() throws Exception{
        String escolha;
        do {
            System.out.println("\n>>> DISCIPLINAS");
            System.out.println("a) Cadastrar disciplina (obrigatória / eletiva)");
            System.out.println("b) Listar disciplinas");
            System.out.println("c) Editar disciplina");
            System.out.println("d) Remover disciplina");
            System.out.println("e) Visualizar alunos matriculados em uma disciplina");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {

                case "a" -> this.adicionarDisciplina();
                case "b" -> this.listarDisciplinas();
                case "c" -> this.editarDisciplina();
                case "d" -> this.removerDisciplina();
                case "e" -> this.listarAlunosMatriculados();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
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


    // ----------------- Funcoes do menu ---------------------
    @Override
    public void adicionarDisciplina() throws Exception {
        try {
            this.disciplinaController.adicionarDisciplina();
        } catch(Exception e) {
            this.print("Erro ao cadastrar disciplina: " + e.getMessage());
        }
    }

    @Override
    public void listarDisciplinas() throws Exception {
        try {
            this.disciplinaController.listarDisciplinas();
        } catch(Exception e) {
            this.print("Erro ao listar disciplina: " + e.getMessage());
        }
    }
    @Override
    public void editarDisciplina() throws Exception {
        try {
            this.print(" ================= Editar Disciplina ================ \n");
            int id = Integer.getInteger(this.getInfo("\nDigite o id da disciplina: "));

            this.disciplinaController.validarExistenciaDisciplina(id);
            Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(id);

            this.printDisciplina(disciplina);

            String escolha;
            this.print("\n\n === Selecione a propridade que deseja editar: ");
            this.print("1. Nome: ");
            this.print("2. Carga horaria: ");
            this.print("3. Professore responsavel: ");
            this.print("4. Voltar: ");
            escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1":
                    disciplina = this.disciplinaController.atualizarNome();
                    break;
                case "2":
                    disciplina = this.disciplinaController.atualizarCargaHoraria();
                    break;
                case "3":
                    disciplina = this.disciplinaController.atualizarProfessorResponsavel();
                    break;
                case "4":
                    return;
                default:
                    this.print("\n\nOpcao invalida, tente novamente: ");
            }

            disciplina = this.disciplinaController.alterarDisciplina(disciplina);
            this.print("\n\nDisciplina alterada com sucesso!\n");
            this.printDisciplina(disciplina);
        } catch(Exception e) {
            throw new Exception("Erro ao editar disciplina: " + e.getMessage());
        }
    }
    @Override
    public void removerDisciplina() throws Exception {
        try {
            this.disciplinaController.removerDisciplina();
        } catch(Exception e) {
            this.print("Erro ao remover disciplina: " + e.getMessage());
        }
    }
    @Override
    public void listarAlunosMatriculados() throws Exception {
        try {
            this.disciplinaController.listarAlunosMatriculados();
        } catch(Exception e){
            this.print("Erro ao listar alunos matriculados: " + e.getMessage());
        }
    }
}
