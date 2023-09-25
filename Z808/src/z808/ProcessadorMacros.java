package z808;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ProcessadorMacros {
    public static final int TAM_MAX = 100;
    private final int DEFINICAO = 0;
    private final int COPIA = 1;
    // private final int EXPANSAO = 2;

    private int estado;
    private int nivel;  // contador
    private Map<String, Macro> macros;  // guarda as definições das macros
    private Macro macro_atual;
    private boolean eh_codigo = false;

    //Inicializa o processador
    public ProcessadorMacros(){
        estado = COPIA;
        this.nivel = 0;
        this.macros = new HashMap<>();
        macro_atual = null;
    }

    // Processa o arquivo selecionado
    public void processar(String diretorio, String arquivo, String arq_simplif) throws Exception {
        File saida = new File(diretorio + "/" + arq_simplif + "-montador.txt");	// Cria um objeto e adiciona a extenção TXT
        saida.createNewFile();	
        FileWriter arq_saida;
        try {
            arq_saida = new FileWriter(saida); // Cria o arquivo de saida no sistema
            Scanner scanner = new Scanner(new File(arquivo));		// Cria um escritor para escrever no arquivo de saida
            LeitorMacro leitor_macro = new LeitorMacro();			// Cria um leitor para ler o conteudo do arquivo de entrada
            while (scanner.hasNextLine()) {                         // Executa para cada linha existente no arquivo
                leitor_macro.lerLinhaScanner(scanner);              // Processa cada linha do arquivo
                String linha = processarLinha(leitor_macro);
                // if (!linha.contains("\n") && !linha.equals("")) // wtf
                    // linha += "\n";
                arq_saida.write(linha);  
            }
            arq_saida.close(); 
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "ERRO!",
                "Erro ao lidar com arquivo de Macro.",
                JOptionPane.ERROR_MESSAGE
            );
        }
        // return saida.toString();
        // return diretorio.toString();
    }

    public String processarLinha(LeitorMacro leitor_macro) {
        String saida = "";								
        String instrucao = leitor_macro.getInstrucao();
        String rotulo = leitor_macro.getRotulo();
        
        // System.out.println("rotulo:"+rotulo+" instrucao:"+instrucao);

        //pra trabalhar com macros aninhadas, o programa precisa entrar
        //em definição e expansão ao mesmo tempo; da maneira atual, isso não acontece
        //pois a var estado só pode ser um dos 3 números (definição, copia, expansao) por vez
        
        if (instrucao.toLowerCase().equals("start")) 
            eh_codigo = true;
        else if (eh_codigo) {
            if (macros.containsKey(instrucao)){             // Se constar a instrução nas definições
                estado = COPIA;
                saida = expandirMacro(leitor_macro);        // Retorna a expansão da macro atual a partir das instruções e operandos
            } else
                saida = leitor_macro.toString();
        }
        else {
            if (rotulo.toLowerCase().equals("macro")) {	// Se for "MACRO"/"Macro"/"macro"/...
                nivel++;						                    // Incrementa o contador de nivel
                if (estado == COPIA) {
                    estado = DEFINICAO;
                    if (macros.containsKey(instrucao)) {    // Se existe o rótulo atual em macros
                        macros.remove(instrucao);			// Remove o rótulo
                    }  
                    macro_atual = null;						// Macro atual vira nula
                }
            } else if (instrucao.toLowerCase().equals("endm")) {	// Se não, se for "ENDM"/"EndM"/"endm"/"Endm"/...
                nivel--;						// Decrementa o contador
                if (nivel == 0) {				// Se o contador apontar para 0
                    estado = COPIA;
                    macro_atual = null;			// Macro atual vira nula
                }
            } 
            if (estado == DEFINICAO) {
                if (macro_atual == null) {					// E se a macro atual for nula
                    String nome_macro = leitor_macro.getInstrucao();
                    ArrayList<String> tokens = leitor_macro.getOperandos();      // operando do macro
                    Macro macro = new Macro(nome_macro, tokens);		
                    this.macro_atual = macro;
                    macros.put(nome_macro, macro);		// Coloca uma nova macro e coloca dentro de definicaoMacros
                } else {
                    if (macros.containsKey(instrucao)) {
                        macro_atual.adicionarNoEsqueleto(expandirMacro(leitor_macro), nivel+1);
                    } else
                        macro_atual.adicionarNoEsqueleto(leitor_macro.toString() + "\n", nivel); // Se existir uma macro atual, concatenar a sua info ao esqueleto da macro
                }
            } 
        }
        
        return saida;
    }

    // Expande a Macro a partir do nome fornecido
    public String expandirMacro(LeitorMacro leitor) {
        ArrayList<String> parametros = new ArrayList<>();
        parametros.add(leitor.getRotulo());
        for (String operando : leitor.getOperandos())
            parametros.add(operando);
        
        Macro macro = macros.get(leitor.getInstrucao());
        // System.out.println("EXPANDIR MACRO -- " + macro.getNomeMacro() + ":\n" + macro.getEsqueletoMacro());

        macro.setParametrosReais(parametros);               // Define os parametros reais com os valores dentro de "parametros"
        return macro.expandirMacro();                            // Retorna uma string com a macro expandida a partir dos parametros fornecidos;
    }

}