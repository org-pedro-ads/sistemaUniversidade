package util;

public class MatriculaGenerator {
    private static int seqAluno = 0;
    private static int seqProfessor = 0;

    public MatriculaGenerator() {}

    public static String gerarParaAluno() {
        seqAluno++;
        return String.format("BT%04d", seqAluno);
    }

    public static String gerarParaProfessor() {
        seqProfessor++;
        return String.format("XX%04d", seqProfessor);
    }

    // para testes
    public static void reset() {
        seqAluno = 0;
        seqProfessor = 0;
    }
}
