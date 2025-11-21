package model;

import java.util.ArrayList;
import java.util.List;

public class ProjetoPesquisa {
    private String titulo;
    private String descricao;
    private Professor orientador;
    private List<Alunos> alunosBolsistas;

    public ProjetoPesquisa() {
        this.alunosBolsistas = new ArrayList<>();
    }

    public ProjetoPesquisa(String titulo, String descricao, Professor orientador) {
        this();
        this.titulo = titulo;
        this.descricao = descricao;
        this.orientador = orientador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Professor getOrientador() {
        return orientador;
    }

    public void setOrientador(Professor orientador) {
        this.orientador = orientador;
    }

    public List<Alunos> getAlunosBolsistas() {
        return alunosBolsistas;
    }

    public void adicionarAluno(Alunos aluno) {
        if (aluno != null && !alunosBolsistas.contains(aluno)) {
            alunosBolsistas.add(aluno);
        }
    }

    public void removerAluno(Alunos aluno) {
        alunosBolsistas.remove(aluno);
    }

    public void listarAlunos() {
        System.out.println("Alunos bolsistas do projeto \"" + titulo + "\":");
        if (alunosBolsistas.isEmpty()) {
            System.out.println("  Nenhum aluno cadastrado.");
        } else {
            for (Alunos a : alunosBolsistas) {
                System.out.println("  - " + a.getNome() + " (Matrícula: " + a.getMatricula() + ")");
            }
        }
    }

    // Metodo do diagrama: alterarOrientador(ProfessorVitalicio professor)
    // Assumindo que ProfessorVitalicio é uma subclasse ou tipo específico de
    // Professor
    public void alterarOrientador(Professor professorVitalicio) {
        if (professorVitalicio != null) {
            this.orientador = professorVitalicio;
            System.out.println("Orientador alterado para: " + professorVitalicio.getNome());
        }
    }

    @Override
    public String toString() {
        return "ProjetoPesquisa{" +
                "titulo='" + titulo + '\'' +
                ", orientador=" + (orientador != null ? orientador.getNome() : "não definido") +
                ", total de bolsistas=" + alunosBolsistas.size() +
                '}';
    }
}