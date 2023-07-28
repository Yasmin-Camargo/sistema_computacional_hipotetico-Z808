/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import static java.lang.System.exit;
import java.util.Map;
import javax.swing.JOptionPane;

public class Memoria {
   public static final int TAM_MAXIMO = 64 * 1024; // Tamanho da memória 64K

    private int[] codigo_dados_pilha;   // primeira parte da memória armazena as instruções do programa
                                        // entre dados e pilha estão as variáveis e constantes (dados)
                                        // parte final da memória armazena informações temporárias (pilha)
    
    private int DS; // Registrador DS -> aponta para o início do segmento de dados na memória
    private int CS; // Registrador CS -> aponta para o início do segmento de instruções na memória
    private int SP; // Registrador SP -> armazena o endereço atual da pilha
    private int SS; // Registrador SS -> aponta para o início do segmento de pilha na memória

    public Memoria (int tamanhoCodigo, Map<Integer, Integer>  dados) {
        tamanhoCodigo -= 1;
        codigo_dados_pilha = new int[TAM_MAXIMO];
        
        if (tamanhoCodigo > TAM_MAXIMO){
            JOptionPane.showMessageDialog(
                null, 
                "O programa excedeu a capacidade de armazenamento.", 
                "ERRO", 
                JOptionPane.ERROR_MESSAGE
            );
            exit(0);
        } else {
            CS = 0;                     // Inicio 
            DS = tamanhoCodigo;         // Aponta o início segmento de dados após o segmento de código
            SP = TAM_MAXIMO - 1;    // Aponta o início da pilha no fim da memória
            SS = TAM_MAXIMO - 1;    // Aponta o início do segmento de pilha (pilha vazia SS = SP)
        }
        
        // area de dados é inicializada com -1 (código para saber que não tem nenhum dado armazenado)
        for (int i = DS; i <= SP; i++){    
            codigo_dados_pilha[i] = -1;
        }
        armazenaDadosMontador(dados);
    }
    
    // Métodos para obter o tamanho dos segmentos de memória
    public int getInicioSegmentoDados() {
        return DS;
    }
    
    public int getInicioSegmentoInstrucoes() {
        return CS;
    }
    
    public int getPosicaoAtualPilha() {
        return SP;
    }
    
    public int getCS() {
        return CS;
    }
    
    public int getDS() {
        return DS;
    }
    
