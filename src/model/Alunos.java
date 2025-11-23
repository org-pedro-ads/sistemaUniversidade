package model;
import java.util.List;
import java.util.ArrayList;

public class Alunos {
    private String nome;
    private String matricula;
    private final List<Integer> disciplinas = new ArrayList<>();

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

    public List<Integer> getDisciplinasMatriculadas() {
        return new ArrayList<>(disciplinas);
    }

    public void removerDisciplina(int d) {
        disciplinas.remove(Integer.valueOf(d));
    }

    public void adicionarDisciplina(int d) {
        if (d > 0 && !disciplinas.contains(d)) {
            disciplinas.add(d);
        }
    }

    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }
}