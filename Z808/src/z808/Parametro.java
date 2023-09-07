package z808;

public class Parametro {
    private String nome;
    private int nivel;
    private int index;

    public Parametro(String nome, int nivel, int index){
        this.nome = nome;
        this.nivel = nivel;
        this.index = index;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNivel(int nivel){
        this.nivel = nivel;
    }

    public int getNivel(){
        return this.nivel;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    @Override
    public String toString() {
        return "Parametro --- valor: " + this.nome + "; pilha: #(" + this.nivel + ',' + this.index + ")";
    }
}
