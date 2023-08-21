package z808;

public class Registradores {
    private int CL;       //Contador de Localização: armazena endereço da instrução que está sendo executada
    private int RI;       //Registrador de Instruções: armazena código da instrução que está sendo executada
    
    private int SI;       //Utilizado como índice no endereçamento indireto 
    private int IP;       //Apontador de instrução: contém o endereço da próxima instrução a ser executada
    
    int SR[] = new int[16]; //Registrador de Estado: seis flags para indicar condições durante a execução do programa
                            // CF [0] = flag operação de adição resulta em vai-um (carry) ou operação de subtração resulta em vem-um (borrow)
                            // PF [6] = flag de paridade
                            // IF [7] = flag de interrupção
                            // ZF [8] = flag zero
                            // SF [9] = flag de sinal (positivo ou negativo)
                            // OF [12] = flag de overflow 

                          //Registradores para fazer interface com  a memória
    private int REM;      //Registrador de Endereço de Memória: armazena endereço da posição que será lida ou escrita na memória
    private int RBM;      //Registrador de Buffer da Memória: conteúdo do endereço de memória especificado pelo REM

                          
    private int AX;       //Registradores de dados
    private int DX;
    
                          //Registrador para pilha
    private int SP;       //Stack Pointer: apontador do topo da pilha

    
    //Métodos Getters
    public int getCL() {
        return CL;
    }

    public int getRI() {
        return RI;
    }

    public int getSI() {
        return SI;
    }

    public int getIP() {
        return IP;
    }

    public int getREM() {
        return REM;
    }

    public int getRBM() {
        return RBM;
    }

    public int getAX() {
        return AX;
    }

    public int getDX() {
        return DX;
    }

    public int getSP() {
        return SP;
    }
    
    public int getSR(String flag){
        switch (flag) {
            case "cf": return SR[0];
            case "pf": return SR[6];
            case "if": return SR[7];
            case "zf": return SR[8];
            case "sf": return SR[9];
            case "of": return SR[12];
            default:
                System.out.println("Opção Inválida");
                return -1;
        }
    }

    //Métodos Setters
    public void setCL(int CL) {
        this.CL = CL;
    }

    public void setRI(int RI) {
        this.RI = RI;
    }

    public void setSI(int SI) {
        this.SI = SI;
    }

    public void setIP(int IP) {
        this.IP = IP;
    }

    public void setREM(int REM) {
        this.REM = REM;
    }

    public void setRBM(int RBM) {
        this.RBM = RBM;
    }

    public void setAX(int AX) {
        this.AX = AX;
    }

    public void setDX(int DX) {
        this.DX = DX;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }
    
    public void setSR(String flag, int valor){
        if (!(valor == 0 || valor ==1)){
            System.out.println("ERRO: valor da flag "+ flag + " deve ser 0 ou 1");
        } 
        else {
            switch (flag) {
                case "cf":
                    SR[0] = valor;
                    break;
                case "pf":
                    SR[6] = valor;
                    break;
                case "if":
                    SR[7] = valor;
                    break;
                case "zf":
                    SR[8] = valor;
                    break;
                case "sf":
                    SR[9] = valor;
                    break;
                case "of":
                    SR[12] = valor;
                    break;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
        }
    } 
    
    public int[] getSR() {
        return SR;
    }
    
    public int JuntarSR(){
        StringBuilder flags = new StringBuilder();
        System.out.println("FLAGS SR");
        for (int i = 0; i < SR.length; i++) {
            flags.append(SR[i]);
            System.out.println(SR[i]);
        }
        return (Integer.parseInt(flags.toString(),2));
    }
    
    public void SepararSR(int valor){
        String binario = Integer.toBinaryString(valor);
        String[] valor_separado = binario.split("");
        
        for (int i = 0; i < SR.length; i++) {
            SR[i] = Integer.parseInt(valor_separado[i]);
        }
    }
    
    public String print_SR(){
        return ("CF:"+ SR[0] + " PF:" + SR[6] +  " IF:" + SR[7] + " ZF:" + SR[8] + " SF:" + SR[9] + " OF:" + SR[12]);
    }
    
    public void reinicializaRegistradores(){
        this.AX = 0;
        this.DX = 0;
        this.SI = 0;
        this.IP = 0;
        this.REM = 0;
        this.RBM = 0;
        this.SP = 0;
        for (int i = 0; i < SR.length; i++) 
            SR[i] = 0;
    }    
}
