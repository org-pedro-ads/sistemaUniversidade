package controller;

import model.*;
import repository.DisciplinaRepository;
import repository.ProfessorRepository;
import repository.ProjetoPesquisaRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfessorController {
    private ProfessorRepository professorRepository;
    private DisciplinaRepository disciplinaRepository;
    private ProjetoPesquisaRepository projetoPesquisaRepository;
    public void cadastrarProfessorVitalicio(
            String nome,
            String matricula,
            TituloProfessor titulo,
            TipoProfessor tipo, int quantDisciplinas, List<String> nomeDisciplinas,
            String projetoPesquisa, Double salarioBase) throws Exception {
        try {
            //add validacao de disciplina
            if(quantDisciplinas > 3){
                throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
            }
            List<Disciplina> disciplinas = new ArrayList<>();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                disciplinas.add(consultarDisciplina);
            }
            ProjetoPesquisa projetoPesquisaCompleto = projetoPesquisaRepository.buscarPorTitulo(projetoPesquisa);
            Professor professorVitalicio = new ProfessorVitalicio(nome, matricula, titulo, tipo, disciplinas, projetoPesquisaCompleto, salarioBase);
            professorRepository.save(professorVitalicio);
            return;
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor");
        }
    }

    public void cadastroProfessorSubstituto(String nome, String matricula, TituloProfessor titulo,
                                            TipoProfessor tipo, int quantDisciplinas, List<String> nomeDisciplinas,
                                            int horasSemana, String dataTerminoContrato) throws Exception {
        try {
            //add validacao de disciplina
            if(quantDisciplinas > 2){
                throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
            }
            List<Disciplina> disciplinas = new ArrayList<>();
            for(String nomeDisciplina : nomeDisciplinas){
                Disciplina consultarDisciplina = disciplinaRepository.buscarDisciplinaPorNome(nomeDisciplina);
                disciplinas.add(consultarDisciplina);
            }
            Professor professorSubstituto = new ProfessorSubstituto(nome, matricula, titulo, tipo, disciplinas, horasSemana, dataTerminoContrato);
            professorRepository.save(professorSubstituto);
            return;
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor");
        }

    }

    public List<Professor> listarTodosProfessores() throws Exception{
        try{
            List<Professor> listProfessor = professorRepository.findAll();
            return listProfessor;
        } catch (Exception e){
            throw new Exception("Erro ao listar professores");
        }
    }

    public Professor encontrarProfessor(String matricula) throws Exception{
        try{
            return professorRepository.findByMatricula(matricula);
        } catch (Exception e){
            throw new Exception("Erro ao encontrar professor");
        }
    }

    public void atualizarProfessor(Professor professor) throws Exception{
        try{
            professorRepository.update(professor);
            return;
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor");
        }
    }
    public Double calcularSalario(String matricula) throws Exception {
        try{
            Professor professor = encontrarProfessor(matricula);
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
