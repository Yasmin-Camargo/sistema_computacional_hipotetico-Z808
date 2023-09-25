
// OBSERVAÇÕES PARA O ARQUVO DE ENTRADA
// 1) No endereçamento imediato para indicar que é um número colocar o "0" na frente
//      ex.: add AX, A      A vai ser interpretado como um label
//           add AX,0A      0A vai ser interpretado como o número 10

// 2) para indicar que é o modo de endereçamento direto (passar um endereço) usar "[]"
//      ex.: add AX, [20]

// 3) para fazer um comentário usar ";"
//      ex.: add AX, [20]

// 4) diretiva EQU é constante então não vai para memória, já as labels que aparecem antes da instrução vão para memória
// 5) colocar diretiva END no final do programa para indicar fim de execução
// 6) usar diretiva ORG para alterar o endereço inicial onde o código deve ser carregado na memória durante a execução do programa

package z808;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class Montador {
    private Map<String, Integer> tabela_simbolos;    // tabela de simbolos
    private Map<Integer, Integer> dados_armazenar;    // local para armazenar dados que irão para memória de dados
    private int LC; // contador de linha
    private int PC; // contador de endereços
    private int diretiva_org;   // local para armazenar novo valor do PC caso seja utilizado a diretiva
    private JanelaMontador janela_montador;
    
    // public Montador(String caminho_arquivo) throws IOException{
    public Montador(String diretorio, String nome_arq) throws IOException{
        this.tabela_simbolos = new HashMap<>(); 
        this.dados_armazenar = new HashMap<>(); 
        this.LC = 0; 
        this.PC = 0; 
        this.diretiva_org = 0;
        
        segundoPasso(primeiroPasso(diretorio + "/" + nome_arq + "-montador.txt", nome_arq), diretorio + "/" + nome_arq);
    }
    
    // primeiro passo do montador de dois passos: criar tabela de simbolos
    private String [][] primeiroPasso(String caminho_arquivo, String nome_arq) throws IOException{
        // matriz para estruturar o código                                                             colunas: [0]     [1]        [2]     [3]        [4]          [5]
        String [][] texto_lido = new String [contarLinhasArquivo(caminho_arquivo)][6];    //          linha | endereço | label | operação | operando 1 | operando 2
        
        // Abre o arquivo
        BufferedReader arquivo = null;
        try {
            arquivo = new BufferedReader(new FileReader(caminho_arquivo));   
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "ERRO!",
                "Erro ao abrir arquivo.",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }
        
        String linha = null;
        // Faz leitura do arquivo por linha
        while ((linha = arquivo.readLine()) != null){ 
            linha = linha.split(";")[0];
            String[] linha_separada = linha.split("\\p{Zs}+"); // Separa o conteúdo da linha (indepedente do tamanho do espaço)
            texto_lido[LC][1] = String.valueOf(PC);             // coloca na matriz do código em qual endereço esta
            
            switch (linha_separada[0]) {
                // instruções
                case "add", "div", "sub", "mul", "cmp", "and", "not", "or", "xor", "jmp", "jz", "jnz", "jp", "call", "pop", "popf", "push", "pushf", "store", "read", "write", "move",
                    "end", "equ", "org", // diretivas
                    "Dados", "Codigo", "Pilha" -> { // Caso 1: não tem label antes da instrução
                    texto_lido[LC][2] = "";                                       
                    texto_lido[LC][3] = linha_separada[0];    // coloca na matriz o código a operação
                    if (linha_separada.length == 1){ // instrução não tem nenhum operando
                        texto_lido[LC][4] = "";  
                        texto_lido[LC][5] = "";
                        PC += 1;
                    }
                    else if (linha_separada[1].contains(",")){  // instrução contem dois operandos
                        texto_lido[LC][4] = linha_separada[1].split(",")[0];      // coloca na matriz o código do operando 1
                        texto_lido[LC][5] = linha_separada[1].split(",")[1];      // coloca na matriz o código do operando 2
                        PC += 3;
                        // verifica se o segundo operando é um label
                        if (!linha_separada[1].split(",")[1].equals("AX") 
                                && !linha_separada[1].split(",")[1].equals("DX") 
                                && !linha_separada[1].split(",")[1].equals("SI") // se o operando 2 não é um registrador, 
                                && !linha_separada[1].split(",")[1].contains("[") 
                                && (!(Character.isDigit(linha_separada[1].split(",")[1].charAt(0))) )) {      // primeiro caracter não é um número, então é um label
                            if (!tabela_simbolos.containsKey(linha_separada[1].split(",")[1])){   // verifica se label já esta na tabela de simbolos
                                tabela_simbolos.put(linha_separada[1].split(",")[1], -1);    
                            }
                        }
                    } else {    // instrução contem um operando
                        texto_lido[LC][4] = linha_separada[1];      // coloca na matriz o código o operando 1
                        texto_lido[LC][5] = "";                    // não tem operando 2
                        PC += 2;
                    }
                    
                }

                default -> {    // Caso 2: tem uma label antes da instrução
                    texto_lido[LC][2] = linha_separada[0];                         // coloca na matriz o código a label identificada
                    System.out.println("LC:" + LC);
                    //System.out.println("linha separada 0:" + linhaSeparada[0]);
                    //System.out.println("linha separada 1:" + linhaSeparada[1]);
                    texto_lido[LC][3] = linha_separada[1];                         // coloca na matriz o código a operação
                    
                    if (!tabela_simbolos.containsKey(linha_separada[0])){   // verifica se rotulo já esta na tabela de simbolos
                        tabela_simbolos.put(linha_separada[0], PC);
                    }
                    
                    if (linha_separada.length == 2){ // instrução não tem nenhum operando
                        texto_lido[LC][4] = "";  
                        texto_lido[LC][5] = "";
                        PC += 1;
                    }
                    else if (linha_separada[2].contains(",")){    // instrução contem dois operandos
                        texto_lido[LC][4] = linha_separada[2].split(",")[0];      // coloca na matriz o código do operando 1
                        texto_lido[LC][5] = linha_separada[2].split(",")[1];      // coloca na matriz o código do operando 2
                        PC += 3;
                        // verifica se o segundo operando é um label
                        if (!linha_separada[2].split(",")[1].equals("AX") 
                            && !linha_separada[2].split(",")[1].equals("DX")
                            && !linha_separada[2].split(",")[1].equals("SI")  
                            && !linha_separada[2].split(",")[1].contains("[") 
                            &&( !(Character.isDigit(linha_separada[2].split(",")[1].charAt(0))) ) ){ // primeiro caracter não é um número, então é um label
                              
                            if (!tabela_simbolos.containsKey(linha_separada[2].split(",")[1])){   // verifica se rotulo já esta na tabela de simbolos
                                tabela_simbolos.put(linha_separada[2].split(",")[1], -1);
                            }
                        }
                    } else {     // instrução contem um operando
                        texto_lido[LC][4] = linha_separada[2];      // coloca na matriz o código o operando 1
                        texto_lido[LC][5] = "";                    // não tem operando 2
                       
                        if (linha_separada[1].equals("equ")) {
                            // VER: aqui pode ser também caracter ou outra expressão dentro da expressão
                            tabela_simbolos.put(linha_separada[0], Integer.valueOf(linha_separada[2]));
                            PC += 1;
                        } else
                            PC += 2;
                    }
                   
                }
            }
            texto_lido[LC][0]  = String.valueOf(LC);   // coloca num linhas
            LC += 1;        // ataualiza contador de lihas
        } 
        PC += 1;
        
        // PARTE VISUAL PARA VER O QUE ESTA ACONTECENDO
        // Print da matriz do código
        System.out.println("linha\t\tendereco  |\tlabel\t\toperacao\toperando1\toperando2");
        for (int i = 0; i < texto_lido.length; i++) {
            for (int j = 0; j < texto_lido[i].length; j++) {
                System.out.print(texto_lido[i][j] + "\t\t");
            }
            System.out.println();
        }
        
        // Print dos contadores
        System.out.println("\nLC =  "+LC+"\nPC =  "+ PC);
        System.out.println("\nTabela de Simbolos ");
        
        // Print da tabela de símbolos
        for (String chave : tabela_simbolos.keySet()) {
            System.out.println(chave + ": " + tabela_simbolos.get(chave));
        }
        
        janela_montador = new JanelaMontador(texto_lido, LC, PC, tabela_simbolos, nome_arq);
        
        return texto_lido;
    }
    
    // Segundo passo do montador de dois passos: gerar código de máquina
    private void segundoPasso(String [][] textScanned, String caminho) throws IOException{
        String nome_arquivo = caminho + "-cod-obj.txt";
        // String nome_arquivo = ".\\src\\z808\\resources\\codigo_objeto.txt";
        // dados_armazenar = new HashMap<>() ;                               // dados definidos pelas label que devem ir para a memória
        int contadorInstrucao = 0, contadorDados = 0;  

        try {
            FileWriter arq = new FileWriter(nome_arquivo);        // Cria um objeto FileWriter para o arquivo específico
            BufferedWriter buffer = new BufferedWriter(arq); // Cria um objeto BufferedWriter para escrever no arquivo

            // Percorrendo tabela do codigo
            for (int i = 0; i < textScanned.length; i++) {
                if (!textScanned[i][2].equals("") && !textScanned[i][3].equals("equ")){     // coloca o endereco correto para a nossa area de dados
                    tabela_simbolos.replace(textScanned[i][2], contadorDados);
                    dados_armazenar.put(contadorDados, contadorInstrucao);
                    // dados_enderecos.put(contadorDados, true);
                    System.out.println("--> dados para armazenar: " + textScanned[i][2] + ": " + contadorDados +" "+contadorInstrucao);
                    contadorDados += 1;
                } 
                switch (textScanned[i][3]) {
                    case "equ":
                        // armazena dados que vão ter que ser colocados na memória na área de dados
                        //dadoParaArmazenar.put(tabelaSimbolos.get(textScanned[i][2]), Integer.valueOf(textScanned[i][4]));
                    break;
                    case "add":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("03C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("03C2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("04");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("05");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("04");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "sub":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("2BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("2BC2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("2C");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("2D");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("2C");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "div":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("F7C0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            buffer.write("F7F6");
                        } 
                        contadorInstrucao += 2;
                    break;
                    case "mul":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("F7F0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            buffer.write("F7F5");
                        } 
                        contadorInstrucao += 2;
                    break;
                    case "cmp":                                   
                        if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("3BC2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("3C");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("3D");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("3C");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "and":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("23C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("23C2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("24");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("25");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("24");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "or":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("0BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("0BC2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("0C");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("0D");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("0C");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    case "xor":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("33C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("33C2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("34");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("35");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("34");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "not":                                   
                        buffer.write("F8C0");
                        contadorInstrucao += 2;
                    break;
                    case "jmp":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("EB");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("EB");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jz":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("74");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("74");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jnz":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("75");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("75");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jp":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("7A");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("7A");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "call":                             
                        // função call que chama subrotinas 
                        // acho que no Ligador? tem que substituir o nome da subrotina pelo endereço efetivo dela  
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("E8");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("E7");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("E8");
                            buffer.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "ret":                                   
                        buffer.write("EF");
                        contadorInstrucao += 1;
                    break;
                    case "end": 
                    case "hlt": 
                        buffer.write("EE");
                        contadorInstrucao += 1;
                    break;
                    case "pop":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("57C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("57C2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("58");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("59");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("58");
                            buffer.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "popf":                                   
                        buffer.write("9D");
                        contadorInstrucao += 1;
                    break;
                    case "push":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("50C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("50C2");
                        }
                        contadorInstrucao += 2;
                    break;
                    case "pushf":                                   
                        buffer.write("9C");
                        contadorInstrucao += 1;
                    break;
                    case "store":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            buffer.write("07C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("07C2");
                        }
                        contadorInstrucao += 2;
                    break;
                    case "read":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("12");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("13");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("12");
                            buffer.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "write":                                   
                        if (tabela_simbolos.containsKey(textScanned[i][4])){  // é uma label
                            buffer.write("08");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            buffer.write("09");
                            buffer.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("08");
                            buffer.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "move":                                   
                        if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            buffer.write("16C2");
                        } else if (tabela_simbolos.containsKey(textScanned[i][5])){  // é uma label
                            buffer.write("14");
                            buffer.write(formataPara16Bits(""+tabela_simbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            buffer.write("15");
                            buffer.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            buffer.write("14");
                            buffer.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "org": //  diretiva para indicar o endereço inicial onde o código deve ser carregado na memória durante a execução do programa
                        diretiva_org = Integer.valueOf(formataPara16Bits(textScanned[i][4]));
                    break;
                }
            }
            
            System.out.println("instrucoes -> " + contadorInstrucao);
            System.out.println("dados -> " + contadorDados);
            buffer.write("\n#" + contadorInstrucao);
            buffer.write("\n#" + contadorDados);

            // Fecha o BufferedWriter
            buffer.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null, 
                "ERRO!", 
                "Ocorreu um erro ao criar o arquivo: " + e.getMessage(), 
                JOptionPane.ERROR_MESSAGE);
        }
        
        // PRINT PARA VER O QUE TÁ ACONTECENDO NO SEGUNDO PASSO
        // mostra o que foi armazenado no arquivo codigo objeto
        System.out.println("\nARQUIVO CODIGO OBJETO: ");
        try {
            FileReader fileReader = new FileReader(nome_arquivo);         // Cria um objeto para ler o arquivo
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linha;
           
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha); 
                janela_montador.addTexto("Arquivo código objeto:\n" + linha);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
        System.out.println("\nTabela de Simbolos ");
        
        // Print da tabela de símbolos
        for (String chave : tabela_simbolos.keySet()) {
            System.out.println(chave + ": " + tabela_simbolos.get(chave));
        }
    }
    
    // Conta  o número de linhas do arquivo
    private int contarLinhasArquivo(String nome_arquivo) {
        int numeroLinhas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(nome_arquivo))) {
            while (br.readLine() != null) {
                numeroLinhas++;
            }
        } catch (IOException e) {
            System.out.println("Erro");
        }
        return numeroLinhas;
    }
    
    // formata operandos de entrada do arquivo no executor para ter 16 bits (4 números em hexadecimal)
    private String formataPara16Bits(String operando) {
        String novo_operando = operando;
        if (operando.length() == 1){
            novo_operando = "000" + operando;
        } else if (operando.length() == 2){
            novo_operando = "00" + operando;
        } else if (operando.length() == 3){
            novo_operando = "0" + operando;
        } else if (operando.length() == 0){
            novo_operando = "0000";
        }
        return novo_operando;
    }
    
    public Map<Integer, Integer> getDadosParaArmazenar(){
        return dados_armazenar;
    }
    
    public int getValorDiretivaORG(){
        return diretiva_org;
    }
    
    public void matarJanela() {
        janela_montador.dispose();
    }
}