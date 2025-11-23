package repository;

import java.util.ArrayList;
import java.util.List;

import model.Disciplina;
import model.DisciplinaEletiva;

public class DisciplinaRepository implements IDisciplinaRepository {

    private static DisciplinaRepository instance;
    private List<Disciplina> disciplinas;
    private int sequenceId = 1;

    private DisciplinaRepository() {
        this.disciplinas = new ArrayList<>();
    }

    // Metodo estático Singleton
    public static DisciplinaRepository getInstance() {
        if (instance == null) {
            instance = new DisciplinaRepository();
        }
        return instance;
    }

    @Override
    public Disciplina adicionarDisciplina(Disciplina disciplina) {
        disciplina.setId(this.sequenceId);
        this.disciplinas.add(disciplina);
        this.sequenceId++;
        return disciplina;
    }

    @Override
    public List<Disciplina> listarDisciplinas() {
        return this.disciplinas;
    }

    @Override
    public Disciplina buscarDisciplinaPorId(int id) {
        for (Disciplina d : this.disciplinas) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    @Override
    public Disciplina buscarDisciplinaPorNome(String nome) {
        for (Disciplina d : this.disciplinas) {
            if (d.getNome().equals(nome)) {
                return d;
            }
        }
        return null;
    }

    @Override
    public boolean removerDisciplina(int id) {
        Disciplina disciplina = this.buscarDisciplinaPorId(id);

        if(disciplina == null) return false;
        this.disciplinas.remove(disciplina);
        return true;
    }

    @Override
    public void atualizarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorId(disciplina.getId());

        if (disciplinaExistente != null) {
            disciplinaExistente.setNome(disciplina.getNome());
            disciplinaExistente.setCargaHoraria(disciplina.getCargaHoraria());
            disciplinaExistente.setProfessorResponsavel(disciplina.getProfessorResponsavel());
            disciplinaExistente.setAlunos(disciplina.getAlunos());
        }
    }

    @Override
    public Disciplina matricularAluno(int idDisciplina, String matriculaAluno) {
        Disciplina disciplina = this.buscarDisciplinaPorId(idDisciplina);

        if (disciplina != null) {
            if (!disciplina.getAlunos().contains(matriculaAluno)) {
                disciplina.getAlunos().add(matriculaAluno);
            }

        }
        return disciplina;
    }

    @Override
    public Disciplina removerAluno(int idDisciplina, String matriculaAluno) {
        Disciplina disciplina = this.buscarDisciplinaPorId(idDisciplina);

        if (disciplina != null) {
            disciplina.getAlunos().remove(matriculaAluno);
        }
        return disciplina;
    }

    @Override
    public List<String> listarMatriculasAlunosPorDisciplina(int idDisciplina) {
        Disciplina disciplina = this.buscarDisciplinaPorId(idDisciplina);

        if (disciplina != null) {
            return disciplina.getAlunos();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> listarMatriculasAlunosInteressados(int idDisciplina) throws Exception {

        // 1. Busca Genérica
        Disciplina disciplina = this.buscarDisciplinaPorId(idDisciplina);

        // 2. Validação de Existência
        if (disciplina == null) {
            throw new Exception("Disciplina com ID " + idDisciplina + " não encontrada.");
        }

        // 3. Validação de Tipo e Extração de Dados (O Pulo do Gato)
        if (disciplina instanceof DisciplinaEletiva) {

            DisciplinaEletiva eletiva = (DisciplinaEletiva) disciplina;

            return eletiva.listarInteressados();

        } else {
            throw new Exception("Apenas disciplinas eletivas possuem lista de interesse.");
        }
    }
}