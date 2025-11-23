package model;

import java.time.LocalDate;
import java.util.List;

public class ProfessorSubstituto extends Professor{
    private Double horasAula;
    private LocalDate tempoDeContrato;

    public ProfessorSubstituto(String nome, String matricula, TituloProfessor titulo, TipoProfessor tipo,
                               List<Disciplina> disciplinas, Double horasAula, LocalDate tempoDeContrato){
        super(nome, matricula, titulo, tipo, disciplinas);
        this.horasAula = horasAula;
        this.tempoDeContrato = tempoDeContrato;
    }

    public void update(ProfessorSubstituto professor){
        this.horasAula = professor.getHorasAula();
        this.tempoDeContrato = professor.getTempoDeContrato();
    }

    public Double getHorasAula() {
        return horasAula;
    }

    public void setHorasAula(Double horasAula) {
        this.horasAula = horasAula;
    }

    public LocalDate getTempoDeContrato() {
        return tempoDeContrato;
    }

    public void setTempoDeContrato(LocalDate tempoDeContrato) {
        this.tempoDeContrato = tempoDeContrato;
    }

    @Override
    public Double calcularSalario(Double horasAula) {
        return horasAula * 80.0;
    }
}