    public int lerCodigo(int endereco) {
        if (endereco <= DS){
            return codigo_dados_pilha[endereco];
        } else{
            JOptionPane.showMessageDialog(
                null, 
                "O endereço fornecido esta fora da área de código.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
            return -1;
        }
    }
    
    public int lerDados(int endereco) {
        endereco = endereco + DS ;
        if (endereco >= DS && endereco < SP){
            return codigo_dados_pilha[endereco];
        } else{
            JOptionPane.showMessageDialog(
                null, 
                "O endereço fornecido esta fora da área de dados.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
            return -1;
        }
    }
    
    public void escreverCodigo(int endereco, int valor) {
        if (endereco < DS){
            codigo_dados_pilha[endereco] = valor;
        } else{
            JOptionPane.showMessageDialog(
                null, 
                "Não é possível armazenar uma instrução fora da área de código.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // armazena na próxima posição livre
    public void escreverDados(int valor) {
        int flag = 0;
        for (int i = DS; i < SP; i++){        
            if (codigo_dados_pilha[i] == -1){
                codigo_dados_pilha[i] = valor;
                flag = 1;
                break;
            }
        }
        if (flag == 0){
            JOptionPane.showMessageDialog(
                null, 
                "A área de dados excedeu a capacidade de armazenamento.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // armazena em um endereço especificado
    public void escreverDados(int valor, int endereco) {
        endereco = endereco + DS;
        if (endereco >= DS && endereco < SP){
            codigo_dados_pilha[endereco] = valor;
        } else{
            JOptionPane.showMessageDialog(
                null, 
                "Endereço fornecido esta fora da área de dados.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
            
        }
    }
    
    // ´Pilha (implementa o sistema de crescimento de pilha para baixo)
    public void push_pilha(int valor){
        if (SP == SS && codigo_dados_pilha[SP] == -1){
            codigo_dados_pilha[SP] = valor;
        }
        else if (codigo_dados_pilha[SP - 1] == -1){
            SP = SP - 1;
            codigo_dados_pilha[SP] = valor;
        } else {
            JOptionPane.showMessageDialog(
                    null, 
                    "Pilha atingiu a capacidade máxima.", 
                    "ERRO!", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    public int pop_pilha(){
        int valor = 0;
        if ((SP + 1) > SS){
            JOptionPane.showMessageDialog(
                null, 
                "Não existem elementos para serem removidos da pilha.", 
                "ERRO!", 
                JOptionPane.ERROR_MESSAGE
            );
            return 0;
        } else if ((SP + 1) == SS){
            SP = SP + 1;
            codigo_dados_pilha[SP] = -1;
            return valor;
        } else {
            SP = SP + 1;
            valor = codigo_dados_pilha[SP];
            codigo_dados_pilha[SP] = -1;
            return valor;
        }
    }
    
    private void armazenaDadosMontador(Map<Integer, Integer> dados){
        for (Integer chave : dados.keySet()) {
            codigo_dados_pilha[chave] = dados.get(chave);
        }
    }
    
    /*
    //Função para ver funcionamento
    public Object[][] printAreaCodigo(){
        System.out.println("\nMEMORIA AREA DE CODIGO: ");
        Object[][] data = new Object[DS][2];
        int i;
        for (i = 0; i < DS; i++) {
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
            data[i] = new Object[] {"["+i+"]", codigo_dados_pilha[i]};
        }
        return data;
        
    }
    
    public Object[][] printAreaDados(){
        System.out.println("\n\nMEMORIA AREA DE DADOS: ");
        Object[][] data = new Object[TAM_MAXIMO][2];
        int i;
        for(i = DS; i < TAM_MAXIMO; i++){
            if (codigo_dados_pilha[i] == -1){
                break;
            }
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
            data[i] = new Object[] {"["+i+"]", codigo_dados_pilha[i]};
        }
        for (; i < TAM_MAXIMO; i++) {
            data[i] = new Object[] {"["+i+"]", "null"};
        }
        return data;
    }
    
    public Object[][] printPilha(){
        System.out.println("\n\nMEMORIA PILHA:");
        Object[][] data = new Object[TAM_MAXIMO][2];
        int i;
        for(i = TAM_MAXIMO - 1; i > DS; i--){
            if (codigo_dados_pilha[i] == -1){
                //System.out.println("break");
                break;
            }
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
            data[i] = new Object[] {"["+i+"]", codigo_dados_pilha[i]};
        }
        
        for (; i > DS; i--) {
            //System.out.print(" | ["+i+"]: "+ "null");
            data[i] = new Object[] {"["+i+"]", "null"};
        }
        System.out.println("");
        return data;
    }
    */
    
    public int[] getDadosMemoria() {
        return codigo_dados_pilha;
    }
    
    
    
    // TESTES: DEPOIS RETIRAR
    //Função para ver funcionamento
    public void print(){
        System.out.println("\nMEMORIA AREA DE CODIGO: ");
        int i;
        for (i = 0; i < DS; i++) {
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
        }  
        
        System.out.println("\n\nMEMORIA AREA DE DADOS: ");
        Object[][] data = new Object[TAM_MAXIMO][2];
        
        for(i = DS; i < TAM_MAXIMO; i++){
            if (codigo_dados_pilha[i] == -1){
                break;
            }
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
        }
      
        System.out.println("\n\nMEMORIA PILHA:");
        for(i = TAM_MAXIMO - 1; i > DS; i--){
            if (codigo_dados_pilha[i] == -1){
                //System.out.println("break");
                break;
            }
            System.out.print(" | ["+i+"]: "+ codigo_dados_pilha[i]);
        }
        
        System.out.println("");
    } 
}