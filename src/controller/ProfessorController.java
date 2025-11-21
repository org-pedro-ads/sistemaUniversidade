package controller;

import model.*;
import repository.DisciplinaRepository;
import repository.ProfessorRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfessorController {
    private ProfessorRepository professorRepository;
    private DisciplinaRepository disciplinaRepository;
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
            Professor professor = new ProfessorVitalicio(nome, matricula, titulo, tipo, disciplinas, projetoPesquisa, salarioBase);
            professorRepository.save();
            return;
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor");
        }
    }

    public void cadastroProfessorSubstituto(String nome, String matricula, TituloProfessor titulo,
                                            TipoProfessor tipo, int quantDisciplinas, List<String> nomeDisciplina,
                                            int horasSemana, String dataTerminoContrato) throws Exception {
        try {
            //add validacao de disciplina
            if(quantDisciplinas > 3){
                throw new Exception("Professor vitalicio nao pode ter mais de 3 disciplinas");
            }

            repository.save();
            return;
        } catch (Exception e){
            throw new Exception("Erro ao cadastrar professor");
        }

    }

    public List<Professor> listarTodosProfessores() throws Exception{
        try{
            List<Professor> listProfessor = repository.findAll();
            return listProfessor;
        } catch (Exception e){
            throw new Exception("Erro ao listar professores");
        }
    }

    public Professor encontrarProfessor(String matricula) throws Exception{
        try{
            return repository.findByMatricula(matricula);
        } catch (Exception e){
            throw new Exception("Erro ao encontrar professor");
        }
    }

    public void atualizarProfessor(Professor professor) throws Exception{
        try{
            repository.update(professor);
            return;
        } catch (Exception e){
            throw new Exception("Erro ao atualizar professor");
        }
    }
}
