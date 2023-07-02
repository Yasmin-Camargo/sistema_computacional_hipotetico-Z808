/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Instrucoes {
    // Aqui vão estar os métodos referente a cada uma das instruções add, sub, mul, div ..
    
    public static int add(int num1, int num2, Registradores registrador){
        int soma = num1 + num2;
        
        //Verificação 1: OF
        int numeroCom15Bits = soma & 0x7FFF;    // Verifica overflow: deixa só os 15 bits (ultimo bit reservado para sinal)
        if (numeroCom15Bits > 32768){           // Se um bit é reservado para sinal, a capacidade de representação não pode passar de 2^15
            registrador.setSR("of", 1);
            System.out.println("Atencao: soma de "+num1+" + "+num2+" resulta em overflow");
        } else {
            registrador.setSR("of", 0);
        }
        
        //Verificação 2: CF
        int carry = soma >>> 16 & 1;            // Verifica o bit de carry (desloca 16 bits para direita, realiza operação and com uma máscara de 1)
        if (carry == 0){                        // se sobrou algum número passou de 16 bits 
            registrador.setSR("cf", 0);
        } else{
            registrador.setSR("cf", 1);
            System.out.println("Atencao: soma de "+num1+" + "+num2+" resulta em um carry");
        }
        
        //Verificação 3: PF
        String binarioString = Integer.toBinaryString(soma);    // Percorre binário para testar se a quantidade de uns é par
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
        
        //Verificação 4: ZF
        if (soma != 0){                             // verifica se o resultado da soma é zero 
            registrador.setSR("zf", 0);
        } else{
            registrador.setSR("zf", 1);
        }
        
        //Verificação 5: SF
        int sinal = soma >>> 15 & 1;                // verifica sinal do bit mais significativo(positivo ou negativo)
        if (sinal == 1){                
            registrador.setSR("sf", 1);
        } else{
            registrador.setSR("sf", 1);
        }
        
        return (soma);
    }
    
    
    
    public static void addr(Registradores registrador1, Registradores registrador2){
        int soma = registrador1.getAX() + registrador2.getAX();
        
        //Verificação 1: OF
        int numeroCom15Bits = soma & 0x7FFF;    // Verifica overflow: deixa só os 15 bits (ultimo bit reservado para sinal)
        if (numeroCom15Bits > 32768){           // Se um bit é reservado para sinal, a capacidade de representação não pode passar de 2^15
            registrador1.setSR("of", 1);
            System.out.println("Atencao: soma de "+registrador1.getAX()+" + "+registrador2.getAX()+" resulta em overflow");
        } else {
            registrador1.setSR("of", 0);
        }
        
        //Verificação 2: CF
        int carry = soma >>> 16 & 1;            // Verifica o bit de carry (desloca 16 bits para direita, realiza operação and com uma máscara de 1)
        if (carry == 0){                        // se sobrou algum número passou de 16 bits 
            registrador1.setSR("cf", 0);
        } else{
            registrador1.setSR("cf", 1);
            System.out.println("Atencao: soma de "+registrador1.getAX()+" + "+registrador2.getAX()+" resulta em um carry");
        }
        
        //Verificação 3: PF
        String binarioString = Integer.toBinaryString(soma);    // Percorre binário para testar se a quantidade de uns é par
        int quantidade_uns = 0;
        for (int i = 0; i < binarioString.length(); i++) {  
            if (binarioString.charAt(i) == '1') {
                quantidade_uns++;
            }
        }
        if (quantidade_uns % 2 == 0){  
            registrador1.setSR("pf", 1);
        } else {
            registrador1.setSR("pf", 0);
        }
        
        //Verificação 4: ZF
        if (soma != 0){                             // verifica se o resultado da soma é zero 
            registrador1.setSR("zf", 0);
        } else{
            registrador1.setSR("zf", 1);
        }
        
        //Verificação 5: SF
        int sinal = soma >>> 15 & 1;                // verifica sinal do bit mais significativo(positivo ou negativo)
        if (sinal == 1){                
            registrador1.setSR("sf", 1);
        } else{
            registrador1.setSR("sf", 1);
        }
        
        registrador1.setAX(soma);
    }
    
    public static int and(int num1, int num2){
        int resultado = num1 & num2;
        return (resultado);
    }
    
    public static void clear(Registradores registrador1){
        registrador1.setAX(0);
        registrador1.setDX(0);
    }
    
    public static int comp(int num1, int num2){
        if(num1 == num2){
            return 1;
        }else{
            return 0;
        }
    }
    
    public static int compr(Registradores registrador1, Registradores registrador2){
        if(registrador1.getAX() == registrador2.getAX()){
            return 1;
        }else{
            return 0;
        }
    }
    
    public static int div(int num1, int num2){
        int resultado = num1/num2;
        return resultado;
    }
    
    public static void divr(Registradores registrador1, Registradores registrador2){
        int resultado = registrador1.getAX()/registrador2.getAX();
        registrador1.setAX(resultado);
    }
}
