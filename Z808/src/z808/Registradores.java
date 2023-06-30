/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

public class Registradores {
   
    private int CL;       //Contador de Localização: armazena endereço da instrução que está sendo executada
    private int RI;       //Registrador de Instruções: armazena código da instrução que está sendo executada
    
    private int SI;       //Utilizado como índice no endereçamento indireto 
    private int IP;       //Apontador de instrução: contém o endereço da próxima instrução a ser executada
    private int SR;       //!!!!Registrador de Estado: seis flags para indicar condições durante a execução do programa

                          //Registradores para fazer interface com  a memória
    private int REM;      //Registrador de Endereço de Memória: armazena endereço da posição que será lida ou escrita na memória
    private int RBM;      //Registrador de Buffer da Memória: conteúdo do endereço de memória especificado pelo REM

                          // Registradores de dados
    private int AX;
    private int DX;
    
                          // Registrador para pilha
    private int SP;       // Stack Pointer: apontador do topo da pilha

    
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

    public int getSR() {
        return SR;
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

    public void setCL(int CL) {
        this.CL = CL;
    }

    public void setRI(int RI) {
        this.RI = RI;
    }

    //Métodos Setters
    public void setSI(int SI) {
        this.SI = SI;
    }

    public void setIP(int IP) {
        this.IP = IP;
    }

    public void setSR(int SR) {
        this.SR = SR;
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
    
    
}
