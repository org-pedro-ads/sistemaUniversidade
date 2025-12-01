import java.util.Scanner;

import controller.AlunoController;
import controller.DisciplinaController;
import controller.ProfessorController;
import controller.ProjetoPesquisaController;

import model.Professor;

import repository.AlunoRepository;
import repository.DisciplinaRepository;
import repository.ProfessorRepository;
import repository.ProjetoPesquisaRepository;

import view.AlunoView;
import view.ProfessorView;
import view.DisciplinaView;
import view.ProjetoPesquisaView;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        // repositorys
        ProfessorRepository professorRepository = ProfessorRepository.getInstance();
        DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
        ProjetoPesquisaRepository projetoPesquisaRepository = ProjetoPesquisaRepository.getInstance();
        AlunoRepository alunoRepository = AlunoRepository.getInstance();

        // views
        ProfessorView professorView = new ProfessorView();
        AlunoView alunoView = new AlunoView();
        ProjetoPesquisaView projetoPesquisaView = new ProjetoPesquisaView();
        DisciplinaView disciplinaView = new DisciplinaView();

        // controllers
        ProfessorController professorController = new ProfessorController(professorRepository, disciplinaRepository,
                projetoPesquisaRepository, professorView);
        AlunoController alunoController = new AlunoController(alunoRepository, alunoView);
        ProjetoPesquisaController projetoPesquisaController = new ProjetoPesquisaController(projetoPesquisaRepository,
                professorRepository, alunoRepository, projetoPesquisaView);
        DisciplinaController disciplinaController = new DisciplinaController(disciplinaRepository, disciplinaView,
                alunoController, professorController);

        // alunoRepository.criarMockAlunos();
        // professorController.criarMockProfessor();
        // disciplinaController.criarMockDiscipliba();

        System.out.println("""
                ╔══════════════════════════════════════════════════╗
                ║    SISTEMA DE GESTÃO ACADÊMICA - IFSP 2025       ║
                ║        Programação Orientada a Objetos           ║
                ╚══════════════════════════════════════════════════╝
                """);
        try {
            int opcao;
            do {
                try {
                    exibirMenuPrincipal();
                    opcao = lerInteiro("→ Digite a opção desejada: ");

                    switch (opcao) {
                        case 1 -> menuProfessores(professorView, professorController);
                        case 2 -> disciplinaController.menuDisciplinas();
                        case 3 -> menuAlunos(disciplinaController, alunoController);
                        case 4 -> menuVinculosEProjetos(disciplinaController, projetoPesquisaController);
                        case 5 -> menuEletivasInteresse(disciplinaController);
                        case 6 -> menuRelatorios(professorController, disciplinaController);
                        case 7 -> menuAjudaSobre();
                        case 0 -> {
                            System.out.println("Saindo do sistema... Até logo!");
                            return;
                        }
                        default -> System.out.println("Opção inválida! Tente novamente.");
                    }

                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            } while (true);
        } catch (Exception e) {
            System.out.println("Erro fatal: " + e.getMessage());
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                MENU PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("1. Professores");
        System.out.println("2. Disciplinas");
        System.out.println("3. Alunos");
        System.out.println("4. Vínculos e Projetos");
        System.out.println("5. Eletivas - Interesse dos Alunos");
        System.out.println("6. Relatórios");
        System.out.println("7. Ajuda / Sobre");
        System.out.println("0. Sair");
        System.out.println("=".repeat(50));
    }

    // ====================== MENU PROFESSORES ======================
    private static void menuProfessores(ProfessorView professorView,
            ProfessorController professorController) throws Exception {
        String escolha;
        do {
            System.out.println("\n>>> PROFESSORES");
            System.out.println("a) Cadastrar professor (vitalício / substituto)");
            System.out.println("b) Listar professores");
            System.out.println("c) Editar professor");
            System.out.println("d) Remover professor");
            System.out.println("e) Calcular salário de um professor");
            System.out.println("0) Voltar ao menu principal");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> {
                    System.out.println("Cadastrar professor...");
                    Professor novoProfessor = professorView.cadastrarProfessor();
                    professorController.cadastrarProfessor(novoProfessor);
                }
                case "b" -> {
                    System.out.println("Listar professores...");
                    professorController.listarProfessores();
                }
                case "c" -> {
                    System.out.println("Editar professor...");
                    professorController.listarProfessores();
                    String matricula = professorView.editarProfessor();
                    professorController.editarProfessor(matricula);
                }
                case "d" -> {
                    System.out.println("Remover professor...");
                    String matricula = professorView.removerProfessor();
                    professorController.removerProfessor(matricula);
                }
                case "e" -> {
                    System.out.println("Calcular salário...");
                    String matricula = professorView.calcularSalarioProfessor();
                    professorController.calcularSalarioRetorno(matricula);
                }
                case "0" -> {
                    System.out.println("Voltando ao menu principal...\n");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ALUNOS ======================
    private static void menuAlunos(DisciplinaController disciplinaController, AlunoController alunoController)
            throws Exception {
        // alunoView.menuAlunos();
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
                case "a" -> alunoController.cadastrarAluno();
                case "b" -> alunoController.listarAlunos();
                case "c" -> disciplinaController.menuMatricularAlunoEmDisciplina();
                case "d" -> disciplinaController.menuDesmatricularAlunoDisciplina();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU VÍNCULOS E PROJETOS ======================
    private static void menuVinculosEProjetos(DisciplinaController disciplinaController,
            ProjetoPesquisaController projetoPesquisaController) throws Exception {
        String escolha;
        do {
            System.out.println("\n>>> VÍNCULOS E PROJETOS");
            System.out.println("a) Atribuir disciplina a professor");
            System.out.println("b) Remover disciplina de professor");
            System.out.println("c) Cadastrar projeto de pesquisa");
            System.out.println("d) Listar projetos de um professor");
            System.out.println("e) Alterar orientador de projeto de pesquisa");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> disciplinaController.menuAtribuirDisciplinaAProfessor();
                case "b" -> disciplinaController.menuRemoverProfessorResponsavel();
                case "c" -> projetoPesquisaController.cadastrarProjeto();
                case "d" -> projetoPesquisaController.listarProjetosDoProfessor();
                case "e" -> projetoPesquisaController.alterarOrientador();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ELETIVAS ======================
    private static void menuEletivasInteresse(DisciplinaController disciplinaController) throws Exception {
        String escolha;
        do {
            System.out.println("\n>>> ELETIVAS - INTERESSE DOS ALUNOS");
            System.out.println("a) Registrar interesse em disciplina eletiva");
            System.out.println("b) Calcular índice de popularidade");
            System.out.println("c) Relatório de popularidade das eletivas");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> disciplinaController.menuDeclararInteresseDisciplina();
                case "b" -> disciplinaController.menuCalcularInteresseDisciplina();
                case "c" -> disciplinaController.menuRelatorioPopulariadeDisciplina();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU RELATÓRIOS ======================
    private static void menuRelatorios(ProfessorController professorController,
            DisciplinaController disciplinaController) throws Exception {
        String escolha;
        do {
            System.out.println("\n>>> RELATÓRIOS");
            System.out.println("a) Relatório resumo de professores");
            System.out.println("b) Relatório de disciplinas");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> {
                    professorController.gerarRelatorio();
                }
                case "b" -> {
                    disciplinaController.menuGerarRelatorioDisciplina();
                }
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU AJUDA / SOBRE ======================
    private static void menuAjudaSobre() {
        System.out.println("\n>>> AJUDA / SOBRE");
        System.out.println("Sistema desenvolvido para a disciplina de");
        System.out.println("Programação Orientada a Objetos - IFSP 2025");
        System.out.println("\nProfessor: Prof. Dr. Anísio Silva");
        System.out.println("Alunos: Davi Celso, Pedro Paulino, Vitor Santos");
        System.out.println("GitHub: https://github.com/org-pedro-ads/sistemaUniversidade.git");
        System.out.println("\nPressione ENTER para voltar...");
        scanner.nextLine();
    }

    // ==================== UTILITÁRIO ====================
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um número válido!");
            }
        }
    }
}
