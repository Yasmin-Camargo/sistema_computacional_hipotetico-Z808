/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Memoria {
   private static final int TAMANHO_MAXIMO = 64 * 1024; // Tamanho da memória 64K

    private int[] codigo_dados_pilha;   // primeira parte da memória armazena as instruções do programa
                                        // entre dados e pilha estão as variáveis e constantes (dados)
                                        // parte final da memória armazena informações temporárias (pilha)
    
    private int DS; // Registrador DS -> aponta para o início do segmento de dados na memória
    private int CS; // Registrador CS -> aponta para o início do segmento de instruções na memória
    private int SP; // Registrador SP -> armazena o endereço atual da pilha
    private int SS; // Registrador SS -> aponta para o inídio do segmento de pilha na memória

    public Memoria (int tamanhoCodigo) {
        codigo_dados_pilha = new int[TAMANHO_MAXIMO];
        
        CS = 0;                     // Inicio 
        DS = tamanhoCodigo;         // Aponta o início segmento de dados após o segmento de código
        SP = TAMANHO_MAXIMO - 1;    // Aponta o início da pilha no fim da memória
        SS = TAMANHO_MAXIMO - 1;    // Aponta o início do segmento de pilha (pilha vazia SS = SP)
    }
    
    // Métodos para obter o tamanho dos segmentos de memória
    public int getInicioSegmentoDados() {
        return DS;
    }
    public int getInicioSegmentoInstrucoes() {
        return CS;
    }
    
    public int lerCodigo(int endereco) {
        return codigo_dados_pilha[endereco];
    }
    
    public int getPosicaoAtualPilha() {
        return SP;
    }
    
    public void escreverCodigo(int endereco, int valor) {
        codigo_dados_pilha[endereco] = valor;
    }
    
    //Função para ver funcionamento
    public void printAreaCodigo(){
        System.out.println("\nMemoria Area de Codigo: ");
        for(int i = 0; i < DS - 1; i++){
            System.out.print(" | "+ codigo_dados_pilha[i]);
        }
    }
    
    /*
    // Métodos para acessar a memória e os registradores
    public void escreverDados(int endereco, int valor) {
        dados[endereco] = valor;
    }

    public int lerDados(int endereco) {
        return dados[endereco];
    }

    public void escreverCodigo(int endereco, int valor) {
        codigo[endereco] = valor;
    }

    public int lerCodigo(int endereco) {
        return codigo[endereco];
    }

  
    */
}

