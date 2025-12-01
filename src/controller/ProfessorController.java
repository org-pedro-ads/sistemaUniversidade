package controller;

import model.*;
import repository.ProfessorRepository;
import repository.DisciplinaRepository;
import repository.ProjetoPesquisaRepository;
import util.MatriculaGenerator;
import view.ProfessorView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static model.TipoProfessor.VITALICIO;
import static model.TituloProfessor.DOUTORADO;

public class ProfessorController {
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ProjetoPesquisaRepository projetoPesquisaRepository;
    private final ProfessorView professorView;

    public ProfessorController(ProfessorRepository professorRepository,DisciplinaRepository disciplinaRepository,
                               ProjetoPesquisaRepository projetoPesquisaRepository, ProfessorView professorView){
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.projetoPesquisaRepository = projetoPesquisaRepository;
        this.professorView = professorView;
    }


    public void criarMockProfessor() throws Exception {
        List<Disciplina> disciplinas =  new ArrayList<>();
        List<String> nomeDisciplina = new ArrayList<>();
        List<ProjetoPesquisa> projetosPesquisas = new ArrayList<>();
        List<String> nomeProjetosPesquisas = new ArrayList<>();

        ProfessorVitalicio professor = new ProfessorVitalicio(
          "Anisio Santos", 
          MatriculaGenerator.gerarParaProfessor(), 
          DOUTORADO, 
          VITALICIO, 
          disciplinas, 
          projetosPesquisas,
          25000.00
        ); 

        this.cadastrarProfessor(
                professor
        );
    }

    public void cadastrarProfessor(Professor professor) throws Exception {
        try{
            if(professor.getNome().isBlank()){
                throw new Exception("Nome nao pode ser vazio");
            }
            professorRepository.save(professor);
        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar professor - " + e.getMessage());
        }
    }

