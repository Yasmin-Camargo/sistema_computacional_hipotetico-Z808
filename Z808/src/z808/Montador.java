
// OBSERVAÇÕES PARA O ARQUVO DE ENTRADA
// 1) No endereçamento imediato para indicar que é um número colocar o "0" na frente
//      ex.: add AX, A      A vai ser interpretado como um label
//           add AX,0A      0A vai ser interpretado como o número 10

// 2) para indicar que é o modo de endereçamento direto (passar um endereço) usar "[]"
//      ex.: add AX, [20]


package z808;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Montador {
    private  Map<String, Integer> tabelaSimbolos;    // tabela de simbolos
    private int LC; // contador de linha
    private int PC; // contador de endereços
    
    public Montador(String caminho_arquivo) throws IOException{
        tabelaSimbolos = new HashMap<>(); 
        int LC = 0; 
        int PC = 0; 
        
        segundoPasso(primeiroPasso(caminho_arquivo));
    }
    
    // primeiro passo do montador de dois passos: criar tabela de simbolos
    private String [][] primeiroPasso(String caminho_arquivo) throws IOException{
        // matriz para estruturar o código                                                             colunas: [0]     [1]        [2]     [3]        [4]          [5]
        String [][] textScanned = new String [contarLinhasArquivo(caminho_arquivo)][6];    //          linha | endereço | label | operação | operando 1 | operando 2
        
        // Abre o arquivo
        BufferedReader arquivo = null;
        try {
            arquivo = new BufferedReader(new FileReader(caminho_arquivo));   
        } catch (IOException e) {
            System.out.println("Erro ao abrir arquivo");
            System.exit(0);
        }
        
        String linha = null;
        // Faz leitura do arquivo por linha
        while ((linha = arquivo.readLine()) != null){ 
            String[] linhaSeparada = linha.split("\\p{Zs}+"); // Separa o conteúdo da linha (indepedente do tamanho do espaço)
            textScanned[LC][1] = String.valueOf(PC);             // coloca na matriz do código em qual enedereço esta
            
            switch (linhaSeparada[0]) {
                // instruções
                case "add":
                case "div":
                case "sub":
                case "mul":
                case "cmp":
                case "and":
                case "not":
                case "or":
                case "xor":
                case "jmp":
                case "jz":
                case "jnz":
                case "jp":
                case "call":
                case "pop":
                case "popf":
                case "push":
                case "pushf":
                case "store":
                case "read":
                case "write":
                case "mov":

                // Diretivas
                case "END":
                case "SEGMENT":
                case "ENDS":
                case "DW":
                case "DUP":
                case "EQU":
                case "ORG":
                case "OFFSET":
                case "ASSUME":
                case "PROC":
                case "ENDP":

                //outros
                case "Dados":
                case "Codigo":
                case "Pilha":   // Caso 1: não tem label antes da instrução
                    textScanned[LC][2] = "";                                       
                    textScanned[LC][3] = linhaSeparada[0];                         // coloca na matriz o código a operação
                    if (linhaSeparada.length == 1){ // instrução não tem nenhum operando
                        textScanned[LC][4] = "";  
                        textScanned[LC][5] = "";
                        PC += 1;
                    }
                    else if (linhaSeparada[1].contains(",")){  // instrução contem dois operandos
                        textScanned[LC][4] = linhaSeparada[1].split(",")[0];      // coloca na matriz o código do operando 1
                        textScanned[LC][5] = linhaSeparada[1].split(",")[1];      // coloca na matriz o código do operando 2
                        PC += 3;
                        // verifica se o segundo operando é um label
                        if (!linhaSeparada[1].split(",")[1].equals("AX") && !linhaSeparada[1].split(",")[1].equals("DX") && !linhaSeparada[1].split(",")[1].equals("SI") && // se o operando 2 não é um registrador, 
                            !linhaSeparada[1].split(",")[1].contains("[") && (                                                              // não é endereçamento direto e o
                            !(Character.isDigit( linhaSeparada[1].split(",")[1].charAt(0))) )){                                       // primeiro caracter não é um número, então é um label
                              
                            if (!tabelaSimbolos.containsKey(linhaSeparada[1].split(",")[1])){   // verifica se label já esta na tabela de simbolos
                                tabelaSimbolos.put(linhaSeparada[1].split(",")[1], -1);    
                            }
                        }
                    } else {    // instrução contem um operando
                        textScanned[LC][4] = linhaSeparada[1];      // coloca na matriz o código o operando 1
                        textScanned[LC][5] = "";                    // não tem operando 2
                        PC += 2;
                    }
                    
                break;

                default:    // Caso 2: tem uma label antes da instrução
                    textScanned[LC][2] = linhaSeparada[0];                         // coloca na matriz o código a label identificada
                    textScanned[LC][3] = linhaSeparada[1];                         // coloca na matriz o código a operação
                   
                    if (linhaSeparada.length == 2){ // instrução não tem nenhum operando
                        textScanned[LC][4] = "";  
                        textScanned[LC][5] = "";
                        PC += 1;
                    }
                    else if (linhaSeparada[2].contains(",")){    // instrução contem dois operandos
                        textScanned[LC][4] = linhaSeparada[2].split(",")[0];      // coloca na matriz o código do operando 1
                        textScanned[LC][5] = linhaSeparada[2].split(",")[1];      // coloca na matriz o código do operando 2
                        PC += 3;
                        // verifica se o segundo operando é um label
                        if (!linhaSeparada[2].split(",")[1].equals("AX") && !linhaSeparada[2].split(",")[1].equals("DX") && !linhaSeparada[2].split(",")[1].equals("SI")  && ( // se o operando 2 não é um registrador e o
                            !(Character.isDigit( linhaSeparada[2].split(",")[1].charAt(0))) )){                 // primeiro caracter não é um número, então é um label
                              
                            if (!tabelaSimbolos.containsKey(linhaSeparada[2].split(",")[1])){   // verifica se rotulo já esta na tabela de simbolos
                                tabelaSimbolos.put(linhaSeparada[2].split(",")[1], -1);
                            }
                        }
                    } else {     // instrução contem um operando
                        textScanned[LC][4] = linhaSeparada[2];      // coloca na matriz o código o operando 1
                        textScanned[LC][5] = "";                    // não tem operando 2
                       
                        if (linhaSeparada[1].equals("equ")){
                            // VER: aqui pode ser também caracter ou outra expressão dentro da expressão
                            tabelaSimbolos.put(linhaSeparada[0], PC);
                            PC += 1;
                        } else{
                            PC += 2;
                        }
                    }
                    
                    if (!tabelaSimbolos.containsKey(linhaSeparada[0])){   // verifica se rotulo já esta na tabela de simbolos
                        tabelaSimbolos.put(linhaSeparada[0], LC);
                    }
                break;
            }
            textScanned[LC][0]  = String.valueOf(LC);   // coloca num linhas
            LC += 1;        // ataualiza contador de lihas
        } 
        
        // PARTE VISUAL PARA VER O QUE ESTA ACONTECENDO
        // Print da matriz do código
        System.out.println("linha\t\tendereco  |\tlabel\t\toperacao\toperando1\toperando2");
        for (int i = 0; i < textScanned.length; i++) {
            for (int j = 0; j < textScanned[i].length; j++) {
                System.out.print(textScanned[i][j] + "\t\t");
            }
            System.out.println();
        }
        
        // Print dos contadores
        System.out.println("\nLC =  "+LC+"\nPC =  "+ PC);
        System.out.println("\nTabela de Simbolos: ");
        
        // Print da tabela de símbolos
        for (String chave : tabelaSimbolos.keySet()) {
            System.out.println(chave + ":    " + tabelaSimbolos.get(chave));
        }
        
        return textScanned;
    }
    
    // Segundo passo do montador de dois passos: gerar código de máquina
    private void segundoPasso(String [][] textScanned) throws IOException{
        String nomeArquivo = "codigoObjeto.txt";

        try {
            FileWriter fileWriter = new FileWriter(nomeArquivo);        // Cria um objeto FileWriter para o arquivo específico
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); // Cria um objeto BufferedWriter para escrever no arquivo

            // Percorrendo tabela do codigo
            for (int i = 0; i < textScanned.length; i++) {
                switch (textScanned[i][3]) {
                    case "equ": 
                        bufferedWriter.write(textScanned[i][4]);
                    break;
                    case "add":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("03C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("03C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("05");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("05");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("04");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    break;
                    case "sub":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("2BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("2BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("2D");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("2D");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("2C");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    break;
                    case "div":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("F7C0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            bufferedWriter.write("F7F6");
                        } 
                    break;
                    case "mul":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("F7F0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            bufferedWriter.write("F7F5");
                        } 
                    break;
                    case "cmp":                                   
                        if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("3BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("3D");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("3D");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("3C");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    break;
                    case "and":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("23C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("23C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("25");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("25");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("24");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    break;
                    case "or":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("0BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("0BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("0D");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("0D");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("0C");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    case "xor":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("33C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("33C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("35");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("35");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("34");
                            bufferedWriter.write(textScanned[i][5]);
                        }
                    break;
                    case "not":                                   
                        bufferedWriter.write("F8C0");
                    break;
                    case "jmp":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("EB");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("EB");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        }
                    break;
                    case "jz":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("74");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("74");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        }
                    break;
                    case "jnz":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("75");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("75");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        }
                    break;
                    case "jp":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("7A");
                            bufferedWriter.write(""+tabelaSimbolos.get(textScanned[i][5]));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("7A");
                            bufferedWriter.write(textScanned[i][5].replace("[", "").replace("]", ""));
                        }
                    break;
                    
                }
            }
            
            // Fecha o BufferedWriter
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o arquivo: " + e.getMessage());
        }
        
        // LER ARQUIVO
        System.out.println("\nARQUIVO CODIGO OBJETO: ");
        try {
            // Cria um objeto FileReader para ler o arquivo
            FileReader fileReader = new FileReader(nomeArquivo);

            // Cria um objeto BufferedReader para ler o arquivo de forma mais eficiente
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Variável para armazenar cada linha lida do arquivo
            String linha;

            // Loop para ler cada linha do arquivo até encontrar o final (null)
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha); // Exibe a linha no console
            }

            // Fecha o BufferedReader
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
      
    }
    
    // Conta  o número de linhas do arquivo
    private int contarLinhasArquivo(String nomeArquivo) {
        int numeroLinhas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            while (br.readLine() != null) {
                numeroLinhas++;
            }
        } catch (IOException e) {
            System.out.println("Erro");
        }
        return numeroLinhas;
    }
}
