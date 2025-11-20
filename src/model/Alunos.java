package model;

public class Alunos {
    private String nome;
    private String matricula;

    // Construtor padrão
    public Alunos() {
    }

    // Construtor com parâmetros
    public Alunos(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }
}