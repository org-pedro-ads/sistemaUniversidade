package controller;

import model.Alunos;
import repository.AlunoRepository;
import view.AlunoView;

public class AlunoController {
    private final AlunoRepository repository;
    private final AlunoView view;

    public AlunoController(AlunoRepository repository, AlunoView view) {
        this.repository = repository;
        this.view = view;
    }

    public void cadastrarAluno() {
        view.exibirTitulo("CADASTRAR ALUNO");

        String nome = view.lerNome();
        if (nome.isBlank()) {
            view.erro("O nome não pode estar vazio!");
            view.pausar();
            return;
        }

        String matricula = view.lerMatricula();
        if (matricula.isBlank()) {
            view.erro("A matrícula não pode estar vazia!");
            view.pausar();
            return;
        }

        if (repository.buscarPorMatricula(matricula) != null) {
            view.erro("Já existe um aluno com a matrícula " + matricula + "!");
            view.pausar();
            return;
        }

        Alunos aluno = new Alunos(nome, matricula);
        repository.cadastrarAluno(aluno);
        view.sucesso("Aluno cadastrado com sucesso!");
        view.pausar();
    }

    public void listarAlunos() {
        if (repository.getTodos().isEmpty()) {
            System.out.println("   → Nenhum aluno cadastrado.");
            return;
        }

        view.exibirTitulo("LISTA DE ALUNOS");
        view.exibirListaAlunos(repository.getTodos());
        view.pausar();
    }

    public Alunos encontrarAluno(String matricula) {
        return repository.buscarPorMatricula(matricula);
    }
}