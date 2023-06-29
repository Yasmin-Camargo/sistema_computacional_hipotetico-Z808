/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package z808;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/*
 * @authors         Grupo Adas & CG
 *  Bianca Beppler Dullius
 *  Caroline Souza Camargo
 *  Cláudio Luis da Silva Machado Junior
 *  Eduarda Abreu Carvalho
 *  Guilherme Braatz Stein
 *  Júlia da Rocha Junqueira
 *  Júlia Veiga da Silva
 *  Maria Julia Duarte Lorenzoni
 *  Yasmin Souza Camargo
 */

public class Z808 {
    // REGISTRADORES
    private short registrador_CL;       //Contador de Localização: armazena endereço da instrução que está sendo executada
    private short registrador_RI;       //Registrador de Instruções: armazena código da instrução que está sendo executada
    
    private short registrador_SI;       //Utilizado como índice no endereçamento indireto 
    private short registrador_IP;       //Apontador de instrução: contém o endereço da próxima instrução a ser executada
    private short registrador_SR;       //!!!!Registrador de Estado: seis flags para indicar condições durante a execução do programa

    // Registradores para fazer interface com  a memória
    private short registrador_REM;      //Registrador de Endereço de Memória: armazena endereço da posição que será lida ou escrita na memória
    private short registrador_RBM;      //Registrador de Buffer da Memória: conteúdo do endereço de memória especificado pelo REM

    // Registradores de dados
    private short registrador_AX;
    private short registrador_DX;
    
    // Registrador para pilha
    private short registrador_SP;       // Stack Pointer: apontador do topo da pilha
    
    
    public static void main(String[] args) {
        String caminho_arquivo = "..\\Z808\\src\\z808\\arquivo_teste.txt"; 
        int flag_final_arquivo = 0;
        
        File arquivo = new File(caminho_arquivo);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(null, "Erro: falha ao abrir o arquivo");
        } else{
            try {
                FileInputStream arquivo_leitura = new FileInputStream(arquivo);
                char[] ch = new char[4];
                while (flag_final_arquivo != 1) {   
                    //lê de 2 em 2 bytes (16 bits) o arquivo
                    ch[0] = (char) arquivo_leitura.read();
                    ch[1] = (char) arquivo_leitura.read();
                    ch[2] = (char) arquivo_leitura.read();
                    ch[3] = (char) arquivo_leitura.read();
                    
                    if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF' || ch[2] == '\uFFFF' || ch[3] == '\uFFFF'){  // Fim do arquivo
                       flag_final_arquivo = 1;
                    } else{
                        String str = new String(ch);             
                        int decimal = (int) Integer.parseInt(str, 16);  
                        System.out.println(Integer.toHexString(decimal));
                    }
                }
                arquivo_leitura.close();
            } catch (IOException e) {
                System.out.println("Erro: ao manipular o arquivo");
            }
        }
    }
    
}
