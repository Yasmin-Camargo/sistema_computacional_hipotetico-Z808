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
      this.comentario = false;
      this.operandos.clear();

      String line = scanner.nextLine();
      if(line.length() > TAM_MAX){
	System.out.println("Linha é maior que o tamanho máximo!");
      }
      String tokens[] = line.split("\\s+");
      if(tokens.length == 0) {
        return;
      }
      if (tokens.length > 0) {
        if (tokens[0].equals("*")){
            this.comentario = true;
            return;
        }
        this.instrucao = tokens[0];
      }
      if (tokens.length > 1) {
        if(tokens[1].equals("*")){
          return;
        }
        this.rotulo = tokens[1];
      }
      if (tokens.length > 2) {
        for (int i = 2; i < tokens.length; i++) {
          if(tokens[1].equals("*")){
            return;
          }
          this.operandos.add(tokens[i]);
        }
      }
    }
  }
  
    public void lerLinhaString(String line) {
    this.comentario = false;
    this.operandos.clear();

    if(line.length() > TAM_MAX){
	System.out.println("Linha é maior que o tamanho máximo!");
      }
    String tokens[] = line.split("\\s+");
    if(tokens.length == 0) {
        return;
      }
    if (tokens.length > 0) {
      if (tokens[0].equals("*")){
          this.comentario = true;
          return;
      }
      this.instrucao = tokens[0];
    }
    if (tokens.length > 1) {
      if(tokens[1].equals("*")){
        return;
      }
      this.rotulo = tokens[1];
    }
    if (tokens.length > 2) {
      for (int i = 2; i < tokens.length; i++) {
        if(tokens[1].equals("*")){
          return;
        }
        this.operandos.add(tokens[i]);
      }
    }
  }

  
  public void resetaValores() {
    this.instrucao = "";
    this.rotulo = "";
    operandos.clear();
    this.comentario = false;
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

  public boolean comentario() {
      return comentario;
  }

  public ArrayList<String> getOperandos() {
      return operandos;
  }

  public String getOperando(int index) {
      return operandos.get(index);
  }

  @Override
  public String toString() {
    String result = this.instrucao + "\t" + 
                  this.rotulo + "\t";
    for (String operand : operandos) {
      result += operand + "\t";
    }
    return result;
  }
}