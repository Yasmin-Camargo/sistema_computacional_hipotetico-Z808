
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

public class Montador {
    private Map<String, Integer> tabelaSimbolos;    // tabela de simbolos
    private int LC; // contador de linha
    private int PC; // contador de endereços
    private Map<Integer, Integer> dadoParaArmazenar;    // local para aramzenar dados que irão para memória de dados
    private int valorDiretivaORG;   // local para armazenar novo valor do PC caso seja utilizado a diretiva
    
    public Montador(String caminho_arquivo) throws IOException{
        this.tabelaSimbolos = new HashMap<>(); 
        this.LC = 0; 
        this.PC = 0; 
        this.valorDiretivaORG = 0;
        
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
            linha = linha.split(";")[0];
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
                case "move":

                // Diretivas
                case "end":
                case "equ":
                case "org":

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
                    
                    if (!tabelaSimbolos.containsKey(linhaSeparada[0])){   // verifica se rotulo já esta na tabela de simbolos
                        tabelaSimbolos.put(linhaSeparada[0], PC);
                    }
                    
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
                            tabelaSimbolos.put(linhaSeparada[0], Integer.valueOf(linhaSeparada[2]));
                            PC += 1;
                        } else{
                            PC += 2;
                        }
                    }
                   
                break;
            }
            textScanned[LC][0]  = String.valueOf(LC);   // coloca num linhas
            LC += 1;        // ataualiza contador de lihas
        } 
        PC += 1;
        
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
        System.out.println("\nTabela de Simbolos ");
        
        // Print da tabela de símbolos
        for (String chave : tabelaSimbolos.keySet()) {
            System.out.println(chave + "\t\t- " + tabelaSimbolos.get(chave));
        }
        
        return textScanned;
    }
    
    // Segundo passo do montador de dois passos: gerar código de máquina
    private void segundoPasso(String [][] textScanned) throws IOException{
        String nomeArquivo = ".\\src\\z808\\resources\\codigoObjeto.txt";
        dadoParaArmazenar = new HashMap<>() ;                               // dados definidos pelas label que devem ir para a memória
        int contadorInstrucao = 0, quantidadeDadosMemoriaParaArmazenar = 0;  

        try {
            FileWriter fileWriter = new FileWriter(nomeArquivo);        // Cria um objeto FileWriter para o arquivo específico
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); // Cria um objeto BufferedWriter para escrever no arquivo

            // Percorrendo tabela do codigo
            for (int i = 0; i < textScanned.length; i++) {
                if (!textScanned[i][2].equals("") && !textScanned[i][3].equals("equ")){     // coloca o endereco correto para a nossa area de dados
                    tabelaSimbolos.replace(textScanned[i][2], quantidadeDadosMemoriaParaArmazenar);
                    dadoParaArmazenar.put(quantidadeDadosMemoriaParaArmazenar, contadorInstrucao);
                } 
                switch (textScanned[i][3]) {
                    case "equ":
                        // armazena dados que vão ter que ser colocados na memória na área de dados
                        //dadoParaArmazenar.put(tabelaSimbolos.get(textScanned[i][2]), Integer.valueOf(textScanned[i][4]));
                    break;
                    case "add":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("03C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("03C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("04");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("05");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("04");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "sub":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("2BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("2BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("2C");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("2D");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("2C");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "div":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("F7C0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            bufferedWriter.write("F7F6");
                        } 
                        contadorInstrucao += 2;
                    break;
                    case "mul":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("F7F0");
                        } else  if (textScanned[i][5].equals("SI")){ // endereçamento via registrador SI
                            bufferedWriter.write("F7F5");
                        } 
                        contadorInstrucao += 2;
                    break;
                    case "cmp":                                   
                        if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("3BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("3C");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("3D");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("3C");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "and":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("23C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("23C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("24");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("25");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("24");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "or":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("0BC0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("0BC2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("0C");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("0D");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("0C");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    case "xor":                                   
                        if (textScanned[i][5].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("33C0");
                        } else  if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("33C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("34");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("35");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("34");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "not":                                   
                        bufferedWriter.write("F8C0");
                        contadorInstrucao += 2;
                    break;
                    case "jmp":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("EB");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("EB");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jz":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("74");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("74");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jnz":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("75");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("75");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "jp":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("7A");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("7A");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "call":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("E8");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("E7");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("E8");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "ret":                                   
                        bufferedWriter.write("EF");
                        contadorInstrucao += 1;
                    break;
                    case "end": 
                    case "hlt": 
                        bufferedWriter.write("EE");
                        contadorInstrucao += 1;
                    break;
                    case "pop":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("57C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("57C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("58");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("59");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("58");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "popf":                                   
                        bufferedWriter.write("9D");
                        contadorInstrucao += 1;
                    break;
                    case "push":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("50C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("50C2");
                        }
                        contadorInstrucao += 2;
                    break;
                    case "pushf":                                   
                        bufferedWriter.write("9C");
                        contadorInstrucao += 1;
                    break;
                    case "store":                                   
                        if (textScanned[i][4].equals("AX")){  // endereçamento via registrador AX
                            bufferedWriter.write("07C0");
                        } else  if (textScanned[i][4].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("07C2");
                        }
                        contadorInstrucao += 2;
                    break;
                    case "read":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("12");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("13");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("12");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "write":                                   
                        if (tabelaSimbolos.containsKey(textScanned[i][4])){  // é uma label
                            bufferedWriter.write("08");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][4])));
                        } else if (textScanned[i][4].contains("[")){ // endereçamento direto
                            bufferedWriter.write("09");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("08");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][4]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "move":                                   
                        if (textScanned[i][5].equals("DX")){ // endereçamento via registrador DX
                            bufferedWriter.write("16C2");
                        } else if (tabelaSimbolos.containsKey(textScanned[i][5])){  // é uma label
                            bufferedWriter.write("14");
                            bufferedWriter.write(formataPara16Bits(""+tabelaSimbolos.get(textScanned[i][5])));
                        } else if (textScanned[i][5].contains("[")){ // endereçamento direto
                            bufferedWriter.write("15");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5].replace("[", "").replace("]", "")));
                        } else {    // endereçamento imediato
                            bufferedWriter.write("14");
                            bufferedWriter.write(formataPara16Bits(textScanned[i][5]));
                        }
                        contadorInstrucao += 2;
                    break;
                    case "org": //  diretiva para indicar o endereço inicial onde o código deve ser carregado na memória durante a execução do programa
                        valorDiretivaORG = Integer.valueOf(formataPara16Bits(textScanned[i][4]));
                    break;
                }
            }
            
            // Fecha o BufferedWriter
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o arquivo: " + e.getMessage());
        }
        
        // PRINT PARA VER O QUE TÁ ACONTECENDO NO SEGUNDO PASSO
        // mostra o que foi armazenado no arquivo codigo objeto
        System.out.println("\nARQUIVO CODIGO OBJETO: ");
        try {
            FileReader fileReader = new FileReader(nomeArquivo);         // Cria um objeto para ler o arquivo
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linha;
           
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha); 
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
        System.out.println("\nTabela de Simbolos ");
        
        // Print da tabela de símbolos
        for (String chave : tabelaSimbolos.keySet()) {
            System.out.println(chave + "\t\t- " + tabelaSimbolos.get(chave));
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
        return dadoParaArmazenar;
    }
    
    public int gerValorDiretivaORG(){
        return valorDiretivaORG;
    }
}