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
}
