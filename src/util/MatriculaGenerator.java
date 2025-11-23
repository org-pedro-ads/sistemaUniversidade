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

    // public static String gerarComPrefixo(String prefixo) {
    //     int seq = switch (prefixo.toUpperCase()) {
    //         case "BT" -> ++seqAluno;
    //         case "XX" -> ++seqProfessor;
    //         default   -> throw new IllegalArgumentException("Prefixo não suportado: " + prefixo);
    //     };
    //     return String.format("%s%04d", prefixo.toUpperCase(), seq);
    // }

    // para testes
    public static void reset() {
        seqAluno = 0;
        seqProfessor = 0;
    }
}
