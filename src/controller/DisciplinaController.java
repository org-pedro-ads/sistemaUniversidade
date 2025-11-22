package controller;

import model.*;
import repository.DisciplinaRepository;
import view.DisciplinaView;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController implements IDisciplinaController {

    DisciplinaRepository disciplinaRepository;
    DisciplinaView disciplinaView;
    ProfessorController professorController;

    public DisciplinaController(
            DisciplinaRepository disciplinaRepository,
            DisciplinaView disciplinaView,
            ProfessorController professorController
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaView = disciplinaView;
        this.professorController = professorController;
    }

    // ------------- Adicionar / remover --------------------

    @Override
    public Disciplina cadastrarDisciplina() throws Exception {
        try {
            // Buscar informacoes pela view
            this.disciplinaView.print(" ================= Cadastro Disciplina ================ \n");
            String nomeDisciplina = this.disciplinaView.getInfo("\nDigite o nome da disciplina: ");
            int cargaHoraria = Integer.getInteger(this.disciplinaView.getInfo("\nDigite a carga-horaria da disciplina: "));
            String professorResponsavel = this.disciplinaView.getInfo("\nDigite a matricula do professor responsavel: ");
            int tipoDisciplina = Integer.getInteger(this.disciplinaView.getInfo("\nDigite o tipo de disciplina: 1 - Obrigatoria;\n2 - Eletiva"));


            Professor professor = this.professorController.encontrarProfessor(professorResponsavel);
            List<String> alunosDisciplina = new ArrayList<>();

            // Validacoes basicas
            if(professor == null){ throw new Exception("Professor nao encontrado"); }
            if(!(tipoDisciplina == 1 || tipoDisciplina == 2)){ throw new Exception("Tipo de matricula invalido"); }
            if(nomeDisciplina.length() < 5){ throw new Exception("Nome da disciplina deve conter ao menos 5 caracteres"); }
            if(cargaHoraria < 9) { throw new Exception("Carga horaria deve ser no minimo 10"); }

            // Criar a disciplina
            if(tipoDisciplina == 1) {
                Disciplina disciplina = new DisciplinaObrigatoria(0, nomeDisciplina, cargaHoraria, professor, alunosDisciplina);
                this.disciplinaRepository.adicionarDisciplina(disciplina);

            } else {
                Disciplina disciplina = new DisciplinaEletiva(0, nomeDisciplina, cargaHoraria, professor, alunosDisciplina, alunosDisciplina);
                this.disciplinaRepository.adicionarDisciplina(disciplina);
            }

            this.disciplinaView.print("Matricula cadastrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar Disciplina" + e.getMessage());
        }
        return null;
    }

    @Override
    public void removerDisciplina(int id) throws Exception {
        // Implementação: Lógica de negócio (regras) e chamada ao Repositório
        try {
            // Se o repositório usa ID, chame: this.disciplinaRepository.removerDisciplina(id);
            this.disciplinaRepository.removerDisciplina(id);
        } catch (Exception e) {
            // Lógica de tratamento de erro e relançamento
            throw new Exception("Erro ao remover Disciplina: " + e.getMessage());
        }
    }

    // ------------- Getters --------------------

    @Override
    public Disciplina getDisciplinaPorId(int id) {
        // Implementação: Chama o Repositório
        return null;
    }

    @Override
    public Disciplina getDisciplinaPorNome(String nome) {
        // Implementação: Chama o Repositório
        return null;
    }

    @Override
    public List<Disciplina> getDisciplinas() {
        // Implementação: Chama o Repositório
        return new ArrayList<>();
    }

    @Override
    public List<Alunos> getAlunosDisciplina(int id) {
        // Implementação: Chama o Repositório (ou um AlunoRepository) e retorna os objetos Alunos
        return new ArrayList<>();
    }

    @Override
    public List<Disciplina> getPopularidadeDisciplina(int id) {
        // Implementação: Lógica de Negócio para calcular a popularidade
        return new ArrayList<>();
    }

    // ------------- Setters (Atualização) --------------------

    @Override
    public Disciplina setNomeDisciplina(int id, String nome) {
        // Implementação: Busca, modifica o objeto (setters) e chama this.disciplinaRepository.updateDisciplina(disciplina);
        return null;
    }

    @Override
    public Disciplina setCargaHorariaDisciplina(int id, int cargaHoraria) {
        // Implementação: Busca, modifica o objeto e chama o Repositório
        return null;
    }

    // ------------- Professor --------------------

    @Override
    public Disciplina setProfessorResponsavelDisciplina(int id, String professor) {
        // Implementação: Busca, atribui o professor e chama o Repositório
        return null;
    }

    // ------------- Aluno --------------------

    @Override
    public Disciplina matricularAlunoDisciplina(int id, String matricula) {
        // Implementação: Lógica de Negócio (ex: verificar vagas) e chama o Repositório
        return null;
    }

    @Override
    public Disciplina desmatricularAlunoDisciplina(int id, String matricula) {
        // Implementação: Lógica de Negócio e chama o Repositório
        return null;
    }

    @Override
    public Disciplina declararInteresseDisciplina(int id, String matricula) {
        // Implementação: Lógica de Negócio e chama o Repositório
        return null;
    }

    @Override
    public Disciplina removerInteresseDisciplina(int id, String matricula) {
        // Implementação: Lógica de Negócio e chama o Repositório
        return null;
    }

    // ------------- Relatoios --------------------

    @Override
    public void relatoriosDisciplinas() {
        // Implementação: Coleta dados (via Getters), formata e envia para a View
    }
}