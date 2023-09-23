package z808;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;


public class Z808 {
    String caminho_macro, arq_final;
    String diretorio, arquivos[], arqs_simplif[];
    Registradores registrador;
    Memoria memoria;
    Montador montador;
    Ligador ligador;
    int tam_area_instrucoes, flag_jump, flag_att_tabelas, cont_instr = 0;
    
    public Z808(String diretorio, String[] arquivos, String[] arqs_simplif) throws IOException {
        this.diretorio = diretorio;
        this.arquivos = arquivos;
        this.arqs_simplif = arqs_simplif;
        iniciarZ808();
    }
    
    private void iniciarZ808() throws IOException {
        criarDiretorio();
        ProcessadorMacros macro = new ProcessadorMacros();
        for (int i = 0; i < arquivos.length; i++) {
        // for (String arq : arquivos) {
            try {
                System.out.println("--- PROCESSADOR DE MACROS ---\n");
                macro.processar(diretorio, arquivos[i], arqs_simplif[i]);
                System.out.println("--- MONTADOR ---\n");
                montador = new Montador(diretorio, arqs_simplif[i]);
                
                // String caminho_montador = macro.processar(caminho_macro);
                // diretorio = macro.processar(diretorio, arq);
                // System.out.println("--- MONTADOR ---\n");
                // montador = new Montador(diretorio, arq);
                // arq_final = ".\\src\\z808\\resources\\codigo_objeto.txt";
                // arq_final = diretorio + "/codigo_objeto.txt";
                // System.out.println("\n--- EXECUTOR ---\n");
                // registrador = new Registradores();
            }
            catch (Exception e ){
                System.out.print(e);
                System.exit(-1);
            } 
        }
        System.out.println("\n--- LIGADOR --- \n");
        ligador = new Ligador(diretorio, arqs_simplif);
        // arq_final = ligador.getArqFinal();
        arq_final = diretorio + "/" + arqs_simplif[0] + "-cod-obj.txt";
        System.out.println("--- EXECUTOR ---\n");
        registrador = new Registradores();
    }

    private void criarDiretorio() {
        File nova_pasta = new File(diretorio);
        if (!nova_pasta.exists()) {
            nova_pasta.mkdirs();
        } else {
            File[] arqs = nova_pasta.listFiles();
            for (File a : arqs) 
                a.delete();
            nova_pasta.delete();
            nova_pasta.mkdirs();
        }
    }
    
    public int[] carregarInstr() {
        tam_area_instrucoes = contarInstr(arq_final); // calcula quanto de espaço as instruções ocupam
        flag_jump = 0;
        
        memoria = new Memoria(tam_area_instrucoes, montador.getDadosParaArmazenar()); // cria memória 
        guardaInstr(arq_final, memoria);  // coloca dados do arquivo na memória
        
        // Inicialização dos registradores
        registrador.setCL(montador.getValorDiretivaORG());  // pega endereço (indice) da primeira instrução na memória
        registrador.setRI(memoria.lerCodigo(registrador.getCL())); // pega o código da instrução
        registrador.setIP(registrador.getCL() + 1); // atualiza o apontador de instrução para o endereço da próxima instrução
        
        // Prints
        System.out.println("\n-------------------\n");
        System.out.println("Registrador AX: "+registrador.getAX());
        System.out.println("Registrador DX: "+registrador.getDX());
        System.out.println("Registrador SR: "+registrador.print_SR());
        
        return memoria.getDadosMemoria();
    }
    
