/*Implementação da lógica para criar e expandir macros*/
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
        this.parametros_formais = analiseParametros(parametros_formais);
        this.parametros_reais = new  Stack <Parametro>();
    }
    /* Expande a definição de macro, substituindo os parâmetros formais pelos valores reais */ 
    public String expandir(){
        String expandirMacro = esqueletoMacro;
        for(int i = 0; i < parametros_formais.size(); i++){  // altera os valores neste loop
            Parametro parametro = parametros_formais.get(i);
            String nivel_par = "#("+parametro.getNivel()+","+parametro.getIndex()+")";  
            expandirMacro = expandirMacro.replace(nivel_par, parametros_reais.get(i).getNome());
        }
        return expandirMacro;
    }

    /* Lança uma mensagem de erro quando ocorre um erro ao número incorreto de operandos*/
    public void NumeroErradoOperadores(String reason) throws NumeroErradoOperadores {
        throw new NumeroErradoOperadores(reason);
    }

    /* Configura os parametros reais que serão usados na expansão */
    public void setParametrosReais(ArrayList<String> parametros) throws NumeroErradoOperadores {
        if (parametros.size() != parametros_formais.size()) {
            throw new NumeroErradoOperadores("ERRO ao configurar os parâmetros, espera-se: " 
            + parametros_formais.size() + ", foi obtido" + parametros.size()); 
        }
        ArrayList<Parametro> analiseParametros = analiseParametros(parametros);
        for (int i = 0; i < analiseParametros.size(); i++) {
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

     private ArrayList<Parametro> analiseParametros (ArrayList<String> entrada) {
            ArrayList<Parametro> result = new ArrayList<>();
            int contadorNivel = 1;
            int indexParametro = 1;
            for (String string : entrada) {
                String formatada = string.replaceAll(",", "");
                Parametro aux = new Parametro(formatada, contadorNivel, indexParametro++);
                result.add(aux);
            }
            return result;
     }


    // getters and setters 
    public String getNomeMacro() {
            return nomeMacro;
        }

    public void setNomeMacro(String nomeMacro){
    this.nomeMacro = nomeMacro;
    }

    public String getEsqueletoMacro(){
        return esqueletoMacro;
    }

    public void setEsqueletoMacro(String esqueletoMacro){
        this.esqueletoMacro = esqueletoMacro;
    }

}
