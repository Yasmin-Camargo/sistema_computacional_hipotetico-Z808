/*
Chamar macros
Definir macros -> CLASSA MACROS
Pilha -> NESTA CLASSE
Processador de Macros -> NESTA CLASSE
*/

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
 
   


}

}

