/*Implementação da lógica para criar e ExpandirMacro macros*/
package z808;
import java.util.ArrayList;
import java.util.Stack;

public class Macro {
    private ArrayList<Parametro> parametros_formais;
    private String nomeMacro; 
    private String esqueletoMacro;
    private Stack <Parametro> parametros_reais;
 
    public Macro (String nomeMacro, ArrayList<String> parametros_formais){
        this.nomeMacro = nomeMacro;
        this.esqueletoMacro = "";
        this.parametros_formais = AnaliseParametros(parametros_formais);
        this.parametros_reais = new Stack<>();
    }
    /* Expande a definição de macro, substituindo os parâmetros formais pelos valores reais */ 
    public String ExpandirMacro(){
        String expansao = esqueletoMacro;
        for(int i = 0; i < parametros_formais.size(); i++){  // altera os valores neste loop
            Parametro parametro = parametros_formais.get(i);
            String nivel_par = "#("+parametro.getNivel()+","+parametro.getIndex()+")";  
            expansao = expansao.replace(nivel_par, parametros_reais.get(i).getNome());
        }
        return expansao;
    }

    /* Lança uma mensagem de erro quando ocorre um erro ao número incorreto de operandos*/
    public void NumeroErradoOperadores(String reason) throws NumeroErradoOperadores {
        throw new NumeroErradoOperadores(reason);
    }

    /* Configura os parametros reais que serão usados na expansão */
    public void SetParametrosReais(ArrayList<String> parametros) throws NumeroErradoOperadores {
        if (parametros.size() != parametros_formais.size()) {
            throw new NumeroErradoOperadores("ERRO ao configurar os parâmetros, espera-se: " 
            + parametros_formais.size() + ", foi obtido" + parametros.size()); 
        }
        for (int i = 0; i < parametros.size(); i++) {
            // the following approach works
            // but we're not able to use Macro inside Macro
            String novo_nome = parametros.get(i).replace(";", "");
            parametros_formais.get(i).setNome(novo_nome);
            parametros_reais.push(parametros_formais.get(i));
        }
    }

    /* Analisa se tem alguma parametro formal, se tiver, substitui pelo valor correpondente #(d,i) */
    public void adicionarNoEsqueleto(String string, int contadorNivel){
        for(Parametro parametro : parametros_formais){
            if (string.contains(parametro.getNome())) {
                String regular = "\\b" + parametro.getNome() + "\\b";
                string = string.replaceAll(
                    regular, "#(" + parametro.getNivel() + "," + parametro.getIndex() + ")"
                );
            }
        }
        this.esqueletoMacro += string;
    }

     private ArrayList<Parametro> AnaliseParametros (ArrayList<String> parametros) {
        ArrayList<Parametro> result = new ArrayList<>();
        int contadorNivel = 1;
        int indexParametro = 1;
        for (String par : parametros) {
            String partes[] = par.split(","); // tenho que verificar se nao veio um parametro grudado em outro: var1,var2 por exemplo
            for (String parte : partes) {
                Parametro aux = new Parametro(parte, contadorNivel, indexParametro++);
                result.add(aux);
            }   
        }
        return result;
     }


    // getters and setters 
    public String GetNomeMacro() {
            return nomeMacro;
        }

    public void SetNomeMacro(String nomeMacro){
    this.nomeMacro = nomeMacro;
    }

    public String GetEsqueletoMacro(){
        return esqueletoMacro;
    }

    public void SetEsqueletoMacro(String esqueletoMacro){
        this.esqueletoMacro = esqueletoMacro;
    }

}
