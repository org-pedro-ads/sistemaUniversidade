package controller;

import model.Alunos;
import model.ProjetoPesquisa;
import model.Professor;
import model.ProfessorVitalicio;
import repository.AlunoRepository;
import repository.ProfessorRepository;
import repository.ProjetoPesquisaRepository;
import view.ProjetoPesquisaView;

public class ProjetoPesquisaController {
    private final ProjetoPesquisaRepository projetoRepo;
    private final ProfessorRepository professorRepo;
    private final AlunoRepository alunoRepo;
    private final ProjetoPesquisaView view;

    public ProjetoPesquisaController(
            ProjetoPesquisaRepository projetoRepo,
            ProfessorRepository professorRepo,
            AlunoRepository alunoRepo,
            ProjetoPesquisaView view) {
        this.projetoRepo = projetoRepo;
        this.professorRepo = professorRepo;
        this.alunoRepo = alunoRepo;
        this.view = view;
    }

    public void cadastrarProjeto() {
        view.exibirTitulo("CADASTRAR NOVO PROJETO DE PESQUISA");

        String titulo = view.lerTituloProjeto();

        if (titulo.isBlank()) {
            view.erro("O título não pode estar vazio!");
            view.pausar();
            return;
        }

        if (projetoRepo.buscarPorTitulo(titulo) != null) {
            view.erro("Já existe um projeto com este título!");
            view.pausar();
            return;
        }

        String descricao = view.lerDescricaoProjeto();

        String matriculaProf = view.lerMatriculaOrientador();
        Professor orientador = professorRepo.findByMatricula(matriculaProf);

        if (orientador == null) {
            view.erro("Professor não encontrado!");
            return;
        }

        ProjetoPesquisa projeto = new ProjetoPesquisa(titulo, descricao, orientador);
        if (orientador instanceof ProfessorVitalicio professorVitalicio) {
            professorVitalicio.getProjetos().add(projeto);
        } else {
            view.erro("Apenas professores vitalícios podem orientar projetos de pesquisa!");
            view.pausar();
            return;
        }

        professorRepo.update(orientador);
        projetoRepo.cadastrarProjeto(projeto);
        view.sucesso("Projeto de pesquisa cadastrado com sucesso!");
        view.pausar();
    }

    public void listarProjetosDoProfessor() {
        view.exibirTitulo("BUSCAR PROJETOS POR ORIENTADOR");

        String matricula = view.lerString("Matrícula do professor: ");
        Professor prof = professorRepo.findByMatricula(matricula);

        if (prof == null) {
            view.erro("Professor não encontrado!");
            view.pausar();
            return;
        }

        if (!(prof instanceof ProfessorVitalicio)) {
            view.erro("Apenas professores vitalícios podem orientar projetos de pesquisa!");
            view.pausar();
            return;
        }

        view.info("Projetos orientados por: " + prof.getNome());
        projetoRepo.listarProjetosDoProfessor((ProfessorVitalicio) prof);
        view.pausar();
    }

    public void adicionarAlunoBolsista() {
        view.exibirTitulo("ADICIONAR ALUNO BOLSISTA");

        String titulo = view.lerString("Título do projeto: ");
        ProjetoPesquisa projeto = projetoRepo.buscarPorTitulo(titulo);

        if (projeto == null) {
            view.erro("Projeto não encontrado!");
            view.pausar();
            return;
        }

        String matricula = view.lerMatriculaAlunoBolsista();
        Alunos aluno = alunoRepo.buscarPorMatricula(matricula);

        if (aluno == null) {
            view.erro("Aluno não encontrado!");
            view.pausar();
            return;
        }

        if (projeto.getAlunosBolsistas().contains(aluno)) {
            view.erro("Aluno já é bolsista deste projeto!");
            view.pausar();
            return;
        }

        projeto.adicionarAluno(aluno);
        view.sucesso("Aluno " + aluno.getNome() + " adicionado como bolsista!");

        view.exibirProjetoDetalhado(projeto);
        view.pausar();
    }

    public void removerAlunoBolsista() {
        view.exibirTitulo("REMOVER ALUNO BOLSISTA");

        String titulo = view.lerString("Título do projeto: ");
        ProjetoPesquisa projeto = projetoRepo.buscarPorTitulo(titulo);

        if (projeto == null) {
            view.erro("Projeto não encontrado!");
            view.pausar();
            return;
        }

        String matricula = view.lerMatriculaAlunoBolsista();
        Alunos aluno = alunoRepo.buscarPorMatricula(matricula);

        if (aluno == null) {
            view.erro("Aluno não encontrado!");
            view.pausar();
            return;
        }

        if (!projeto.getAlunosBolsistas().contains(aluno)) {
            view.erro("Aluno não é bolsista deste projeto!");
            view.pausar();
            return;
        }

        projeto.removerAluno(aluno);
        view.sucesso("Aluno " + aluno.getNome() + " removido do projeto!");
        view.pausar();
    }

    public void alterarOrientador() {
        view.exibirTitulo("ALTERAR ORIENTADOR DO PROJETO");

        String titulo = view.lerString("Título do projeto: ");
        ProjetoPesquisa projeto = projetoRepo.buscarPorTitulo(titulo);

        if (projeto == null) {
            view.erro("Projeto não encontrado!");
            view.pausar();
            return;
        }

        String matricula = view.lerString("Matrícula do novo orientador (vitalício): ");
        Professor novoOrientador = professorRepo.findByMatricula(matricula);

        if (novoOrientador == null) {
            view.erro("Professor não encontrado!");
            view.pausar();
            return;
        }

        if (!(novoOrientador instanceof ProfessorVitalicio)) {
            view.erro("Apenas professores vitalícios podem ser orientadores!");
            view.pausar();
            return;
        }else{
            ProfessorVitalicio antigoOrientador = (ProfessorVitalicio) professorRepo.findByMatricula(projeto.getOrientador().getMatricula());
            antigoOrientador.removerProjeto(projeto);
            professorRepo.update(antigoOrientador);
        }

        projeto.alterarOrientador((ProfessorVitalicio) novoOrientador);
        ((ProfessorVitalicio) novoOrientador).adicionarProjeto(projeto);
        professorRepo.update(novoOrientador);
        projetoRepo.atualizarProjeto(projeto);
        view.sucesso("Orientador alterado com sucesso!");

        view.exibirProjetoDetalhado(projeto);
        view.pausar();
    }

    public void detalharProjeto() {
        view.exibirTitulo("DETALHES DO PROJETO");

        String titulo = view.lerString("Título do projeto: ");
        ProjetoPesquisa projeto = projetoRepo.buscarPorTitulo(titulo);

        if (projeto == null) {
            view.erro("Projeto não encontrado!");
        } else {
            view.exibirProjetoDetalhado(projeto);
        }
        view.pausar();
    }
}