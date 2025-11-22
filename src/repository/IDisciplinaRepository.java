package repository;

import model.Disciplina;
import java.util.List;

public interface IDisciplinaRepository {

    // ------------------ CRUD --------------------------
    Disciplina buscarDisciplinaPorNome(String nome);
    Disciplina buscarDisciplinaPorId(int id);
    Disciplina adicionarDisciplina(Disciplina disciplina);

    // Atualiza a disciplina existente. Usamos 'void' ou retornamos a Disciplina
    void atualizarDisciplina(Disciplina disciplina);

    // O Repositório deve receber o ID para remover. Usamos 'boolean' para indicar sucesso/falha.
    boolean removerDisciplina(int id);

    // ------------------ Listas --------------------------
    List<Disciplina> listarDisciplinas();

    // Lista as matrículas dos alunos em uma disciplina específica (consistente com o Repository)
    List<String> listarMatriculasAlunosPorDisciplina(int idDisciplina);

    // ------------------ Aluno (Gerenciamento de Matrícula) --------------------------
    // Os métodos agora lidam com strings (matrículas) e retornam a disciplina atualizada
    Disciplina matricularAluno(int idDisciplina, String matriculaAluno);
    Disciplina removerAluno(int idDisciplina, String matriculaAluno);
}