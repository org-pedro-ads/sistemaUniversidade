package model;

import java.util.List;

import static model.TituloProfessor.DOUTORADO;

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
        if(getTitulo() == DOUTORADO){
            salarioBase = salarioBase + (salarioBase*0.2);
        }
        return salarioBase;
    }

    public void adicionarProjeto(ProjetoPesquisa p) {
        if (!projetos.contains(p)) {
            projetos.add(p);
        }
    }

    public void removerProjeto(ProjetoPesquisa p) {
        projetos.removeIf(d -> d.getTitulo().equals(p.getTitulo()));
    }

    public List<ProjetoPesquisa> listarProjetos() {
        return projetos;
    }
}
