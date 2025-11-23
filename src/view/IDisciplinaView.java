package view;

import model.Alunos;
import model.Disciplina;

import java.util.List;

public interface IDisciplinaView {
    // ----------------- Funcoes genericas ---------------------
    public void print(String s);
    public String getInfo(String message);

    // ----------------- Prints especificos ---------------------
    public void printDisciplina(Disciplina disciplina);
    public void printAlunos(Disciplina disciplina, List<Alunos> alunos);

    // ----------------- Relatorios ---------------------
    public void printRelatorios(List<Disciplina> disciplina);

    // ----------------- Prints menu ---------------------
    public void adicionarDisciplina() throws Exception;
    public void listarDisciplinas() throws Exception;
    public void editarDisciplina() throws Exception;
    public void removerDisciplina() throws Exception;
    public void listarAlunosMatriculados() throws Exception;
}
