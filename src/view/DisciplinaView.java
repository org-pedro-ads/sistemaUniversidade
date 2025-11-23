package view;

import controller.AlunoController;
import controller.DisciplinaController;
import controller.ProfessorController;
import model.Alunos;
import model.Disciplina;
import repository.AlunoRepository;
import repository.DisciplinaRepository;

import java.util.List;
import java.util.Scanner;

/**
 * Caracteres de Desenho de Caixa (Box-Drawing Characters):
 * Linha Dupla: ╔ ╗ ╚ ╝ ═ ║ ╦ ╩ ╠ ╣ ╬
 * Linha Simples: ┌ ┐ └ ┘ ─ │ ┬ ┴ ├ ┤ ┼
 * Blocos e Sombras: █ ▀ ▄ ░ ▒ ▓
 */

public class DisciplinaView implements IDisciplinaView {

    private static DisciplinaView instance;

    // Dependências (usando getInstance para garantir que o Controller seja
    // inicializado corretamente)
    private final DisciplinaController disciplinaController;
    private final Scanner scanner = new Scanner(System.in);

    // Construtor Privado (Singleton)
    private DisciplinaView() {
        // Inicialização das dependências necessárias para o Controller
        DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
        AlunoRepository alunoRepository = AlunoRepository.getInstance();

        ProfessorController professorController = new ProfessorController();
        AlunoView alunoView = new AlunoView();
        AlunoController alunoController = new AlunoController(alunoRepository, alunoView);

        // Inicializa o Controller com as dependências
        this.disciplinaController = new DisciplinaController(
                disciplinaRepository,
                this,
                professorController,
                alunoController);
    }

    // Metodo estático para obter a instância (Singleton)
    public static DisciplinaView getInstance() {
        if (instance == null) {
            instance = new DisciplinaView();
        }
        return instance;
    }

    // ----------------- Funcoes genericas (Implementação da Interface)
    // ---------------------
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public String getInfo(String message) {
        print(message);
        return scanner.nextLine().trim();
    }

    @Override
    public int getIntInfo(String message) {
        while (true) {
            try {
                String input = getInfo(message);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                print("❌ Entrada inválida. Por favor, digite um número inteiro.");
            }
        }
    }

    // ====================== MENU DISCIPLINAS ======================
    public void menuDisciplinas() throws Exception {
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

            try {
                switch (escolha) {
                    case "a" -> this.adicionarDisciplina();
                    case "b" -> this.listarDisciplinas();
                    case "c" -> this.editarDisciplina();
                    case "d" -> this.removerDisciplina();
                    case "e" -> this.listarAlunosMatriculados();
                    case "0" -> System.out.println("Voltando ao menu principal...\n");
                    default -> System.out.println("❌ Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Ocorreu um erro: " + e.getMessage());
            }
        } while (!escolha.equals("0"));
    }

    // ----------------- Prints especificos (Implementação da Interface)
    // ---------------------
    @Override
    public void printDisciplina(Disciplina disciplina) {

        int idDisciplina = disciplina.getId();
        String nomeDisciplina = disciplina.getNome();
        String tipoDisciplina = disciplina.getTipo();
        int cargaHoraria = disciplina.getCargaHoraria();
        String nomeProfessor = disciplina.getProfessorResponsavel() != null
                ? disciplina.getProfessorResponsavel().getNome()
                : "N/A";
        int qtdeAlunos = disciplina.getAlunos().size();

        // Exemplo de cálculo de popularidade
        String statusPopularidade = qtdeAlunos >= 5 ? "ALTA" : "BAIXA";
        String iconePopularidade = qtdeAlunos >= 5 ? "📈" : "📉";
        double percentualInteresse = (double) qtdeAlunos / 20 * 100; // Exemplo: 20 alunos e o máximo

        print("╔════════════════════════════════════════════════════════════════╗");
        print("║                  DADOS DA DISCIPLINA                           ║");
        print("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ ID: %-60s ║%n", idDisciplina);
        System.out.printf("║ Nome: %-58s ║%n", nomeDisciplina);
        System.out.printf("║ Tipo: %-58s ║%n", tipoDisciplina);
        print("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Carga Horária: %d horas%-47s ║%n", cargaHoraria, "");
        System.out.printf("║ Professor Responsável: %-49s ║%n", nomeProfessor);
        print("╠════════════════════════════════════════════════════════════════╣");
        // Status de popularidade com formatação corrigida
        System.out.printf("║ Status de Popularidade: [%s] %s (%.0f%% de interesse) %-17s║%n", statusPopularidade,
                iconePopularidade, percentualInteresse, "");
        System.out.printf("║ Alunos Matriculados: %-47s ║%n", qtdeAlunos);
        print("╚════════════════════════════════════════════════════════════════╝");
    }

