/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package z808;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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
    String caminho_arquivo;
    Registradores registrador;
    Memoria memoria;
    int tam_area_instrucoes, flag_jump, flag_att_tabelas;
    
    public Z808(String caminho_arquivo) throws IOException {
        this.caminho_arquivo = caminho_arquivo;
        //gui = new JanelaZ808(caminho_arquivo);
        iniciarZ808();
        //gui.setVisible(true);
    }
    
    public void iniciarZ808() throws IOException {
        // TESTE MONTADOR
        System.out.println("-------------------- MONTADOR --------------------------------------------------------------------\n");
        Montador novoMontador = new Montador(".\\src\\z808\\resources\\teste1_montador.txt");
        //this.caminho_arquivo = ".\\src\\z808\\resources\\codigoObjeto.txt";
        //----------------------------
        
        System.out.println("\n-------------------- EXECUTOR ---------------------------------------------------------------------\n");
      
        registrador = new Registradores();
    }
    
    public int[] carregar_instrucoes() {
        tam_area_instrucoes = conta_quantidade_instrucoes(caminho_arquivo); // Para criar uma memória deve-se saber primeiramente quanto de espaço ocupa as instruções
        flag_jump = 0;
        
        memoria = new Memoria(tam_area_instrucoes); // cria memória com o espaco para as instruções já definido
        armazena_instrucoes(caminho_arquivo, memoria);  // coloca dados do arquivo na memória
        
        // Inicialização dos registradores
        registrador.setCL(memoria.getInicioSegmentoInstrucoes());  // pega endereço (indice) da primeira instrução na memória
        registrador.setRI(memoria.lerCodigo(registrador.getCL())); // pega o código da instrução
        registrador.setIP(registrador.getCL() + 1); // atualiza o apontador de instrução para o endereço da próxima instrução
        
        // Prints
        System.out.println("\n-------------------\n");
        System.out.println("Registrador AX: "+registrador.getAX());
        System.out.println("Registrador DX: "+registrador.getDX());
        System.out.println("Registrador SR: "+registrador.print_SR());
        
        return memoria.getDadosMemoria();
    }
    
    public String[] executar_passo() {       
        flag_att_tabelas = 0;
        String instrucao = "", saida = "";
        flag_jump = 0;
        switch (registrador.getRI()){
            case 3: // add AX,AX  e add AX,DX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) { 
                    instrucao = "add AX,AX";   
                    registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao = "add AX,DX";   
                    registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getDX(), registrador));
                }
                break;

            case 4: // add AX, opd  (imediato)
                instrucao = "add AX,opd (imediato)";  
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRI(), registrador));
                break;

            case 5: // add AX, opd  (direto)
                instrucao = "add AX,opd (direto)";  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRBM(), registrador));
                break;

            case 43: // sub AX,AX e sub AX,DX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "sub AX,AX";
                    registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao = "sub AX,DX";  
                    registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getDX(), registrador));
                }
                break;

            case 44: // sub AX,opd  (imediato)
                instrucao = "sub AX,opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRI(), registrador));
                break;

            case 45: // sub AX,opd  (direto)
                instrucao = "sub AX,opd (direto)";  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRBM(), registrador));  
                break;

            case 247: // div AX, SI | div AX, AX | mul AX, SI | mul AX, AX 
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 246) {  
                    instrucao = "div AX, SI";
                    registrador.setAX(Instrucoes.div(registrador.getAX(), registrador.getSI(), registrador));
                } else if (registrador.getRI() == 192) {
                    instrucao = "div AX, AX"; 
                    registrador.setAX(Instrucoes.div(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 245) {  
                    instrucao = "mul AX, SI";
                    registrador.setAX(Instrucoes.mult(registrador.getAX(), registrador.getSI(), registrador));
                } else if (registrador.getRI() == 240) {
                    instrucao = "mul AX, AX"; 
                    registrador.setAX(Instrucoes.mult(registrador.getAX(), registrador.getAX(), registrador));                    }
                break;

            case 60: // cmp AX,opd (imediato)
                instrucao = "cmp AX,opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                Instrucoes.cmp(registrador.getRI(), registrador);
                break;

            case 61: // cmp AX,opd (direto)
                instrucao = "cmp AX,opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                Instrucoes.cmp(registrador.getRBM(), registrador);
                break;

            case 59: // cmp AX,DX 
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 194) {  
                    instrucao = "cmp AX,DX";
                    Instrucoes.cmp(registrador.getDX(), registrador);
                }
                break;

             // ----> Instruções lógicas
             case 35: // and AX,AX e and AX,DX
             atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
             if (registrador.getRI() == 192) {  
                 instrucao = "and AX,AX";
                 registrador.setAX(Instrucoes.and(registrador.getAX(), registrador.getAX(), registrador));
             } else if (registrador.getRI() == 194) {
                 instrucao = "and AX,DX";  
                 registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getDX(), registrador));
             }
             break;

            case 36: // and AX,opd (imediato)
                instrucao = "and AX,opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.and(registrador.getAX(), registrador.getRI(), registrador));
                break;

            case 37: // and AX,opd (direto)
                instrucao = "and AX,opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.and(registrador.getAX(), registrador.getRBM(), registrador));  
                break;               

            case 11: // or AX,AX e or AX,DX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "or AX,AX";
                    registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao = "or AX,DX";  
                    registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getDX(), registrador));
                }
                break;

            case 12: // or AX,opd (imediato)
                instrucao = "or AX,opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getRI(), registrador));
                break;

            case 13: // or AX,opd (direto)
                instrucao = "or AX,opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getRBM(), registrador));  
                break;

            case 248: // not AX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "not AX";
                    registrador.setAX(Instrucoes.not(registrador.getAX(), registrador));
                }
                break;

            case 51: // xor AX,AX e xor AX,DX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "xor AX,AX";
                    registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao = "xor AX,DX";  
                    registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getDX(), registrador));

                }
                break;

            case 52: // xor AX,opd (imediato)
                instrucao = "xor AX,opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getRI(), registrador));

                break;

            case 53: // xor AX,opd (direto)
                instrucao = "xor AX,opd (direto)";
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getRBM(), registrador)); 
                break;

            // ----> Instruções de desvio 
            case 235: // jmp opd (direto)       
                instrucao = "jmp opd (direto)";
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jmp(registrador.getRBM(), registrador, memoria);
                break;

            case 116: // jz opd (direto)
                instrucao = "jz opd (direto)";    
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jz(registrador.getRBM(), registrador, memoria);
                break;

            case 117: // jnz opd (direto)
                instrucao = "jnz opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jnz(registrador.getRBM(), registrador, memoria);
                break;

            case 122: // jp opd (direto)
                instrucao = "jp opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jp(registrador.getRBM(), registrador, memoria);
                break;

            // ----> Instruções da pilha 
            case 232: // call opd (imediato)
                instrucao = "call opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                memoria.push_pilha(registrador.getIP());
                registrador.setIP(registrador.getRI());
                flag_att_tabelas = 1;
                break;

            case 231: // call opd (direto)
                instrucao = "jp opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                memoria.push_pilha(registrador.getIP());
                registrador.setIP(registrador.getRBM());
                flag_att_tabelas = 1;
                break;

            case 239: // ret
                instrucao = "ret";   
                registrador.setSP(memoria.pop_pilha());
                break;

            case 87: // pop DX e pop AX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "pop AX";
                    registrador.setAX(memoria.pop_pilha());
                } else if (registrador.getRI() == 194) {
                    instrucao = "pop DX";  
                    registrador.setDX(memoria.pop_pilha());
                }
                break;

            case 88: // pop opd (imediato)
                instrucao = "pop opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                flag_att_tabelas = 1;
                break;

            case 89: // pop opd (direto)
                instrucao = "pop opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                flag_att_tabelas = 1;
                break;

            case 157: // popf 
                instrucao = "popf";
                registrador.desconcatena_SR(memoria.pop_pilha());
                break;

            case 80: // push DX e push AX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "push AX";
                    memoria.push_pilha(registrador.getAX());
                } else if (registrador.getRI() == 194) {
                    instrucao = "push DX";  
                    memoria.push_pilha(registrador.getDX());
                }
                flag_att_tabelas = 1;
                break;

            case 156: // pushf 
                instrucao = "pushf";   
                memoria.push_pilha(registrador.concatena_SR());
                flag_att_tabelas = 1;
                break;

            // ----> Memória
            case 7: // store DX e store AX
                atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao = "store AX";
                    Instrucoes.store(registrador.getAX(), memoria);
                } else if (registrador.getRI() == 194) {
                    instrucao = "store DX";  
                    Instrucoes.store(registrador.getDX(), memoria);
                }
                break; 

            case 18: // read opd (imediato)
                instrucao = "read opd (imediato)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereco (16 bits)
                String aux1 = "";
                    while (aux1.equals("")) {
                        aux1 = JOptionPane.showInputDialog(
                            "Em qual endereço você deseja armazenar o valor "
                            + registrador.getRI()+"? "
                        );
                    }
                    int endereco;
                    try {
                        endereco = Integer.parseInt(aux1);
                    }
                    catch (NumberFormatException e){
                        endereco = memoria.getInicioSegmentoDados();
                        JOptionPane.showMessageDialog(
                            null, 
                            "O valor inserido não é um número. Valor " + endereco + " foi atribuido.", 
                            "ERRO!",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                memoria.escreverDados(registrador.getRI(), endereco);
                flag_att_tabelas = 1;
                break;

            case 19: // read opd (direto)
                instrucao = "read opd (direto)";        
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereco (16 bits)
                String aux2 = "";
                    while (aux2.equals("")) {
                        aux2 = JOptionPane.showInputDialog(
                            "Qual valor você deseja armazenar no endereço de memória "
                            + registrador.getRI()+"? "
                        );
                    }
                    int valor;
                    try {
                        valor = Integer.parseInt(aux2);
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "O valor inserido não é um número. Valor 1 foi atribuido.", 
                            "ERRO!",
                            JOptionPane.ERROR_MESSAGE
                        );
                        valor = 1;
                    }       
                memoria.escreverDados(valor, registrador.getRI());
                flag_att_tabelas = 1;
                break;

            case 9: // write opd (direto)
                instrucao = "write opd (direto)"; // primeiro vai na memória no endereço indicado na instrução, lê o dado, neste dado esta o endereço que quer acessar    
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereco (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                //gui.atualizarSaida("dataAreaMemory[" + (registrador.getRBM() + memoria.getDS())+ "] = " + memoria.lerDados(registrador.getRBM()));
                saida = "dataAreaMemory[" + (registrador.getRBM() + memoria.getInicioSegmentoDados())+ "] = " + memoria.lerDados(registrador.getRBM());
                break;

            case 8: // write opd (imediato)
                instrucao = "write opd (imediato)"; // mostra para o usuário valor armazenado no endereço de memória indicado na instrução 
                atualiza_CL_RI_IP(registrador, memoria); //leitura do endereco (16 bits) 
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                saida = "dataAreaMemory[" + (memoria.getInicioSegmentoDados() + registrador.getREM()) + "] = " + registrador.getRBM();
                //gui.atualizarSaida("dataAreaMemory[" + (memoria.getDS() + registrador.getREM()) + "] = " + registrador.getRBM());
                break;

            case 21:    // move AX,opd (direto)
                instrucao = "move AX,opd (direto)";  
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(registrador.getRI());
                break;   

            case 20:    // move AX,opd (imediato)
                instrucao = "move AX,opd (direto)";  
                atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(registrador.getRBM());
                break;

            case 22:    // move AX,DX
                if (registrador.getRI() == 194) {
                    instrucao = "move AX,DX";  
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setDX(registrador.getAX());
                }
                break;

            case 238: // hlt
                instrucao = "hlt";
                registrador.setIP(tam_area_instrucoes);
                flag_jump = 1;
                break;

            default:
                instrucao = "ERRO: Instrução ["+registrador.getRI()+"] não é aceita.";
                JOptionPane.showMessageDialog(
                    null, "Instrução ["+registrador.getRI()+"] não é aceita.", "ERRO", JOptionPane.ERROR_MESSAGE
                );
                return new String[] {instrucao, saida, "0"};
        }

        // se o jump foi acionado não atualiza apontadores de instrução
        if (flag_jump == 0){
            // atualiza apontadores de instruções
            registrador.setCL(registrador.getIP());
            registrador.setRI(memoria.lerCodigo(registrador.getCL()));
            registrador.setIP(registrador.getIP() + 1);
        }
        
        System.out.println(instrucao);
        return new String[] {instrucao, saida, ""+flag_att_tabelas};
    }
    
    public String[] executar_codigo() {
         String[] aux, retorno = {"", "", "1"};
        // while que percorre a area de codigo(instruções) da memória
        while (registrador.getIP() != tam_area_instrucoes){
            flag_jump = 0;
            aux = executar_passo();
            if(aux[0].contains("ERRO")) break;
            else {
                retorno[0] += (aux[0] + "\n");
                if (!aux[1].equals(""))
                    retorno[1] += (aux[1] + "\n");
            }
        }
        return retorno;
    }
    
    
    // Atualiza Contador de Localização, Registrador de Instruções e o Apontador de instrução
    public void atualiza_CL_RI_IP(Registradores registrador, Memoria memoria){
        registrador.setCL(registrador.getIP());
        registrador.setRI(memoria.lerCodigo(registrador.getCL()));
        registrador.setIP(registrador.getIP() + 1);
        //gui.atualizarRegistradores(registrador);
    }
    
    // Conta quantos espaços de memória deverão ser reservados para área de instruções
    public int conta_quantidade_instrucoes(String caminho_arquivo){
        char[] opd = new char[4];   //16 bits
        char[] ch = new char[2];    //8 bits
        int flag_final_arquivo = 0, quantidade_memoria = 0;
        String instrucao;
        
        File arquivo = new File(caminho_arquivo);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(
                    null, "Falha ao abrir o arquivo.", "ERRO", JOptionPane.ERROR_MESSAGE
            );
        } else {
            try {
                FileInputStream arquivo_leitura = new FileInputStream(arquivo);
                while (flag_final_arquivo != 1) {
                    //lê primeiro byte (8 bits) da instrução
                    ch[0] = (char) arquivo_leitura.read();
                    ch[1] = (char) arquivo_leitura.read();
                    quantidade_memoria += 1;
                    // Verifica se chegou no Fim do arquivo
                    if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF') {
                        flag_final_arquivo = 1;
                    } else {
                         instrucao = new String(ch);
                        
                        // Instruções que armazenam + 8 bits
                        if (instrucao.equals("03") || instrucao.equals("F7") || instrucao.equals("2B") || instrucao.equals("3B") ||
                            instrucao.equals("23") || instrucao.equals("F8") || instrucao.equals("0B") || instrucao.equals("33") ||
                            instrucao.equals("57") || instrucao.equals("50") || instrucao.equals("07") || instrucao.equals("16")){
                            ch[0] = (char) arquivo_leitura.read();
                            ch[1] = (char) arquivo_leitura.read();
                            quantidade_memoria += 1;
                        } 
                        // Instruções que armazenam + 16 bits
                        else if(instrucao.equals("05") || instrucao.equals("04") || instrucao.equals("2D") || instrucao.equals("2C") ||
                                instrucao.equals("3D") || instrucao.equals("3C") || instrucao.equals("25") || instrucao.equals("24") ||
                                instrucao.equals("0D") || instrucao.equals("0C") || instrucao.equals("35") || instrucao.equals("34") ||
                                instrucao.equals("EB") || instrucao.equals("74") || instrucao.equals("75") || instrucao.equals("7A") || 
                                instrucao.equals("E8") || instrucao.equals("E7") || instrucao.equals("59") || instrucao.equals("58") ||
                                instrucao.equals("12") || instrucao.equals("13") || instrucao.equals("08") || instrucao.equals("09") ||
                                instrucao.equals("14") || instrucao.equals("15")){  
                            //leitura do operando (16 bits)
                                opd[0] = (char) arquivo_leitura.read();
                                if (opd[0] == '-'){              //verifica se é um número negativo
                                    opd[0] = (char) arquivo_leitura.read();
                                }
                                opd[1] = (char) arquivo_leitura.read();
                                opd[2] = (char) arquivo_leitura.read();
                                opd[3] = (char) arquivo_leitura.read();
                                quantidade_memoria += 1;
                        }
                        // Instruções que armazenam + nada
                        else if(instrucao.equals("EF") || instrucao.equals("EE")|| instrucao.equals("9D") || instrucao.equals("9C")) {
                        }
                    }
                }
                arquivo_leitura.close();
            } catch (IOException e) {
                System.out.println("Erro: ao manipular o arquivo");
            }
        }
        return (quantidade_memoria);
    }
    
    // Coloca cada uma das intruções na memória
    public void armazena_instrucoes(String caminho_arquivo, Memoria memoria){
        char[] opd = new char[4];
        char[] ch = new char[2];
        int flag_final_arquivo = 0, quant_instrucoes = 0, numNegativo = 0;
        String instrucao;
        
        File arquivo = new File(caminho_arquivo);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(null, "Erro: falha ao abrir o arquivo");
        } else {
            try {
                FileInputStream arquivo_leitura = new FileInputStream(arquivo);
                while (flag_final_arquivo != 1) {
                    //lê primeiro byte (8 bits) da instrução
                    ch[0] = (char) arquivo_leitura.read();
                    ch[1] = (char) arquivo_leitura.read();
                  
                    // Verifica se chegou no Fim do arquivo
                    if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF') {
                        flag_final_arquivo = 1;
                    } else {
                         instrucao = new String(ch);
                         memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                         quant_instrucoes += 1;

                        // Instruções que armazenam + 8 bits
                        if (instrucao.equals("03") || instrucao.equals("F7") || instrucao.equals("2B") || instrucao.equals("3B") ||
                            instrucao.equals("23") || instrucao.equals("F8") || instrucao.equals("0B") || instrucao.equals("33") ||
                            instrucao.equals("57") || instrucao.equals("50") || instrucao.equals("07") || instrucao.equals("16")){
                            ch[0] = (char) arquivo_leitura.read();
                            ch[1] = (char) arquivo_leitura.read();
                            instrucao = new String(ch);
                            memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                            quant_instrucoes += 1;
                        } 
                        // Instruções que armazenam + 16 bits
                        else if(instrucao.equals("05") || instrucao.equals("04") || instrucao.equals("2D") || instrucao.equals("2C") ||
                                instrucao.equals("3D") || instrucao.equals("3C") || instrucao.equals("25") || instrucao.equals("24") ||
                                instrucao.equals("0D") || instrucao.equals("0C") || instrucao.equals("35") || instrucao.equals("34") ||
                                instrucao.equals("EB") || instrucao.equals("74") || instrucao.equals("75") || instrucao.equals("7A") || 
                                instrucao.equals("E8") || instrucao.equals("E7") || instrucao.equals("59") || instrucao.equals("58") ||
                                instrucao.equals("12") || instrucao.equals("13") || instrucao.equals("08") || instrucao.equals("09") ||
                                instrucao.equals("14") || instrucao.equals("15")){ 
                            opd[0] = (char) arquivo_leitura.read();
                            if (opd[0] == '-'){ //verifica se é um número negativo
                                opd[0] = (char) arquivo_leitura.read();
                                numNegativo = 1;
                            }
                            opd[1] = (char) arquivo_leitura.read();
                            opd[2] = (char) arquivo_leitura.read();
                            opd[3] = (char) arquivo_leitura.read();
                            instrucao = new String(opd);
                            if (numNegativo == 1){
                                numNegativo = 0;
                                memoria.escreverCodigo(quant_instrucoes, -((int) Integer.parseInt(instrucao, 16)));
                            } else{
                                memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                            }
                            quant_instrucoes += 1;
                        }
                        // Instruções que armazenam + nada
                        else if(instrucao.equals("EF") || instrucao.equals("EE")|| instrucao.equals("9D") || instrucao.equals("9C")) {
                        }
                    }
                }
                arquivo_leitura.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao manipular o arquivo.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public Memoria getMemoria() {
        return memoria;
    }
    
    public int[] getDadosMemoria() {
        return memoria.getDadosMemoria();
    }
    
    public int getDSMemoria() {
        return memoria.getInicioSegmentoDados();
    }

  public void atualizar_memoria(Memoria memoria) {
        this.memoria = memoria;
    }
    
    public Registradores getRegistrador() {
        return registrador;
    }
    
    public int getQuantInstr() {
        return tam_area_instrucoes;
    }
}
