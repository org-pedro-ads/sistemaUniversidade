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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public TituloProfessor getTitulo() {
        return titulo;
    }

    public void setTitulo(TituloProfessor titulo) {
        this.titulo = titulo;
    }

    public TipoProfessor getTipo() {
        return tipo;
    }

    public void setTipo(TipoProfessor tipo) {
        this.tipo = tipo;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public abstract Double calcularSalario(Double horasTrabalhadas);

    public void adicionarDisciplina(Disciplina disciplina){
        if(!this.disciplinas.contains(disciplina)){
            this.disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina){
        this.disciplinas.removeIf(d -> d.getNome().equals(disciplina.getNome()));
    }

    public List<Disciplina> listarDisciplinas(){
        return disciplinas;
    }
    
}
