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
    // METODOS AUXILIARES (Private) - Para evitar repeticao de codigo
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
     * Usado pelos metodos logicos.
     */
    public Disciplina validarExistenciaDisciplina(int id) throws Exception {
        Disciplina disciplina = this.disciplinaRepository.buscarDisciplinaPorId(id);
        if (disciplina == null) {
            throw new Exception("Disciplina com ID " + id + " nao encontrada.");
        }
        return disciplina;
    }

    // ==================================================================================
    // CRIACAO (Create)
    // ==================================================================================

    @Override
    public Disciplina adicionarDisciplina() {
        while (true) {
            try {
                this.disciplinaView.print(" ================= Cadastro Disciplina ================ \n");

                // Coleta de dados
                String nome = this.disciplinaView.getInfo("\nDigite o nome da disciplina: ");
                int cargaHoraria = lerIdInteiro("\nDigite a carga-horaria da disciplina: ");
                String matriculaProf = this.disciplinaView.getInfo("\nDigite a matricula do professor responsavel: ");
                int tipo = lerIdInteiro("\nDigite o tipo (1 - Obrigatoria, 2 - Eletiva): ");

                // Validacoes de Negocio
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

            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO DE FORMATO: Digite apenas numeros inteiros onde solicitado.\n");
                return null;
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO AO CADASTRAR: " + e.getMessage() + "\n");
                return null;
            }
        }
    }

    // ==================================================================================
    // REMOCAO (Delete)
    // ==================================================================================

    @Override
    public void removerDisciplina(int id) throws Exception {
        validarExistenciaDisciplina(id); // Valida se existe antes de tentar remover
        this.disciplinaRepository.removerDisciplina(id);
    }

    @Override
    public void removerDisciplina() throws Exception {
        boolean sucesso = false;
        while (!sucesso) {
            try {
                this.disciplinaView.print(" ================= Remover Disciplina ================ \n");
                int id = lerIdInteiro("\nDigite o ID da disciplina a ser removida: ");

                this.removerDisciplina(id); // Chama metodo logico

                this.disciplinaView.print("\nDisciplina " + id + " removida com sucesso.\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID deve ser um numero inteiro.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + " Tente novamente.\n");
            }
        }
    }

    // ==================================================================================
    // LEITURA (Read / Getters)
    // ==================================================================================

    @Override
    public Disciplina buscarDisciplinaPorId(int id) throws Exception {
        return this.disciplinaRepository.buscarDisciplinaPorId(id);
    }

    @Override
    public Disciplina buscarDisciplinaPorNome(String nome) throws Exception {
        return this.disciplinaRepository.buscarDisciplinaPorNome(nome);
    }

    @Override
    public List<Disciplina> listarDisciplinas() throws Exception {

        // 1. Busca a lista de disciplinas no Repository
        List<Disciplina> lista = this.disciplinaRepository.listarDisciplinas();

        // Verifica se a lista não é nula, usando ArrayList vazia como fallback
        if (lista == null) {
            lista = new ArrayList<>();
        }

        // 2. Verifica se a lista está vazia para fornecer feedback ao usuário
        if (lista.isEmpty()) {
            this.disciplinaView.print("\n⚠️ Nenhuma disciplina cadastrada.\n");
            return lista;
        }

        // 3. Envia a lista para a View para exibição formatada (Relatório)
        // O metodo printRelatorios da View deve se encarregar de exibir a lista.
        this.disciplinaView.printRelatorios(lista);

        // 4. Retorna a lista (útil para testes ou outros Controllers)
        return lista;
    }

    // --- Listar Alunos Matriculados ---

    @Override
    public List<Alunos> listarAlunosMatriculados(int id) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
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
    public List<Alunos> listarAlunosMatriculados() {
        boolean sucesso = false;
        List<Alunos> lista = null;

        this.disciplinaView.print(" ================= Listar alunos matriculados ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");
                lista = this.listarAlunosMatriculados(id); // Chama logico
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return lista;
    }

    // --- Listar Alunos Interessados (Eletiva) ---

    @Override
    public List<Alunos> listarAlunosInteressados(int id) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);

        if (!(disciplina instanceof DisciplinaEletiva)) {
            throw new Exception("Apenas disciplinas Eletivas possuem lista de interesse.");
        }

        List<Alunos> alunosObj = new ArrayList<>();
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
    public List<Alunos> listarAlunosInteressados() throws Exception {
        boolean sucesso = false;
        List<Alunos> lista = null;

        this.disciplinaView.print(" ================= Listar alunos interessados ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");
                lista = this.listarAlunosInteressados(id);
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return lista;
    }

    // ==================================================================================
    // ATUALIZACAO - DADOS BASICOS
    // ==================================================================================

    @Override
    public Disciplina alterarDisciplina(Disciplina disciplina) throws Exception {
        try {
            this.validarExistenciaDisciplina(disciplina.getId());
            this.disciplinaRepository.atualizarDisciplina(disciplina);
            return disciplina;
        } catch (Exception e) {
            this.disciplinaView.print("\nERRO: " + e.getMessage() + " Tente novamente.\n");
            return null;
        }
    }

    @Override
    public Disciplina atualizarNome(int id, String nome) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
        disciplina.setNome(nome);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarNome() {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Alterar nome de disciplina ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");

                // Validacao rapida para UX
                if (buscarDisciplinaPorId(id) == null) throw new Exception("ID nao encontrado.");

                String novoNome = this.disciplinaView.getInfo("Digite o novo nome: ");
                if (novoNome.length() < 3) throw new Exception("Nome muito curto (min 3 chars).");

                d = this.atualizarNome(id, novoNome);

                this.disciplinaView.print("\nNome atualizado com sucesso!\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + " Tente novamente.\n");
            }
        }
        return d;
    }

    @Override
    public Disciplina atualizarCargaHoraria(int id, int cargaHoraria) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
        if (cargaHoraria < 10) throw new Exception("Carga horaria deve ser ao menos 10 horas.");

        disciplina.setCargaHoraria(cargaHoraria);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    @Override
    public Disciplina atualizarCargaHoraria() throws Exception {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Alterar carga horaria ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");

                // Validacao rapida para UX
                if (buscarDisciplinaPorId(id) == null) throw new Exception("ID nao encontrado.");

                int carga = lerIdInteiro("Digite a nova carga horaria: ");

                d = this.atualizarCargaHoraria(id, carga);

                this.disciplinaView.print("\nCarga horaria atualizada com sucesso!\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: Digite apenas numeros.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + " Tente novamente.\n");
            }
        }
        return d;
    }

    // ==================================================================================
    // ATUALIZACAO - PROFESSOR
    // ==================================================================================

    @Override
    public Disciplina atualizarProfessorResponsavel(int id, String matriculaProfessor) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
        Professor professor = this.professorController.encontrarProfessor(matriculaProfessor);

        if (professor == null) {
            throw new Exception("Professor com matricula '" + matriculaProfessor + "' nao encontrado.");
        }

        disciplina.setProfessorResponsavel(professor);
        this.disciplinaRepository.atualizarDisciplina(disciplina);
        return disciplina;
    }

    public Disciplina removerProfessorResponsavel(int id) throws Exception {
        try {
            Disciplina disciplina = buscarDisciplinaPorId(id);
            if(disciplina == null) { throw new Exception("Disciplina nao encontrado."); }

            disciplina.setProfessorResponsavel(null);
            disciplinaRepository.atualizarDisciplina(disciplina);
            return disciplina;

        } catch (Exception e) {
            throw new Exception("Erro ao remover professor de disciplina: " + e.getMessage());
        }

    }

    @Override
    public Disciplina atualizarProfessorResponsavel() {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Alterar Professor ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");

                // Validacao rapida
                if (buscarDisciplinaPorId(id) == null) throw new Exception("ID nao encontrado.");

                String mat = this.disciplinaView.getInfo("Digite a matricula do novo professor: ");

                d = this.atualizarProfessorResponsavel(id, mat);

                this.disciplinaView.print("\nProfessor atualizado: " + d.getProfessorResponsavel().getNome() + "\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return d;
    }

    // ==================================================================================
    // GERENCIAMENTO DE ALUNOS (Matricula)
    // ==================================================================================

    @Override
    public Disciplina matricularAlunoDisciplina(int idDisciplina, String matricula) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(idDisciplina);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno com matricula " + matricula + " nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();
        if (alunosMatriculados == null) alunosMatriculados = new ArrayList<>();

        if (alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno ja esta matriculado nesta disciplina.");
        }

        aluno.adicionarDisciplina(idDisciplina);
        alunosMatriculados.add(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    @Override
    public Disciplina matricularAlunoDisciplina() throws Exception {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Matricular Aluno ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");
                String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

                d = this.matricularAlunoDisciplina(id, mat);

                this.disciplinaView.print("\nMatricula realizada com sucesso em " + d.getNome() + "\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return d;
    }

    @Override
    public Disciplina desmatricularAlunoDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
        Alunos aluno = this.alunoController.encontrarAluno(matricula);

        if (aluno == null) throw new Exception("Aluno nao encontrado.");

        List<String> alunosMatriculados = disciplina.getAlunos();

        if (alunosMatriculados == null || !alunosMatriculados.contains(matricula)) {
            throw new Exception("Aluno nao esta matriculado nesta disciplina.");
        }

        aluno.removerDisciplina(id);
        alunosMatriculados.remove(matricula);
        disciplina.setAlunos(alunosMatriculados);
        this.disciplinaRepository.atualizarDisciplina(disciplina);

        return disciplina;
    }

    @Override
    public Disciplina desmatricularAlunoDisciplina() throws Exception {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Desmatricular Aluno ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina: ");
                String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

                d = this.desmatricularAlunoDisciplina(id, mat);

                this.disciplinaView.print("\nAluno removido da disciplina com sucesso.\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return d;
    }

    // ==================================================================================
    // GERENCIAMENTO DE INTERESSE (Eletivas)
    // ==================================================================================

    @Override
    public Disciplina declararInteresseDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
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
    public Disciplina declararInteresseDisciplina() {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Declarar Interesse ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina eletiva: ");
                String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

                d = this.declararInteresseDisciplina(id, mat);

                this.disciplinaView.print("\nInteresse registrado com sucesso!\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return d;
    }

    @Override
    public Disciplina removerInteresseDisciplina(int id, String matricula) throws Exception {
        Disciplina disciplina = validarExistenciaDisciplina(id);
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
    public Disciplina removerInteresseDisciplina() {
        boolean sucesso = false;
        Disciplina d = null;

        this.disciplinaView.print(" ================= Remover Interesse ================ \n");

        while (!sucesso) {
            try {
                int id = lerIdInteiro("\nDigite o ID da disciplina eletiva: ");
                String mat = this.disciplinaView.getInfo("Digite a matricula do aluno: ");

                d = this.removerInteresseDisciplina(id, mat);

                this.disciplinaView.print("\nInteresse removido com sucesso!\n");
                sucesso = true;
            } catch (NumberFormatException e) {
                this.disciplinaView.print("\nERRO: ID invalido.\n");
            } catch (Exception e) {
                this.disciplinaView.print("\nERRO: " + e.getMessage() + "\n");
            }
        }
        return d;
    }

    // ==================================================================================
    // RELATORIOS
    // ==================================================================================

    @Override
    public void gerarRelatorioDisciplinas() {
        try {
            this.disciplinaView.print(" ================= Relatorio Geral ================ \n");

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