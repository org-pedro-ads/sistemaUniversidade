package view;

import model.*;
import util.MatriculaGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProfessorView {
    private Scanner sc = new Scanner(System.in);
    public Professor cadastrarProfessor() throws Exception {
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

        // Se quiser pegar informações extras dependendo do tipo
        Professor novoProfessor = null;
        if (tipo == TipoProfessor.SUBSTITUTO) {
            System.out.print("Horas de trabalho semanal: ");
            double horas = sc.nextDouble();
            sc.nextLine();

            System.out.print("Data término de contrato (AAAA-MM-DD): ");
            LocalDate data = LocalDate.parse(sc.nextLine());
            novoProfessor = new ProfessorSubstituto(nome, matricula, titulo, tipo, null, horas, data);
        } else {
            System.out.print("Qual é o salario base do professor: ");
            Double salarioBase = sc.nextDouble();
            sc.nextLine();
            novoProfessor = new ProfessorVitalicio(nome, matricula, titulo, tipo, null, null, salarioBase);
        }

        System.out.println("\nProfessor cadastrado com sucesso!");
        return novoProfessor;
    }

    public List<String> capturaDisicplinas(){
        System.out.println("Quantas disciplinas ele leciona?");
        int qtd = sc.nextInt();
        sc.nextLine();

        List<String> nomeDisciplinas = new ArrayList<>();

        for (int i = 0; i < qtd; i++) {
            System.out.print("Nome da disciplina " + (i + 1) + ": ");
            String nomeDisc = sc.nextLine();
            nomeDisciplinas.add(nomeDisc);
        }

        return nomeDisciplinas;
    }

    public List<String> capturaProjetosPesquisa(){
        System.out.println("Quantos projetos de pesquisa ele orienta?");
        int qtd = sc.nextInt();
        sc.nextLine();

        List<String> nomesProjetosPesquisa = new ArrayList<>();

        for (int i = 0; i < qtd; i++) {
            System.out.print("Nome do projeto de pesquisa " + (i + 1) + ": ");
            String nomeDisc = sc.nextLine();
            nomesProjetosPesquisa.add(nomeDisc);
        }

        return nomesProjetosPesquisa;
    }

    public void listarProfessores(List<Professor> professores) {

        System.out.println("\n===== LISTA DE PROFESSORES =====");

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

    public String removerProfessor() {
        System.out.println("\n===== REMOÇÃO DE PROFESSOR =====");

        System.out.print("Informe a matrícula do professor: ");
        String matricula = sc.nextLine();
        return matricula;
    }

    public void mostrarMensagemRemocaoProfessor(String mensagem){
        System.out.println(mensagem);
    }

    public String calcularSalarioProfessor() {
        System.out.println("\n===== CÁLCULO DE SALÁRIO DO PROFESSOR =====");

        System.out.print("Informe a matrícula do professor: ");
        String matricula = sc.nextLine();
        return matricula;
    }

    public void mostrarSalario(Double salario){
        System.out.println("Salário calculado: R$ " + salario);
    }

    public String editarProfessor() throws Exception {
        System.out.println("==== EDITAR PROFESSOR ====");

        System.out.print("Com base na lista de professores\nDigite a matricula do professor que deseja editar: ");
        String matriucla = sc.nextLine();
        return matriucla;
    }

    public int capturaCampoEditarProfessor(Professor professor){
        int opcao;
        do {
            System.out.println("O que deseja editar?");
            System.out.println("1 - Nome");
            System.out.println("2 - Título (GRADUACAO, POS_GRADUACAO, MESTRADO, DOUTORADO)");
            System.out.println("3 - Disciplinas");
            System.out.println("4 - Dados específicos (Substituto / Vitalício)");
            System.out.println("0 - Cancelar");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.print("Novo nome: ");
                        professor.setNome(sc.nextLine());
                        return 1;

                    case 2:
                        System.out.print("Novo título: ");
                        professor.setTitulo(TituloProfessor.valueOf(sc.nextLine()));
                        return 2;

                    case 3:
                        return 3;

                    case 4:
                        return 4;

                    case 0:
                        System.out.println("Operação cancelada.");
                        return 0;

                    default:
                        System.out.println("Opção inválida!");
                }
        } while(opcao != 0);

        return opcao;
    }

    public void gerarRelatorioProfessores(List<Professor> professores, List<Double> salario) throws Exception {

        System.out.println("\n===== RELATÓRIO DE PROFESSORES =====");
        int i = 0;
        for (Professor p : professores) {
            System.out.println(
                    "Nome: " + p.getNome() +
                            " | Disciplinas: " + p.getDisciplinas().size() +
                            " | Salário: R$ " + salario.get(i)
            );
            i++;
        }

        System.out.println("=====================================\n");
    }

    public List<String> editarDisciplinas(Professor professor) throws Exception {
        System.out.println("== EDITAR DISCIPLINAS ==");
        System.out.println("Disciplinas atuais: " +
                professor.getDisciplinas().stream()
                        .map(Disciplina::getNome)
                        .collect(Collectors.joining(", "))
        );
        int escolha;
        List<String>nomes = new ArrayList<>();
        do {
            System.out.println("1 - Remover todas e adicionar novas");
            System.out.println("2 - Acrescentar novas");
            System.out.println("0 - Cancelar");
            System.out.print("Escolha: ");
            escolha = Integer.parseInt(sc.nextLine());
                switch (escolha) {
                    case 1:
                        professor.setDisciplinas(new ArrayList<>());
                        return nomes;
                    case 2:
                        System.out.println("Quantas disciplinas deseja cadastrar?");
                        int qtd = Integer.parseInt(sc.nextLine());

                        nomes = professor.getDisciplinas().stream()
                                .map(Disciplina::getNome)
                                .collect(Collectors.toList());
                        for (int i = 0; i < qtd; i++) {
                            System.out.print("Nome da disciplina: ");
                            String nome = sc.nextLine();
                            nomes.add(nome);
                        }
                        return nomes;
                    case 0:
                        return null;
                    default:
                        System.out.println("Opção inválida!");
                }
        }while (escolha != 0);
        return nomes;
    }

    public List<String> editarTipoEspecifico(Professor professor) {
        List<String> nomeProjetoPesqueisa = new ArrayList<>();
        if (professor instanceof ProfessorSubstituto sub) {
            editarProfessorSubstituto(sub);
        }
        else if (professor instanceof ProfessorVitalicio vit) {
            nomeProjetoPesqueisa = editarProfessorVitalicio(vit);
        }
        else {
            System.out.println("Tipo desconhecido.");
        }

        return nomeProjetoPesqueisa;
    }
    private void editarProfessorSubstituto(ProfessorSubstituto sub) {
        int opcao;

        do {
            System.out.println("\n== EDITAR PROFESSOR SUBSTITUTO ==");
            System.out.println("1. Alterar carga horária");
            System.out.println("2. Alterar data de fim do contrato");
            System.out.println("0. Voltar");
            System.out.print("→ Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    System.out.print("Nova carga horária: ");
                    double horas = Double.parseDouble(sc.nextLine());
                    sub.setHorasAula(horas);
                    System.out.println("Carga horária atualizada!");
                    return;
                }

                case 2 -> {
                    System.out.print("Nova data fim contrato (yyyy-MM-dd): ");
                    LocalDate fimContrato = LocalDate.parse(sc.nextLine());
                    sub.setTempoDeContrato(fimContrato);
                    System.out.println("Data de contrato atualizada!");
                    return;
                }

                case 0 -> {
                    System.out.println("Voltando...");
                    return;
                }

                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private List<String> editarProfessorVitalicio(ProfessorVitalicio vit) {
        int opcao;
        List<String> nomeProjetoPesqusia = new ArrayList<>();
        do{
            System.out.println("\n== EDITAR PROFESSOR VITALÍCIO ==");
            System.out.println("1. Editar projetos orientados");
            System.out.println("2. Alterar salário base");
            System.out.println("0. Voltar");
            System.out.print("→ Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    nomeProjetoPesqusia = editarProjetosPesquisa(vit);
                }

                case 2 -> {
                    System.out.print("Novo salário base: ");
                    double salario = Double.parseDouble(sc.nextLine());
                    vit.setSalarioBase(salario);
                    System.out.println("Salário atualizado!");
                    return null;
                }

                case 0 -> {
                    System.out.println("Voltando...");
                    return null;
                }

                default -> System.out.println("Opção inválida!");
            }
        } while(opcao != 0);
        return nomeProjetoPesqusia;
    }

    private List<String> editarProjetosPesquisa(ProfessorVitalicio vit) {
        int opcao;
        List<String> nomeProjetoPesqusia = new ArrayList<>();
        do{
            System.out.println("\n== EDITAR PROJETOS DE PESQUISA ==");
            System.out.println("Projetos atuais:");

            List<String> projetos = vit.getProjetos()
                    .stream()
                    .map(ProjetoPesquisa::getTitulo)
                    .collect(Collectors.toList());

            if (projetos.isEmpty()) {
                System.out.println("(Nenhum projeto cadastrado)");
            } else {
                for (int i = 0; i < projetos.size(); i++) {
                    System.out.println((i + 1) + ". " + projetos.get(i));
                }
            }

            System.out.println("\n1. Adicionar novos projetos");
            System.out.println("2. Remover TODOS os projetos");
            System.out.println("0. Voltar");
            System.out.print("→ Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> nomeProjetoPesqusia = adicionarProjetos(vit, projetos);
                case 2 -> nomeProjetoPesqusia = removerTodosProjetos(vit);
                case 0 -> {
                    System.out.println("Voltando...");
                    return projetos;
                }
                default -> System.out.println("Opção inválida!");
            }
        } while(opcao != 0);
        return nomeProjetoPesqusia;
    }

    private List<String> adicionarProjetos(ProfessorVitalicio vit, List<String> projetos) {
        List<String> nomeProjetoPesqusia = new ArrayList<>();
        System.out.print("Quantos projetos deseja adicionar? ");
        int qtd = Integer.parseInt(sc.nextLine());

        nomeProjetoPesqusia = vit.getProjetos().stream()
                .map(ProjetoPesquisa::getTitulo)
                .collect(Collectors.toList());

        for (int i = 0; i < qtd; i++) {
            System.out.print("Nome do novo projeto " + (i + 1) + ": ");
            projetos.add(sc.nextLine());
        }

        System.out.println("Projetos adicionados com sucesso!");
        return nomeProjetoPesqusia;
    }

    private List<String> removerTodosProjetos(ProfessorVitalicio vit) {
        List<String> nomeProjetoPesqusia = new ArrayList<>();
        System.out.print("Tem certeza que deseja remover TODOS os projetos? (s/n): ");
        String confirmacao = sc.nextLine().trim().toLowerCase();

        if (confirmacao.equals("s")) {
            vit.setProjetos(new ArrayList<>());
            System.out.println("Todos os projetos foram removidos.");
        } else {
            System.out.println("Operação cancelada.");
        }
        return nomeProjetoPesqusia;
    }

}