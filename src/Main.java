import java.util.Scanner;
import controller.AlunoController;
import repository.AlunoRepository;
import view.AlunoView;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final AlunoRepository alunoRepository = new AlunoRepository();
    private static final AlunoView alunoView = new AlunoView();
    private static final AlunoController alunoController = new AlunoController(alunoRepository, alunoView);

    public static void main(String[] args) {
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
                case 1 -> menuProfessores();
                case 2 -> menuDisciplinas();
                case 3 -> menuAlunos();
                case 4 -> menuVinculosEProjetos();
                case 5 -> menuEletivasInteresse();
                case 6 -> menuRelatorios();
                case 7 -> menuAjudaSobre();
                case 8 -> {
                    System.out.println("Saindo do sistema... Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        } while (true);
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
        System.out.println("8. Sair");
        System.out.println("=".repeat(50));
    }

    // ====================== MENU PROFESSORES ======================
    private static void menuProfessores() {
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
                case "a" -> System.out.println("[Implementar] Cadastrar professor...");
                case "b" -> System.out.println("[Implementar] Listar professores...");
                case "c" -> System.out.println("[Implementar] Editar professor...");
                case "d" -> System.out.println("[Implementar] Remover professor...");
                case "e" -> System.out.println("[Implementar] Calcular salário...");
                case "0" -> System.out.println("Voltando ao menu principal...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU DISCIPLINAS ======================
    private static void menuDisciplinas() {
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
                case "a", "b", "c", "d", "e" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento...");
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ALUNOS ======================
    private static void menuAlunos() {
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
                case "c", "d" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento...");
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU VÍNCULOS E PROJETOS ======================
    private static void menuVinculosEProjetos() {
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
                case "a", "b" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento...");
                case "c" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento..."); // davi
                case "d" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento..."); // davi
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU ELETIVAS ======================
    private static void menuEletivasInteresse() {
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
                case "a", "b", "c" -> System.out.println("[Implementar] Funcionalidade em desenvolvimento...");
                case "0" -> System.out.println("Voltando...\n");
                default -> System.out.println("Opção inválida!");
            }
        } while (!escolha.equals("0"));
    }

    // ====================== MENU RELATÓRIOS ======================
    private static void menuRelatorios() {
        String escolha;
        do {
            System.out.println("\n>>> RELATÓRIOS");
            System.out.println("a) Relatório resumo de professores");
            System.out.println("b) Relatório de disciplinas");
            System.out.println("c) Exportar dados (JSON/CSV) - opcional");
            System.out.println("0) Voltar");
            System.out.print("→ Opção: ");
            escolha = scanner.nextLine().trim().toLowerCase();

            switch (escolha) {
                case "a", "b", "c" -> System.out.println("[Implementar] Gerando relatório...");
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
        System.out.println("Aluno: SEU NOME AQUI");
        System.out.println("GitHub: https://github.com/seuusuario/poo-2025");
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