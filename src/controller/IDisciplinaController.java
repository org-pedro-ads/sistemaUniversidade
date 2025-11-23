package controller;

import model.Alunos;
import model.Disciplina;

import java.util.List;

public interface IDisciplinaController {

    // ------------- CRUD / GESTÃO --------------------

    /** Processa a entrada da View, valida e envia ao Repositório. */
    Disciplina adicionarDisciplina() throws Exception;

    /** Remove a disciplina e trata as exceções/regras de negócio. */
    void removerDisciplina(int id) throws Exception;
    void removerDisciplina() throws Exception;

    /** Atualiza o nome da disciplina (operação de edição). */
    Disciplina atualizarNome(int id, String novoNome) throws Exception;
    Disciplina atualizarNome() throws Exception;

    /** Atualiza a carga horária da disciplina (operação de edição). */
    Disciplina atualizarCargaHoraria(int id, int novaCargaHoraria) throws Exception;
    Disciplina atualizarCargaHoraria() throws Exception;

    // ------------- BUSCAS (GETTERS) --------------------

    /** Busca uma disciplina pelo seu identificador. */
    Disciplina buscarDisciplinaPorId(int id) throws Exception;

    /** Busca uma disciplina pelo seu nome. */
    Disciplina buscarDisciplinaPorNome(String nome) throws Exception;

    /** Retorna a lista de todas as disciplinas registradas. */
    List<Disciplina> listarDisciplinas() throws Exception;

    /** Retorna a lista de alunos (objetos Alunos) matriculados na disciplina. */
    List<Alunos> listarAlunosMatriculados(int idDisciplina) throws Exception;
    List<Alunos> listarAlunosMatriculados() throws Exception;

    /** Retorna o cálculo ou o relatório de popularidade (Lógica de Negócio).
     * O retorno List<Disciplina> é mantido por consistência com o original.
     */
    List<Alunos> listarAlunosInteressados(int idDisciplina) throws Exception;
    List<Alunos> listarAlunosInteressados() throws Exception;

    // ------------- VÍNCULOS E PROFESSOR --------------------

    /** Atribui o professor responsável à disciplina (operação de edição). */
    public Disciplina atualizarProfessorResponsavel(int id, String matriculaProfessor) throws Exception;
    public Disciplina atualizarProfessorResponsavel() throws Exception;

    /** Matricula um aluno na disciplina (Lógica de Negócio). */
    Disciplina matricularAlunoDisciplina(int idDisciplina, String matriculaAluno) throws Exception;
    Disciplina matricularAlunoDisciplina() throws Exception;

    /** Desmatricula um aluno da disciplina (Lógica de Negócio). */
    Disciplina desmatricularAlunoDisciplina(int idDisciplina, String matriculaAluno) throws Exception;
    Disciplina desmatricularAlunoDisciplina() throws Exception;

    /** Registra o interesse do aluno na disciplina eletiva (Lógica de Negócio). */
    Disciplina declararInteresseDisciplina(int idDisciplina, String matriculaAluno) throws  Exception;
    Disciplina declararInteresseDisciplina() throws  Exception;

    /** Remove o interesse do aluno na disciplina eletiva (Lógica de Negócio). */
    Disciplina removerInteresseDisciplina(int idDisciplina, String matriculaAluno) throws  Exception;
    Disciplina removerInteresseDisciplina() throws  Exception;

    // ------------- RELATÓRIOS --------------------

    /** Gera e exibe o relatório formatado das disciplinas. */
    void gerarRelatorioDisciplinas();
}
