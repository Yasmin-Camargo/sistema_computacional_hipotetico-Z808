/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Memoria {
    private static final int TAMANHO_MAXIMO = 64 * 1024; // Tamanho da memória 64K

    private int[] dados;
    private int[] codigo;
    private int[] pilha;

    public Memoria (int tamanhoDados, int tamanhoCodigo, int tamanhoPilha) {
        int tamanhoTotal = tamanhoDados + tamanhoCodigo + tamanhoPilha;
        
        // Impede que os três segmentos juntos não ultrapassem 64 K
        if (tamanhoTotal > TAMANHO_MAXIMO) { 
            throw new IllegalArgumentException("O tamanho total dos segmentos excede o limite máximo de 64K");
        }
        
        dados = new int[tamanhoDados];
        codigo = new int[tamanhoCodigo];
        pilha = new int[tamanhoPilha];
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
}
   