    private List<Disciplina> capturaDisciplinas(List<String> nomeDisciplinas, Professor professor) throws Exception {
        try{
            List<Disciplina> disciplinas = new ArrayList<>();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                if (consultarDisciplina == null) {
                    throw new Exception("Disciplina nao encontrada: " + nomeDisciplina);
                }
                if (disciplinas.contains(consultarDisciplina)) {
                    throw new Exception("Disciplina duplicada: " + nomeDisciplina);
                }
                if(consultarDisciplina.getProfessorResponsavel() != null && consultarDisciplina.getProfessorResponsavel().getMatricula() != professor.getMatricula()){
                    Professor professor1 = professorRepository.findByMatricula(consultarDisciplina.getProfessorResponsavel().getMatricula());
                    professor1.removerDisciplina(consultarDisciplina);
                    professorRepository.update(professor1);
                }
                disciplinas.add(consultarDisciplina);
            }
            return disciplinas;
        } catch (Exception e){
            throw new Exception("Erro ao capturar disciplinas - "+ e.getMessage());
        }
    }

    public void listarProfessores() throws Exception{
        try{
            List<Professor> listProfessor = professorRepository.findAll();
            professorView.listarProfessores(listProfessor);
        } catch (Exception e){
            throw new Exception("Erro ao listar professores");
        }
    }

    public void removerProfessor(String matricula) throws Exception{
        try{
            if(encontrarProfessor(matricula) != null){
                Professor professor = professorRepository.findByMatricula(matricula);
                if(professor instanceof ProfessorVitalicio professorVitalicio){
                    List<ProjetoPesquisa> projetos = professorVitalicio.getProjetos();
                    if(!projetos.isEmpty()){
                        throw new Exception("Professor vitalicio nao pode ter projetos pesquisas, vinculada ao seu professor");
                    }
                }
                List<Disciplina> disciplinas = professor.getDisciplinas();
                for(Disciplina d : disciplinas){
                    Disciplina consultaDisicplina = disciplinaRepository.buscarDisciplinaPorNome(d.getNome());
                    consultaDisicplina.setProfessorResponsavel(null);
                    disciplinaRepository.atualizarDisciplina(consultaDisicplina);
                }
                professorRepository.delete(matricula);
                professorView.mostrarMensagemRemocaoProfessor("\nProfessor removido com sucesso!\n");
            } else {
                professorView.mostrarMensagemRemocaoProfessor("\nProfessor não encontrado!\n");
            }
        } catch (Exception e){
            throw new Exception("Erro ao remover professor" + e.getMessage());
        }
    }

    public void editarProfessor(String matricula) throws Exception {
        try {
            Professor professor = encontrarProfessor(matricula);
            if(professor == null){
                throw new Exception("Professor nao encontrado");
            }
            int opcaoEscolhida = professorView.capturaCampoEditarProfessor(professor);
            if(opcaoEscolhida == 0){
                return;
            } else if (opcaoEscolhida == 3) {
                List<String> nomeDiciplinas = professorView.editarDisciplinas(professor);
                atualizarDisciplinas(nomeDiciplinas, professor);
            } else if (opcaoEscolhida == 4) {
                List<String> nomeProjetoPesquesa = professorView.editarTipoEspecifico(professor);
                atualizacaoCompleta(nomeProjetoPesquesa, professor);
                System.out.println("Projeto de pesquisa do Professor atualizado com sucesso");
            }
            professorRepository.update(professor);
        } catch (Exception e){
            throw new Exception("Erro ao editar professor - "+e.getMessage());
        }
    }

    public Professor encontrarProfessor(String matricula) throws Exception{
        try{
            return professorRepository.findByMatricula(matricula);
        } catch (Exception e){
            throw new Exception("Erro ao encontrar professor");
        }
    }

    public void atualizarDisciplinas(List<String> nomeDisciplinas, Professor professor) throws Exception{
        try{
            if(professor.getTipo() == VITALICIO) {
                if (professor.getDisciplinas().size() >= 3) {
                    throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
                }
            }else {
                if(professor.getDisciplinas().size() >= 2){
                    throw new Exception("Professor substituto nao pode ter mais de 2 disciplinas");
                }
            }
            List<Disciplina> disciplinas = capturaDisciplinas(nomeDisciplinas, professor);
            professor.setDisciplinas(disciplinas);
            professorRepository.update(professor);
            for(Disciplina d : disciplinas){
                d.setProfessorResponsavel(professor);
                disciplinaRepository.atualizarDisciplina(d);
            }
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor - " + e.getMessage());
        }
    }

    public void atualizacaoCompleta(List<String> projetoPesquisa,Professor professor) throws Exception {
        try{
            if(professor instanceof ProfessorVitalicio professorVitalicio){
                List<ProjetoPesquisa> projetos = new ArrayList<>();
                for (String nomeProjeto : projetoPesquisa) {
                    ProjetoPesquisa projeto = projetoPesquisaRepository.buscarPorTitulo(nomeProjeto);
                    if (projeto == null) {
                        throw new Exception("Projeto não encontrado: " + nomeProjeto);
                    }
                    if (projetos.contains(projeto)) {
                        throw new Exception("projeto duplicada: " + projeto);
                    }
                    if(projeto.getOrientador() != null && projeto.getOrientador().getMatricula() != professor.getMatricula()){
                        Professor professor1 = professorRepository.findByMatricula(projeto.getOrientador().getMatricula());
                        ((ProfessorVitalicio) professor1).removerProjeto(projeto);
                        professorRepository.update(professor1);
                    }
                    projetos.add(projeto);
                }
                professorVitalicio.setProjetos(projetos);
                for (ProjetoPesquisa p : professorVitalicio.getProjetos()) {
                    p.setOrientador(professorVitalicio);
                    projetoPesquisaRepository.atualizarProjeto(p);
                }
            }
            professorRepository.update(professor);
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor - "+e.getMessage());
        }
    }

    public void gerarRelatorio() throws Exception {
        try{
            List<Professor> listProfessor = professorRepository.findAll();
            List<Double> listSalario = new ArrayList<>();
            for(Professor professor : listProfessor){
                listSalario.add(calcularSalario(professor.getMatricula()));
            }
            professorView.gerarRelatorioProfessores(listProfessor, listSalario);
        } catch (Exception e){
            throw new Exception("Erro ao gerar relatorio");
        }
    }

    public void calcularSalarioRetorno(String matricula) throws Exception {
        try{
            Double salarioProfessor = calcularSalario(matricula);
            professorView.mostrarSalario(salarioProfessor);
        } catch (Exception e){
            throw new Exception("Erro ao calcular salario do professor - " + e.getMessage());
        }
    }

    private Double calcularSalario(String matricula) throws Exception {
        try{
            Professor professor = encontrarProfessor(matricula);
            if(professor == null){
                throw new Exception("Professor nao encontrado");
            }
            Double salarioProfessor;
            if(professor.getTipo() == VITALICIO){
                salarioProfessor = calcularSalarioProfessorVitalicio(professor.getTitulo(), professor);
            } else{
                ProfessorSubstituto professorSubstituto = (ProfessorSubstituto) professor;
                salarioProfessor = professorSubstituto.calcularSalario(professorSubstituto.getHorasAula());
            }
            return salarioProfessor;
        } catch (Exception e){
            throw new Exception("Erro ao calcular salario - "+e.getMessage());
        }
    }

    private Double calcularSalarioProfessorVitalicio(TituloProfessor titulo, Professor professor) {
        ProfessorVitalicio professorVitalicio = (ProfessorVitalicio) professor;
        Double salarioProfessor = professorVitalicio.calcularSalario(0.0);
        return salarioProfessor;
    }
}
