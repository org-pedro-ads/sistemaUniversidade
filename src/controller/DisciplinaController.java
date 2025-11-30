package controller;

import model.*;
import repository.DisciplinaRepository;
import view.DisciplinaView;
import repository.AlunoRepository;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController {

  private final DisciplinaRepository disciplinaRepository;
  private final DisciplinaView disciplinaView;
  private final ProfessorController professorController;
  private final AlunoController alunoController;

  public DisciplinaController(
    DisciplinaRepository disciplinaRepository,
    DisciplinaView disciplinaView,
    AlunoController alunoController,
    ProfessorController professorController
  ) 
  {
    this.disciplinaRepository = disciplinaRepository;
    this.disciplinaView = disciplinaView;
    this.alunoController = alunoController;
    this.professorController = professorController;
  }

  // =================================================================================
  // CRIACAO DOS MENUS
  // =================================================================================
  
  public void menuDisciplinas() throws Exception {
    String escolha = "0";

    do {
      escolha = this.disciplinaView.menuDisciplinas();
      
      try {
        switch(escolha) {
          case "a" -> getInfoCadDisciplina();
          case "b" -> listarDisciplinas();
          case "c" -> editarDisciplina();
          case "d" -> removerDisciplina(disciplinaView.removerDisciplina());
          case "e" -> listarAlunosMatriculados();
          case "0" -> this.disciplinaView.info("Voldando ao menu principal");
          default -> this.disciplinaView.erro("Opcao invalida!");
        }
      } catch (Exception e) {
        this.disciplinaView.erro(e.getMessage());
      }
    } while (!escolha.equals("0"));
  }

  public void menuMatricularAlunoEmDisciplina() throws Exception { getInfoMatriculaAluno(); }
  public void menuDesmatricularAlunoDisciplina() throws Exception { desmatricularAlunoDisciplina(); }
  public void menuAtribuirDisciplinaAProfessor() throws Exception { atribuirDisciplinaAProfessor(); }
  public void menuRemoverProfessorResponsavel() throws Exception { removerProfessorDisciplina(); }
  public void menuDeclararInteresseDisciplina() throws Exception { declararInteresseDisciplina(); }
  public void menuCalcularInteresseDisciplina() throws Exception { calcularIndiceInteresseDisciplina(); }
  public void menuRelatorioPopulariadeDisciplina() throws Exception { relatorioPopularidadeDisciplina(); }
  public void menuGerarRelatorioDisciplina() throws Exception { gerarRelatorioDisciplina(); }

  // ============================invalido======================================================
  // CRIACAO (Create)
  // ==================================================================================
  
  public void getInfoCadDisciplina() {
    try {
    disciplinaView.exibirTitulo("Cadastro de disciplina");
    String nomeDisciplina = disciplinaView.lerString("\nInforme o nome do disciplina: ");
    int cargaHoraria = disciplinaView.lerInteiro("\nInforme a carga horaria da disciplina: ");
    String matriculaProfessor = disciplinaView.lerString("\nInforme a matricula do professor responsavel: ");
    int tipoDisciplina = disciplinaView.lerInteiro("\nInforme a tipo de disciplina: 1 - Disciplina obrigatoria, 2 - Disciplina eletiva: ");
    
    adicionarDisciplina(nomeDisciplina, cargaHoraria, matriculaProfessor, tipoDisciplina);
    } catch (Exception e) {
      disciplinaView.erro(e.getMessage());
    }
  }

  public void adicionarDisciplina(
    String nomeDisciplina, 
    int cargaHoraria, 
    String matriculaProfessor, 
    int tipoDisciplina
  ) throws Exception 
  {
 
    Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

    if (professor == null)
      throw new Exception("Professor com matricula " + matriculaProfessor + " nao encontrado.");

    if (tipoDisciplina != 1 && tipoDisciplina != 2)
      throw new Exception("Tipo invalido. Use 1 (Obrigatoria) ou 2 (Eletiva).");

    if (nomeDisciplina.length() < 5)
      throw new Exception("Nome deve conter ao menos 5 caracteres");

    if (cargaHoraria < 10)
      throw new Exception("Carga horaria deve ser no minimo 10 horas.");

    Disciplina novaDisciplina;
    List<String> listaVazia = new ArrayList<>();

    if (tipoDisciplina == 1) {
      novaDisciplina = new DisciplinaObrigatoria(0, nomeDisciplina, cargaHoraria, professor, listaVazia);
    } else {
      novaDisciplina = new DisciplinaEletiva(0, nomeDisciplina, cargaHoraria, professor, listaVazia, new ArrayList<>());
    }

    novaDisciplina = this.disciplinaRepository.adicionarDisciplina(novaDisciplina);

    List<Disciplina> disciplinas = professor.getDisciplinas();
    List<String> nomeDisciplinas = new ArrayList<>();

    for(Disciplina d : disciplinas) { nomeDisciplinas.add(d.getNome()); }

    nomeDisciplinas.add(novaDisciplina.getNome());

    professorController.atualizarDisciplinas(nomeDisciplinas, professor);

    disciplinaView.sucesso("Disciplina cadastrada com sucesso");
    disciplinaView.exibirDetalhesDisciplina(novaDisciplina);

  }

  // ==================================================================================
  // REMOCAO (Delete)
  // ==================================================================================

  public void removerDisciplina(int id) throws Exception {
    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    String matriculaProfessor = disciplina.getProfessorResponsavel().getMatricula();
    Professor professor = professorController.encontrarProfessor(matriculaProfessor);
    List<Disciplina> disciplinasProfessor = professor.getDisciplinas();
    List<String> nomeDisciplinas = new ArrayList<>();
    
    for(Disciplina d : disciplinasProfessor){ nomeDisciplinas.add(d.getNome()); }; 


    nomeDisciplinas.remove(disciplina.getNome());

    professorController.atualizarDisciplinas(nomeDisciplinas, professor);
    this.disciplinaRepository.removerDisciplina(id);

    disciplinaView.limparTela();
    disciplinaView.sucesso("Disciplina exluida com sucesso");
    disciplinaView.pausar();
  }

  // ==================================================================================
  // LEITURA (Read / Getters)
  // ==================================================================================

  public Disciplina buscarDisciplinaPorId(int id) {
    return this.disciplinaRepository.buscarDisciplinaPorId(id);
  }
  
  public void listarDisciplinas() {
    disciplinaView.listarDisciplinas(disciplinaRepository.listarDisciplinas());
  }

  // --- Listar Alunos Matriculados ---
  
  public void listarAlunosMatriculados() throws Exception {
    disciplinaView.exibirTitulo("Listar alunos matriculados");

    int idDisciplina = disciplinaView.lerInteiro("Digite o id da disciplina: ");
    List<Alunos> alunos = listarAlunosMatriculados(idDisciplina);
    Disciplina disciplina = buscarDisciplinaPorId(idDisciplina);

    disciplinaView.listarAlunosMatriculados(alunos, disciplina);
  }

  public List<Alunos> listarAlunosMatriculados(int id) throws Exception {

    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    List<Alunos> alunos = new ArrayList<>();

    if (disciplina.getAlunos() != null) {
      for (String matricula : disciplina.getAlunos()) {
        Alunos a = this.alunoController.encontrarAluno(matricula);
        if (a != null)
          alunos.add(a);
      }
    }
    return alunos;
  }

  // --- Listar Alunos Interessados (Eletiva) ---

  public List<Alunos> listarAlunosInteressados(int id) throws Exception {

    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    if (!(disciplina instanceof DisciplinaEletiva)) {
      throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
    }

    List<Alunos> alunosObj = new ArrayList<>();
    List<String> matriculas = this.disciplinaRepository.listarMatriculasAlunosInteressados(id);

    if (matriculas != null) {
      for (String m : matriculas) {
        Alunos a = this.alunoController.encontrarAluno(m);
        if (a != null)
          alunosObj.add(a);
      }
    }
    return alunosObj;
  }

  // ==================================================================================
  // ATUALIZACAO - DADOS BASICOS
  // ==================================================================================

  public void editarDisciplina() throws Exception {
    disciplinaView.exibirTitulo("Editar disciplina");

    int id = disciplinaView.lerInteiro("\nDigite o ID da disciplina: ");
    Disciplina disciplina = buscarDisciplinaPorId(id);

    if (disciplina == null)
      throw new Exception("ID invalido");

    disciplinaView.exibirDetalhesDisciplina(disciplina);

    String escolha;
    Disciplina disciplinaAtualizada = null;

    disciplinaView.print("\n\n === Selecione a propriedade que deseja editar: ");
    disciplinaView.print("1. Nome");
    disciplinaView.print("2. Carga horaria");
    disciplinaView.print("3. Professor responsavel");
    disciplinaView.print("4. Voltar");
    escolha = disciplinaView.lerString("\nDigite a sua escolha: ");

    switch (escolha) {
      case "1":
        String nome = disciplinaView.lerString("Digite o nome do disciplina: ");
        disciplinaAtualizada = atualizarNome(id, nome);
        break;

      case "2":
        int cargaHoraria = disciplinaView.lerInteiro("Digite o carga horaria da disciplina: ");
        disciplinaAtualizada = atualizarCargaHoraria(id, cargaHoraria);
        break;

      case "3":
        String matriculaProfessor = disciplinaView.lerString("Digite o matricula do professor: ");
        disciplinaAtualizada = atualizarProfessorResponsavel(id, matriculaProfessor);
        break;

      case "4":
        return;

      default:
        disciplinaView.erro("\n\nOpção inválida, tente novamente: ");
    }

    if (disciplinaAtualizada != null) {
      alterarDisciplina(disciplinaAtualizada);
      disciplinaView.limparTela();
      disciplinaView.sucesso("Disciplina alterada com sucesso");
      disciplinaView.exibirDetalhesDisciplina(disciplinaAtualizada);
    }
  }


  public void alterarDisciplina(Disciplina disciplina1) throws Exception {
    try {
      Disciplina disciplina = buscarDisciplinaPorId(disciplina1.getId());
      if (disciplina == null) {
        throw new Exception("Erro: Id invalido");
      }

      this.disciplinaRepository.atualizarDisciplina(disciplina);

    } catch (Exception e) {
      throw new Exception("Erro ao atualizar disciplina: " + e.getMessage());
    }
  }

  public Disciplina atualizarNome(int id, String nome) throws Exception {
    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    disciplina.setNome(nome);
    this.disciplinaRepository.atualizarDisciplina(disciplina);
    return disciplina;
  }

  public Disciplina atualizarCargaHoraria(int id, int cargaHoraria) throws Exception {
    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    if (cargaHoraria < 10)
      throw new Exception("Carga horaria deve ser ao menos 10 horas.");

    disciplina.setCargaHoraria(cargaHoraria);
    this.disciplinaRepository.atualizarDisciplina(disciplina);
    return disciplina;
  }

  // ==================================================================================
  // ATUALIZACAO - PROFESSOR
  // ==================================================================================

  public Disciplina atualizarProfessorResponsavel(int id, String matriculaProfessor) throws Exception {
    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

    if (professor == null) {
      throw new Exception("Professor com matricula '" + matriculaProfessor + "' nao encontrado.");
    }

    removerProfessorResponsavel(disciplina.getId());

    disciplina.setProfessorResponsavel(professor);
    this.disciplinaRepository.atualizarDisciplina(disciplina);

    List<Disciplina> disciplinasProfessor = professor.getDisciplinas();
    List<String> nomesDisciplinas = new ArrayList<>();

    for(Disciplina d : disciplinasProfessor) { nomesDisciplinas.add(d.getNome()); }

    nomesDisciplinas.add(disciplina.getNome());

    professorController.atualizarDisciplinas(nomesDisciplinas, professor);

    return disciplina;
  }

  public Disciplina removerProfessorResponsavel(int id) throws Exception {
    try {
      Disciplina disciplina = buscarDisciplinaPorId(id);
      if (disciplina == null) {
        throw new Exception("Erro: Id invalido");
      }

      Professor professor = disciplina.getProfessorResponsavel();
      List<Disciplina> disciplinasProfessor = professor.getDisciplinas();
      List<String> nomesDisciplinas = new ArrayList<>();

      for(Disciplina d : disciplinasProfessor) { nomesDisciplinas.add(d.getNome()); }

      nomesDisciplinas.remove(disciplina.getNome());

      disciplina.setProfessorResponsavel(null);
      professorController.atualizarDisciplinas(nomesDisciplinas, professor);
      disciplinaRepository.atualizarDisciplina(disciplina);
      return disciplina;

    } catch (Exception e) {
      throw new Exception("Erro ao remover professor de disciplina: " + e.getMessage());
    }

  }

  public void atribuirDisciplinaAProfessor() throws Exception {
    try {

      disciplinaView.exibirTitulo("Atribuir professor responsavel");

      int idDisciplina = disciplinaView.lerInteiro("Digite o ID da disciplina: ");
      String matriculaProfessor = disciplinaView.lerString("Digite a matricula da professor: ");

      atualizarProfessorResponsavel(idDisciplina, matriculaProfessor);
      disciplinaView.sucesso("Disciplina atribuida ao professor com sucesso");
      disciplinaView.pausar();
    } catch (Exception e) {
      disciplinaView.erro("Erro ao atribuir disciplina ao professor: " + e.getMessage());
    }
  }

  public void removerProfessorDisciplina() throws Exception {
    try {
      disciplinaView.exibirTitulo("Remover professor responsavel");

      int idDisciplina = disciplinaView.lerInteiro("Digite o ID da disciplina: ");

      removerProfessorResponsavel(idDisciplina);
      disciplinaView.sucesso("Professor removido com sucesso");
      disciplinaView.pausar();
    } catch (Exception e) {
      disciplinaView.erro("Erro ao remover professor da disciplina: " + e.getMessage());
    }
  }

  public void declararInteresseDisciplina() throws Exception {
    try {

      int idDisciplina = disciplinaView.lerInteiro("Digite o ID disciplina: ");
      String matrculaAluno = disciplinaView.lerString("Digite a matricula da aluno: ");

      declararInteresseDisciplina(idDisciplina, matrculaAluno);

    } catch (Exception e) {
      disciplinaView.erro("Erro ao declarar interesse na disciplina: " + e.getMessage());
      disciplinaView.pausar();
    }
  }

  public void removerInteresseDisciplina() throws Exception {
    try {

      disciplinaView.exibirTitulo("Remover interesse de disciplina");
      
      int id = disciplinaView.lerInteiro("Digite o id da disciplina: ");
      String matricula = disciplinaView.lerString("Digite a matricula do aluno: ");

      removerInteresseDisciplina(id, matricula);

    } catch (Exception e) {
      disciplinaView.erro("Erro ao remover interesse de disciplina: " + e.getMessage());
    }
  }

  public void calcularIndiceInteresseDisciplina() throws Exception {
    int idDisciplina = disciplinaView.lerInteiro("Digite o ID disciplina: ");
    int interesse = getPopularidadeDisciplina(idDisciplina);

    disciplinaView.info(">>> Interesse na disciplina: " + interesse);
  }


  // ==================================================================================
  // GERENCIAMENTO DE ALUNOS (Matricula)
  // ==================================================================================

  public void getInfoMatriculaAluno() throws Exception {
    
    int idDisciplina = disciplinaView.lerInteiro("Digite o ID da disciplina: ");
    String matricula = disciplinaView.lerString("Digite a matricula do aluno: ");
    
    matricularAlunoDisciplina(idDisciplina, matricula);

  }

  public Disciplina matricularAlunoDisciplina(int idDisciplina, String matricula) throws Exception {
    
    disciplinaView.exibirTitulo("Matricular aluno em disciplina");

    Disciplina disciplina = buscarDisciplinaPorId(idDisciplina);
    
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    Alunos aluno = this.alunoController.encontrarAluno(matricula);

    if (aluno == null)
      throw new Exception("Aluno com matricula " + matricula + " nao encontrado.");

    List<String> alunosMatriculados = disciplina.getAlunos();
    if (alunosMatriculados == null)
      alunosMatriculados = new ArrayList<>();

    if (alunosMatriculados.contains(matricula)) {
      throw new Exception("Aluno ja esta matriculado nesta disciplina." + aluno.getMatricula() + disciplina.getId());
    }

    aluno.adicionarDisciplina(idDisciplina);
    alunosMatriculados.add(matricula);
    disciplina.setAlunos(alunosMatriculados);
    this.disciplinaRepository.atualizarDisciplina(disciplina);

    return disciplina;
  }

  public Disciplina desmatricularAlunoDisciplina() throws Exception {

    disciplinaView.exibirTitulo("Desmatricular aluno");
    int id = disciplinaView.lerInteiro("Digite o ID da disciplina: ");
    String matricula = disciplinaView.lerString("Digite a matricula do aluno: ");

    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    Alunos aluno = this.alunoController.encontrarAluno(matricula);
    if (aluno == null)
      throw new Exception("Aluno nao encontrado.");

    List<String> alunosMatriculados = disciplina.getAlunos();
    if (alunosMatriculados == null || !alunosMatriculados.contains(matricula)) {
      throw new Exception("Aluno nao esta matriculado nesta disciplina.");
    }

    aluno.removerDisciplina(id);
    alunosMatriculados.remove(matricula);
    disciplina.setAlunos(alunosMatriculados);
    this.disciplinaRepository.atualizarDisciplina(disciplina);

    return disciplina;
  }

  // ==================================================================================
  // GERENCIAMENTO DE INTERESSE (Eletivas)
  // ==================================================================================

  public Disciplina declararInteresseDisciplina(int id, String matricula) throws Exception {

    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    Alunos aluno = this.alunoController.encontrarAluno(matricula);
    if (aluno == null)
      throw new Exception("Aluno nao encontrado.");

    if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
      throw new Exception("Apenas disciplinas Eletivas aceitam interesse.");
    }
    List<String> interessados = disciplinaEletiva.listarInteressados();
    if (interessados == null)
      interessados = new ArrayList<>();

    if (interessados.contains(matricula)) {
      throw new Exception("Aluno ja registrou interesse.");
    }

    interessados.add(matricula);
    disciplinaEletiva.setListaAlunosInteressados(interessados);
    this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

    return disciplinaEletiva;

  }

  public Disciplina removerInteresseDisciplina(int id, String matricula) throws Exception {

    Disciplina disciplina = buscarDisciplinaPorId(id);
    if (disciplina == null) {
      throw new Exception("Erro: Id invalido");
    }

    Alunos aluno = this.alunoController.encontrarAluno(matricula);
    if (aluno == null)
      throw new Exception("Aluno nao encontrado.");

    if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
      throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
    }

    List<String> interessados = disciplinaEletiva.listarInteressados();

    if (interessados == null || !interessados.contains(matricula)) {
      throw new Exception("Aluno nao esta na lista de interessados.");
    }

    interessados.remove(matricula);
    disciplinaEletiva.setListaAlunosInteressados(interessados);
    this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

    return disciplinaEletiva;
  }

  public int getPopularidadeDisciplina(int idDisciplina) throws Exception {
    Disciplina disciplina = buscarDisciplinaPorId(idDisciplina);

    if (disciplina == null)
      throw new Exception("ID nao encontrado");

    if (disciplina instanceof DisciplinaEletiva disciplinaEletiva) {
      return disciplinaEletiva.listarInteressados().size();
    } else {
      return 0;
    }

  }


  // Relatorio de disciplina
  public void gerarRelatorioDisciplina() throws Exception {
    disciplinaView.gerarRelatorioDisciplina(disciplinaRepository.listarDisciplinas());
  }

  public void relatorioPopularidadeDisciplina() throws Exception {
    disciplinaView.relatorioPopularidadeDisciplina(disciplinaRepository.listarDisciplinas());
  }

  public void criarMockDiscipliba() throws Exception {

    adicionarDisciplina("Programacao orientada a objetos", 600, "XX0001", 1);
    adicionarDisciplina("Tecnicas de programacao 1", 600, "XX0001", 1);
    adicionarDisciplina("Desenvolvimento mobile", 600, "XX0001", 2);

    List<Alunos> alunos = AlunoRepository.getInstance().getTodos();

    // Matrícula na Obrigatória 1 (usando o ID gerado pelo repositório)
    for (Alunos aluno : alunos) {
      matricularAlunoDisciplina(1, aluno.getMatricula());
    }

    // Matrícula na Obrigatória 2
    for (Alunos aluno : alunos) {
      matricularAlunoDisciplina(2, aluno.getMatricula());
    }

    // Matrícula e Declaração de Interesse na Eletiva
    for (Alunos aluno : alunos) {
      matricularAlunoDisciplina(3, aluno.getMatricula());
      declararInteresseDisciplina(3, aluno.getMatricula());
    }
  }
}
