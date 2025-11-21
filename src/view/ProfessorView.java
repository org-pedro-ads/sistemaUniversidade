package view;

import controller.ProfessorController;
import model.Professor;
import model.TipoProfessor;
import model.TituloProfessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProfessorView {
    private Scanner sc = new Scanner(System.in);
    private ProfessorController controller;

    public void cadastrarProfessor() throws Exception {
        System.out.println("===== Cadastro de Professor =====");

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Matrícula: ");
        String matricula = sc.nextLine();

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
            int horas = sc.nextInt();
            sc.nextLine();

            System.out.print("Data término de contrato (AAAA-MM-DD): ");
            String data = sc.nextLine();

            controller.cadastroProfessorSubstituto(nome, matricula, titulo, tipo, qtd, nomeDisciplinas, horas, data);
        } else if (tipo == TipoProfessor.VITALICIO) {
            System.out.print("Nome do projeto de pesquisa: ");
            String projeto = sc.nextLine();

            System.out.print("Qual é o salario base do professor: ");
            Double salarioBase = sc.nextDouble();
            sc.nextLine();
            controller.cadastrarProfessorVitalicio(nome, matricula, titulo, tipo,qtd, nomeDisciplinas, projeto, salarioBase);
        }

        System.out.println("\nProfessor cadastrado com sucesso!");
        return;
    }
}