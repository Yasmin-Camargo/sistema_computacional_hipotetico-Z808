/*
Chamar macros -> ok
Definir macros -> ok
Pilha -> a fazer
Processador de Macros -> a fazer */

package z808;
import static java.lang.System.exit;
import java.util.Map;
import javax.swing.JOptionPane;

public class ProcessadorMacros {
    private EstadoMacros estadoAtual;  // se está em definição ou processamento
    private int nivelContador;  // contador
    private Hashtable <String, Macro> definicaoMacros;  // guarda as definições das macros
    private Macro macroAtual; 
    
    public ProcessadorMacros {
      this.estadoAtual = EstadoMacros.COPIA;
      this.nivelContador = 0;
      this.definicaoMacros = new Hashtable<String,Macro>();
      macroAtual = null;
    }

    /*Inicio da leitura e processamento de arquivo */
    /* Ainda precisa de ajustes */ 
     public void process(String filename) throws IOException, Exception {
        String conteudoDoArquivo = lerArquivo(filename);
        // Criar um Scanner para ler linha por linha e processar os macros.
    }

    // Método para ler um arquivo e retornar o conteúdo como uma String
    private static String lerArquivo(String caminhoArquivo) throws IOException {
        // Implementação do método lerArquivo
    }

  
}


}

}

