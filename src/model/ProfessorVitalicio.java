package model;

import java.util.List;

public class ProfessorVitalicio extends Professor{
    private List<ProjetoPesquisa> projetos;

    public ProfessorVitalicio(String nome, String matricula, TituloProfessor titulo, TipoProfessor tipo,
                               List<Disciplina> disciplinas, List<ProjetoPesquisa> projetos){
        super(nome, matricula, titulo, tipo, disciplinas);
        this.projetos = projetos;
    }

    @Override
    public Double calcularSalario(Double horasTrabalhadas) {
        return 0.0;
    }

    @Override
    public void adicionarDisciplina(Disciplina disciplina) {

    }

    @Override
    public void removerDisciplina(Disciplina disciplina) {

    }

    public List<ProjetoPesquisa> listarProjetos(){
        return null;
    }
}
