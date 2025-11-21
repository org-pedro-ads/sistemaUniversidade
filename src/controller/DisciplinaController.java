package controller;

import model.*;
import repository.DisciplinaRepository;
import view.DisciplinaView;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController {

    private DisciplinaRepository repo;
    private DisciplinaView view;
    private ProfessorController professorController;
    private AlunoController alunoController;

    public DisciplinaController(DisciplinaRepository repo, DisciplinaView view, ProfessorController professorController, AlunoController alunoController) {
        this.repo = repo;
        this.view = view;
        this.professorController = professorController;
        this.alunoController = alunoController;
    }

    //----------------------------- METODOS -----------------------------
    public void cadastrarDisciplina() throws Exception {
        try {

            String nome = view.getInfo("Digite o nome da disciplina:");
            int cargaHoraria = Integer.parseInt(view.getInfo("Digite a carga horária da disciplina:"));
            String matriculaProfessor = view.getInfo("Digite a matricula do professor responsavel:");
            int tipo = Integer.parseInt(view.getInfo("Digite o tipo da disciplina (1 para obrigatoria, 2 para eletiva):"));

            int id = repo.getNextId();
            List<Alunos> alunos = new ArrayList<>();
            Professor professorResponsavel = professorController.encontrarProfessor(matriculaProfessor);

            if (professorResponsavel == null) {
                view.print("Erro: Professor com matricula " + professorResponsavel + " não encontrado. Cadastro cancelado.");
                return;
            }

            if (tipo == 1) {
                DisciplinaObrigatoria disciplina = new DisciplinaObrigatoria(
                        id,
                        nome,
                        cargaHoraria,
                        professorResponsavel,
                        alunos
                );

                Disciplina disciplinaAdicionada = repo.adicionar(disciplina);
                view.print("Disciplina adicionada com sucesso!");
                view.showDisciplina(disciplinaAdicionada);

            } else if (tipo == 2) {
                DisciplinaEletiva disciplina = new DisciplinaEletiva(
                        id,
                        nome,
                        cargaHoraria,
                        professorResponsavel,
                        alunos,
                        alunos
                );

                Disciplina disciplinaAdicionada = repo.adicionar(disciplina);
                view.print("Disciplina adicionada com sucesso!");
                view.showDisciplina(disciplinaAdicionada);

            } else {
                view.print("Erro: Tipo de disciplina invalido");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar disciplina " + e.getMessage(), e);
        }
    }

    public void removerDisciplina(int id) {
        // TODO: Remover disciplina do aluno tambem
        // TODO: Fazer implementacao
    }

    public void showDisciplinas(){
        List<Disciplina> disciplinas = repo.listarDisciplinas();

        if(disciplinas.isEmpty()){
            view.print("Nenhuma disciplina cadastrada.");
            return;
        }

        view.showDisciplinas(disciplinas);
    }

    public Alunos matricularAluno() throws Exception {
        try{
            String matriculaAluno = view.getInfo("Digite a matricula do aluno:");
            int idDisciplina = Integer.parseInt(view.getInfo("Digite o id da disciplina:"));

            Alunos aluno = alunoController.encontrarAluno(matriculaAluno);

            if(aluno == null) {
                view.print("Erro: Aluno com matricula " + matriculaAluno + " não encontrado. Matricula cancelada.");
                return null;
            }

            Disciplina disciplina = repo.buscarDisciplinaPorId(idDisciplina);
            if(disciplina == null) {
                view.print("Erro: Disciplina com id " + idDisciplina + " não encontrada. Matricula cancelada.");
                return null;
            }

            repo.matricularAluno(aluno, disciplina.getId());
            disciplinaAtualizada = repo.buscarDisciplinaPorId(idDisciplina);
            aluno.adicionarDisciplina(package controller;

import model.*;
import repository.DisciplinaRepository;
import view.DisciplinaView;

import java.util.ArrayList;
import java.util.List;

            public class DisciplinaController {

                private DisciplinaRepository repo;
                private DisciplinaView view;
                private ProfessorController professorController;
                private AlunoController alunoController;

                public DisciplinaController(DisciplinaRepository repo, DisciplinaView view, ProfessorController professorController, AlunoController alunoController) {
                    this.repo = repo;
                    this.view = view;
                    this.professorController = professorController;
                    this.alunoController = alunoController;
                }

                //----------------------------- METODOS -----------------------------
                public void cadastrarDisciplina() throws Exception {
                    try {

                        String nome = view.getInfo("Digite o nome da disciplina:");
                        int cargaHoraria = Integer.parseInt(view.getInfo("Digite a carga horária da disciplina:"));
                        String matriculaProfessor = view.getInfo("Digite a matricula do professor responsavel:");
                        int tipo = Integer.parseInt(view.getInfo("Digite o tipo da disciplina (1 para obrigatoria, 2 para eletiva):"));

                        int id = repo.getNextId();
                        List<Alunos> alunos = new ArrayList<>();
                        Professor professorResponsavel = professorController.encontrarProfessor(matriculaProfessor);

                        if (professorResponsavel == null) {
                            view.print("Erro: Professor com matricula " + professorResponsavel + " não encontrado. Cadastro cancelado.");
                            return;
                        }

                        if (tipo == 1) {
                            DisciplinaObrigatoria disciplina = new DisciplinaObrigatoria(
                                    id,
                                    nome,
                                    cargaHoraria,
                                    professorResponsavel,
                                    alunos
                            );

                            Disciplina disciplinaAdicionada = repo.adicionar(disciplina);
                            view.print("Disciplina adicionada com sucesso!");
                            view.showDisciplina(disciplinaAdicionada);

                        } else if (tipo == 2) {
                            DisciplinaEletiva disciplina = new DisciplinaEletiva(
                                    id,
                                    nome,
                                    cargaHoraria,
                                    professorResponsavel,
                                    alunos,
                                    alunos
                            );

                            Disciplina disciplinaAdicionada = repo.adicionar(disciplina);
                            view.print("Disciplina adicionada com sucesso!");
                            view.showDisciplina(disciplinaAdicionada);

                        } else {
                            view.print("Erro: Tipo de disciplina invalido");
                        }
                    } catch (Exception e) {
                        throw new Exception("Erro ao cadastrar disciplina " + e.getMessage(), e);
                    }
                }

                public void removerDisciplina(int id) {
                    // TODO: Remover disciplina do aluno tambem
                    // TODO: Fazer implementacao
                }

                public void showDisciplinas(){
                    List<Disciplina> disciplinas = repo.listarDisciplinas();

                    if(disciplinas.isEmpty()){
                        view.print("Nenhuma disciplina cadastrada.");
                        return;
                    }

                    view.showDisciplinas(disciplinas);
                }

                public Alunos matricularAluno() throws Exception {
                    try{
                        String matriculaAluno = view.getInfo("Digite a matricula do aluno:");
                        int idDisciplina = Integer.parseInt(view.getInfo("Digite o id da disciplina:"));

                        Alunos aluno = alunoController.encontrarAluno(matriculaAluno);

                        if(aluno == null) {
                            view.print("Erro: Aluno com matricula " + matriculaAluno + " não encontrado. Matricula cancelada.");
                            return null;
                        }

                        Disciplina disciplina = repo.buscarDisciplinaPorId(idDisciplina);
                        if(disciplina == null) {
                            view.print("Erro: Disciplina com id " + idDisciplina + " não encontrada. Matricula cancelada.");
                            return null;
                        }

                        repo.matricularAluno(aluno, disciplina.getId());
                        disciplinaAtualizada = repo.buscarDisciplinaPorId(idDisciplina);
                        aluno.adicionarDisciplina(disciplinaAtualizada);
                        view.print("Aluno matriculado com sucesso!");
                        return aluno;

                    } catch (Exception e) {
                        throw new Exception("Erro ao matricular aluno " + e.getMessage(), e);
                    }
                }

                public Disciplina buscarPorNome(String nome) throws Exception {
                    try {
                        return repo.buscarDisciplinaPorNome(nome);
                    } catch (Exception e) {
                        throw new Exception("Erro ao buscar disciplina pelo nome " + nome + ":" + e.getMessage(), e);
                    }
                }

                public Disciplina buscarDisciplinaPorId(int id) throws Exception {
                    try {
                        return repo.buscarDisciplinaPorId(id);
                    } catch (Exception e) {
                        throw new Exception("Erro ao buscar disciplina pelo id " + id + ":" + e.getMessage(), e);
                    }
                }

            }
);
            view.print("Aluno matriculado com sucesso!");
            return aluno;

        } catch (Exception e) {
            throw new Exception("Erro ao matricular aluno " + e.getMessage(), e);
        }
    }

    public Disciplina buscarPorNome(String nome) throws Exception {
        try {
            return repo.buscarDisciplinaPorNome(nome);
        } catch (Exception e) {
            throw new Exception("Erro ao buscar disciplina pelo nome " + nome + ":" + e.getMessage(), e);
        }
    }

    public Disciplina buscarDisciplinaPorId(int id) throws Exception {
        try {
            return repo.buscarDisciplinaPorId(id);
        } catch (Exception e) {
            throw new Exception("Erro ao buscar disciplina pelo id " + id + ":" + e.getMessage(), e);
        }
    }

}
