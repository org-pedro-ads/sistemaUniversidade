package controller;

import model.Alunos;
import model.Disciplina;

import java.util.List;

public interface IDisciplinaController {
    // ------------- Adicionar / remover --------------------
    Disciplina cadastrarDisciplina();
    void removerDisciplina(int id);

    // ------------- Getters --------------------
    Disciplina getDisciplinaPorId(int id);
    Disciplina getDisciplinaPorNome(String nome);
    List<Disciplina> getDisciplinas();
    List<Alunos> getAlunosDisciplina(int id);
    List<Disciplina> getPopularidadeDisciplina(int id);

    // ------------- Setters --------------------
    Disciplina setNomeDisciplina(int id, String nome);
    Disciplina setCargaHorariaDisciplina(int id, int cargaHoraria);

    // ------------- Professor --------------------
    Disciplina setProfessorResponsavelDisciplina(int id, String professor);

    // ------------- Aluno --------------------
    Disciplina matricularAlunoDisciplina(int id, String matricula);
    Disciplina desmatricularAlunoDisciplina(int id, String matricula);
    Disciplina declararInteresseDisciplina(int id, String matricula);
    Disciplina removerInteresseDisciplina(int id, String matricula);

    // ------------- Relatoios --------------------
    void relatoriosDisciplinas();
}
