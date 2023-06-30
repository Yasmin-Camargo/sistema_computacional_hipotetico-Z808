/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Instrucoes {
    // Aqui vão estar os métodos referente a cada uma das instruções add, sub, mul, div ..
    
    public static int add(int num1, int num2, Registradores registrador){
        int soma = num1 + num2;
        
        //Verificação 1: CF
        int carry = soma >>> 16 & 1;    // Verifica o bit de carry (desloca 16 bits para direita, realiza operação and com uma máscara de 1)
        if (carry == 0){                // se sobrou algum número passou da capacidade de representação de 16 bits 
            registrador.setSR("cf", 0);
        } else{
            registrador.setSR("cf", 1);
            System.out.println("Atencao: soma de "+num1+" + "+num2+" resulta em um carry");
        }
        //Verificação 2: PF
        
        //Verificação 3: PF
        
        //Verificação 4: SF
        
        //Verificação 5: OF
        
        return (num1 + num2);
    }
    
    
}