    public String[] executarPasso() {
        flag_att_tabelas = 0;
        cont_instr++;
        String instrucao = cont_instr + "\t", saida = "";
        flag_jump = 0;
        
        switch (registrador.getRI()){
            case 3 -> {
                int AX = registrador.getAX();
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {
                    instrucao += "add AX,AX";   
                    registrador.setAX(Instrucoes.add(AX, AX, registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao += "add AX,DX";   
                    registrador.setAX(Instrucoes.add(AX, registrador.getDX(), registrador));
                }
            }
            case 4 -> {
                instrucao += "add AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRI(), registrador));
            }
            case 5 -> {
                instrucao += "add AX,opd (direto)";  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getRBM(), registrador));
            }
            case 43 -> {
                int AX = registrador.getAX();
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "sub AX,AX";
                    registrador.setAX(Instrucoes.sub(AX, AX, registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao += "sub AX,DX";  
                    registrador.setAX(Instrucoes.sub(AX, registrador.getDX(), registrador));
                }
            }
            case 44 -> {
                instrucao += "sub AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRI(), registrador));
            }
            case 45 -> {
                instrucao += "sub AX,opd (direto)";  //!!! Considerando que o endereço digitado pelo usuário é exatamente onde esta o dado
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.sub(registrador.getAX(), registrador.getRBM(), registrador));
            }

            case 247 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                int AX = registrador.getAX();
                switch (registrador.getRI()) {  
                    case 246 -> {
                        instrucao += "div AX, SI";
                        registrador.setAX(Instrucoes.div(AX, registrador.getSI(), registrador));
                    }
                    case 192 -> { 
                        instrucao += "div AX, AX";
                        registrador.setAX(Instrucoes.div(AX, AX, registrador));
                    }
                    case 245 -> {
                        instrucao += "mul AX, SI";
                        registrador.setAX(Instrucoes.mult(AX, registrador.getSI(), registrador));
                    }
                    case 240 -> {
                        instrucao += "mul AX, AX";
                        registrador.setAX(Instrucoes.mult(AX, AX, registrador));
                    }
                }
            }
            case 60 -> {
                instrucao += "cmp AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                Instrucoes.cmp(registrador.getRI(), registrador);
            }
            case 61 -> {
                instrucao += "cmp AX,opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                Instrucoes.cmp(registrador.getRBM(), registrador);
            }
            case 59 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 194) {  
                    instrucao += "cmp AX,DX";
                    Instrucoes.cmp(registrador.getDX(), registrador);
                }
            }
             case 35 -> {
                 int AX = registrador.getAX();
                 att3Registr(registrador, memoria); // lê próximo código da memória
                 if (registrador.getRI() == 192) {
                     instrucao += "and AX,AX";
                     registrador.setAX(Instrucoes.and(AX, AX, registrador));
                 } else if (registrador.getRI() == 194) {
                     instrucao += "and AX,DX";
                     registrador.setAX(Instrucoes.add(AX, registrador.getDX(), registrador));
                 }
            }
            case 36 -> {
                instrucao += "and AX,opd (direto)";        
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.and(registrador.getAX(), registrador.getRI(), registrador));
            }
            case 37 -> {
                instrucao += "and AX,opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.and(registrador.getAX(), registrador.getRBM(), registrador));
            }               
            case 11 -> {
                int AX = registrador.getAX();
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "or AX,AX";
                    registrador.setAX(Instrucoes.or(AX, AX, registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao += "or AX,DX";  
                    registrador.setAX(Instrucoes.or(AX, registrador.getDX(), registrador));
                }
            }
            case 12 -> {
                instrucao += "or AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getRI(), registrador));
            }
            case 13 -> {
                instrucao += "or AX,opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.or(registrador.getAX(), registrador.getRBM(), registrador));
            }
            case 248 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "not AX";
                    registrador.setAX(Instrucoes.not(registrador.getAX(), registrador));
                }
            }
            case 51 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "xor AX,AX";
                    registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getAX(), registrador));
                } else if (registrador.getRI() == 194) {
                    instrucao += "xor AX,DX";  
                    registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getDX(), registrador));
                }
            }
            case 52 -> {
                instrucao += "xor AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getRI(), registrador));
            }
            case 53 -> {
                instrucao += "xor AX,opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(Instrucoes.xor(registrador.getAX(), registrador.getRBM(), registrador));
            }
            case 235 -> {
                instrucao += "jmp opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jmp(registrador.getRBM(), registrador, memoria);
            }
            case 116 -> {
                instrucao += "jz opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jz(registrador.getRBM(), registrador, memoria);
            }
            case 117 -> {
                instrucao += "jnz opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jnz(registrador.getRBM(), registrador, memoria);
            }
            case 122 -> {
                instrucao += "jp opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                flag_jump = Instrucoes.jp(registrador.getRBM(), registrador, memoria);
            }
            case 232 -> {
                instrucao += "call opd (imediato)";        
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                memoria.push_pilha(registrador.getIP());
                registrador.setIP(registrador.getRI());
                flag_att_tabelas = 1;
            }
            case 231 -> {
                instrucao += "jp opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                memoria.push_pilha(registrador.getIP());
                registrador.setIP(registrador.getRBM());
                flag_att_tabelas = 1;
            }
            case 239 -> {
                instrucao += "ret";
                registrador.setSP(memoria.pop_pilha());
                flag_att_tabelas = 1;
            }
            case 87 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "pop AX";
                    registrador.setAX(memoria.pop_pilha());
                } else if (registrador.getRI() == 194) {
                    instrucao += "pop DX";  
                    registrador.setDX(memoria.pop_pilha());
                }
                flag_att_tabelas = 1;
            }
            case 88 -> {
                instrucao += "pop opd (imediato)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                flag_att_tabelas = 1;
            }
            case 89 -> {
                instrucao += "pop opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereço (16 bits)
                memoria.escreverDados(memoria.pop_pilha(), registrador.getRI());
                flag_att_tabelas = 1;
            }
            case 157 -> {
                instrucao += "popf";
                registrador.separarSR(memoria.pop_pilha());
                flag_att_tabelas = 1;
            }
            case 80 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "push AX";
                    memoria.push_pilha(registrador.getAX());
                } else if (registrador.getRI() == 194) {
                    instrucao += "push DX";  
                    memoria.push_pilha(registrador.getDX());
                }
                flag_att_tabelas = 1;
            }
            case 156 -> {
                instrucao += "pushf";
                memoria.push_pilha(registrador.juntarSR());
                flag_att_tabelas = 1;
            }
            case 7 -> {
                att3Registr(registrador, memoria); // lê próximo código da memória
                if (registrador.getRI() == 192) {  
                    instrucao += "store AX";
                    Instrucoes.store(registrador.getAX(), memoria);
                } else if (registrador.getRI() == 194) {
                    instrucao += "store DX";  
                    Instrucoes.store(registrador.getDX(), memoria);
                }
                flag_att_tabelas = 1;
            } 
            case 18 -> {
                instrucao += "read opd (imediato)";
                att3Registr(registrador, memoria); //leitura do endereco (16 bits)
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
            }
            case 19 -> {
                instrucao += "read opd (direto)";
                att3Registr(registrador, memoria); //leitura do endereco (16 bits)
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
            }
            case 9 -> {
                instrucao += "write opd (direto)"; // primeiro vai na memória no endereço indicado na instrução, lê o dado, neste dado esta o endereço que quer acessar
                att3Registr(registrador, memoria); //leitura do endereco (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                saida = "dataAreaMemory[" + (registrador.getRBM() + memoria.getInicioSegmentoDados())+ "] = " + memoria.lerDados(registrador.getRBM());
            }
            case 8 -> {
                instrucao += "write opd (imediato)"; // mostra para o usuário valor armazenado no endereço de memória indicado na instrução
                att3Registr(registrador, memoria); //leitura do endereco (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                saida = "dataAreaMemory[" + (memoria.getInicioSegmentoDados() + registrador.getREM()) + "] = " + registrador.getRBM();
            }
            case 20 -> {
                instrucao += "move AX,opd (imediato)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setAX(registrador.getRI());
            }   
            case 21 -> {
                instrucao += "move AX,opd (direto)";
                att3Registr(registrador, memoria); //leitura do operando (16 bits)
                registrador.setREM(registrador.getRI()); // Coloca endereço no registrador de endereço de memória
                registrador.setRBM(memoria.lerDados(registrador.getREM())); // coloca conteudo no registrador de Buffer da Memória
                registrador.setAX(registrador.getRBM());
            }
            case 22 -> {
                if (registrador.getRI() == 194) {
                    instrucao += "move AX,DX";  
                    att3Registr(registrador, memoria); //leitura do operando (16 bits)
                    registrador.setDX(registrador.getAX());
                }
            }
            case 238 -> {
                instrucao += "hlt";
                registrador.setIP(tam_area_instrucoes);
                flag_jump = 1;
            }
            default -> {
                instrucao = "ERRO: Instrução ["+registrador.getRI()+"] não é aceita.";
                JOptionPane.showMessageDialog(
                    null, 
                    "Instrução ["+registrador.getRI()+"] não é aceita.", 
                    "ERRO", 
                    JOptionPane.ERROR_MESSAGE
                );
                return new String[] {instrucao, saida, "0"};
            }
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
    
    public String[] executar() {
         String[] aux, retorno = {"", "", "1"};
        // while que percorre a area de codigo(instruções) da memória
        while (registrador.getIP() != tam_area_instrucoes){
            flag_jump = 0;
            aux = executarPasso();
            if(aux[0].contains("ERRO")) break;
            else {
                retorno[0] += (aux[0] + "\n");
                if (!aux[1].equals(""))
                    retorno[1] += (aux[1] + "\n");
            }
        }
        
        memoria.print();
        return retorno;
    }
    
    // Atualiza Contador de Localização, Registrador de Instruções e o Apontador de instrução
    // Antigo Atualizar_CL_RI_IP()
    public void att3Registr(Registradores registrador, Memoria memoria){
        registrador.setCL(registrador.getIP());
        registrador.setRI(memoria.lerCodigo(registrador.getCL()));
        registrador.setIP(registrador.getIP() + 1);
        Instrucoes.verificaAX(registrador.getAX(), registrador);
    }
    
    // Conta quantos espaços de memória deverão ser reservados para área de instruções
    public int contarInstr(String caminho_arquivo){
        char[] opd = new char[4];   //16 bits
        char[] ch = new char[2];    //8 bits
        int flag_final_arquivo = 0, quantidade_memoria = 0;
        String instrucao;
        
        File arquivo = new File(caminho_arquivo);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao abrir o arquivo.", 
                "ERRO", 
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            try {
                FileInputStream arq_leitura = new FileInputStream(arquivo);
                while (flag_final_arquivo != 1) {
                    //lê primeiro byte (8 bits) da instrução
                    ch[0] = (char) arq_leitura.read();
                    ch[1] = (char) arq_leitura.read();
                    quantidade_memoria += 1;
                    if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF') { // Verifica se chegou no Fim do arquivo
                        flag_final_arquivo = 1;
                    } else {
                         instrucao = new String(ch);
                        
                        // Instruções que armazenam + 8 bits
                        switch (instrucao) {
                            case "03", "F7", "2B", "3B", "23", "F8", "0B", "33", "57", "50", "07", "16" -> {
                                ch[0] = (char) arq_leitura.read();
                                ch[1] = (char) arq_leitura.read();
                                quantidade_memoria += 1;
                            }
                            case "05", "04", "2D", "2C", "3D", "3C", "25", "24", "0D", "0C", "35", "34", "EB", "74", "75", "7A", "E8", "E7", "59", "58", "12", "13", "08", "09", "14", "15" -> {
                                //leitura do operando (16 bits)
                                opd[0] = (char) arq_leitura.read();
                                if (opd[0] == '-'){              //verifica se é um número negativo
                                    opd[0] = (char) arq_leitura.read();
                                }
                                opd[1] = (char) arq_leitura.read();
                                opd[2] = (char) arq_leitura.read();
                                opd[3] = (char) arq_leitura.read();
                                quantidade_memoria += 1;
                            }
                            case "EF", "EE", "9D", "9C" -> {
                            }
                        }
                    }
                }
                arq_leitura.close();
            } catch (IOException e) {
                System.out.println("Erro: ao manipular o arquivo");
            }
        }
        return (quantidade_memoria);
    }
    
    // Coloca cada uma das intruções na memória
    public void guardaInstr(String caminho_arquivo, Memoria memoria){
        char[] opd = new char[4];
        char[] ch = new char[2];
        int flag_final_arquivo = 0, quant_instrucoes = 0, numNegativo = 0;
        String instrucao;
        
        File arquivo = new File(caminho_arquivo);
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(null, "Erro: falha ao abrir o arquivo");
        } else {
            try {
                FileInputStream arq_leitura = new FileInputStream(arquivo);
                while (flag_final_arquivo != 1) {
                    //lê primeiro byte (8 bits) da instrução
                    ch[0] = (char) arq_leitura.read();
                    ch[1] = (char) arq_leitura.read();
                  
                    // Verifica se chegou no Fim do arquivo
                    if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF') {
                        flag_final_arquivo = 1;
                    } else {
                         instrucao = new String(ch);
                        //  System.out.println("instrucao -----> " + instrucao);
                        //  if (instrucao != "\n#") {
                             memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                             quant_instrucoes += 1;
                        //  }

                        // Instruções que armazenam + 8 bits
                        switch (instrucao) {
                            case "03", "F7", "2B", "3B", "23", "F8", "0B", "33", "57", "50", "07", "16" -> {
                                ch[0] = (char) arq_leitura.read();
                                ch[1] = (char) arq_leitura.read();
                                instrucao = new String(ch);
                                memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                                quant_instrucoes += 1;
                            }
                            case "05", "04", "2D", "2C", "3D", "3C", "25", "24", "0D", "0C", "35", "34", "EB", "74", "75", "7A", "E8", "E7", "59", "58", "12", "13", "08", "09", "14", "15" -> {
                                opd[0] = (char) arq_leitura.read();
                                if (opd[0] == '-'){ //verifica se é um número negativo
                                    opd[0] = (char) arq_leitura.read();
                                    numNegativo = 1;
                                }   opd[1] = (char) arq_leitura.read();
                                opd[2] = (char) arq_leitura.read();
                                opd[3] = (char) arq_leitura.read();
                                instrucao = new String(opd);
                                if (numNegativo == 1){
                                    numNegativo = 0;
                                    memoria.escreverCodigo(quant_instrucoes, -((int) Integer.parseInt(instrucao, 16)));
                                } else {
                                    memoria.escreverCodigo(quant_instrucoes, (int) Integer.parseInt(instrucao, 16));
                                }   quant_instrucoes += 1;
                            }
                            case "EF", "EE", "9D", "9C" -> {
                            }
                            // case "#" -> {
                            //     System.out.println("aaaaaaaaaaaaaa");
                            //     char a = '#';
                            //     do {
                            //         a = (char) arq_leitura.read();
                            //         System.out.println(a);
                            //     } while (a != '\n');
                            //     }
                            // }
                            default -> {
                                // System.out.println("instrucao --> " + instrucao);
                            }
                        }
                    }
                }
                arq_leitura.close();
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

  public void attMemoria(Memoria memoria) {
        this.memoria = memoria;
    }
    
    public Registradores getRegistr() {
        return registrador;
    }
    
    public int getQuantInstr() {
        return tam_area_instrucoes;
    }
    
    public String getCaminhoMacro() {
        return caminho_macro;
    }
    
    public void matarJanelaMontador() {
        montador.matarJanela();
        
        // @ JULIA COLOCA UMA ASCII DE FAQUINHA AQUI
        
    }
}
