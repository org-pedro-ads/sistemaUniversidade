package repository;

import model.Disciplina;
import java.util.List;

public interface IDisciplinaRepository {

    // ------------------ CRUD --------------------------
    Disciplina buscarDisciplinaPorNome(String nome);
    Disciplina buscarDisciplinaPorId(int id);
    Disciplina adicionarDisciplina(Disciplina disciplina);
    void atualizarDisciplina(Disciplina disciplina);
    boolean removerDisciplina(int id) ;

    // ------------------ Listas --------------------------
    List<Disciplina> listarDisciplinas();

    // Lista as matrículas dos alunos em uma disciplina específica
    List<String> listarMatriculasAlunosPorDisciplina(int idDisciplina);

    // Listar as matriculas dos alunos que tem interesse em uma disciplina eletiva especifica
    List<String> listarMatriculasAlunosInteressados(int idDisciplina) throws Exception;

    // ------------------ Aluno (Gerenciamento de Matrícula) --------------------------
    // Os métodos agora lidam com strings (matrículas) e retornam a disciplina atualizada
    Disciplina matricularAluno(int idDisciplina, String matriculaAluno);
    Disciplina removerAluno(int idDisciplina, String matriculaAluno);
}