/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Memoria {
    private static final int TAMANHO_MAXIMO = 64 * 1024; // Tamanho da memória 64K

    private int[] dados;    //armazena as variáveis e constantes
    private int[] codigo;   //armazena as instruções do programa
    private int[] pilha;    //armazena informações temporárias
    
    private int DS; // Registrador DS -> aponta para o início do segmento de dados na memória
    private int CS; // Registrador CS -> aponta para o início do segmento de instruções na memória
    private int SP; // Registrador SP -> armazena o endereço atual da pilha
    private int SS; // Registrador SS -> aponta para o inídio do segmento de pilha na memória

    public Memoria (int tamanhoDados, int tamanhoCodigo, int tamanhoPilha) {
        int tamanhoTotal = tamanhoDados + tamanhoCodigo + tamanhoPilha;
        
        // Impede que os três segmentos juntos não ultrapassem 64 K
        if (tamanhoTotal > TAMANHO_MAXIMO) { 
            throw new IllegalArgumentException("O tamanho total dos segmentos excede o limite máximo de 64K");
        }
        
        dados = new int[tamanhoDados];
        codigo = new int[tamanhoCodigo];
        pilha = new int[tamanhoPilha];
        
        DS = 0;                     //Inicio
        CS = tamanhoDados;          // Aponta o início do segmento de código após o segmento de dados
        SP = TAMANHO_MAXIMO - 1;    // Aponta o início da pilha no fim da memória
        SS = TAMANHO_MAXIMO - tamanhoPilha; // Aponta o início do segmento de pilha
    }
   
// Métodos para obter o tamanho dos segmentos de memória
    public int tamanhoDados() {
        return dados.length;
    }
    
    public int tamanhoCodigo() {
        return codigo.length;
    }
    
    public int tamanhoPilha() {
        return pilha.length;
    }
    
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

    public void escreverPilha(int endereco, int valor) {
        pilha[endereco] = valor;
    }

    public int lerPilha(int endereco) {
        return pilha[endereco];
    }
    
}

