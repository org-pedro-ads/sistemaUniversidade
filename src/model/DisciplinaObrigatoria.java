package model;

import java.util.List;

public class DisciplinaObrigatoria extends Disciplina {

    public DisciplinaObrigatoria(
            int id,
            String nome,
            int cargaHoraria,
            Professor professorResponsavel,
            List<String> alunos
    ) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.professorResponsavel = professorResponsavel;
        this.alunos = alunos;
    }

    @Override
    public String getTipo() {
        return "Obrigatoria";
    }
}
