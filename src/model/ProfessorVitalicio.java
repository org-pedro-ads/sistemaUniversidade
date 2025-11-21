package model;

import java.util.List;

public class ProfessorVitalicio extends Professor{
    private List<ProjetoPesquisa> projetos;
    private Double salarioBase;

    public ProfessorVitalicio(String nome, String matricula, TituloProfessor titulo, TipoProfessor tipo,
                               List<Disciplina> disciplinas, List<ProjetoPesquisa> projetos, Double salarioBase){
        super(nome, matricula, titulo, tipo, disciplinas);
        this.projetos = projetos;
        this.salarioBase = salarioBase;
    }

    public List<ProjetoPesquisa> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<ProjetoPesquisa> projetos) {
        this.projetos = projetos;
    }

    public Double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(Double salarioBase) {
        this.salarioBase = salarioBase;
    }

    @Override
    public Double calcularSalario(Double horasTrabalhadas) {
        return 0.0;
    }

    public List<ProjetoPesquisa> listarProjetos(){
        return null;
    }
}
