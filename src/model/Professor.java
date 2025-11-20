package model;

import java.util.List;

public abstract class Professor {
    private String nome;
    private String matricula;
    private TituloProfessor titulo;
    private TipoProfessor tipo;
    private List<Disciplina> disciplinas;

    public Professor(String nome, String matricula, TituloProfessor titulo, TipoProfessor tipo, List<Disciplina> disciplinas){
        this.nome = nome;
        this.matricula= matricula;
        this.titulo = titulo;
        this.tipo = tipo;
        this.disciplinas = disciplinas;
    }
    public abstract Double calcularSalario(Double horasTrabalhadas);

    public abstract void adicionarDisciplina(Disciplina disciplina);

    public abstract void removerDisciplina(Disciplina disciplina);

    public List<Disciplina> listarDisciplinas(){
        return null;
    }
    
}
