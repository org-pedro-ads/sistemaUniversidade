import java.util.List;
import java.util.Scanner;
import controller.AlunoController;
import controller.DisciplinaController;
import controller.ProfessorController;
import controller.ProjetoPesquisaController;
import model.Alunos;
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
        //repositorys
        ProfessorRepository professorRepository = ProfessorRepository.getInstance();
        DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
        ProjetoPesquisaRepository projetoPesquisaRepository = ProjetoPesquisaRepository.getInstance();
        AlunoRepository alunoRepository = AlunoRepository.getInstance();

        //views
        ProfessorView professorView = new ProfessorView();
        AlunoView alunoView = new AlunoView();
        ProjetoPesquisaView projetoPesquisaView = new ProjetoPesquisaView();
        DisciplinaView disciplinaView = new DisciplinaView();

        //controllers
        ProfessorController professorController = new ProfessorController
                (professorRepository, disciplinaRepository, projetoPesquisaRepository, professorView);
        DisciplinaController disciplinaController = new DisciplinaController();
        AlunoController alunoController = new AlunoController(alunoRepository, alunoView);
        ProjetoPesquisaController projetoPesquisaController = new ProjetoPesquisaController(
                projetoPesquisaRepository, professorRepository, alunoRepository, projetoPesquisaView);

        try {
            alunoRepository.criarMockAlunos();
            professorController.criarMockProfessor();
            disciplinaController.criarMockDiscipliba();

            System.out.println("""
                    ╔══════════════════════════════════════════════════╗
                    ║    SISTEMA DE GESTÃO ACADÊMICA - IFSP 2025       ║
                    ║        Programação Orientada a Objetos           ║
                    ╚══════════════════════════════════════════════════╝
                    """);

            int opcao;
            do {
                exibirMenuPrincipal();
                opcao = lerInteiro("→ Digite a opção desejada: ");

                switch (opcao) {
                    case 1 -> menuProfessores(professorView, professorController);
                    case 2 -> menuDisciplinas();
                    case 3 -> menuAlunos();
                    case 4 -> menuVinculosEProjetos();
                    case 5 -> menuEletivasInteresse();
                    case 6 -> menuRelatorios(professorController);
                    case 7 -> menuAjudaSobre();
                    case 0 -> {
                        System.out.println("Saindo do sistema... Até logo!");
                        return;
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } while (true);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
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
        System.out.println("5. Eletivas — Interesse dos Alunos");
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

    // ====================== MENU DISCIPLINAS ======================
    private static void menuDisciplinas() {
        String escolha;
        do {
            System.out.println("\n>>>DISCIPLINAS");
            System.out.println("a) Cadastrar disciplina (obrigatória / eletiva)");
            System.out.println("b) Listar disciplinas");
            System.out.println("c) Editar disciplina");
            System.out.println("d) Remover disciplina");
            System.out.println("e) Visualizar alunos matriculados em uma disciplina");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            try {
                switch (escolha) {
                    case "a" -> disciplinaView.cadastrarDisciplina();
                    case "b" -> disciplinaView.listarDisciplinas();
                    case "c" -> disciplinaView.editarDisciplina();
                    case "d" -> disciplinaView.removerDisciplina();
                    case "e" -> disciplinaView.listarAlunosMatriculados();
                    case "0" -> System.out.println("Voltando ao menu principal...\n");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ALUNOS ======================
    private static void menuAlunos() throws Exception {
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
                case "a" -> alunoView.cadastrarAluno();
                case "b" -> alunoView.listarAlunos();
                case "c" -> disciplinaView.matricularAlunoEmDisciplina();
                case "d" -> disciplinaView.desmatricularAlunoEmDisciplina();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU VÍNCULOS E PROJETOS ======================
    private static void menuVinculosEProjetos() throws Exception {
        String escolha;
        do {
            System.out.println("\n>>> VÍNCULOS E PROJETOS");
            System.out.println("a) Atribuir disciplina a professor");
            System.out.println("b) Remover disciplina de professor");
            System.out.println("c) Cadastrar projeto de pesquisa");
            System.out.println("d) Listar projetos de um professor");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> disciplinaView.atribuirDisciplinaAProfessor(); // Pedro
                case "b" -> disciplinaView.removerProfessorDisciplina(); // Pedro
                case "c" -> projetoPesquisaView.cadastrarProjeto(); // davi
                case "d" -> projetoPesquisaView.listarProjetosDoProfessor(); // davi
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ELETIVAS ======================
    private static void menuEletivasInteresse() throws Exception{
        String escolha;
        do {
            System.out.println("\n>>> ELETIVAS — INTERESSE DOS ALUNOS");
            System.out.println("a) Registrar interesse em disciplina eletiva");
            System.out.println("b) Calcular índice de popularidade");
            System.out.println("c) Relatório de popularidade das eletivas");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a" -> disciplinaView.declararInteresseDisciplina();
                case "b" -> disciplinaView.calcularIndiceInteresseDisciplina();
                case "c" -> disciplinaView.relatorioPopularidadeDisciplina();
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU RELATÓRIOS ======================
    private static void menuRelatorios(ProfessorController professorController) throws Exception {
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
                    disciplinaView.gerarRelatorioDisciplina();
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