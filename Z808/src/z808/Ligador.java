package z808;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ligador {
    // private Map<String, Integer> tabela_global = new HashMap<>();
    private String diretorio;
    private ArrayList<String> arquivos_simplif;
    private ArrayList<Montador> montadores;

    private Map<Integer, Integer> dados;

    private int endereco_base, desloc_instr, desloc_dados;
    private String instrucoes_final;

    Ligador(String diretorio, ArrayList<String> arquivos_simplif, ArrayList<Montador> montadores) throws IOException {
        // o primeiro arquivo de arquivos_simplif é o arquivo principal
        // usar o endereço dele para o primeiro arquivo!
        this.diretorio = diretorio;
        this.montadores = montadores;
        this.arquivos_simplif = arquivos_simplif;
        this.dados = new HashMap<>();
        ligarModulos();
    }

    private void ligarModulos() throws IOException {
        this.instrucoes_final = "";
        this.desloc_instr = 0;
        this.desloc_dados = 0;

        // LEMBRAR --- primeiro arquivo é o Main!
        acessarModulo(diretorio + "\\" + arquivos_simplif.get(0) + "-cod-obj.txt", montadores.get(0), true);
        
        // LEMBRAR --- os outros são secundários
        for (int i = 1; i < arquivos_simplif.size(); i++) {
            acessarModulo(diretorio + "\\" + arquivos_simplif.get(i) + "-cod-obj.txt",  montadores.get(i), false);
        }

        salvarArquivoFinal();
    }

    private void acessarModulo(String caminho, Montador montador, Boolean eh_principal) throws IOException {
        String instrucoes = "", cont_instrucoes = "", cont_dados = "";
        // remove quant de instrucoes e dados inseridos no arquivo de codigo objeto
        try {
            File f1 = new File(caminho);
            FileReader fr = new FileReader(f1);
            BufferedReader leitor = new BufferedReader(fr);
            instrucoes = leitor.readLine();
            cont_instrucoes = leitor.readLine();
            cont_dados = leitor.readLine();
            fr.close();
            leitor.close();

            if (eh_principal) {
                System.out.println("Main file!");
                System.out.println(cont_instrucoes);
                // System.out.println(cont_dados);
                endereco_base = Integer.parseInt(cont_instrucoes.replace("#", ""));
                int contador_dados = 0;
                for (Integer chave : montador.getDadosParaArmazenar().keySet()) {
                    dados.put(contador_dados, (montador.getDadosParaArmazenar().get(chave)));
                    contador_dados += 1;
                }
            } else {
                instrucoes = modificarEnderecos(instrucoes);
                int contador_dados = 0;
                for (Integer chave : montador.getDadosParaArmazenar().keySet()) {
                    dados.put(contador_dados, (montador.getDadosParaArmazenar().get(chave) + endereco_base));
                    contador_dados += 1;
                }
            }

            FileWriter fw = new FileWriter(f1);
            BufferedWriter escritor = new BufferedWriter(fw);
            escritor.write(instrucoes);
            escritor.flush();
            escritor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        

        // int contador_dados = 0;
        // for (Integer chave : montador.getDadosParaArmazenar().keySet()) {
        //     dados.put(contador_dados, montador.getDadosParaArmazenar().get(chave));
        //     contador_dados += 1;
        //     // System.out.println(montador.getDadosParaArmazenar().get(chave));
        // }

        instrucoes_final += instrucoes;
        desloc_instr += Integer.parseInt(cont_instrucoes.replace("#", ""));
        desloc_dados += Integer.parseInt(cont_dados.replace("#", ""));

        System.out.println(instrucoes_final + "\n" + desloc_instr +"\n"+ desloc_dados);
    }

    private void salvarArquivoFinal() throws IOException {
        String nome_arquivo = diretorio + "\\cod-obj-final.txt";
        FileWriter arq = new FileWriter(nome_arquivo);       
        BufferedWriter escritor = new BufferedWriter(arq);
        escritor.write(instrucoes_final);
        escritor.close();
    }

    public int getContagemInstrucoes() {
        return desloc_instr;
    }

    public Map<Integer,Integer> getDados() {
        return dados;
    }

    private String modificarEnderecos(String instrucoes) {
        // Instruções que armazenam + 8 bits
        // só preciso consumir as instruções
        // e quando for endereçamento direto, alterar os valores do endereço
        char[] opd = new char[4];
        char[] ch = new char[2];

        int cont_char = 0, endereco;
        
        ch[0] = (char) instrucoes.charAt(cont_char++);
        ch[1] = (char) instrucoes.charAt(cont_char++);
        
        String codigo = new String(ch);

        switch (codigo) {
            case "03", "07", "0B", "16", "23", "2B", "3B", "33", "50", "57", "F7", "F8" -> {
                ch[0] = (char) instrucoes.charAt(cont_char++);
                ch[1] = (char) instrucoes.charAt(cont_char++);
                codigo = new String(ch);
            }
            // operações com endereçamento direto
            case "05", "09", "13", "15", "59", "0D", "25", "2D", "35", "3D", "E7" -> {
                // considerando que é um endereçamento, não deveria ter endereços negativos
                opd[0] = (char) instrucoes.charAt(cont_char++);
                opd[1] = (char) instrucoes.charAt(cont_char++);
                opd[2] = (char) instrucoes.charAt(cont_char++);
                opd[3] = (char) instrucoes.charAt(cont_char++);

                System.out.println("INSTRUCAO --> " + codigo);
                codigo = new String(opd);
                System.out.println("endereço atual --> " + codigo);
                endereco = Integer.parseInt(codigo) + desloc_dados;
                System.out.println("novo endereço --> " + endereco);


                // if (opd[0] == '-'){ //verifica se é um número negativo
                //     opd[0] = (char) instrucoes.charAt(cont_char++);
                // }   opd[1] = (char) instrucoes.charAt(cont_char++);
            }
            // instruções jz, jp, jnz, jmp
            case "74", "75", "7A", "EB" -> {
                opd[0] = (char) instrucoes.charAt(cont_char++);
                opd[1] = (char) instrucoes.charAt(cont_char++);
                opd[2] = (char) instrucoes.charAt(cont_char++);
                opd[3] = (char) instrucoes.charAt(cont_char++);
                System.out.println("INSTRUCAO --> " + codigo);
                codigo = new String(opd);
                System.out.println("endereço atual --> " + codigo);
                endereco = Integer.parseInt(codigo) + desloc_dados;
                System.out.println("novo endereço --> " + endereco);
            }
            // operações com endereçamento imediato, logo puxam 4 digitos do arquivo
            case "04", "08", "12", "14", "58", "0C", "24", "2C", "34", "3C", "E8" -> {
                opd[0] = (char) instrucoes.charAt(cont_char++);
                if (opd[0] == '-') { 
                    opd[0] = (char) instrucoes.charAt(cont_char++);
                }   opd[1] = (char) instrucoes.charAt(cont_char++);
                opd[2] = (char) instrucoes.charAt(cont_char++);
                opd[3] = (char) instrucoes.charAt(cont_char++);
            }
            case "EF", "EE", "9D", "9C" -> { }
        }
        return instrucoes;
    }










    // private void criarTabelaSimbolos(String arquivo) {
    //     try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
    //         String linha;
    //         int enderecoRelativo = 0; // Inicializa o endereço relativo para o arquivo atual
    //         while ((linha = br.readLine()) != null) {
    //             // Analise a linha para extrair símbolos e endereços relativos
    //             String[] partes = linha.split(" "); // Assumindo que os símbolos estão separados por espaço
    //             if (partes.length == 2) {
    //                 String simbolo = partes[0];
    //                 // Não sei, aqui tinha que pegar o end relativo, mas de onde pega???
    //                 int enderecoSimbolo = enderecoRelativo;
    //                 // Verifica se o símbolo já existe na tabela global
    //                 if (!tabela_global.containsKey(simbolo)) {
    //                     // Se não existe, adiciona o símbolo com o endereço relativo
    //                     tabela_global.put(simbolo, enderecoSimbolo);
    //                 } else {
    //                     // Caso encontre um símbolo duplicado
    //                     System.err.println("Aviso: Símbolo duplicado encontrado - " + simbolo);
    //                 }
    //             }
    //             // Atualiza o endereço relativo para a próxima linha
    //             enderecoRelativo += 1;
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // public int buscarSimboloGlobal(String nome) {
    //     if (tabela_global.containsKey(nome)) {
    //         return tabela_global.get(nome); // Retorna o valor associado ao símbolo global
    //     } else {
    //         return -1; // Retorna -1 se o símbolo não for encontrado
    //     }
    // }

    // public void realizarLigacao(String diretorio, String[] arquivos_simplif) {
    //     System.out.println("-- segundo passo --");
    //     // Executa a ligação dos módulos usando a tabela de símbolos
    //     for (String arquivo : arquivos_simplif) {
    //         ligarModulo(diretorio + arquivo);
    //     }
    // }

    // private void ligarModulo(String arquivo) {
    //     try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
    //         String linha;
    //         int enderecoRelativo = 0; // Inicializa o endereço relativo para o arquivo atual
    //         while ((linha = br.readLine()) != null) {
    //             // Analise a linha para encontrar referências externas
    //             String[] partes = linha.split(" "); // Assumindo que as instruções estão separadas por espaço
    //             if (partes.length == 3 && partes[0].equals("EXTERN")) {
    //                 String simboloExtern = partes[1];
    //                 // Verifica se o símbolo externo existe na tabela global
    //                 if (tabela_global.containsKey(simboloExtern)) {
    //                     int enderecoGlobal = tabela_global.get(simboloExtern);
    //                     // Atualiza o endereço no módulo atual
    //                     int novoEndereco = enderecoRelativo + Integer.parseInt(partes[2]) + enderecoGlobal;
    //                     System.out.println("Ligando " + simboloExtern + " em " + novoEndereco);
    //                     // 
    //                     // Você pode fazer algo com o novoEndereco, como atualizar a instrução no módulo atual
    //                 } else {
    //                     System.err.println("Erro: Símbolo externo não encontrado - " + simboloExtern);
    //                 }
    //             }
    //             // Atualiza o endereço relativo para a próxima linha
    //             enderecoRelativo += 1;
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}