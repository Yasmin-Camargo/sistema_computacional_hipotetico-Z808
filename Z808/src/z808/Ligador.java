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
        this.endereco_base = 0;
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

            FileWriter fw = new FileWriter(f1);
            BufferedWriter escritor = new BufferedWriter(fw);
            escritor.write(instrucoes);
            escritor.flush();
            escritor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (eh_principal) {
            System.out.println("Main file!");
            System.out.println(cont_instrucoes);
        } else {
            instrucoes = modificarEnderecos(instrucoes);
        }
        int contador_dados = 0;
        for (Integer chave : montador.getDadosParaArmazenar().keySet()) {
            dados.put(contador_dados, (montador.getDadosParaArmazenar().get(chave) + endereco_base));
            contador_dados += 1;
        }
        endereco_base += (Integer.parseInt(cont_instrucoes.replace("#", "")));
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

                codigo = new String(opd);
                endereco = Integer.parseInt(codigo) + desloc_dados;
                String novo_texto = "";
                if (endereco < 10) {
                    novo_texto = "000";
                } else if (endereco < 100) {
                    novo_texto = "00"; 
                } else if (endereco < 1000) {
                    novo_texto = "0";
                }
                novo_texto += String.valueOf(endereco);
                
                String parte1 = instrucoes.substring(0, cont_char-4);
                String parte2 = instrucoes.substring(cont_char, instrucoes.length());

                // System.out.println(instrucoes);
                // System.out.println(parte1 + novo_texto + parte2);

                instrucoes = parte1 + novo_texto + parte2;

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

}