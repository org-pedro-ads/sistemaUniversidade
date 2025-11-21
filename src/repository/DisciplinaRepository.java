package repository;

import java.util.ArrayList;
import java.util.List;

import model.Alunos;
import model.Disciplina;

public class DisciplinaRepository {

    private static DisciplinaRepository instance;
    private List<Disciplina> disciplinas;
    private int sequenceId = 1;

    private DisciplinaRepository() {
        this.disciplinas = new ArrayList<>();
    }

    public static DisciplinaRepository getInstance() {
        if (instance == null) {
            instance = new DisciplinaRepository();
        }
        return instance;
    }

    public int getNextId() {
        return this.sequenceId;
    }

    public Disciplina adicionar(Disciplina disciplina) {
        disciplina.setId(this.sequenceId);
        this.disciplinas.add(disciplina);
        this.sequenceId++;
        return disciplina;
    }

    public List<Disciplina> listarDisciplinas() {
        return this.disciplinas;
    }

    public Disciplina buscarDisciplinaPorId(int id) {
        for (Disciplina d : this.disciplinas) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        for (Disciplina d : this.disciplinas) {
            if (d.getNome().equals(nome)) {
                return d;
            }
        }
        return null;
    }

    public boolean removerDisciplina(int id) {
        return this.disciplinas.removeIf(d -> d.getId() == id);
    }

    public void updateDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorId(disciplina.getId());

        if (disciplinaExistente != null) {
            disciplinaExistente.setNome(disciplina.getNome());
            disciplinaExistente.setCargaHoraria(disciplina.getCargaHoraria());
            disciplinaExistente.setProfessorResponsavel(disciplina.getProfessorResponsavel());
            disciplinaExistente.setAlunos(disciplina.getAlunos());
        }
    }

    public Disciplina matricularAluno(Alunos aluno, int idDisciplina) {
        Disciplina disciplina = this.buscarDisciplinaPorId(idDisciplina);

        if (disciplina != null) {
            disciplina.getAlunos().add(aluno);
            this.updateDisciplina(disciplina);
        }
        return disciplina;
    }
}