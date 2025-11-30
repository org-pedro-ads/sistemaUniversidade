package view;

import model.Alunos;
import model.Disciplina;
import model.DisciplinaEletiva;
import model.DisciplinaObrigatoria;

import java.util.List;
import java.util.Scanner;

public class DisciplinaView extends BaseView {

  private final Scanner scanner = new Scanner(System.in);

  public DisciplinaView(){}

  public void print(String s) {
    System.out.println(s);
  }
  
  // ====================== MENU DISCIPLINAS ======================
  public String menuDisciplinas() {
    String escolha;

    System.out.println("\n>>>DISCIPLINAS");
    System.out.println("a) Cadastrar disciplina (obrigatória / eletiva)");
    System.out.println("b) Listar disciplinas");
    System.out.println("c) Editar disciplina");
    System.out.println("d) Remover disciplina");
    System.out.println("e) Visualizar alunos matriculados em uma disciplina");
    System.out.println("0) Voltar");
    System.out.print("→ Opção: ");
    escolha = scanner.nextLine().trim().toLowerCase();
    
    return escolha;
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

  public void listarDisciplinas(List<Disciplina> disciplinas) {

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

 

  public int removerDisciplina() throws Exception {
    exibirTitulo("Remocao de disciplina");
    int idDisciplina = lerInteiro("\nDigite o ID da disciplina: ");
    return idDisciplina;
  }

  public void listarAlunosMatriculados(List<Alunos> alunos, Disciplina disciplina) throws Exception {

    if(alunos.size() == 0) {
      info("Nao existem alunos matriculados nessa disciplina");
      return;
    }
    
    print("Disciplina: " + disciplina.getNome());
    AlunoView alunoView = new AlunoView();
    alunoView.exibirListaAlunos(alunos);
    pausar();
  }
 
  public void relatorioPopularidadeDisciplina(List<Disciplina> disciplinas) throws Exception {

    exibirTitulo("\nPopularidade das disciplinas");

    for (Disciplina disciplina : disciplinas) {
      if (disciplina instanceof DisciplinaEletiva) {
        DisciplinaEletiva disciplinaEletiva = (DisciplinaEletiva) disciplina;
        print("Disciplina: " + disciplinaEletiva.getNome());
        print("Professor responsavel: " + disciplinaEletiva.getProfessorResponsavel().getNome());
        print("Popularidade: " + disciplinaEletiva.listarInteressados().size());
      }
    }

    print("\n____________________________________________________________________\n");
  }

  public void gerarRelatorioDisciplina(List<Disciplina> disciplinas) throws Exception {

    int interesse = 0;

    for (Disciplina d : disciplinas) {
      if (d instanceof DisciplinaEletiva) {
        DisciplinaEletiva de = (DisciplinaEletiva) d;
        interesse = de.listarInteressados().size();
      }
      print(
        "Nome: " + d.getNome() +
        " | Tipo: " + d.getTipo() +
        " | Alunos Matriculados: " + d.getAlunos().size() +
        " | Popularidade: " + interesse
      );
    }
    print("=====================================\n");
  }

}
