package model;
import java.util.List;
import java.util.ArrayList;

public class Alunos {
    private String nome;
    private String matricula;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    // Construtor com parâmetros
    public Alunos(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }
    public String getMatricula() {
        return matricula;
    }

    public List<Disciplina> getDisciplinasMatriculadas() {
        return new ArrayList<>(disciplinas);
    }

    public void removerDisciplina(Disciplina d) {
        disciplinas.remove(d);
    }

    public void adicionarDisciplina(Disciplina d) {
        if (d != null && !disciplinas.contains(d)) {
            disciplinas.add(d);
        }
    }

    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }
}