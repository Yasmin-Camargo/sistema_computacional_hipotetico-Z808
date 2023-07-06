/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package z808;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.System.exit;
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
    JanelaZ808 gui;
    
    public Z808(String caminho_arquivo) {
        this.caminho_arquivo = caminho_arquivo;
        gui = new JanelaZ808(caminho_arquivo);
        iniciarZ808();
    }
    
    public void iniciarZ808() {
        Registradores registrador = new Registradores();
        
        int tam_area_instrucoes = conta_quantidade_instrucoes(caminho_arquivo); // Para criar uma memória deve-se saber primeiramente quanto de espaço ocupa as instruções
        int flag_jump = 0;
        
        Memoria memoria = new Memoria(tam_area_instrucoes); // cria memória com o espaco para as instruções já definido
        armazena_instrucoes(caminho_arquivo, memoria);  // coloca dados do arquivo na memória
        
        // Inicialização dos registradores
        registrador.setCL(memoria.getInicioSegmentoInstrucoes());  // pega endereço (indice) da primeira instrução na memória
        registrador.setRI(memoria.lerCodigo(registrador.getCL())); // pega o código da instrução
        registrador.setIP(registrador.getCL() + 1); // atualiza o apontador de instrução para o endereço da próxima instrução
        gui.atualizarRegCL(registrador.getCL());
        gui.atualizarRegRI(registrador.getRI());
        gui.atualizarRegIP(registrador.getIP());

        // while que percorre a area de codigo(instruções) da memória
        while (registrador.getIP() != tam_area_instrucoes){
            flag_jump = 0;
            switch (registrador.getRI()){
                case 3: // add AX,AX  e add AX,DX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) { 
                        System.out.println("add AX,AX ");   
                        registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getAX(), registrador));
                        gui.atualizarRegAX(registrador.getAX());
                    } else if (registrador.getRI() == 194) {
                        System.out.println("add AX,DX ");   
                        registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getDX(), registrador));
                        gui.atualizarRegAX(registrador.getAX());
                    }
                    break;
                
                case 4: // add AX, opd  (imediato)
                    System.out.println("add AX,opd (imediato)");  
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRI(), registrador));
                    gui.atualizarRegAX(registrador.getAX());
                    break;
                    
                case 5: // add AX, opd  (direto)
                    System.out.println("add AX,opd (direto)");  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRBM(), registrador));  
                    break;
                    
                case 43: // sub AX,AX e sub AX,DX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("sub AX,AX ");
                        registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getAX(), registrador));
                        gui.atualizarRegAX(registrador.getAX());
                    } else if (registrador.getRI() == 194) {
                        System.out.print("sub AX,DX ");  
                        registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getDX(), registrador));
                        gui.atualizarRegAX(registrador.getAX());
                    }
                    break;
                 
                case 44: // sub AX,opd  (imediato)
                    System.out.println("sub AX,opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRI(), registrador));
                    gui.atualizarRegAX(registrador.getAX());
                    break;
                    
                case 45: // sub AX,opd  (direto)
                    System.out.println("add AX,opd (direto)");  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRBM(), registrador));  
                    break;
                
                case 247: // div AX, SI | div AX, AX | mul AX, SI | mul AX, AX 
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 246) {  
                        System.out.println("div AX, SI ");
                        registrador.setAX(Instrucoes.div(registrador.getAX(), registrador.getSI(), registrador));
                    } else if (registrador.getRI() == 192) {
                        System.out.print("div AX, AX "); 
                        registrador.setAX(Instrucoes.div(registrador.getAX(), registrador.getAX(), registrador));
                    } else if (registrador.getRI() == 246) {  
                        System.out.println("mul AX, SI ");
                        registrador.setAX(Instrucoes.mult(registrador.getAX(), registrador.getSI(), registrador));
                    } else if (registrador.getRI() == 240) {
                        System.out.print("mul AX, AX "); 
                        registrador.setAX(Instrucoes.mult(registrador.getAX(), registrador.getAX(), registrador));                    }
                    break;
                    
                case 60: // cmp AX,opd (imediato)
                    System.out.println("cmp AX,opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    Instrucoes.cmp(registrador.getRI(), registrador);
                    break;
                    
                case 61: // cmp AX,opd (direto)
                    System.out.println("cmp AX,opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    Instrucoes.cmp(registrador.getRBM(), registrador);
                    break;
                    
                case 59: // cmp AX,DX 
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 194) {  
                        System.out.println("cmp AX,DX ");
                        Instrucoes.cmp(registrador.getDX(), registrador);
                    }
                    break;
                    
                // ----> Instruções lógicas
                case 35: // and AX,AX e and AX,DX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("and AX,AX ");
                        //ADICIONAR
                    } else if (registrador.getRI() == 194) {
                        System.out.print("and AX,DX ");  
                        //ADICONAR
                    }
                    break;
                    
                case 36: // and AX,opd (imediato)
                    System.out.println("and AX,opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    //ADICIONAR
                    break;
                    
                case 37: // and AX,opd (direto)
                    System.out.println("and AX,opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    //ADICIONAR
                    break;               
                
                case 11: // or AX,AX e or AX,DX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("or AX,AX ");
                        //ADICIONAR
                    } else if (registrador.getRI() == 194) {
                        System.out.print("or AX,DX ");  
                        //ADICONAR
                    }
                    break;
                    
                case 12: // or AX,opd (imediato)
                    System.out.println("or AX,opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    //ADICIONAR
                    break;
                
                case 13: // or AX,opd (direto)
                    System.out.println("or AX,opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    //ADICIONAR
                    break;
                    
                case 248: // not AX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("not AX ");
                        //ADICIONAR
                    }
                    break;
                    
                case 51: // xor AX,AX e xor AX,DX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("xor AX,AX ");
                        //ADICIONAR
                    } else if (registrador.getRI() == 194) {
                        System.out.print("xor AX,DX ");  
                        //ADICONAR
                    }
                    break;
                    
                case 52: // xor AX,opd (imediato)
                    System.out.println("xor AX,opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    //ADICIONAR
                    break;
                
                case 53: // xor AX,opd (direto)
                    System.out.println("xor AX,opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    //ADICIONAR
                    break;
                
                // ----> Instruções de desvio 
                case 235: // jmp opd (direto)       
                    System.out.println("jmp opd (direto)");
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    flag_jump = Instrucoes.jmp(registrador.getRBM(), registrador, memoria);
                    break;
                
                case 116: // jz opd (direto)
                    System.out.println("jz opd (direto)");    
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    flag_jump = Instrucoes.jz(registrador.getRBM(), registrador, memoria);
                    break;
                    
                case 117: // jnz opd (direto)
                    System.out.println("jnz opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    flag_jump = Instrucoes.jnz(registrador.getRBM(), registrador, memoria);
                    break;
                    
                case 122: // jp opd (direto)
                    System.out.println("jp opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    flag_jump = Instrucoes.jp(registrador.getRBM(), registrador, memoria);
                    break;
                    
                // ----> Instruções da pilha 
                case 232: // call opd (imediato)
                    System.out.println("call opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    memoria.push_pilha(registrador.getIP());
                    registrador.setIP(registrador.getRI());
                    break;
                    
                case 231: // call opd (direto)
                    System.out.println("jp opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    memoria.push_pilha(registrador.getIP());
                    registrador.setIP(registrador.getRBM());
                    break;
                    
                case 239: // ret
                    System.out.println("ret");   
                    registrador.setSP(memoria.pop_pilha());
                    break;
                    
                case 87: // pop DX e pop AX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("pop AX ");
                        registrador.setAX(memoria.pop_pilha());
                    } else if (registrador.getRI() == 194) {
                        System.out.print("pop DX ");  
                        registrador.setDX(memoria.pop_pilha());
                    }
                    break;
                    
                case 88: // pop opd (imediato)
                    System.out.println("pop opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                    break;
                
                case 89: // pop opd (direto)
                    System.out.println("pop opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                    break;
                    
                case 157: // popf 
                    System.out.println("popf");
                    registrador.desconcatena_SR(memoria.pop_pilha());
                    break;
                    
                case 80: // push DX e push AX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("push AX ");
                        memoria.push_pilha(registrador.getAX());
                    } else if (registrador.getRI() == 194) {
                        System.out.print("push DX ");  
                        memoria.push_pilha(registrador.getDX());
                    }
                    break;
                    
                case 156: // pushf 
                    System.out.println("pushf");   
                    memoria.push_pilha(registrador.concatena_SR());
                    break;
                    
                // ----> Memória
                case 7: // store DX e store AX
                    atualiza_CL_RI_IP(registrador, memoria); // lê próximo código da memória
                    if (registrador.getRI() == 192) {  
                        System.out.println("store AX ");
                        Instrucoes.store(registrador.getAX(), memoria);
                    } else if (registrador.getRI() == 194) {
                        System.out.print("store DX ");  
                        Instrucoes.store(registrador.getDX(), memoria);
                    }
                    break; 
                    
                case 18: // read opd (imediato)
                    System.out.println("read opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setAX(Instrucoes.read(registrador.getRI(), memoria)); // coloca conteudo no registrador de Buffer da Memória
                    break;
                
                case 19: // read opd (direto)
                    System.out.println("read opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setAX(Instrucoes.read(registrador.getRI(), memoria)); // coloca conteudo no registrador de Buffer da Memória
                    break;
                    
                case 9: // write opd (direto)
                    System.out.println("write opd (direto)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do endereco (16 bits)atualiza_CL_RI_IP(registrador, memoria); //leitura do endereço (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    Instrucoes.store(registrador.getRBM(), memoria);
                    break;
                
                case 8: // write opd (imediato)
                    System.out.println("write opd (imediato)");        
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    Instrucoes.store(registrador.getRI(), memoria);
                    break;
                
                case 21:    // move AX,opd (direto)
                    System.out.println("move AX,opd (direto)");  
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setAX(registrador.getRI());
                    break;   
                      
                case 20:    // move AX,opd (imediato)
                    System.out.println("move AX,opd (direto)");  
                    atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setREM(registrador.getCL()); // Coloca endereço no registrador de endereço de memória
                    registrador.setRBM(memoria.lerCodigo(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                    registrador.setAX(registrador.getRBM());
                    break;
                    
                case 22:    // move AX,DX
                    if (registrador.getRI() == 194) {
                        System.out.println("move AX,DX");  
                        atualiza_CL_RI_IP(registrador, memoria); //leitura do operando (16 bits)
                        registrador.setDX(registrador.getAX());
                    }
                    break;
                    
                case 238: // hlt
                    System.out.println("hlt");  
                    exit(0);
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(null, "Intrução [" + registrador.getRI() + "] não é aceita");
                    break;
            }
            
            // se o jump foi acionado não atualiza apontadores de instrução
            if (flag_jump == 0){
                // atualiza apontadores de instruções
                registrador.setCL(registrador.getIP());
                registrador.setRI(memoria.lerCodigo(registrador.getCL()));
                registrador.setIP(registrador.getIP() + 1);
            }
            
            gui.atualizarRegCL(registrador.getCL());
            gui.atualizarRegRI(registrador.getRI());
            gui.atualizarRegIP(registrador.getIP());  
            //
            //
            // SE O INICIO DE TODOS OS CASOS CHAMA ATUALIZA_CL_RI_IP
            // ISSO NAO FICARIA ERRADO?
            //
        }
        
        // Prints
        System.out.println("\n-------------------\n");
        System.out.println("Registrador AX: "+registrador.getAX());
        System.out.println("Registrador DX: "+registrador.getDX());
        System.out.println("Registrador SR: "+registrador.print_SR());
        memoria.printAreaCodigo();
        memoria.printAreaDados();
        memoria.printPilha();
    }
    
    // Atualiza Contador de Localização, Registrador de Instruções e o Apontador de instrução
    public void atualiza_CL_RI_IP(Registradores registrador, Memoria memoria){
        registrador.setCL(registrador.getIP());
        registrador.setRI(memoria.lerCodigo(registrador.getCL()));
        registrador.setIP(registrador.getIP() + 1);
        gui.atualizarRegCL(registrador.getCL());
        gui.atualizarRegRI(registrador.getRI());
        gui.atualizarRegIP(registrador.getIP());  
    }
    
    // Conta quantos espaços de memória deverão ser reservados para área de instruções
    public int conta_quantidade_instrucoes(String caminho_arquivo){
        char[] opd = new char[4];   //16 bits
        char[] ch = new char[2];    //8 bits
        int flag_final_arquivo = 0, quantidade_memoria = 0;
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
        int flag_final_arquivo = 0, quant_instrucoes = 0;
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
                            opd[1] = (char) arquivo_leitura.read();
                            opd[2] = (char) arquivo_leitura.read();
                            opd[3] = (char) arquivo_leitura.read();
                            instrucao = new String(opd);
                            memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                            quant_instrucoes += 1;
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
    }
}
