package view;

import model.ProjetoPesquisa;
import model.Alunos;

import java.util.List;

public class ProjetoPesquisaView extends BaseView {
    public String lerTituloProjeto() {
        System.out.print("Título do projeto: ");
        return scanner.nextLine().trim();
    }

    public String lerDescricaoProjeto() {
        System.out.print("Descrição do projeto: ");
        return scanner.nextLine().trim();
    }

    public String lerMatriculaOrientador() {
        System.out.print("Matrícula do professor orientador (vitalício): ");
        return scanner.nextLine().trim();
    }

    public String lerMatriculaAlunoBolsista() {
        System.out.print("Matrícula do aluno bolsista: ");
        return scanner.nextLine().trim();
    }

    public String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    // ====================== EXIBIÇÃO DE PROJETOS ======================
    public void exibirProjetoDetalhado(ProjetoPesquisa projeto) {
        System.out.println("\n" + "═".repeat(70));
        System.out.printf(" TÍTULO: %s%n", projeto.getTitulo());
        System.out.printf(" DESCRIÇÃO: %s%n", projeto.getDescricao());
        System.out.printf(" ORIENTADOR: %s%n",
                projeto.getOrientador() != null ? projeto.getOrientador().getNome() : "Não definido");
        System.out.printf(" TOTAL DE BOLSISTAS: %d%n", projeto.getAlunosBolsistas().size());
        System.out.println("═".repeat(70));

        if (projeto.getAlunosBolsistas().isEmpty()) {
            System.out.println("   Nenhum aluno bolsista vinculado.");
        } else {
            System.out.println("   ALUNOS BOLSISTAS:");
            for (Alunos a : projeto.getAlunosBolsistas()) {
                System.out.printf("   • %s (%s)%n", a.getNome(), a.getMatricula());
            }
        }
        System.out.println("═".repeat(70));
    }

    public void exibirListaProjetos(List<ProjetoPesquisa> projetos) {
        System.out.printf("%-40s %-25s %s%n", "TÍTULO", "ORIENTADOR", "BOLSISTAS");
        System.out.println("─".repeat(90));

        for (ProjetoPesquisa p : projetos) {
            String orientadorNome = p.getOrientador() != null ? p.getOrientador().getNome() : "Sem orientador";
            System.out.printf("%-40s %-25s %d%n",
                    truncar(p.getTitulo(), 38),
                    truncar(orientadorNome, 23),
                    p.getAlunosBolsistas().size());
        }
        System.out.println("─".repeat(90));
        System.out.println("Total de projetos: " + projetos.size());
    }
}