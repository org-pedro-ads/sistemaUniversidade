package controller;

import model.*;
import repository.DisciplinaRepository;
import view.DisciplinaView;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController implements IDisciplinaController {

    private final DisciplinaRepository disciplinaRepository;
    private final DisciplinaView disciplinaView;
    private final ProfessorController professorController;
    private final AlunoController alunoController;

    public DisciplinaController(
            DisciplinaRepository disciplinaRepository,
            DisciplinaView disciplinaView,
            ProfessorController professorController,
            AlunoController alunoController
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaView = disciplinaView;
        this.professorController = professorController;
        this.alunoController = alunoController;
    }

    // ==================================================================================
    // METODOS AUXILIARES (Private)
    // ==================================================================================

    /**
     * Auxiliar: Solicita um ID a View e converte para int.
     * Lanca excecao se nao for numero.
     */
    private int lerIdInteiro(String mensagem) throws NumberFormatException {
        String input = this.disciplinaView.getInfo(mensagem);
        return Integer.parseInt(input);
    }

    /**
     * Auxiliar: Busca disciplina e lanca erro se nao existir.
     */
    public boolean validarExistenciaDisciplina(int id) {
        Disciplina disciplina = this.disciplinaRepository.buscarDisciplinaPorId(id);
        return disciplina != null;
    }

    // ==================================================================================
    // CRIACAO (Create) - Loop e try-catch removidos
    // ==================================================================================

    @Override
    public Disciplina adicionarDisciplina() throws Exception {
        this.disciplinaView.print(" ================= Cadastro Disciplina ================ \n");

        // Coleta de dados - Exceções de NumberFormatException agora são lançadas por lerIdInteiro
        String nome = this.disciplinaView.getInfo("\nDigite o nome da disciplina: ");
        int cargaHoraria = lerIdInteiro("\nDigite a carga-horaria da disciplina: ");
        String matriculaProf = this.disciplinaView.getInfo("\nDigite a matricula do professor responsavel: ");
        int tipo = lerIdInteiro("\nDigite o tipo (1 - Obrigatoria, 2 - Eletiva): ");

        // Validacoes de Negócio
        Professor professor = this.professorController.encontrarProfessor(matriculaProf);
        if (professor == null) {
            throw new Exception("Professor com matricula " + matriculaProf + " nao encontrado.");
        }

        if (tipo != 1 && tipo != 2) {
            throw new Exception("Tipo invalido. Use 1 (Obrigatoria) ou 2 (Eletiva).");
        }
        if (nome.length() < 5) {
            throw new Exception("Nome deve conter ao menos 5 caracteres.");
        }
        if (cargaHoraria < 10) {
            throw new Exception("Carga horaria deve ser no minimo 10 horas.");
        }

        // Instanciacao
        Disciplina novaDisciplina;
        List<String> listaVazia = new ArrayList<>();

        if (tipo == 1) {
            novaDisciplina = new DisciplinaObrigatoria(0, nome, cargaHoraria, professor, listaVazia);
        } else {
            novaDisciplina = new DisciplinaEletiva(0, nome, cargaHoraria, professor, listaVazia, new ArrayList<>());
        }

        // Persistencia
        novaDisciplina = this.disciplinaRepository.adicionarDisciplina(novaDisciplina);

        this.disciplinaView.print("\nDisciplina '" + nome + "' adicionada com sucesso! (ID: " + novaDisciplina.getId() + ")\n");
        return novaDisciplina;
    }

    // ==================================================================================
    // REMOCAO (Delete) - Loop e try-catch removidos
    // ==================================================================================

    @Override
    public void removerDisciplina(int id) throws Exception {
        try {
            if(validarExistenciaDisciplina(id)) {
                this.disciplinaRepository.removerDisciplina(id);
            }
        } catch (Exception ex) {
            throw new Exception("Erro ao tentar remover Disciplina: " + ex.getMessage());
        }

    }

    @Override
    public void removerDisciplina() throws Exception {
        this.disciplinaView.print(" ================= Remover Disciplina ================ \n");
        // Lança NumberFormatException se o input não for número
        int id = lerIdInteiro("\nDigite o ID da disciplina a ser removida: ");

        this.removerDisciplina(id); // Lança Exception se não existir

        this.disciplinaView.print("\nDisciplina " + id + " removida com sucesso.\n");
    }

    // ==================================================================================
    // LEITURA (Read / Getters)
    // ==================================================================================

    @Override
    public Disciplina buscarDisciplinaPorId(int id) {
        return this.disciplinaRepository.buscarDisciplinaPorId(id);
    }

    @Override
    public Disciplina buscarDisciplinaPorNome(String nome) {
        return this.disciplinaRepository.buscarDisciplinaPorNome(nome);
    }

    @Override
    public List<Disciplina> listarDisciplinas() {
        List<Disciplina> lista = this.disciplinaRepository.listarDisciplinas();

        if (lista == null) {
            lista = new ArrayList<>();
        }

        if (lista.isEmpty()) {
            this.disciplinaView.print("\nNenhuma disciplina cadastrada.\n");
            return lista;
        }

        this.disciplinaView.printRelatorios(lista);
        return lista;
    }

    // --- Listar Alunos Matriculados ---

    @Override
    public List<Alunos> listarAlunosMatriculados(int id) {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        List<Alunos> alunosObj = new ArrayList<>();

        if (disciplina.getAlunos() != null) {
            for (String matricula : disciplina.getAlunos()) {
                Alunos a = this.alunoController.encontrarAluno(matricula);
                if (a != null) alunosObj.add(a);
            }
        }
        return alunosObj;
    }

    @Override
    public List<Alunos> listarAlunosMatriculados() { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Listar alunos matriculados ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");
        return this.listarAlunosMatriculados(id);
    }

    // --- Listar Alunos Interessados (Eletiva) ---

    @Override
    public List<Alunos> listarAlunosInteressados(int id) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);

        if (!(disciplina instanceof DisciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
        }

        List<Alunos> alunosObj = new ArrayList<>();
        // Correção na chamada (mantendo o código original, que usa o repository com o ID)
        List<String> matriculas = this.disciplinaRepository.listarMatriculasAlunosInteressados(id);

        if (matriculas != null) {
            for (String m : matriculas) {
                Alunos a = this.alunoController.encontrarAluno(m);
                if (a != null) alunosObj.add(a);
            }
        }
        return alunosObj;
    }

    @Override
    public List<Alunos> listarAlunosInteressados() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Listar alunos interessados ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");
        return this.listarAlunosInteressados(id);
    }

    // ==================================================================================
    // ATUALIZACAO - DADOS BASICOS
    // ==================================================================================

    @Override
    public Disciplina alterarDisciplina(Disciplina disciplina) {
        this.validarExistenciaDisciplina(disciplina.getId());
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarNome(int id, String nome) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);

        if (nome.length() < 3) throw new Exception("Nome muito curto (min 3 chars).");

        disciplina.setNome(nome);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarNome() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Alterar nome de disciplina ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");
        if(!validarExistenciaDisciplina(id)) { throw new Exception("Erro: Disciplina com id " + id + " nao encontrada"); }

        String novoNome = this.disciplinaView.getInfo("Digite o novo nome: ");

        return this.atualizarNome(id, novoNome);
    }

    @Override
    public Disciplina atualizarCargaHoraria(int id, int cargaHoraria) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        if (cargaHoraria < 10) throw new Exception("Carga horaria deve ser ao menos 10 horas.");

        disciplina.setCargaHoraria(cargaHoraria);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarCargaHoraria() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Alterar carga horaria ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");

        int carga = lerIdInteiro("Digite a nova carga horaria: ");

        return this.atualizarCargaHoraria(id, carga); // Lança exceção se ID ou carga for inválida
    }

    // ==================================================================================
    // ATUALIZACAO - PROFESSOR - Loop e try-catch removidos
    // ==================================================================================

    @Override
    public Disciplina atualizarProfessorResponsavel(int id, String matriculaProfessor) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

        if (professor == null) {
            throw new Exception("Professor com matricula '" + matriculaProfessor + "' nao encontrado.");
        }

        disciplina.setProfessorResponsavel(professor);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarProfessorResponsavel() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Alterar Professor ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");

        String mat = this.disciplinaView.getInfo("Digite a matricula do novo professor: ");

        Disciplina d = this.atualizarProfessorResponsavel(id, mat);

        this.disciplinaView.print("\nProfessor atualizado: " + d.getProfessorResponsavel().getNome() + "\n");
        return d;
    }

    // ==================================================================================
    // GERENCIAMENTO DE ALUNOS (Matricula) - Loop e try-catch removidos
    // ==================================================================================

    @Override
    public Disciplina matricularAlunoDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno com matricula " + matricula + " nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();
        if (alunosMatriculados == null) alunosMatriculados = new ArrayList<>();

        if (alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno ja esta matriculado nesta disciplina.");
        }

        alunosMatriculados.add(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    @Override
    public Disciplina matricularAlunoDisciplina() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Matricular Aluno ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");
        String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

        Disciplina d = this.matricularAlunoDisciplina(id, mat);

        this.disciplinaView.print("\nMatricula realizada com sucesso em " + d.getNome() + "\n");
        return d;
    }

    @Override
    public Disciplina desmatricularAlunoDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();

        if (alunosMatriculados == null || !alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno nao esta matriculado nesta disciplina.");
        }

        alunosMatriculados.remove(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    @Override
    public Disciplina desmatricularAlunoDisciplina() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Desmatricular Aluno ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina: ");
        String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

        Disciplina d = this.desmatricularAlunoDisciplina(id, mat);

        this.disciplinaView.print("\nAluno removido da disciplina com sucesso.\n");
        return d;
    }

    // ==================================================================================
    // GERENCIAMENTO DE INTERESSE (Eletivas) - Loop e try-catch removidos
    // ==================================================================================

    @Override
    public Disciplina declararInteresseDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        // Pattern Matching (Java 16+)
        if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas aceitam interesse.");
        }

        List<String> interessados = disciplinaEletiva.listarInteressados();
        if (interessados == null) interessados = new ArrayList<>();

        if (interessados.contains(matricula)) {
            throw new Exception("Aluno ja registrou interesse.");
        }

        interessados.add(matricula);
        disciplinaEletiva.setListaAlunosInteressados(interessados);
        this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

        return disciplinaEletiva;
    }

    @Override
    public Disciplina declararInteresseDisciplina() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Declarar Interesse ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina eletiva: ");
        String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

        Disciplina d = this.declararInteresseDisciplina(id, mat);

        this.disciplinaView.print("\nInteresse registrado com sucesso!\n");
        return d;
    }

    @Override
    public Disciplina removerInteresseDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = buscarDisciplinaPorId(id);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        if (!(disciplina instanceof DisciplinaEletiva disciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
        }

        List<String> interessados = disciplinaEletiva.listarInteressados();

        if (interessados == null || !interessados.contains(matricula)) {
            throw new Exception("Aluno nao esta na lista de interessados.");
        }

        interessados.remove(matricula);
        disciplinaEletiva.setListaAlunosInteressados(interessados);
        this.disciplinaRepository.atualizarDisciplina(disciplinaEletiva);

        return disciplinaEletiva;
    }

    @Override
    public Disciplina removerInteresseDisciplina() throws Exception { // Alterado para lançar exceção
        this.disciplinaView.print(" ================= Remover Interesse ================ \n");

        int id = lerIdInteiro("\nDigite o ID da disciplina eletiva: ");
        String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

        Disciplina d = this.removerInteresseDisciplina(id, mat);

        this.disciplinaView.print("\nInteresse removido com sucesso!\n");
        return d;
    }

    // ==================================================================================
    // RELATORIOS (Mantido)
    // ==================================================================================

    @Override
    public void gerarRelatorioDisciplinas() {
        try {
            this.disciplinaView.print(" ================= Relatorio Geral ================ \n");
            // ... (lógica mantida)
            List<Disciplina> disciplinas = this.disciplinaRepository.listarDisciplinas();

            if (disciplinas == null || disciplinas.isEmpty()) {
                this.disciplinaView.print("\nNenhuma disciplina cadastrada.\n");
                return;
            }

            String header = String.format("%-4s | %-25s | %-12s | %-20s | %s",
                    "ID", "NOME", "TIPO", "PROFESSOR", "ALUNOS");

            this.disciplinaView.print(header);
            this.disciplinaView.print("------------------------------------------------------------------------------------------");

            for (Disciplina d : disciplinas) {
                String tipo = (d instanceof DisciplinaEletiva) ? "Eletiva" : "Obrigatoria";
                String nomeProf = (d.getProfessorResponsavel() != null) ? d.getProfessorResponsavel().getNome() : "N/D";
                int qtdAlunos = (d.getAlunos() != null) ? d.getAlunos().size() : 0;

                String linha = String.format("%-4d | %-25s | %-12s | %-20s | %d matriculados",
                        d.getId(),
                        formatarString(d.getNome(), 25),
                        tipo,
                        formatarString(nomeProf, 20),
                        qtdAlunos
                );

                this.disciplinaView.print(linha);

                if (d instanceof DisciplinaEletiva eletiva) {
                    int qtdInteressados = (eletiva.listarInteressados() != null) ? eletiva.listarInteressados().size() : 0;
                    if (qtdInteressados > 0) {
                        this.disciplinaView.print(String.format("      -> Fila de Interesse: %d alunos", qtdInteressados));
                    }
                }
            }
            this.disciplinaView.print("\nTotal de registros: " + disciplinas.size() + "\n");

        } catch (Exception e) {
            this.disciplinaView.print("Erro ao gerar relatorio: " + e.getMessage());
        }
    }

    private String formatarString(String texto, int tamanhoMax) {
        if (texto == null) return "";
        if (texto.length() > tamanhoMax) {
            return texto.substring(0, tamanhoMax - 3) + "...";
        }
        return texto;
    }
}