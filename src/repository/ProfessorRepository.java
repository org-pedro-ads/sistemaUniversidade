package repository;

import model.Professor;
import model.ProfessorVitalicio;
import model.TipoProfessor;
import model.TituloProfessor;

import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository {
    private final List<Professor> professores = new ArrayList<>() {
        {
            add(new ProfessorVitalicio("Ana Silva", "XX0001",
                    TituloProfessor.MESTRADO,
                    TipoProfessor.VITALICIO,
                    new ArrayList<>(), null, 8000.0));
        }
    };

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
