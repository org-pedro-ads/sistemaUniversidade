package model;

import java.util.List;

public class Disciplina {
    protected int id;
    protected String nome;
    protected int cargaHoraria;
    protected Professor professorResponsavel;
    protected List<Alunos> alunos;

    public Disciplina(int id, String nome, int cargaHoraria, Professor professorResponsavel, List<Alunos> alunos) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.professorResponsavel = professorResponsavel;
        this.alunos = alunos;
        this.id = id;
    }

    public Disciplina() {}

    // ------------------- GETTERS -------------------

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public List<Alunos> getAlunos() {
        return alunos;
    }

    public String getTipo() {
        return "";
    }


    // ------------------- SETTERS -------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public void setProfessorResponsavel(Professor professorResponsavel) {
        this.professorResponsavel = professorResponsavel;
    }

    public void setAlunos(List<Alunos> alunos) {
        this.alunos = alunos;
    }

    // ------------------- METODOS -------------------

    public Alunos matricularAluno(Alunos aluno) {
        // TODO: implementar funcao matricular aluno
        return aluno;
    }

    public Alunos desmatricularAluno(Alunos aluno) {
        // TODO: implementar funcao desmatricular aluno
        return aluno;
    }
}