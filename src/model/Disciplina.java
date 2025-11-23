package model;

import java.util.List;

public class Disciplina {
    protected int id;
    protected String nome;
    protected int cargaHoraria;
    protected Professor professorResponsavel;
    protected List<String> alunos;

    public Disciplina(int id, String nome, int cargaHoraria, Professor professorResponsavel, List<String> alunos) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.professorResponsavel = professorResponsavel;
        this.alunos = alunos;
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

    public List<String> getAlunos() {
        return alunos;
    }

    public String getTipo(){
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

    public void setAlunos(List<String> alunos) {
        this.alunos = alunos;
    }
}