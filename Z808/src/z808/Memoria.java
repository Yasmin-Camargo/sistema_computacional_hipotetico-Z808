/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import static java.lang.System.exit;
import javax.swing.JOptionPane;

public class Memoria {
   private static final int TAMANHO_MAXIMO = 64 * 1024; // Tamanho da memória 64K

    private int[] codigo_dados_pilha;   // primeira parte da memória armazena as instruções do programa
                                        // entre dados e pilha estão as variáveis e constantes (dados)
                                        // parte final da memória armazena informações temporárias (pilha)
    
    private int DS; // Registrador DS -> aponta para o início do segmento de dados na memória
    private int CS; // Registrador CS -> aponta para o início do segmento de instruções na memória
    private int SP; // Registrador SP -> armazena o endereço atual da pilha
    private int SS; // Registrador SS -> aponta para o início do segmento de pilha na memória

    public Memoria (int tamanhoCodigo) {
        codigo_dados_pilha = new int[TAMANHO_MAXIMO];
        
        if (tamanhoCodigo > TAMANHO_MAXIMO){
            JOptionPane.showMessageDialog(null, "Erro!! O programa excedeu a capacidade de armazenamento");
            exit(0);
        } else {
            CS = 0;                     // Inicio 
            DS = tamanhoCodigo;         // Aponta o início segmento de dados após o segmento de código
            SP = TAMANHO_MAXIMO - 1;    // Aponta o início da pilha no fim da memória
            SS = TAMANHO_MAXIMO - 1;    // Aponta o início do segmento de pilha (pilha vazia SS = SP)
        }
        
        // area de dados é inicializada com -1 (código para saber que não tem nenhum dado armazenado)
        for (int i = DS; i < SP; i++){    
            codigo_dados_pilha[i] = -1;
        }
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
    
    public int lerCodigo(int endereco) {
        if (endereco < DS){
            return codigo_dados_pilha[endereco];
        } else{
            System.out.println("O endereco fornecido esta fora da area de codigo");
            return -1;
        }
    }
    
    public int lerDados(int endereco) {
        if (endereco >= DS && endereco < SP){
            return codigo_dados_pilha[endereco];
        } else{
            System.out.println("O endereco fornecido esta fora da area de dados");
            return -1;
        }
    }
    
    public void escreverCodigo(int endereco, int valor) {
        if (endereco < DS){
            codigo_dados_pilha[endereco] = valor;
        } else{
            System.out.println("!!! Não é possivel armazenar uma instrução fora da area de codigo");
        }
    }
    
    public void escreverDados(int valor) {
        int flag = 0;
        for (int i = DS; i < SP; i++){          // armazena dado na próxima posição livre
            if (codigo_dados_pilha[i] == -1){
                codigo_dados_pilha[i] = valor;
                flag = 1;
                break;
            }
        }
        if (flag == 0){
            JOptionPane.showMessageDialog(null, "Erro!! a area de dados excedeu a capacidade de armazenamento");
        }
    }
    
    //Função para ver funcionamento
    public void printAreaCodigo(){
        System.out.println("\nMemoria Area de codigo: ");
        for(int i = 0; i < DS; i++){
            System.out.print(" | "+ codigo_dados_pilha[i]);
        }
    }
    
    public void printAreaDados(){
        System.out.println("\nMemoria Area de dados: ");
        for(int i = DS; i < SP; i++){
            if (codigo_dados_pilha[i] == -1){
                break;
            }
            System.out.print(" | "+ codigo_dados_pilha[i]);
        }
        System.out.println("");
    }
    
}

