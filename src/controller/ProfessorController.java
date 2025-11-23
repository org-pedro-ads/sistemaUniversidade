package controller;

import model.*;
import repository.ProfessorRepository;
import repository.DisciplinaRepository;
import repository.ProjetoPesquisaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfessorController {
    private static final ProfessorRepository professorRepository = ProfessorRepository.getInstance();
    private static final DisciplinaRepository disciplinaRepository = DisciplinaRepository.getInstance();
    private static final ProjetoPesquisaRepository projetoPesquisaRepository = new ProjetoPesquisaRepository();
    public void cadastrarProfessorVitalicio(
            String nome,
            String matricula,
            TituloProfessor titulo,
            TipoProfessor tipo, int quantDisciplinas, List<String> nomeDisciplinas,
            List<String> projetoPesquisa, Double salarioBase) throws Exception {
        try {
            //add validacao de disciplina
            if(quantDisciplinas > 3){
                throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
            }

            List<Disciplina> disciplinas = new ArrayList<>();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                if (consultarDisciplina == null) {
                    throw new Exception("Disciplina nao encontrada: " + nomeDisciplina);
                }
                if (disciplinas.contains(consultarDisciplina)) {
                    throw new Exception("Disciplina duplicada: " + nomeDisciplina);
                }
                disciplinas.add(consultarDisciplina);
            }

            List<ProjetoPesquisa> projetos = new ArrayList<>();
            for (String nomeProjeto : projetoPesquisa) {
                ProjetoPesquisa projeto = projetoPesquisaRepository.buscarPorTitulo(nomeProjeto);
                if (projeto == null) {
                    throw new Exception("Projeto não encontrado: " + nomeProjeto);
                }
                projetos.add(projeto);
            }

            Professor professorVitalicio = new ProfessorVitalicio(nome, matricula, titulo, tipo, disciplinas, projetos, salarioBase);
            for (ProjetoPesquisa p : projetos) {
                p.setOrientador(professorVitalicio);
                projetoPesquisaRepository.atualizarProjeto(p);
            }

            for(Disciplina d : disciplinas){
                d.setProfessorResponsavel(professorVitalicio);
                disciplinaRepository.atualizarDisciplina(d);
            }
            professorRepository.save(professorVitalicio);
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor: " +  e.getMessage());
        }
    }

    public void cadastroProfessorSubstituto(String nome, String matricula, TituloProfessor titulo,
                                            TipoProfessor tipo, int quantDisciplinas, List<String> nomeDisciplinas,
                                            double horasSemana, LocalDate dataTerminoContrato) throws Exception {
        try {
            //add validacao de disciplina
            if(quantDisciplinas > 2){
                throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
            }
            List<Disciplina> disciplinas = new ArrayList<>();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                if (consultarDisciplina == null) {
                    throw new Exception("Disciplina nao encontrada: " + nomeDisciplina);
                }
                if (disciplinas.contains(consultarDisciplina)) {
                    throw new Exception("Disciplina duplicada: " + nomeDisciplina);
                }
                disciplinas.add(consultarDisciplina);
            }
            Professor professorSubstituto = new ProfessorSubstituto(nome, matricula, titulo, tipo, disciplinas, horasSemana, dataTerminoContrato);
            professorRepository.save(professorSubstituto);
            for(Disciplina d : disciplinas){
                d.setProfessorResponsavel(professorSubstituto);
                disciplinaRepository.atualizarDisciplina(d);
            }
            return;
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor");
        }

    }

    public List<Professor> listarProfessores() throws Exception{
        try{
            List<Professor> listProfessor = professorRepository.findAll();
            return listProfessor;
        } catch (Exception e){
            throw new Exception("Erro ao listar professores");
        }
    }

    public boolean removerProfessor(String matricula) throws Exception{
        try{
            if(encontrarProfessor(matricula) != null){
                professorRepository.delete(matricula);
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            throw new Exception("Erro ao remover professor");
        }
    }

    public Professor encontrarProfessor(String matricula) throws Exception{
        try{
            return professorRepository.findByMatricula(matricula);
        } catch (Exception e){
            throw new Exception("Erro ao encontrar professor");
        }
    }

    public void atualizarDisciplinas(int qunatDisciplinas, List<String> nomeDisciplinas, Professor professor) throws Exception{
        try{
            int quantidadeDisciplinasAtual = professor.getDisciplinas().size() + qunatDisciplinas;
            if(professor.getTipo() == TipoProfessor.VITALICIO) {
                if (quantidadeDisciplinasAtual > 3) {
                    throw new Exception("Professor vitalicio nao pode ter menos de 3 disciplinas");
                }
            }else {
                if(quantidadeDisciplinasAtual > 2){
                    throw new Exception("Professor vitalicio nao pode ter mais de 2 disciplinas");
                }
            }
            List<Disciplina> disciplinas = professor.getDisciplinas();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                if (consultarDisciplina == null) {
                    throw new Exception("Disciplina nao encontrada: " + nomeDisciplina);
                }
                if (disciplinas.contains(consultarDisciplina)) {
                    throw new Exception("Disciplina duplicada: " + nomeDisciplina);
                }
                disciplinas.add(consultarDisciplina);
            }
            professor.setDisciplinas(disciplinas);
            professorRepository.update(professor);
            for(Disciplina d : disciplinas){
                d.setProfessorResponsavel(professor);
                disciplinaRepository.atualizarDisciplina(d);
            }
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor");
        }
    }

    public void atualizacaoCompleta(Professor professor) throws Exception {
        try{
            professorRepository.update(professor);
            if(professor instanceof ProfessorVitalicio){
                ProfessorVitalicio professorVitalicio = (ProfessorVitalicio) professor;
                for (ProjetoPesquisa p : professorVitalicio.getProjetos()) {
                    p.setOrientador(professorVitalicio);
                    projetoPesquisaRepository.atualizarProjeto(p);
                }
            }
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor");
        }
    }

    public Double calcularSalario(String matricula) throws Exception {
        try{
            Professor professor = encontrarProfessor(matricula);
            if(professor == null){
                throw new Exception("Professor nao encontrado");
            }
            Double salarioProfessor;
            if(professor.getTipo() == TipoProfessor.VITALICIO){
                salarioProfessor = calcularSalarioProfessorVitalicio(professor.getTitulo(), professor);
            } else{
                ProfessorSubstituto professorSubstituto = (ProfessorSubstituto) professor;
                salarioProfessor = professor.calcularSalario(professorSubstituto.getHorasAula());
            }
            return salarioProfessor;
        } catch (Exception e){
            throw new Exception("Erro ao calcular salario do professor");
        }
    }

    private Double calcularSalarioProfessorVitalicio(TituloProfessor titulo, Professor professor) {
        Double salarioProfessor = professor.calcularSalario(0.0);
        if(titulo == TituloProfessor.DOUTORADO){
            salarioProfessor = salarioProfessor + (salarioProfessor*0.2);
        }
        return salarioProfessor;
    }
}
