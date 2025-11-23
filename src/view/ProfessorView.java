package view;

import controller.ProfessorController;
import model.*;
import util.MatriculaGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProfessorView {
    private Scanner sc = new Scanner(System.in);
    private static final ProfessorController controller = new ProfessorController();
    public void cadastrarProfessor() throws Exception {
        System.out.println("===== Cadastro de Professor =====");

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        String matricula = MatriculaGenerator.gerarParaProfessor();

        System.out.println("Informe o título do professor:");
        System.out.println("1 - Graduado");
        System.out.println("2 - Pos-graduado");
        System.out.println("3 - Mestre");
        System.out.println("4 - Doutor");
        int opTitulo = sc.nextInt();
        sc.nextLine();

        TituloProfessor titulo = switch (opTitulo) {
            case 1 -> TituloProfessor.GRADUACAO;
            case 2 -> TituloProfessor.POS_GRADUACAO;
            case 3 -> TituloProfessor.MESTRADO;
            case 4 -> TituloProfessor.DOUTORADO;
            default -> {
                System.out.println("Opção inválida! Definindo como GRADUADO");
                yield TituloProfessor.GRADUACAO;
            }
        };

        System.out.println("Tipo de professor:");
        System.out.println("1 - Titular");
        System.out.println("2 - Substituto");
        int opTipo = sc.nextInt();
        sc.nextLine();

        TipoProfessor tipo = switch (opTipo) {
            case 1 -> TipoProfessor.VITALICIO;
            case 2 -> TipoProfessor.SUBSTITUTO;
            default -> {
                System.out.println("Opção inválida! Definindo como SUBSTITUTO");
                yield TipoProfessor.SUBSTITUTO;
            }
        };

        System.out.println("Quantas disciplinas ele leciona?");
        int qtd = sc.nextInt();
        sc.nextLine();

        List<String> nomeDisciplinas = new ArrayList<>();

        for (int i = 0; i < qtd; i++) {
            System.out.print("Nome da disciplina " + (i + 1) + ": ");
            String nomeDisc = sc.nextLine();
            nomeDisciplinas.add(nomeDisc);
        }

        // Se quiser pegar informações extras dependendo do tipo
        if (tipo == TipoProfessor.SUBSTITUTO) {
            System.out.print("Horas de trabalho semanal: ");
            double horas = sc.nextDouble();
            sc.nextLine();

            System.out.print("Data término de contrato (AAAA-MM-DD): ");
            LocalDate data = LocalDate.parse(sc.nextLine());

            controller.cadastroProfessorSubstituto(nome, matricula, titulo, tipo, qtd, nomeDisciplinas, horas, data);
        } else if (tipo == TipoProfessor.VITALICIO) {
            System.out.println("Quantos projetos de pesquisa ele orienta?");
            qtd = sc.nextInt();
            sc.nextLine();

            List<String> nomesProjetosPesquisa = new ArrayList<>();

            for (int i = 0; i < qtd; i++) {
                System.out.print("Nome do projeto de pesquisa " + (i + 1) + ": ");
                String nomeDisc = sc.nextLine();
                nomesProjetosPesquisa.add(nomeDisc);
            }

            System.out.print("Qual é o salario base do professor: ");
            Double salarioBase = sc.nextDouble();
            sc.nextLine();
            controller.cadastrarProfessorVitalicio(nome, matricula, titulo, tipo,qtd, nomeDisciplinas, nomesProjetosPesquisa, salarioBase);
        }

        System.out.println("\nProfessor cadastrado com sucesso!");
    }

    public void listarProfessores() throws Exception {

        System.out.println("\n===== LISTA DE PROFESSORES =====");

        List<Professor> professores = controller.listarProfessores();

        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.\n");
            return;
        }

        for (Professor p : professores) {
            System.out.println("----------------------------------");
            System.out.println("Nome: " + p.getNome());
            System.out.println("Matrícula: " + p.getMatricula());
            System.out.println("Título: " + p.getTitulo());
            System.out.println("Tipo: " + p.getTipo());
            System.out.println("Disciplinas: " + p.getDisciplinas().stream()
                                                                    .map(Disciplina::getNome)
                                                                    .collect(Collectors.joining(", ")));
            if (p instanceof ProfessorVitalicio pv) {
                System.out.println("Projeto de Pesquisa: " + pv.getProjetos());
                System.out.println("Salário base: " + pv.getSalarioBase());
            }
            else if (p instanceof ProfessorSubstituto ps) {
                System.out.println("Horas semanais: " + ps.getHorasAula());
                System.out.println("Término do contrato: " + ps.getTempoDeContrato());
            }
        }

        System.out.println("----------------------------------\n");
    }

    public void removerProfessor() {
        System.out.println("\n===== REMOÇÃO DE PROFESSOR =====");

        System.out.print("Informe a matrícula do professor: ");
        String matricula = sc.nextLine();

        try {
            boolean removido = controller.removerProfessor(matricula);

            if (removido) {
                System.out.println("\nProfessor removido com sucesso!\n");
            } else {
                System.out.println("\nProfessor não encontrado!\n");
            }

        } catch (Exception e) {
            System.out.println("\nErro ao remover professor: " + e.getMessage());
        }
    }

    public void calcularSalarioProfessor() {
        System.out.println("\n===== CÁLCULO DE SALÁRIO DO PROFESSOR =====");

        System.out.print("Informe a matrícula do professor: ");
        String matricula = sc.nextLine();

        try {
            Double salario = controller.calcularSalario(matricula);
            System.out.println("Salário calculado: R$ " + salario);
        } catch (Exception e) {
            System.out.println("Erro ao calcular salário: " + e.getMessage());
        }

        System.out.println();
    }

    public void editarProfessor() throws Exception {
        System.out.println("==== EDITAR PROFESSOR ====");

        // listar para escolha
        listarProfessores();

        System.out.print("Digite a matricula do professor que deseja editar: ");
        String matriucla = sc.nextLine();

        Professor professor = controller.encontrarProfessor(matriucla);

        if (professor == null) {
            System.out.println("Professor não encontrado!");
            return;
        }

        System.out.println("O que deseja editar?");
        System.out.println("1 - Nome");
        System.out.println("2 - Título (GRADUACAO, POS_GRADUACAO, MESTRADO, DOUTORADO)");
        System.out.println("3 - Disciplinas");
        System.out.println("4 - Dados específicos (Substituto / Vitalício)");
        System.out.println("0 - Cancelar");
        System.out.print("Escolha: ");
        int opcao = Integer.parseInt(sc.nextLine());

        switch (opcao) {
            case 1:
                System.out.print("Novo nome: ");
                professor.setNome(sc.nextLine());
                break;

            case 2:
                System.out.print("Novo título: ");
                professor.setTitulo(TituloProfessor.valueOf(sc.nextLine()));
                break;

            case 3:
                editarDisciplinas(professor);
                break;

            case 4:
                editarTipoEspecifico(professor);
                break;

            case 0:
                System.out.println("Operação cancelada.");
                return;

            default:
                System.out.println("Opção inválida!");
                return;
        }

        controller.atualizacaoCompleta(professor);
        System.out.println("Professor atualizado com sucesso!");
    }

    private void editarDisciplinas(Professor professor) throws Exception {
        System.out.println("== EDITAR DISCIPLINAS ==");
        System.out.println("Disciplinas atuais: " +
                professor.getDisciplinas().stream()
                        .map(Disciplina::getNome)
                        .collect(Collectors.joining(", "))
        );

        System.out.println("1 - Remover todas e adicionar novas");
        System.out.println("2 - Acrescentar novas");
        System.out.print("Escolha: ");
        int escolha = Integer.parseInt(sc.nextLine());

        switch (escolha) {
            case 1:
                professor.getDisciplinas().clear();
            case 2:
                System.out.println("Quantas disciplinas deseja cadastrar?");
                int qtd = Integer.parseInt(sc.nextLine());

                List<String> nomes = new ArrayList<>();
                for (int i = 0; i < qtd; i++) {
                    System.out.print("Nome da disciplina: ");
                    String nome = sc.nextLine();
                    nomes.add(nome);
                }
                controller.atualizarDisciplinas(qtd, nomes, professor);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    public void gerarRelatorioProfessores() throws Exception {

        System.out.println("\n===== RELATÓRIO DE PROFESSORES =====");
        List<Professor> professores = controller.listarProfessores();
        for (Professor p : professores) {
            double salario = controller.calcularSalario(p.getMatricula());
            System.out.println(
                    "Nome: " + p.getNome() +
                            " | Disciplinas: " + p.getDisciplinas().size() +
                            " | Salário: R$ " + salario
            );
        }

        System.out.println("=====================================\n");
    }

    private void editarTipoEspecifico(Professor professor) {

        if (professor instanceof ProfessorSubstituto sub) {
            System.out.println("== EDITAR PROFESSOR SUBSTITUTO ==");
            System.out.print("Nova carga horária: ");
            double horasAulas = sc.nextDouble();
            sc.nextLine();
            sub.setHorasAula(horasAulas);

            System.out.print("Nova data fim contrato (yyyy-MM-dd): ");
            LocalDate tempoContrato = LocalDate.parse(sc.nextLine());
            sub.setTempoDeContrato(tempoContrato);
        }
        else if (professor instanceof ProfessorVitalicio vit) {
            System.out.println("== EDITAR PROFESSOR VITALÍCIO ==");
            System.out.println("Quantos projetos de pesquisa ele orienta?");
            int qtd = sc.nextInt();
            sc.nextLine();

            List<String> nomesProjetosPesquisa = vit.getProjetos().stream()
                    .map(ProjetoPesquisa::getTitulo)
                    .collect(Collectors.toList());

            for (int i = 0; i < qtd; i++) {
                System.out.print("Nome do projeto de pesquisa " + (i + 1) + ": ");
                String nomeDisc = sc.nextLine();
                nomesProjetosPesquisa.add(nomeDisc);
            }

            System.out.print("Qual é o salario base do professor: ");
            Double salarioBase = sc.nextDouble();
            sc.nextLine();
            vit.setSalarioBase(salarioBase);
        } else {
            System.out.println("Tipo desconhecido.");
        }
    }
}