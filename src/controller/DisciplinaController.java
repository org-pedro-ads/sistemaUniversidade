package controller;

import model.*;
import repository.AlunoRepository;
import repository.DisciplinaRepository;
import view.AlunoView;
import view.DisciplinaView;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
    private final ProfessorController professorController =  new ProfessorController();
    private final AlunoController alunoController = new AlunoController(AlunoRepository.getInstance(), new AlunoView());

    public DisciplinaController() {}

    // ==================================================================================
    // CRIACAO (Create)
    // ==================================================================================

    public Disciplina adicionarDisciplina(String nomeDisciplina, int cargaHoraria, String matriculaProfessor, int tipoDisciplina) {
        try {

            Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

            if (professor == null) {
                throw new Exception("Professor com matricula " + matriculaProfessor + " nao encontrado.");
            }

            if (tipoDisciplina != 1 && tipoDisciplina != 2) {
                throw new Exception("Tipo invalido. Use 1 (Obrigatoria) ou 2 (Eletiva).");
            }
            if (nomeDisciplina.length() < 5) {
                throw new Exception("Nome deve conter ao menos 5 caracteres.");
            }
            if (cargaHoraria < 10) {
                throw new Exception("Carga horaria deve ser no minimo 10 horas.");
            }

            Disciplina novaDisciplina;
            List<String> listaVazia = new ArrayList<>();

            if (tipoDisciplina == 1) {
                novaDisciplina = new DisciplinaObrigatoria(0, nomeDisciplina, cargaHoraria, professor, listaVazia);
            } else {
                novaDisciplina = new DisciplinaEletiva(0, nomeDisciplina, cargaHoraria, professor, listaVazia, new ArrayList<>());
            }

            novaDisciplina = this.disciplinaRepository.adicionarDisciplina(novaDisciplina);

            return novaDisciplina;

        } catch (Exception e) {
            return null;
        }
    }

    // ==================================================================================
    // REMOCAO (Delete)
    // ==================================================================================

    public void removerDisciplina(int id) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }
        this.disciplinaRepository.removerDisciplina(id);
    }

    // ==================================================================================
    // LEITURA (Read / Getters)
    // ==================================================================================

    public Disciplina buscarDisciplinaPorId(int id) {
        return this.disciplinaRepository.buscarDisciplinaPorId(id);
    }

    public List<Disciplina> listarDisciplinas() {
        return this.disciplinaRepository.listarDisciplinas();
    }

    // --- Listar Alunos Matriculados ---

    public List<Alunos> listarAlunosMatriculados(int id) throws Exception {

        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        List<Alunos> alunos = new ArrayList<>();

        if (disciplina.getAlunos() != null) {
            for (String matricula : disciplina.getAlunos()) {
                Alunos a = this.alunoController.encontrarAluno(matricula);
                if (a != null) alunos.add(a);
            }
        }
        return alunos;
    }

    // --- Listar Alunos Interessados (Eletiva) ---

    public List<Alunos> listarAlunosInteressados(int id) throws Exception {

        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        if (!(disciplina instanceof DisciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
        }

        List<Alunos> alunosObj = new ArrayList<>();
        List<String> matriculas = this.disciplinaRepository.listarMatriculasAlunosInteressados(id);

        if (matriculas != null) {
            for (String m : matriculas) {
                Alunos a = this.alunoController.encontrarAluno(m);
                if (a != null) alunosObj.add(a);
            }
        }
        return alunosObj;
    }

    // ==================================================================================
    // ATUALIZACAO - DADOS BASICOS
    // ==================================================================================

    public void alterarDisciplina(Disciplina disciplina1) throws Exception {
        try {
            Disciplina disciplina = buscarDisciplinaPorId(disciplina1.getId());
            if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

            this.disciplinaRepository.atualizarDisciplina(disciplina);

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }

    public Disciplina atualizarNome(int id, String nome) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        disciplina.setNome(nome);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    public Disciplina atualizarCargaHoraria(int id, int cargaHoraria) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        if (cargaHoraria < 10) throw new Exception("Carga horaria deve ser ao menos 10 horas.");

        disciplina.setCargaHoraria(cargaHoraria);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    // ==================================================================================
    // ATUALIZACAO - PROFESSOR
    // ==================================================================================

    public Disciplina atualizarProfessorResponsavel(int id, String matriculaProfessor) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

        if (professor == null) {
            throw new Exception("Professor com matricula '" + matriculaProfessor + "' nao encontrado.");
        }

        disciplina.setProfessorResponsavel(professor);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    public Disciplina removerProfessorResponsavel(int id) throws Exception {
        try {
            Disciplina disciplina = buscarDisciplinaPorId(id);
            if(disciplina == null) { throw new Exception("Erro: Id invalido"); }

            disciplina.setProfessorResponsavel(null);
            disciplinaRepository.atualizarDisciplina(disciplina);
            return disciplina;

        } catch (Exception e) {
            throw new Exception("Erro ao remover professor de disciplina: " + e.getMessage());
        }

    }

    // ==================================================================================
    // GERENCIAMENTO DE ALUNOS (Matricula)
    // ==================================================================================

    public Disciplina matricularAlunoDisciplina(int idDisciplina, String matricula) throws Exception {

        Disciplina disciplina = buscarDisciplinaPorId(idDisciplina);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno com matricula " + matricula + " nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();
        if (alunosMatriculados == null) alunosMatriculados = new ArrayList<>();

        if (alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno ja esta matriculado nesta disciplina.");
        }

        aluno.adicionarDisciplina(idDisciplina);
        alunosMatriculados.add(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    public Disciplina desmatricularAlunoDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        Alunos aluno = this.alunoController.encontrarAluno(matricula);
        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();
        if (alunosMatriculados == null || !alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno nao esta matriculado nesta disciplina.");
        }

        aluno.removerDisciplina(id);
        alunosMatriculados.remove(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    // ==================================================================================
    // GERENCIAMENTO DE INTERESSE (Eletivas)
    // ==================================================================================

    public Disciplina declararInteresseDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        Alunos aluno = this.alunoController.encontrarAluno(matricula);
        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        // Pattern Matching (Java 16+)
        if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas aceitam interesse.");
        }
        List<String> interessados = disciplinaEletiva.listarInteressados();
        if (interessados == null) interessados = new ArrayList<>();

        if (interessados.contains(matricula)) {
            throw new Exception("Aluno ja registrou interesse.");
        }

        interessados.add(matricula);
        disciplinaEletiva.setListaAlunosInteressados(interessados);
        this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

        return disciplinaEletiva;
    }

    public Disciplina removerInteresseDisciplina(int id, String matricula) throws Exception {

        Disciplina disciplina = buscarDisciplinaPorId(id);
        if(disciplina == null){ throw new Exception("Erro: Id invalido"); }

        Alunos aluno = this.alunoController.encontrarAluno(matricula);
        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
        }

        List<String> interessados = disciplinaEletiva.listarInteressados();

        if (interessados == null || !interessados.contains(matricula)) {
            throw new Exception("Aluno nao esta na lista de interessados.");
        }

        interessados.remove(matricula);
        disciplinaEletiva.setListaAlunosInteressados(interessados);
        this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

        return disciplinaEletiva;
    }

    public int getPopularidadeDisciplina(int idDisciplina) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(idDisciplina);

        if(disciplina == null) throw new Exception("ID nao encontrado");

        if(disciplina instanceof DisciplinaEletiva disciplinaEletiva) {
            return disciplinaEletiva.listarInteressados().size();
        }else {
            return 0;
        }

    }
}