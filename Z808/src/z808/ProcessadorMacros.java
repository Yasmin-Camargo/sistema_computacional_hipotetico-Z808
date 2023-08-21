package z808;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProcessadorMacros {
    public static final int TAM_MAX = 100;

    private EstadoMacros estado_atual;  // se está em definição ou processamento
    private int nivel;  // contador
    private Map<String, Macro> macros;  // guarda as definições das macros
    private Macro macro_atual;
    private boolean eh_codigo = false;

    //Inicializa o processador
    public ProcessadorMacros(){
        this.estado_atual = EstadoMacros.COPIA;
        this.nivel = 0;
        this.macros = new HashMap<>();
        macro_atual = null;
    }

    // Processa o arquivo selecionado
    public String processar(String arquivo) throws IOException, NumeroErradoOperadores{
        File saida = new File(arquivo.substring(0, arquivo.length() - 4) + "_montador.txt");	// Cria um objeto e adiciona a extenção MXF
        saida.createNewFile();										// Cria o arquivo de saida no sistema
        FileWriter arq_saida = new FileWriter(saida);

        Scanner scanner = new Scanner(new File(arquivo));		// Cria um escritor para escrever no arquivo de saida
        LeitorMacro leitorMacro = new LeitorMacro();				// Cria um leitor para ler o conteudo do arquivo de entrada
        while (scanner.hasNextLine()) {                                         // Executa para cada linha existente no arquivo
            leitorMacro.lerLinhaScanner(scanner);                               // Processa cada linha do arquivo
            //String linha = leitorMacro.toString();
            String linha = processarLinha(leitorMacro);
            arq_saida.write(linha);  
        }
        arq_saida.close();
        return saida.toString();
    }

    // Expande a Macro a partir do nome fornecido
    public String expandirMacro(LeitorMacro leitor) throws NumeroErradoOperadores{
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(leitor.getRotulo());
        for (String operando : leitor.getOperandos())
            parametros.add(operando);
        Macro macro = macros.get(leitor.getInstrucao());
        macro.setParametrosReais(parametros);               // Define os parametros reais com os valores dentro de "parametros"
        return macro.expandirMacro();                            // Retorna uma string com a macro expandida a partir dos parametros fornecidos;
    }
	
    // Processa individualmente uma linha da fonte
    public String processarLinha(LeitorMacro leitorMacro) throws NumeroErradoOperadores {
        String saida = "";								// Armazena o resultado do processamento no final								        
        String instrucao = leitorMacro.getInstrucao();
        String rotulo = leitorMacro.getRotulo();
        
//        System.out.println("rotulo:"+rotulo+" instrucao:"+instrucao);
        
        if (instrucao.toLowerCase().equals("start")) 
            eh_codigo = true;
        else if (eh_codigo) {
            if (macros.containsKey(instrucao)){             // Se constar a instrução nas definições
                this.estado_atual = EstadoMacros.COPIA;			// Estado atual recebe a cópia atual do estado de macros
                saida = expandirMacro(leitorMacro);                     // Retorna a expansão da macro atual a partir das instruções e operandos
            } else
                saida = leitorMacro.toString();
        }
        else {
            if (rotulo.toLowerCase().equals("macro")) {			// Se for "MACRO"/"Macro"/"macro"/...
                nivel++;						// Incrementa o contador de nivel
                if (estado_atual == EstadoMacros.COPIA) {			// Se o estado atual for igual a uma cópia dos estados de macro atual
                    estado_atual = EstadoMacros.DEFINICAO;			// Estado atual vira a definição
                    if (macros.containsKey(instrucao)) {		// Se existe o rótulo atual em definicaoMacros
                        macros.remove(instrucao);			// Remove o rótulo
                    }  
                    macro_atual = null;						// Macro atual vira nula
                }
            } else if (instrucao.toLowerCase().equals("endm")) {		// Se não, se for "ENDM"/"EndM"/"endm"/"Endm"/...
                nivel--;						// Decrementa o contador
                if (nivel == 0) {					// Se o contador apontar para 0
                    estado_atual = EstadoMacros.COPIA;				// Estado atual se forna uma cópia do estado de macro atual
                    macro_atual = null;						// Macro atual vira nula
                }
            } /*else if (definicaoMacros.containsKey(instrucao)){		// Se não, se constar a instrução nas definições
                this.estadoAtual = EstadoMacros.COPIA;				// Estado atual recebe a cópia atual do estado de macros
                saida = expandirMacro(instrucao, tokens);	// Retorna a expansão da macro atual a partir das instruções e operandos
            }*/

            if (this.estado_atual == EstadoMacros.DEFINICAO) {			// Se o estado atual for igual a definição no estado de macros
                if (macro_atual == null) {					// E se a macro atual for nula
                    String nomeMacro = leitorMacro.getInstrucao();
                    ArrayList<String> tokens = leitorMacro.getOperandos();      // operando do macro
                    Macro macro = new Macro(nomeMacro, tokens);		
                    this.macro_atual = macro;
                    macros.put(nomeMacro, macro);		// Coloca uma nova macro e coloca dentro de definicaoMacros
                } else 
                    macro_atual.adicionarNoEsqueleto(leitorMacro.toString() + "\n", nivel
                    ); // Se existir uma macro atual, concatenar a sua info ao esqueleto da macro
            } 
        }
        
        return saida;
    }
}