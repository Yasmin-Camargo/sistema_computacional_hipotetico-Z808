package z808;

import java.util.Map;

public class Ligador {
    private Map<String, Integer> tabela_global; 

    Ligador(String diretorio, String[] arquivos_simplif) {
        // vai receber N códigos-objetos
        // PASSO 1 - ligador cria tabela de simbolol globais 
        // PASSO 2 - "modulos jsutapostos e referencias externas resolvidas"
        // possivelmente implementar NAME, PUBLIC, EXTRN, WORD, NEAR e ABS no Montador
        System.out.println("diretorio: " + diretorio);
        for (String a : arquivos_simplif) {
            System.out.println(a);
        }
        PrimeiroPasso();
        SegundoPasso();
    }
    
    private void PrimeiroPasso() {
        System.out.println("-- primeiro passo --");
    } 
    
    private void SegundoPasso() {
        System.out.println("-- segundo passo --");
    }
}

