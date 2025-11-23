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
}
