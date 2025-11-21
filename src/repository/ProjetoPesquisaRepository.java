package repository;

import model.Professor;
import model.ProjetoPesquisa;
import model.ProfessorVitalicio;

import java.util.ArrayList;
import java.util.List;

public class ProjetoPesquisaRepository {
    private final List<ProjetoPesquisa> projetos;

    public ProjetoPesquisaRepository() {
        this.projetos = new ArrayList<>();
    }

    // (c) Cadastrar projeto de pesquisa (só professor vitalício)
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

    // (d) Listar projetos de um professor
    public void listarProjetosDoProfessor(ProfessorVitalicio professor) {
        if (professor == null) {
            System.out.println("Professor inválido.");
            return;
        }

        List<ProjetoPesquisa> projetosDoProf = projetos.stream()
                .filter(p -> p.getOrientador() != null && p.getOrientador().equals(professor)).toList();

        System.out.println("\n=== PROJETOS DE PESQUISA DE " + professor.getNome().toUpperCase() + " ===");
        if (projetosDoProf.isEmpty()) {
            System.out.println("Nenhum projeto encontrado.");
        } else {
            for (ProjetoPesquisa p : projetosDoProf) {
                System.out.printf("• %s | Alunos bolsistas: %d%n", p.getTitulo(), p.getAlunosBolsistas().size());
            }
        }
        System.out.println("Total: " + projetosDoProf.size() + " projeto(s)\n");
    }

    public ProjetoPesquisa buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) return null;

        return projetos.stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo.trim()))
                .findFirst()
                .orElse(null);
    }

    // LISTAR PROJETOS DE UM PROFESSOR ESPECÍFICO
    public void listarProjetosDoProfessor(Professor professor) {
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
                System.out.printf("• %s (Bolsistas: %d)%n", p.getTitulo(), p.getAlunosBolsistas().size());
            }
            System.out.println("─".repeat(80));
            System.out.println("Total: " + projetosDoProf.size() + " projeto(s)\n");
        }

    }

    // VERIFICAR SE EXISTE PROJETO COM ESSE TÍTULO
    public boolean existeProjetoComTitulo(String titulo) {
        return buscarPorTitulo(titulo) != null;
    }

    // REMOVER PROJETO (opcional, se precisar no futuro)
    public boolean removerProjeto(ProjetoPesquisa projeto) {
        return projetos.remove(projeto);
    }

    public List<ProjetoPesquisa> getTodosProjetos() {
        return new ArrayList<>(projetos);
    }
}