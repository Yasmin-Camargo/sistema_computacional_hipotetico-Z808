package z808;

import javax.swing.JOptionPane;

public class Instrucoes {
    // --> Instruções aritméticas
    public static int add(int num1, int num2, Registradores registrador){
        int soma = num1 + num2;
        verificaOF(soma, registrador);  // verificação das flags afetadas
        verificaPF(soma, registrador);
        verificaZF(soma, registrador);
        verificaSF(soma, registrador);
        return (soma);
    }
    
    public static int sub(int num1, int num2, Registradores registrador){
        int subtracao = num1 - num2;
        verificaOF(subtracao, registrador);  // verificação das flags afetadas
        verificaPF(subtracao, registrador);
        verificaZF(subtracao, registrador);
        verificaSF(subtracao, registrador);
        return (subtracao);
    }
    
    public static int div(int num1, int num2, Registradores registrador){
        int divisao = 0;
        if (num2 != 0){
            divisao = num1 / num2;
            registrador.setAX(num1 % num2);       // resto da divisão é colocado em AX
        } 
        verificaOF(divisao, registrador);  // verificação das flags afetadas
        verificaPF(divisao, registrador);
        verificaZF(divisao, registrador);
        verificaSF(divisao, registrador);
        return divisao;
    }
    
    public static int mult(int num1, int num2, Registradores registrador){
        int multiplicacao = num1 * num2;
        verificaOF(multiplicacao, registrador);  // verificação das flags afetadas
        verificaPF(multiplicacao, registrador);
        verificaZF(multiplicacao, registrador);
        verificaSF(multiplicacao, registrador);
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
    public static int and(int num1, int num2, Registradores registrador){
        int operacaoAND = num1 & num2;
        verificaOF(operacaoAND, registrador);  // verificação das flags afetadas
        verificaPF(operacaoAND, registrador);
        verificaZF(operacaoAND, registrador);
        verificaSF(operacaoAND, registrador);
        return (operacaoAND);
    }
    
    public static int not(int num1, Registradores registrador){
        int numeroBits = Integer.toBinaryString(num1).length(); // Número de bits do valor decimal
        int maxValor = (1 << numeroBits) - 1; // Valor máximo representável com o mesmo número de bits
        int operacaoNOT = maxValor - num1; // Operação NOT sem alterar o sinal
        verificaOF(operacaoNOT, registrador);  // verificação das flags afetadas
        verificaPF(operacaoNOT, registrador);
        verificaZF(operacaoNOT, registrador);
        verificaSF(operacaoNOT, registrador);
        return (operacaoNOT);
    }
    
    public static int or(int num1, int num2, Registradores registrador){
        int operacaoOR = num1 | num2;
        verificaOF(operacaoOR, registrador);  // verificação das flags afetadas
        verificaPF(operacaoOR, registrador);
        verificaZF(operacaoOR, registrador);
        verificaSF(operacaoOR, registrador);
        return (operacaoOR);
    }
    
    public static int xor(int num1, int num2, Registradores registrador){
        int operacaoXOR = num1 ^ num2;
        verificaOF(operacaoXOR, registrador);  // verificação das flags afetadas
        verificaPF(operacaoXOR, registrador);
        verificaZF(operacaoXOR, registrador);
        verificaSF(operacaoXOR, registrador);
        return (operacaoXOR);
    }
    
    // --> Instruções de desvio
    public static int jmp(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS()) {
            registrador.setCL(endereco);                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerDados(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima           
            return 1;
        } else {
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
    }
    
    public static int jz(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("zf") == 1) {
            registrador.setCL(endereco);                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima                 
            return 1;
        } else if (!(endereco >= memoria.getCS() && endereco <= memoria.getDS())){
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
        return 1;
    }
     
    public static int jnz(int endereco, Registradores registrador, Memoria memoria){
       if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("zf") != 1) {
            registrador.setCL(endereco);                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima         
            return 1;
        } else if (!(endereco >= memoria.getCS() && endereco <= memoria.getDS())){
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
       return 1;
    }
    
    public static int jp(int endereco, Registradores registrador, Memoria memoria){
        if (endereco >= memoria.getCS() && endereco <= memoria.getDS() && registrador.getSR("sf") == 0) {
            registrador.setCL(endereco);                    // atualiza indice da memória para a informada no devio
            registrador.setRI(memoria.lerCodigo(registrador.getCL())); // coloca o conteudo dessa posição (código da instrução) no registrador
            registrador.setIP(registrador.getCL() + 1);                         // atualiza apontador da próxima        
            return 1;
        } else if (!(endereco >= memoria.getCS() && endereco <= memoria.getDS())){
            System.err.println("Endereco de destino invalido.");
            return 0;
        }
        return 0;
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
    private static  void verificaOF(int num, Registradores registrador){
        int num_15bits = num & 0x7FFF;     // Deixa só os 15 bits (ultimo bit reservado para sinal)
        if (num_15bits > 32768){           // Se um bit é reservado para sinal, a capacidade de representação não pode passar de 2^15
            registrador.setSR("of", 1);
            JOptionPane.showMessageDialog(null, "Overflow !!!");
        } else {
            registrador.setSR("of", 0);
        }
    }
    
    //Verificação paridade de uns (registrador PF)
    private static void verificaPF(int num, Registradores registrador){
        String binario = Integer.toBinaryString(num);    // Percorre binário para testar se a quantidade de uns é par
        int quant_uns = 0;
        for (int i = 0; i < binario.length(); i++) {  
            if (binario.charAt(i) == '1') {
                quant_uns++;
            }
        }
        if (quant_uns % 2 == 0){  
            registrador.setSR("pf", 1);
        } else {
            registrador.setSR("pf", 0);
        }
    }
    
    //Verificação positivo ou negativo (Registrador SF)
    private static void verificaSF(int num, Registradores registrador){
        if (num > 0){                
            registrador.setSR("sf", 0);
        } else{
            registrador.setSR("sf", 1);
        }
    }
    
    //Verificação se o resultado é zero (Registrador ZF)
    private static void verificaZF(int num, Registradores registrador){     
        if (num != 0){                             
            registrador.setSR("zf", 0);
        } else{
            registrador.setSR("zf", 1);
        }
    }
    
    public static void verificaAX(int num, Registradores registrador){
        verificaOF(num, registrador);  // verificação das flags afetadas
        verificaPF(num, registrador);
        verificaZF(num, registrador);
        verificaSF(num, registrador);
    }
}