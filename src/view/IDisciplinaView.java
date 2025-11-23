package view;

import model.Alunos;
import model.Disciplina;

import java.util.List;

/**
 * Interface que define as operações de visualização (entrada e saída de dados)
 * relacionadas à entidade Disciplina no sistema.
 */
public interface IDisciplinaView {

    // ----------------- Funcoes genericas ---------------------
    // Métodos de utilidade para comunicação com o usuário

    void print(String s);
    String getInfo(String message);

    // Método utilitário para obter inteiros de forma segura (Adicionado por padronização)
    int getIntInfo(String message);

    // ----------------- Prints especificos ---------------------
    // Métodos para formatar e exibir dados complexos

    void printDisciplina(Disciplina disciplina);
    void printAlunos(Disciplina disciplina, List<Alunos> alunos);

    // ----------------- Relatorios ---------------------
    // Métodos para exibição de relatórios

    void printRelatorios(List<Disciplina> disciplina);

    // ----------------- Funcoes de Menu (Fluxo de Operações) ---------------------
    // Métodos que iniciam ou gerenciam o fluxo de ações da interface de usuário

    void adicionarDisciplina() throws Exception;
    void listarDisciplinas() throws Exception;
    void editarDisciplina() throws Exception;
    void removerDisciplina() throws Exception;
    void listarAlunosMatriculados() throws Exception;
}