    @Override
    public void printAlunos(Disciplina disciplina, List<Alunos> alunos) {

        String nomeDisciplina = disciplina.getNome();

        print("╔════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ Disciplina: %-58s ║%n", nomeDisciplina);
        print("╠════════════════════════════════════════════════════════════════╣");

        // Cabeçalho da tabela de alunos
        print("║ Matrícula  │ Nome Completo                                     ║");
        print("╠════════════╪═══════════════════════════════════════════════════╣");

        // Loop 'for' para mostrar os alunos
        if (alunos != null && !alunos.isEmpty()) {
            for (Alunos aluno : alunos) {
                String matricula = aluno.getMatricula();
                String nome = aluno.getNome();

                System.out.printf("║ %-10s │ %-49s ║%n", matricula, nome);
            }
        } else {
            print("║ Nenhuma aluno matriculado nesta disciplina.                     ║");
        }

        print("╚════════════╧═══════════════════════════════════════════════════╝");
    }

    // ----------------- Relatorios (Implementação da Interface)
    // ---------------------
    @Override
    public void printRelatorios(List<Disciplina> disciplina) {
        // Dispara o relatório que foi implementado dentro do Controller,
        // mantendo a responsabilidade de *exibir* dados na View
        this.disciplinaController.gerarRelatorioDisciplinas();
    }

    // ----------------- Funcoes do menu (Implementação da Interface)
    // ---------------------
    @Override
    public void adicionarDisciplina() throws Exception {
        try {
            this.disciplinaController.adicionarDisciplina();
        } catch (Exception e) {
            this.print("Erro ao cadastrar disciplina: " + e.getMessage());
        }
    }

    @Override
    public void listarDisciplinas() throws Exception {
        try {
            this.disciplinaController.listarDisciplinas();
        } catch (Exception e) {
            this.print("Erro ao listar disciplina: " + e.getMessage());
        }
    }

    @Override
    public void editarDisciplina() throws Exception {
        try {
            this.print(" ================= ✏️ Editar Disciplina ================ \n");

            // A View solicita o ID de forma segura
            int id = this.getIntInfo("\nDigite o ID da disciplina: ");

            // Valida a existência e busca a disciplina no Controller
            Disciplina disciplina = this.disciplinaController.validarExistenciaDisciplina(id);
            this.printDisciplina(disciplina);

            String escolha;
            Disciplina disciplinaAtualizada = null;

            this.print("\n\n === Selecione a propriedade que deseja editar: ");
            this.print("1. Nome");
            this.print("2. Carga horaria");
            this.print("3. Professor responsavel");
            this.print("4. Voltar");
            escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1":
                    // Chama o metodo sem parâmetros do seu Controller, que gerencia a coleta de
                    // dados
                    disciplinaAtualizada = this.disciplinaController.atualizarNome();
                    break;
                case "2":
                    // Chama o metodo sem parâmetros do seu Controller
                    disciplinaAtualizada = this.disciplinaController.atualizarCargaHoraria();
                    break;
                case "3":
                    // Chama o metodo sem parâmetros do seu Controller
                    disciplinaAtualizada = this.disciplinaController.atualizarProfessorResponsavel();
                    break;
                case "4":
                    return;
                default:
                    this.print("\n\nOpção inválida, tente novamente: ");
            }

            // Se a atualização ocorreu com sucesso no Controller, exibe o resultado
            if (disciplinaAtualizada != null) {
                // Chama o metodo que persiste a alteração
                this.disciplinaController.alterarDisciplina(disciplinaAtualizada);
                this.print("\n\n✅ Disciplina alterada com sucesso!\n");
                this.printDisciplina(disciplinaAtualizada);
            }

        } catch (Exception e) {
            // Captura NumberFormatException (se usar getInfo direto) ou exceções de negócio
            // do Controller
            this.print("❌ Erro ao editar disciplina: " + e.getMessage());
        }
    }

    @Override
    public void removerDisciplina() throws Exception {
        try {
            // Chama o metodo do Controller, que gerencia o loop de UI e remoção
            this.disciplinaController.removerDisciplina();
        } catch (Exception e) {
            this.print("Erro ao remover disciplina: " + e.getMessage());
        }
    }

    @Override
    public void listarAlunosMatriculados() throws Exception {
        try {
            // Chama o metodo do Controller, que gerencia o loop de UI e a exibição
            this.disciplinaController.listarAlunosMatriculados();
        } catch (Exception e) {
            this.print("Erro ao listar alunos matriculados: " + e.getMessage());
        }
    }

    public void matricularAlunoEmDisciplina() throws Exception {
        disciplinaController.matricularAlunoDisciplina();
    }

    public void desmatricularAlunoEmDisciplina() throws Exception {
        disciplinaController.desmatricularAlunoDisciplina();
    }
}