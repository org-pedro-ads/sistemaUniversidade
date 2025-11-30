package view;

import java.util.Scanner;

public abstract class BaseView {
  public final Scanner scanner = new Scanner(System.in);

  // ====================== EXIBIÇÃO DE TÍTULOS ======================
  public void exibirTitulo(String titulo) {
    System.out.println("\n" + "=".repeat(70));
    String espacos = " ".repeat(Math.max(0, (70 - titulo.length()) / 2));
    System.out.println(espacos + titulo);
    System.out.println("=".repeat(70));
  }

  // ====================== MENSAGENS PADRÃO ======================
  public void sucesso(String mensagem) {
    System.out.println("SUCESSO: " + mensagem);
  }

  public void erro(String mensagem) {
    System.err.println("ERRO: " + mensagem);
  }

  public void info(String mensagem) {
    System.out.println("INFO: " + mensagem);
  }

  // ====================== LEITURA DE DADOS ======================
  public String lerString(String mensagem) {
    System.out.print(mensagem);
    return scanner.nextLine().trim();
  }

  public int lerInteiro(String mensagem) throws Exception {
    try {
      
      System.out.print(mensagem);
      int num = scanner.nextInt();
      scanner.nextLine(); // Adicionado para limpar buffer
      return num;
    
    } catch (Exception e) {
      scanner.nextLine(); // limpar buffer
      throw new Exception("Entrada invalida: Valor nao numerico");
    }
   

  }

  // ====================== PAUSA ======================
  public void pausar() {
    System.out.print("\nPressione ENTER para continuar...");
    scanner.nextLine();
  }

  // ====================== LIMPEZA DE TELA (opcional) ======================
  public void limparTela() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public String truncar(String texto, int max) {
    if (texto == null)
      return "";
    return texto.length() > max ? texto.substring(0, max - 3) + "..." : texto;
  }
}
