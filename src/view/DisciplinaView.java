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

  public DisciplinaView(DisciplinaController disciplinaController) {
    this.disciplinaController = disciplinaController;
  }

  public void print(String s) {
    System.out.println(s);
  }
  
  // ====================== MENU DISCIPLINAS ======================
  public void menuDisciplinas() {
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
          case "a" -> cadastrarDisciplina();
          case "b" -> listarDisciplinas();
          case "c" -> editarDisciplina();
          case "d" -> removerDisciplina();
          case "e" -> listarAlunosMatriculados();
          case "0" -> info("Voltando ao menu principal...\n");
          default -> erro("Opção inválida!");
        }

      } catch (Exception e) {
        System.out.println("Ocorreu um erro: " + e.getMessage());
      }

    } while (!escolha.equals("0"));
  }

  public void exibirDetalhesDisciplina(Disciplina disciplina) {
    if (disciplina == null) {
      erro("Disciplina não encontrada.");
      return;
    }

    String separador = "====================================================================";
    String tipoDisciplina = disciplina.getTipo();
    String nomeProfessor = disciplina.getProfessorResponsavel() != null ? disciplina.getProfessorResponsavel().getNome()
        : "Não Atribuído";

    exibirTitulo("Dados de disciplina");
    print("Disciplina: " + disciplina.getId() + " - " + disciplina.getNome());
    print(separador.replace('=', '-'));
    print("Tipo de disciplina: " + tipoDisciplina);
    print("Professor responsável: " + nomeProfessor);
    print("Carga horaria: " + disciplina.getCargaHoraria());
    print("Quantidade de alunos matriculados: " + disciplina.getAlunos().size());
    print(separador);
    pausar();
  }

  public void cadastrarDisciplina() throws Exception {

    exibirTitulo("Cadastro de disciplina");
    String nome = lerString("\nInforme o nome do disciplina: ");
    int cargaHoraria = lerInteiro("\nInforme a carga horaria da disciplina: ");
    String matriculaProfessor = lerString("\nInforme a matricula do professor responsavel: ");
    int tipoDisciplina = lerInteiro(
        "\nInforme a tipo de disciplina: 1 - Disciplina obrigatoria, 2 - Disciplina eletiva: ");

    Disciplina disciplina = this.disciplinaController.adicionarDisciplina(nome, cargaHoraria, matriculaProfessor,
        tipoDisciplina);

    if (disciplina == null)
      throw new Exception("Erro ao cadstrar disciplina");

    limparTela();
    info("\nDisciplina cadastrada com sucesso!");
    exibirDetalhesDisciplina(disciplina);

  }

  public void listarDisciplinas() {
    List<Disciplina> disciplinas = this.disciplinaController.listarDisciplinas();

    limparTela();
    exibirTitulo("Lista de disciplinas");

    String formato = "| %-5s | %-40s | %-20s | %-12s | %5s |%n";
    String separador = "+-------+------------------------------------------+----------------------+--------------+-------+%n";

    System.out.printf(separador);
    System.out.printf(formato, "ID", "NOME", "PROFESSOR", "TIPO", "ALUNOS");
    System.out.printf(separador);

    for (Disciplina disciplina : disciplinas) {

      String tipoDisciplina = disciplina instanceof DisciplinaObrigatoria ? "Obrigatória" : "Eletiva";

      int qtdeAlunos = disciplina.getAlunos().size();

      String nomeProfessor = disciplina.getProfessorResponsavel() != null
          ? disciplina.getProfessorResponsavel().getNome()
          : "N/A";

      System.out.printf(
          formato,
          disciplina.getId(),
          disciplina.getNome(),
          nomeProfessor,
          tipoDisciplina,
          qtdeAlunos);
    }

    System.out.printf(separador);
    pausar();
  }

  public void editarDisciplina() throws Exception {

    exibirTitulo("Editar disciplina");

    int id = this.lerInteiro("\nDigite o ID da disciplina: ");
    Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(id);

    if (disciplina == null)
      throw new Exception("ID invalido");

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
        String nome = lerString("Digite o nome do disciplina: ");
        disciplinaAtualizada = this.disciplinaController.atualizarNome(id, nome);
        break;

      case "2":
        int cargaHoraria = lerInteiro("Digite o carga horaria da disciplina: ");
        disciplinaAtualizada = this.disciplinaController.atualizarCargaHoraria(id, cargaHoraria);
        break;

      case "3":
        String matriculaProfessor = lerString("Digite o matricula do professor: ");
        disciplinaAtualizada = this.disciplinaController.atualizarProfessorResponsavel(id, matriculaProfessor);
        break;

      case "4":
        return;

      default:
        erro("\n\nOpção inválida, tente novamente: ");
    }

    if (disciplinaAtualizada != null) {
      disciplinaController.alterarDisciplina(disciplinaAtualizada);
      limparTela();
      sucesso("Disciplina alterada com sucesso");
      this.exibirDetalhesDisciplina(disciplinaAtualizada);
    }
  }

  public void removerDisciplina() throws Exception {
    int idDisciplina = lerInteiro("Digite o ID da disciplina: ");
    this.disciplinaController.removerDisciplina(idDisciplina);
    limparTela();
    sucesso("Disciplina exluida com sucesso");
    pausar();
  }

  public void listarAlunosMatriculados() throws Exception {

    exibirTitulo("Listar alunos matriculados");

    int idDisciplina = lerInteiro("Digite o ID da disciplina: ");
    Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(idDisciplina);
    List<Alunos> alunos = disciplinaController.listarAlunosMatriculados(idDisciplina);

    if (alunos.isEmpty()) {
      print("Nenhum aluno encontrado!");
      return;
    }

    print("Disciplina: " + disciplina.getNome());
    AlunoView alunoView = new AlunoView();
    alunoView.exibirListaAlunos(alunos);
    pausar();
  }

  public void listarAlunosMatriculados(int idDisciplina) throws Exception {

    exibirTitulo("Listar alunos matriculados");
    List<Alunos> alunos = disciplinaController.listarAlunosMatriculados(idDisciplina);
    Disciplina disciplina = this.disciplinaController.buscarDisciplinaPorId(idDisciplina);

    print("Disciplina: " + disciplina.getNome());
    AlunoView alunoView = new AlunoView();
    alunoView.exibirListaAlunos(alunos);
    pausar();
  }

  public void matricularAlunoEmDisciplina() throws Exception {

    exibirTitulo("Matricular aluno em disciplina");

    int id = lerInteiro("Digite o ID da disciplina: ");
    String matricula = lerString("Digite a matricula do aluno: ");

    Disciplina disciplina = disciplinaController.matricularAlunoDisciplina(id, matricula);

    if (disciplina == null)
      throw new Exception("Erro ao matricular aluno");

    sucesso("Aluno matriculado com sucesso");
    listarAlunosMatriculados(disciplina.getId());

  }

  public void desmatricularAlunoEmDisciplina() throws Exception {

    int id = lerInteiro("Digite o ID da disciplina: ");
    String matricula = lerString("Digite a matricula do aluno: ");

    disciplinaController.desmatricularAlunoDisciplina(id, matricula);
  }

  public void atribuirDisciplinaAProfessor() throws Exception {

    print("\n================= Atribuir professor responsavel ================\n");

    int idDisciplina = lerInteiro("Digite o ID da disciplina: ");
    String matriculaProfessor = lerString("Digite a matricula da professor: ");

    disciplinaController.atualizarProfessorResponsavel(idDisciplina, matriculaProfessor);
  }

  public void removerProfessorDisciplina() throws Exception {
    print("\n================= Remover professor responsavel ================\n");

    int idDisciplina = lerInteiro("Digite o ID da disciplina: ");

    Disciplina disciplina = disciplinaController.removerProfessorResponsavel(idDisciplina);

  }

  public void declararInteresseDisciplina() throws Exception {
    try {

      int idDisciplina = lerInteiro("Digite o ID disciplina: ");
      String matrculaAluno = lerString("Digite a matricula da aluno: ");

      disciplinaController.declararInteresseDisciplina(idDisciplina, matrculaAluno);

    } catch (Exception e) {
      erro("Erro ao declarar interesse na disciplina: " + e.getMessage());
      pausar();
    }
  }

  public void calcularIndiceInteresseDisciplina() throws Exception {
    int idDisciplina = lerInteiro("Digite o ID disciplina: ");
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
              " | Popularidade: " + listarInteressados);
    }
    System.out.println("=====================================\n");
  }

}
