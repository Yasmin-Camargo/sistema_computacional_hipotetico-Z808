package z808;

import java.util.ArrayList;
import java.util.Scanner;

public class LeitorMacro {
    public static final int TAM_MAX = 100;
    private String instrucao;
    private String rotulo;
    ArrayList<String> operandos = new ArrayList<String>();
    private boolean comentario;

    public void ResetarLinhas(){
        resetaValores();
    }

    public void lerLinhaScanner(Scanner scanner) {
        if (scanner.hasNext()) {
            String line = scanner.nextLine();
            lerLinhaString(line);
        }
    }

    public void lerLinhaString(String line) {
        resetaValores();
        
        if (line.length() > TAM_MAX) {
            System.out.println("Linha é maior que o tamanho máximo!");
            return;
        }
        
        // remove espaços e tabs da frente das instruções
        int cont = 0;
        for (char c : line.toCharArray()) {
            if (c == '\s' || c == '\t') cont++;
            else break;
        }
        line = line.substring(cont);
        
        String tokens[] = line.split("\\s+");
        for (int i = 0; i < tokens.length; ++i) {
            if (tokens[i].contains("#")) 
                break;
            else if (i == 0)
                this.instrucao = tokens[i];
            else if (i == 1)
                this.rotulo = tokens[i];
            else 
                this.operandos.add(tokens[i]);
            // colocar um tratamento
            // caso tenha texto entre virgula
            // podem ser coisas diferentes 
            // como dois parametros
            //      isso é feito em
            //      macro.java atualmente
            //      em analiseParametro
            // o que fazer com a virgula, tho?
        }
    }

  
  public void resetaValores() {
    this.instrucao = "";
    this.rotulo = "";
    operandos.clear();
    //operandos.add("");
  }

  public int getNumeroDeOperandosLer() {
    return operandos.size();
  }
    
  public String getInstrucao() {
      return instrucao;
  }

  public String getRotulo() {
      return rotulo;
  }
  
  public void setRotulo(String value) {
       rotulo = value;
  }

  public ArrayList<String> getOperandos() {
      return operandos;
  }

  public String getOperando(int index) {
      return operandos.get(index);
  }

  @Override
  public String toString() {
    String result = this.instrucao + " " + 
                  this.rotulo + " ";
    for (String operand : operandos) 
      result += operand + " ";

    return result;
  }
}