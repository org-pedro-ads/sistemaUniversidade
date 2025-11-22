package controller;

import model.Alunos;
import model.Disciplina;

import java.util.List;

public interface IDisciplinaController {

    // ------------- CRUD / GESTÃO --------------------

    /** Processa a entrada da View, valida e envia ao Repositório. */
    Disciplina adicionarDisciplina();

    /** Remove a disciplina e trata as exceções/regras de negócio. */
    void removerDisciplina(int id) throws Exception;

    /** Atualiza o nome da disciplina (operação de edição). */
    Disciplina atualizarNome(int id, String novoNome);

    /** Atualiza a carga horária da disciplina (operação de edição). */
    Disciplina atualizarCargaHoraria(int id, int novaCargaHoraria);

    // ------------- BUSCAS (GETTERS) --------------------

    /** Busca uma disciplina pelo seu identificador. */
    Disciplina buscarPorId(int id);

    /** Busca uma disciplina pelo seu nome. */
    Disciplina buscarPorNome(String nome);

    /** Retorna a lista de todas as disciplinas registradas. */
    List<Disciplina> listarTodas();

    /** Retorna a lista de alunos (objetos Alunos) matriculados na disciplina. */
    List<Alunos> listarAlunosMatriculados(int idDisciplina);

    /** Retorna o cálculo ou o relatório de popularidade (Lógica de Negócio).
     * O retorno List<Disciplina> é mantido por consistência com o original.
     */
    List<Disciplina> calcularPopularidade(int idDisciplina);

    // ------------- VÍNCULOS E PROFESSOR --------------------

    /** Atribui o professor responsável à disciplina (operação de edição). */
    Disciplina atribuirProfessorResponsavel(int idDisciplina, String matriculaProfessor);

    /** Matricula um aluno na disciplina (Lógica de Negócio). */
    Disciplina matricularAluno(int idDisciplina, String matriculaAluno);

    /** Desmatricula um aluno da disciplina (Lógica de Negócio). */
    Disciplina desmatricularAluno(int idDisciplina, String matriculaAluno);

    /** Registra o interesse do aluno na disciplina eletiva (Lógica de Negócio). */
    Disciplina registrarInteresse(int idDisciplina, String matriculaAluno);

    /** Remove o interesse do aluno na disciplina eletiva (Lógica de Negócio). */
    Disciplina removerInteresse(int idDisciplina, String matriculaAluno);

    // ------------- RELATÓRIOS --------------------

    /** Gera e exibe o relatório formatado das disciplinas. */
    void gerarRelatorioDisciplinas();
}