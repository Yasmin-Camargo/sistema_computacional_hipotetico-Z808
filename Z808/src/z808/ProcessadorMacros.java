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
import java.util.Hashtable;
import java.util.Scanner;

public class ProcessadorMacros {

	public static final int TAM_MAX = 100;
	
	private EstadoMacros estadoAtual;  // se está em definição ou processamento
	private int nivelContador;  // contador
	private Hashtable<String, Macro> definicaoMacros;  // guarda as definições das macros
	private Macro macroAtual;
	
	private boolean comentario; //Checa pra ver se a linha é um comentário
	private String rotulo; //Armazena o rotulo da linha
	private String instrucao; //Armazena a instrução da linha
	ArrayList<String> operandos = new ArrayList<>(); // Armazena os operandos da linha

	//Inicializa o processador
	public ProcessadorMacros(){
		this.estadoAtual = EstadoMacros.COPIA;
		this.nivelContador = 0;
		this.definicaoMacros = new Hashtable<>();
		macroAtual = null;
		resetar();
	}

	// Processa o arquivo selecionado
	public void processar(String arquivo) throws IOException, NumeroErradoOperadores{
		File saida = new File(arquivo.substring(0, arquivo.length() - 4) + ".MXF");	// Cria um objeto e adiciona a extenção MXF
		saida.createNewFile();												// Cria o arquivo de saida no sistema
		
		try (FileWriter escritor = new FileWriter(saida)) {							// Cria um escritor para escrever no arquivo de saida
			Scanner scanner = new Scanner(saida);						// Cria um leitor para ler o conteudo do arquivo de entrada
			while(scanner.hasNextLine()){									// Executa para cada linha existente no arquivo
				lerLinha(scanner, null);								// Processa cada linha do arquivo
			} if(!comentario){												// Verifica se a linha é um comentário
				String linha = processarLinha();								// Processa a proxima linha
				escritor.write(linha);										// Escreve a linha processada no arquivo de saida 
			}
		}
	}

	// Expande a Macro a partir do nome fornecido
	public String expandirMacro(String nomeMacro, ArrayList<String> parametros) throws NumeroErradoOperadores{
		String saida = "";													// Inicializa onde vai ser a saída do resultado expandido
		Macro macro = definicaoMacros.get(nomeMacro);						// Recebe a definição da macro correspondente em definicaoMacros
		macro.setParametrosReais(parametros);								// Define os parametros reais com os valores dentro de "parametros"
		
		String expansao = macro.expandir();									// Retorna uma string com a macro expandida a partir dos parametros fornecidos
		String linhas[] = expansao.split("\\n");								// Divide a string e armazena cada linha num array
		
		for(String linha : linhas){											// Para cada linha
			lerLinha(null, linha);									// Chama o método de leitura de linha
			saida += processarLinha();										// Concatena o processo da linha atual na saida
		}
		return saida;													// Retorna a saida
	}
	
	// Processa individualmente uma linha da fonte
	public String processarLinha() throws NumeroErradoOperadores{
		String saida = "";													// Armazena o resultado do processamento no final								
		String rotulo = this.rotulo;
		String instrucao = this.instrucao;
		ArrayList<String> tokens = this.operandos;
		
		if (instrucao.equals("MACRO")){								// Se for "MACRO"
			nivelContador++;											// Incrementa o contador de nivel
			
			if(estadoAtual == EstadoMacros.COPIA){							// Se o estado atual for igual a uma cópia dos estados de macro atual
				estadoAtual = EstadoMacros.DEFINICAO;						// Estado atual vira a definição
				
				if(definicaoMacros.containsKey(rotulo)){						// Se existe o rótulo atual em definicaoMacros
					definicaoMacros.remove(rotulo);						// Remove o rótulo
				}
				
				macroAtual = null;										// Macro atual vira nula
				return saida;
			}
			
		} else if (instrucao.equals("MEND")){								// Se não, se for "MEND"
			nivelContador--;												// Decrementa o contador
			
			if (nivelContador == 0){										// Se o contador apontar para 0
				estadoAtual = EstadoMacros.COPIA;							// Estado atual se forna uma cópia do estado de macro atual
				macroAtual = null;										// Macro atual vira nula
				return saida;
			}
		} else if (definicaoMacros.containsKey(instrucao)){						// Se não, se constar a instrução nas definições
			estadoAtual = EstadoMacros.COPIA;								// Estado atual recebe a cópia atual do estado de macros
			return expandirMacro(instrucao, tokens);				// Retorna a expansão da macro atual a partir das instruções e operandos
		}
			
		if(estadoAtual == EstadoMacros.DEFINICAO){							// Se o estado atual for igual a definição no estado de macros
			if(macroAtual == null){											// E se a macro atual for nula
				String nomeMacro = this.instrucao;
				tokens = this.operandos;
				Macro macro = new Macro(nomeMacro, tokens);		
				macroAtual = macro;
				definicaoMacros.put(nomeMacro, macro);				// Coloca uma nova macro e coloca dentro de definicaoMacros
			} else {
				macroAtual.adicionarNoEsqueleto(info() + "\n", nivelContador); // Se existir uma macro atual, concatenar a sua info ao esqueleto da macro
			}
		} else {
			saida = info() + "\n";											// Concatena a info da  proxima macro na saida
		}
		
		return saida;
	}
	
	// Verifica se existem informações importantes na linha lida
	public void lerLinha(Scanner scanner, String entrada){
		
		String linha = scanner.nextLine();										// Inicializa linha
		
		if(scanner.hasNext() || scanner == null){									// Dependendo da situação, vai chamar a função para String ou para Scanner
			this.comentario = false;										// Indica que linha não é comentario
			this.operandos.clear();											// Limpa a lista de operadores
			if (scanner == null){											// Se a entrada em Scanner for nulo então usamos a linha como String, se não, segue como Scanner
				linha = entrada;
			}
			
			if(linha.length() > TAM_MAX){									// Checa o tamanho máximo da linha
				String erro = "Linha é maior que o tamanho máximo!";
				System.out.println(erro);
			}
			
			String tokens[] = linha.split("\\s+");								// Divide a linha em tokens a partir dos espaços em branco
			
			checarTokens(tokens);												// Chama o método auxiliar
		}
	}
	
	// Método auxiliar da função lerLinha
	public void checarTokens(String tokens[]){
		if(tokens.length > 0){												// Verifica se há pelo menos um token na linha
			if(tokens[0].equals("*")){									// Se o primeiro token for * então é um comentário
				this.comentario = true;
				return;
			}
			this.rotulo = tokens[0];											// Define o primeiro token como rotulo
		}
		
		if(tokens.length > 1){												// Se o segundo token for um *, ignoramos
			if(tokens[1].equals("*")){
				return;
			}
			this.instrucao = tokens[1];										// Define o segundo token como instrução
		}
		
		if(tokens.length > 2){												// Se existirem mais que 2 tokens
			this.operandos.addAll(Arrays.asList(tokens));						// Adiciona todos os tokens para a lista de operandos
		}
	}
	
	// Retorna toda a informação obtida de uma macro
	public String info(){
		String resultado = this.rotulo + "\t" + this.instrucao + "\t";
		for (String operando : operandos){
			resultado += operando + "\t";
		}
		return resultado;
	}
	
	// Reseta informação necessaria para processo de cada Macro
	public final void resetar(){
		this.comentario = false;
		this.rotulo = "";
		this.instrucao = "";
		this.operandos.clear();
	}
}