/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import javax.swing.JOptionPane;

public class Instrucoes {
    // --> Instruções aritméticas
    public static int add(int num1, int num2, Registradores registrador){
        int soma = num1 + num2;
        verificacao_OF(soma, registrador);  // verificação das flags afetadas
        verificacao_PF(soma, registrador);
        verificacao_ZF(soma, registrador);
        verificacao_SF(soma, registrador);
        return (soma);
    }
    
    public static int sub(int num1, int num2, Registradores registrador){
        int subtracao = num1 - num2;
        verificacao_OF(subtracao, registrador);  // verificação das flags afetadas
        verificacao_PF(subtracao, registrador);
        verificacao_ZF(subtracao, registrador);
        verificacao_SF(subtracao, registrador);
        return (subtracao);
    }
    
    public static int div(int num1, int num2, Registradores registrador){
        int divisao = 0;
        if (num2 != 0){
            divisao = num1 / num2;
            registrador.setAX(num1 % num2);       // resto da divisão é colocado em AX
        } 
        verificacao_OF(divisao, registrador);  // verificação das flags afetadas
        verificacao_PF(divisao, registrador);
        verificacao_ZF(divisao, registrador);
        verificacao_SF(divisao, registrador);
        return divisao;
    }
    
    public static int mult(int num1, int num2, Registradores registrador){
        int multiplicacao = num1 * num2;
        verificacao_OF(multiplicacao, registrador);  // verificação das flags afetadas
        verificacao_PF(multiplicacao, registrador);
        verificacao_ZF(multiplicacao, registrador);
        verificacao_SF(multiplicacao, registrador);
        if (registrador.getSR("of") == 1){          // Se deu overflow uma parte do número vai para o registrador AX e a outra para DX
            int parteAlta = multiplicacao >> 16;        // Desloca 16 bits para direita
            int parteBaixa = multiplicacao & 0xFFFF;    // Máscara de 16 bits
            registrador.setDX(parteAlta);
            return parteBaixa;
        } else{
            registrador.setDX(0);
            return multiplicacao;
        }        
    }
    
    public static void cmp(int num1, Registradores registrador){
        if (num1 == registrador.getAX()){   // compara se os valores são iguais
            registrador.setSR("zf", 1);
        } else {
            registrador.setSR("zf", 0);
        }
    }
    
    // --> Instruções lógicas
   
    
    // --> Instruções de desvio
    public static int jmp(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS()) {
            registrador.setCL(memoria.lerDados(endereco));                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima           
            return 1;
        } else {
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
    }
    
    public static int jz(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("zf") == 1) {
            registrador.setCL(memoria.lerDados(endereco));                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima                 
            return 1;
        } else {
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
    }
     
    public static int jnz(int endereco, Registradores registrador, Memoria memoria){
       if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("zf") != 1) {
            registrador.setCL(memoria.lerDados(endereco));                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima         
            return 1;
        } else {
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
    }
    
    public static int jp(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("sf") == 0) {
            registrador.setCL(memoria.lerDados(endereco));                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima        
            return 1;
        } else {
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
    }
    
    // --> Instruções de acesso a memória
    public static void store(int num1, Memoria memoria){
        memoria.escreverDados(num1);
    }
    
    public static int read(int endereco, Memoria memoria){
        return memoria.lerDados(endereco);
    }
   
    
    // ----------------- VERIFICAÇÕES REGISTRADOR DE FLAG -----------------------
    //Verificação de Overflow (registrador OF)
    private static  void verificacao_OF(int num, Registradores registrador){
        int numeroCom15Bits = num & 0x7FFF;     // Deixa só os 15 bits (ultimo bit reservado para sinal)
        if (numeroCom15Bits > 32768){           // Se um bit é reservado para sinal, a capacidade de representação não pode passar de 2^15
            registrador.setSR("of", 1);
            JOptionPane.showMessageDialog(null, "Overflow !!!");
        } else {
            registrador.setSR("of", 0);
        }
    }
    
    //Verificação paridade de uns (registrador PF)
    private static void verificacao_PF(int num, Registradores registrador){
        String binarioString = Integer.toBinaryString(num);    // Percorre binário para testar se a quantidade de uns é par
        int quantidade_uns = 0;
        for (int i = 0; i < binarioString.length(); i++) {  
            if (binarioString.charAt(i) == '1') {
                quantidade_uns++;
            }
        }
        if (quantidade_uns % 2 == 0){  
            registrador.setSR("pf", 1);
        } else {
            registrador.setSR("pf", 0);
        }
    }
    
    //Verificação positivo ou negativo (Registrador SF)
    private static void verificacao_SF(int num, Registradores registrador){
        if (num > 0){                
            registrador.setSR("sf", 0);
        } else{
            registrador.setSR("sf", 1);
        }
    }
    
    //Verificação se o resultado é zero (Registrador ZF)
    private static void verificacao_ZF(int num, Registradores registrador){     
        if (num != 0){                             
            registrador.setSR("zf", 0);
        } else{
            registrador.setSR("zf", 1);
        }
    }
}