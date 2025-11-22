package model;

import java.util.List;

public class DisciplinaEletiva extends Disciplina{
    private List<String> interessados;

    public DisciplinaEletiva(
            int id,
            String nome,
            int cargaHoraria,
            Professor professorResponsavel,
            List<String> alunos,
            List<String> interessados
    ){
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.professorResponsavel = professorResponsavel;
        this.alunos = alunos;
        this.interessados = interessados;
    }

    @Override
    public String getTipo() {
        return "Eletiva";
    }

    public void setListaAlunosInteressados(List<String> lista) {
        this.interessados = lista;
    }

    public List<String> listarInteressados(){
        return interessados;
    }
}
