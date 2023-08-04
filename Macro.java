/*
 Classe utilizaada para chamar e definir as macros
 Os resultados daqui passam para processador de macros
 */
package z808;
import java.util.ArrayList;
import java.util.Stack;


public class Macro {
    private ArrayList<Parametro> parametros_formais;
    private String nomeMacro; 
    private String esqueletoMacro;
    private Stack <Parametro> parametros_reais;
 
public Macro (String nomeMacro, ArrayList<Parametro> parametros_formais){
    this.nomeMacro = nomeMacro;
    this.esqueletoMacro = "";
    this.parametros_formais = analiseParametros (parametros_formais);
    this.parametros_reais = new  Stack <Parametro>();
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
