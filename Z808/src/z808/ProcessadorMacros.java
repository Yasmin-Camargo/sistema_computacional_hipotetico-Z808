/*
Chamar macros -> ok
Definir macros -> ok
Pilha -> a fazer
Processador de Macros -> ok
Teste -> a fazer
*/

package z808;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProcessadorMacros {
    public static final int TAM_MAX = 100;

    private EstadoMacros estadoAtual;  // se está em definição ou processamento
    private int nivelContador;  // contador
    private Map<String, Macro> definicaoMacros;  // guarda as definições das macros
    private Macro macroAtual;

    //Inicializa o processador
    public ProcessadorMacros(){
        this.estadoAtual = EstadoMacros.COPIA;
        this.nivelContador = 0;
        this.definicaoMacros = new HashMap<>();
        macroAtual = null;
    }

    // Processa o arquivo selecionado
    public String processar(String arquivo) throws IOException, NumeroErradoOperadores{
        File saida = new File(arquivo.substring(0, arquivo.length() - 4) + "_montador.mxf");	// Cria um objeto e adiciona a extenção MXF
        saida.createNewFile();										// Cria o arquivo de saida no sistema
        FileWriter output_file = new FileWriter(saida);

        Scanner scanner = new Scanner(new File(arquivo));		// Cria um escritor para escrever no arquivo de saida
        LeitorMacro leitorMacro = new LeitorMacro();				// Cria um leitor para ler o conteudo do arquivo de entrada
        while (scanner.hasNextLine()) {                                         // Executa para cada linha existente no arquivo
            leitorMacro.lerLinhaScanner(scanner);                               // Processa cada linha do arquivo
            //String linha = leitorMacro.toString();
            String linha = processarLinha(leitorMacro);
            System.out.println("LINHA -- " + linha);
            output_file.write(linha+"\n");  
        }
        output_file.close();
        return saida.toString();
    }

    // Expande a Macro a partir do nome fornecido
    public String expandirMacro(String nomeMacro, ArrayList<String> parametros) throws NumeroErradoOperadores{
        LeitorMacro leitorMacro = new LeitorMacro();
        //String saida = "";					// Inicializa onde vai ser a saída do resultado expandido
        Macro macro = definicaoMacros.get(nomeMacro);	// Recebe a definição da macro correspondente em definicaoMacros
        macro.setParametrosReais(parametros);			// Define os parametros reais com os valores dentro de "parametros"

        String expansao = macro.expandir();			// Retorna uma string com a macro expandida a partir dos parametros fornecidos
        return expansao;
        /*
        String linhas[] = expansao.split("\\n");		// Divide a string e armazena cada linha num array

        for(String linha : linhas){				// Para cada linha
            leitorMacro.lerLinhaString(linha);		// Chama o método de leitura de linha
            saida += processarLinha(leitorMacro);		// Concatena o processo da linha atual na saida
        }
        System.out.println("SAIDA -- \n" + saida);
        return saida;	*/					// Retorna a saida
    }
	
    // Processa individualmente uma linha da fonte
    public String processarLinha(LeitorMacro leitorMacro) throws NumeroErradoOperadores{
        String saida = "";								// Armazena o resultado do processamento no final								
        String rotulo = leitorMacro.getRotulo();
        String instrucao = leitorMacro.getInstrucao();
        ArrayList<String> tokens = new ArrayList<String>();
            tokens.add(instrucao);
            for (String operando : leitorMacro.getOperandos())
                tokens.add(operando);
            
        //System.out.println("rotulo: " + rotulo+ "-- instrucao: " + instrucao);
        
        if (rotulo.equals("MACRO")){						// Se for "MACRO"
            nivelContador++;								// Incrementa o contador de nivel
            if(estadoAtual == EstadoMacros.COPIA){					// Se o estado atual for igual a uma cópia dos estados de macro atual
                estadoAtual = EstadoMacros.DEFINICAO;					// Estado atual vira a definição
                if (definicaoMacros.containsKey(instrucao)){				// Se existe o rótulo atual em definicaoMacros
                    definicaoMacros.remove(instrucao);				// Remove o rótulo
                }  
                macroAtual = null;							// Macro atual vira nula
            }
            // não quero "NomeMacro MACRO parametro" no montador
            // saida = leitorMacro.toString() + "\n";
        } else if (instrucao.equals("MEND")){					// Se não, se for "MEND"
            nivelContador--;								// Decrementa o contador
            if (nivelContador == 0){							// Se o contador apontar para 0
                estadoAtual = EstadoMacros.COPIA;					// Estado atual se forna uma cópia do estado de macro atual
                macroAtual = null;							// Macro atual vira nula
            }
        } else if (definicaoMacros.containsKey(instrucao)){				// Se não, se constar a instrução nas definições
            this.estadoAtual = EstadoMacros.COPIA;					// Estado atual recebe a cópia atual do estado de macros
            saida = expandirMacro(instrucao, tokens);			// Retorna a expansão da macro atual a partir das instruções e operandos
        }

        if (this.estadoAtual == EstadoMacros.DEFINICAO){				// Se o estado atual for igual a definição no estado de macros
            if (macroAtual == null){							// E se a macro atual for nula
                String nomeMacro = leitorMacro.getInstrucao();
                tokens = leitorMacro.getOperandos(); // operando do macro
                Macro macro = new Macro(nomeMacro, tokens);		
                this.macroAtual = macro;
                definicaoMacros.put(nomeMacro, macro);				// Coloca uma nova macro e coloca dentro de definicaoMacros
            } else 
                macroAtual.adicionarNoEsqueleto(
                    leitorMacro.toString() + "\n", nivelContador
                ); // Se existir uma macro atual, concatenar a sua info ao esqueleto da macro
        } else 
            saida = leitorMacro.toString();

        return saida;
    }
}