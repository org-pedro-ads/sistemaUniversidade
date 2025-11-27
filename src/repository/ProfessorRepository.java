package repository;

import model.Professor;
import model.ProfessorVitalicio;
import model.TipoProfessor;
import model.TituloProfessor;

import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository {

    private static ProfessorRepository instance;

    private final List<Professor> professores = new ArrayList<>();

    private ProfessorRepository() {}

    public static ProfessorRepository getInstance() {
        if (instance == null) {
            instance = new ProfessorRepository();
        }
        return instance;
    }

    public void save(Professor professor) {
        professores.add(professor);
    }

    public List<Professor> findAll() {
        return professores;
    }

    public Professor findByMatricula(String matricula) {
        for (Professor p : professores) {
            if (p.getMatricula().equals(matricula)) {
                return p;
            }
        }
        return null;
    }

    public void delete(String matricula) {
        professores.removeIf(p -> p.getMatricula().equals(matricula));
    }

    public void update(Professor professor) {
        for (int i = 0; i < professores.size(); i++) {
            if (professores.get(i).getMatricula().equals(professor.getMatricula())) {
                professores.set(i, professor);
                return;
            }
        }
    }
}

