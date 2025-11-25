package view;

import controller.DisciplinaController;
import model.Alunos;
import model.Disciplina;
import model.DisciplinaEletiva;
import model.DisciplinaObrigatoria;

import java.util.List;
import java.util.Scanner;

public class DisciplinaView extends BaseView {

    private final DisciplinaController disciplinaController;
    private final Scanner scanner = new Scanner(System.in);

    public DisciplinaView() {
        this.disciplinaController = new DisciplinaController();
    }

    public void print(String s) {
        System.out.println(s);
    }

    public String getInfo(String message) {
        print(message);
        return scanner.nextLine().trim();
    }

    public int getIntInfo(String message) {
        while (true) {
            try {
                String input = getInfo(message);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                print("Entrada inválida. Por favor, digite um número inteiro.");
            }
        }
    }

    /* Dados gerais de cada disciplina */
    public void exibirDetalhesDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada.");
            return;
        }

        String separador = "--------------------------------------------------------------------";
        String tipoDisciplina = disciplina instanceof DisciplinaObrigatoria ? "Obrigatória" : "Eletiva";
        String nomeProfessor = disciplina.getProfessorResponsavel() != null ? disciplina.getProfessorResponsavel().getNome() : "Não Atribuído";

        System.out.println("\n" + separador);
        System.out.println("Disciplina: " + disciplina.getId() + " - " + disciplina.getNome());
        System.out.println(separador.replace('-', '=')); // Usando um separador diferente para o subtítulo
        System.out.println("Tipo de disciplina: " + tipoDisciplina);
        System.out.println("Professor responsável: " + nomeProfessor);
        System.out.println(separador);
    }

    public void cadastrarDisciplina() throws Exception {

        String nome = getInfo("\nInforme o nome do disciplina: ");
        int cargaHoraria = getIntInfo("\nInforme a carga horaria da disciplina: ");
        String matriculaProfessor = getInfo("\nInforme a matricula do professor responsavel: ");
        int tipoDisciplina = getIntInfo("\nInforme a tipo de disciplina: \n1 - Disciplina obrigatoria, 2 - Disciplina eletiva: ");

        Disciplina disciplina = this.disciplinaController.adicionarDisciplina(nome, cargaHoraria, matriculaProfessor, tipoDisciplina);
        print("\nDisciplina cadastrada com sucesso!");

    }

    public void listarDisciplinas() {
        List<Disciplina> disciplinas = this.disciplinaController.listarDisciplinas();

        String formato = "| %-5s | %-40s | %-20s | %-12s | %5s |%n";
        String separador = "+-------+------------------------------------------+----------------------+--------------+-------+%n";

        System.out.printf(separador);
        System.out.printf(formato, "ID", "NOME", "PROFESSOR", "TIPO", "ALUNOS");
        System.out.printf(separador);

        for (Disciplina disciplina : disciplinas) {

            String tipoDisciplina = disciplina instanceof DisciplinaObrigatoria ? "Obrigatória" : "Eletiva";

            int qtdeAlunos = disciplina.getAlunos().size();

            String nomeProfessor = disciplina.getProfessorResponsavel() != null ? disciplina.getProfessorResponsavel().getNome() : "N/A";

            System.out.printf(
                    formato,
                    disciplina.getId(),
                    disciplina.getNome(),
                    nomeProfessor,
                    tipoDisciplina,
                    qtdeAlunos
            );
        }

        System.out.printf(separador);
    }

    public void editarDisciplina() throws Exception{

        this.print(" ================= Editar Disciplina ================ \n");

        int id = this.getIntInfo("\nDigite o ID da disciplina: ");
        Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(id);
        this.exibirDetalhesDisciplina(disciplina);

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
                String nome = getInfo("Digite o nome do disciplina: ");
                disciplinaAtualizada = this.disciplinaController.atualizarNome(id, nome);
                break;
            case "2":
                int cargaHoraria = getIntInfo("Digite o carga horaria da disciplina: ");
                disciplinaAtualizada = this.disciplinaController.atualizarCargaHoraria(id, cargaHoraria);
                break;
            case "3":
                String matriculaProfessor = getInfo("Digite o matricula do professor: ");
                disciplinaAtualizada = this.disciplinaController.atualizarProfessorResponsavel(id, matriculaProfessor);
                break;
            case "4":
                return;
            default:
                this.print("\n\nOpção inválida, tente novamente: ");
        }

        if (disciplinaAtualizada != null) {
            disciplinaController.alterarDisciplina(disciplinaAtualizada);
            this.print("\n\nDisciplina alterada com sucesso!\n");
            this.exibirDetalhesDisciplina(disciplinaAtualizada);
        }
    }

    public void removerDisciplina() throws Exception {
        int idDisciplina = getIntInfo("Digite o ID da disciplina: ");
        this.disciplinaController.removerDisciplina(idDisciplina);
    }

    public void listarAlunosMatriculados() throws Exception{
        print(" ================= Listar alunos ================ \n");
        int idDisciplina = getIntInfo("Digite o ID da disciplina: ");
        Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(idDisciplina);
        List<Alunos> alunos = disciplinaController.listarAlunosMatriculados(idDisciplina);

        if(alunos.isEmpty()){ print("Nenhum aluno encontrado!"); return; }

        print("Disciplina: " + disciplina.getNome());
        AlunoView alunoView = new AlunoView();
        alunoView.exibirListaAlunos(alunos);
    }

    public void listarAlunosMatriculados(int idDisciplina) throws Exception{
        print(" ================= Listar alunos ================ \n");
        List<Alunos> alunos = disciplinaController.listarAlunosMatriculados(idDisciplina);
        Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(idDisciplina);

        print("Disciplina: " + disciplina.getNome());
        AlunoView alunoView = new AlunoView();
        alunoView.exibirListaAlunos(alunos);
    }

    public void matricularAlunoEmDisciplina() throws Exception {

        int id = getIntInfo("Digite o ID da disciplina: ");
        String matricula = getInfo("Digite a matricula do aluno: ");

        disciplinaController.matricularAlunoDisciplina(id, matricula);
    }

    public void desmatricularAlunoEmDisciplina() throws Exception {

        int id = getIntInfo("Digite o ID da disciplina: ");
        String matricula = getInfo("Digite a matricula do aluno: ");

        disciplinaController.desmatricularAlunoDisciplina(id,  matricula);
    }

    public void atribuirDisciplinaAProfessor() throws Exception {

        print("\n================= Atribuir professor responsavel ================\n");

        int idDisciplina = getIntInfo("Digite o ID da disciplina: ");
        String matriculaProfessor = getInfo("Digite a matricula da professor: ");

        disciplinaController.atualizarProfessorResponsavel(idDisciplina, matriculaProfessor);
    }

    public void removerProfessorDisciplina() throws Exception {
        print("\n================= Remover professor responsavel ================\n");

        int idDisciplina = getIntInfo("Digite o ID da disciplina: ");

        Disciplina disciplina = disciplinaController.removerProfessorResponsavel(idDisciplina);

    }

    public void declararInteresseDisciplina() throws Exception {

        int idDisciplina = getIntInfo("Digite o ID disciplina: ");
        String matrculaAluno = getInfo("Digite a matricula da aluno: ");

        disciplinaController.declararInteresseDisciplina(idDisciplina, matrculaAluno);
    }

    public void calcularIndiceInteresseDisciplina() throws Exception {
        int idDisciplina = getIntInfo("Digite o ID disciplina: ");
        int interesse = disciplinaController.getPopularidadeDisciplina(idDisciplina);

        print(">>> Interesse na disciplina: " + interesse);
    }

    public void relatorioPopularidadeDisciplina() throws Exception {

        print("\n ================= Popularidade das disciplinas ================ \n");

        List<Disciplina> disciplinas = disciplinaController.listarDisciplinas();

        for (Disciplina disciplina : disciplinas) {
            if (disciplina instanceof DisciplinaEletiva) {
                print("Disciplina: " + disciplina.getNome());
                print("Professor responsavel: " + disciplina.getProfessorResponsavel().getNome());
                print("Popularidade: " + disciplinaController.getPopularidadeDisciplina(disciplina.getId()));
            }
        }

        print("\n____________________________________________________________________\n");
    }

    public void gerarRelatorioDisciplina() throws Exception {

        List<Disciplina> disciplinas = disciplinaController.listarDisciplinas();

        for (Disciplina d : disciplinas) {
            List<Alunos> listarInteressados = null;
            if (d instanceof DisciplinaEletiva) {
                listarInteressados = disciplinaController.listarAlunosInteressados(d.getId());
            }
            System.out.println(
                    "Nome: " + d.getNome() +
                            " | Tipo: " + d.getTipo() +
                            " | Alunos Matriculados: " + d.getAlunos() +
                            " | Popularidade: " + listarInteressados
            );
        }

        System.out.println("=====================================\n");
    }

}