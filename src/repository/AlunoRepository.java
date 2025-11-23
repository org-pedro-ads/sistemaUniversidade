package repository;

import model.Alunos;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepository {
    private List<Alunos> alunos = new ArrayList<>();
    private int sequenceId = 1;
    private static AlunoRepository instance;

    public static AlunoRepository getInstance() {
        if (instance == null) {
            instance = new AlunoRepository();
        }
        return instance;
    }

    public void cadastrarAluno(Alunos aluno) {
        if (aluno != null && buscarPorMatricula(aluno.getMatricula()) == null) {
            alunos.add(aluno);
            this.sequenceId++;
            System.out.println("Aluno cadastrado: " + aluno);
        } else {
            System.out.println("Erro: Matrícula já existe ou aluno inválido.");
        }
    }

    public void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        System.out.println("\n=== LISTA DE ALUNOS ===");
        for (Alunos a : alunos) {
            System.out.printf("Matrícula: %s | Nome: %s | Disciplinas: %d%n",
                    a.getMatricula(), a.getNome(), a.getDisciplinasMatriculadas().size());
        }
        System.out.println("Total: " + alunos.size() + "\n");
    }

    public int getNewId() {
        return this.sequenceId;
    }

    public Alunos buscarPorMatricula(String matricula) {
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public List<Alunos> getTodos() {
        return new ArrayList<>(alunos);
    }

    // ================================================
    // MOCK - LISTA PRONTA DE ALUNOS PARA TESTES
    // ================================================
    public List<Alunos> criarMockAlunos() {
        List<Alunos> mock = new ArrayList<>();

        alunos.add(new Alunos("Ana Silva", "BT0001"));
        alunos.add(new Alunos("Bruno Oliveira", "BT0002"));
        alunos.add(new Alunos("Carla Santos", "BT0003"));
        alunos.add(new Alunos("Diego Costa", "BT0004"));
        alunos.add(new Alunos("Eduarda Lima", "BT0005"));
        alunos.add(new Alunos("Felipe Almeida", "BT0006"));
        alunos.add(new Alunos("Gabriela Ferreira", "BT0007"));
        alunos.add(new Alunos("Henrique Mendes", "BT0008"));
        alunos.add(new Alunos("Isabela Rocha", "BT0009"));
        alunos.add(new Alunos("João Pedro Nunes", "BT0010"));

        // // Simulando matrículas em disciplinas (IDs fictícios)
        // mock.get(0).adicionarDisciplina(101); // Ana → POO
        // mock.get(0).adicionarDisciplina(102); // Ana → Banco de Dados
        // mock.get(1).adicionarDisciplina(101); // Bruno → POO
        // mock.get(1).adicionarDisciplina(103); // Bruno → Redes
        // mock.get(2).adicionarDisciplina(101); // Carla → POO
        // mock.get(3).adicionarDisciplina(104); // Diego → Inteligência Artificial
        // mock.get(4).adicionarDisciplina(102); // Eduarda → Banco de Dados
        // mock.get(5).adicionarDisciplina(101); // Felipe → POO
        // mock.get(5).adicionarDisciplina(105); // Felipe → Desenvolvimento Web
        // mock.get(6).adicionarDisciplina(106); // Gabriela → Eletiva: Games
        // mock.get(7).adicionarDisciplina(101); // Henrique → POO
        // mock.get(8).adicionarDisciplina(107); // Isabela → Eletiva: UX/UI
        // mock.get(9).adicionarDisciplina(101); // João Pedro → POO
        // mock.get(9).adicionarDisciplina(102);
        // mock.get(9).adicionarDisciplina(105);

        return mock;
    }
}