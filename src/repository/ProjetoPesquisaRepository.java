package repository;

import model.ProjetoPesquisa;
import model.ProfessorVitalicio;

import java.util.ArrayList;
import java.util.List;

public class ProjetoPesquisaRepository {
    private final List<ProjetoPesquisa> projetos;
    private static ProjetoPesquisaRepository instance;

    public ProjetoPesquisaRepository() {
        this.projetos = new ArrayList<>();
    }

    public static ProjetoPesquisaRepository getInstance() {
        if (instance == null) {
            instance = new ProjetoPesquisaRepository();
        }
        return instance;
    }

    public void cadastrarProjeto(ProjetoPesquisa projeto) {
        if (projeto == null) {
            System.out.println("Projeto inválido.");
            return;
        }
        if (!(projeto.getOrientador() instanceof ProfessorVitalicio)) {
            System.out.println("Erro: Apenas professores vitalícios podem orientar projetos de pesquisa.");
            return;
        }
        if (projetos.contains(projeto)) {
            System.out.println("Este projeto já está cadastrado.");
            return;
        }

        projetos.add(projeto);
        System.out.println("Projeto de pesquisa cadastrado com sucesso: " + projeto.getTitulo());
    }

    public ProjetoPesquisa buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            return null;

        return projetos.stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo.trim()))
                .findFirst()
                .orElse(null);
    }

    public void listarProjetosDoProfessor(ProfessorVitalicio professor) {
        if (professor == null) {
            System.out.println("Professor inválido.");
            return;
        }

        List<ProjetoPesquisa> projetosDoProf = projetos.stream()
                .filter(p -> p.getOrientador() != null && p.getOrientador().equals(professor)).toList();

        if (projetosDoProf.isEmpty()) {
            System.out.println("Nenhum projeto encontrado para o professor: " + professor.getNome());
        } else {
            System.out.println("\nPROJETOS ORIENTADOS POR " + professor.getNome().toUpperCase() + ":");
            System.out.println("─".repeat(80));
            for (ProjetoPesquisa p : projetosDoProf) {
                System.out.printf("%s (Bolsistas: %d)%n", p.getTitulo(), p.getAlunosBolsistas().size());
            }
            System.out.println("─".repeat(80));
            System.out.println("Total: " + projetosDoProf.size() + " projeto(s)\n");
        }

    }

    public void atualizarProjeto(ProjetoPesquisa projeto) {
        if (projeto == null) {
            System.out.println("Projeto inválido.");
            return;
        }
        if (!projetos.contains(projeto)) {
            System.out.println("Projeto não encontrado.");
            return;
        }
        projetos.set(projetos.indexOf(projeto), projeto);
        System.out.println("Projeto de pesquisa atualizado com sucesso: " + projeto.getTitulo());
    }

    public boolean existeProjetoComTitulo(String titulo) {
        return buscarPorTitulo(titulo) != null;
    }

    public boolean removerProjeto(ProjetoPesquisa projeto) {
        return projetos.remove(projeto);
    }

    public List<ProjetoPesquisa> getTodosProjetos() {
        return new ArrayList<>(projetos);
    }
}