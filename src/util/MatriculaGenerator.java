package util;

public class MatriculaGenerator {
    private static int seqAluno = 0;
    private static int seqProfessor = 0;

    private MatriculaGenerator() {} // impede instanciação

    public static String gerarParaAluno() {
        seqAluno++;
        return String.format("BT%04d", seqAluno); // BT0001, BT0002...
    }

    public static String gerarParaProfessor() {
        seqProfessor++;
        return String.format("XX%04d", seqProfessor); // XX0001, XX0002...
    }

    // Bonus: versão genérica com prefixo customizado
    public static String gerarComPrefixo(String prefixo) {
        int seq = switch (prefixo.toUpperCase()) {
            case "BT" -> ++seqAluno;
            case "XX" -> ++seqProfessor;
            default   -> throw new IllegalArgumentException("Prefixo não suportado: " + prefixo);
        };
        return String.format("%s%04d", prefixo.toUpperCase(), seq);
    }

    // Só pra teste (opcional)
    public static void reset() {
        seqAluno = 0;
        seqProfessor = 0;
    }
}
