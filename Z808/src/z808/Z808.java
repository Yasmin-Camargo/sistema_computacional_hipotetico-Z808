/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package z808;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/*
 * @authors         Grupo Adas & CG
 *  Bianca Beppler Dullius
 *  Caroline Souza Camargo
 *  Cláudio Luis da Silva Machado Junior
 *  Eduarda Abreu Carvalho
 *  Guilherme Braatz Stein
 *  Júlia da Rocha Junqueira
 *  Júlia Veiga da Silva
 *  Maria Julia Duarte Lorenzoni
 *  Yasmin Souza Camargo
 */
public class Z808 {

	public static void main(String[] args) throws InterruptedException {
		Registradores registrador = new Registradores();
		GUI gui = new GUI();
		
		// Enquanto não tem arquivo não é possivel avançar o código
		while(gui.getCaminho().equals("")){
			Thread.sleep(100);
		}
		
		String caminho_arquivo = gui.getCaminho();

		int flag_final_arquivo = 0;

		// Leitura do arquivo
		File arquivo = new File(caminho_arquivo);
		if (!arquivo.exists()) {
			JOptionPane.showMessageDialog(null, "Erro: falha ao abrir o arquivo");
		} else {
			try {
				FileInputStream arquivo_leitura = new FileInputStream(arquivo);
				char[] ch = new char[2];
				while (flag_final_arquivo != 1) {
					//lê primeiro byte (8 bits) da instrução
					ch[0] = (char) arquivo_leitura.read();
					ch[1] = (char) arquivo_leitura.read();
					String instrucao = new String(ch);

					// Verifica se chegou no Fim do arquivo
					if (ch[0] == '\uFFFF' || ch[1] == '\uFFFF') {
						flag_final_arquivo = 1;
					} else {
						// Identificando qual é a intrução
						switch (instrucao) {
							// ----> Instruções aritméticas
							case "03":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // add AX,DX
									System.out.println("add AX,DX");
									registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getAX(), registrador));
								} else if (instrucao.equals("C0")) {   // add AX,AX
									System.out.println("add AX,AX");
									registrador.setAX(Instrucoes.add(registrador.getAX(), registrador.getDX(), registrador));
								}
								break;

							case "05":                                     // add AX,opd
								System.out.print("add AX,opd");
								//leitura do operando (16 bits)
								char[] opd = new char[4];
								opd[0] = (char) arquivo_leitura.read();
								opd[1] = (char) arquivo_leitura.read();
								opd[2] = (char) arquivo_leitura.read();
								opd[3] = (char) arquivo_leitura.read();
								instrucao = new String(opd);

								//converte para inteiro e chama função add  
								System.out.println("(" + (int) Integer.parseInt(instrucao, 16) + ")");
								registrador.setAX(Instrucoes.add(registrador.getAX(), (int) Integer.parseInt(instrucao, 16), registrador));
								break;

							case "2B":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // sub AX,DX
									System.out.println("sub AX,DX");
								} else if (instrucao.equals("C0")) {   // sub AX,AX
									System.out.println("sub AX,AX");
								}
								break;

							case "2D":                                     // sub AX,opd
								System.out.println("sub AX,opd");
								break;

							case "F7":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("F6"))) {      // div SI
									System.out.println("div SI");
								} else if (instrucao.equals("F0")) {   // div AX
									System.out.println("div AX");
								} else if ((instrucao.equals("E6"))) { // mul SI
									System.out.println("mul SI");
								} else if (instrucao.equals("E0")) {   // mul AX
									System.out.println("mul AX");
								} else if (instrucao.equals("D0")) {   // not AX
									System.out.println("not AX");
								}
								break;

							case "3D":                                     // cmp AX,opd
								System.out.println("cmp AX,opd");
								break;

							case "3B":                                     // cmp AX,DX
								System.out.println("cmp AX,DX");
								break;

							// ----> Instruções lógicas
							case "23":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // and AX,DX
									System.out.println("and AX,DX");
								} else if (instrucao.equals("C0")) {   // and AX,AX
									System.out.println("and AX,AX");
								}
								break;

							case "25":                                     // and AX,opd
								System.out.println("and AX,opd");
								break;

							case "0B":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // or AX,DX
									System.out.println("or AX,DX");
								} else if (instrucao.equals("C0")) {   // or AX,AX
									System.out.println("or AX,AX");
								}
								break;

							case "0D":                                     // or AX,opd
								System.out.println("or AX,opd");
								break;

							case "33":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // xor AX,DX
									System.out.println("xor AX,DX");
								} else if (instrucao.equals("C0")) {   // xor AX,AX
									System.out.println("xor AX,AX");
								}
								break;

							case "35":                                     // xor AX,opd
								System.out.println("xor AX,opd");
								break;

							// ----> Instruções de desvio
							case "EB":                                     // jmp opd
								System.out.println("jmp opd");
								break;

							case "75":                                     // jnz opd
								System.out.println("jnz opd");
								break;

							case "74":                                     // jz opd
								System.out.println("jZ opd");
								break;

							case "7A":                                     // jp opd
								System.out.println("jp opd");
								break;

							case "E8":                                     // call opd
								System.out.println("call opd");
								break;

							// ----> Instruções da pilha
							case "58":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // pop DX
									System.out.println("xor AX,DX");
								} else if (instrucao.equals("C0")) {   // pop AX
									System.out.println("xor AX,AX");
								}
								break;

							case "59":                                     // pop opd
								System.out.println("pop opd");
								break;

							case "50":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // push DX
									System.out.println("xor AX,DX");
								} else if (instrucao.equals("C0")) {   // push AX
									System.out.println("xor AX,AX");
								}
								break;

							case "9D":                                     // popf AX
								System.out.println("popf AX");
								break;

							case "9C":                                     // pushf AX
								System.out.println("pushf AX");
								break;

							// ----> Outras Instruções
							case "07":
								ch[0] = (char) arquivo_leitura.read();
								ch[1] = (char) arquivo_leitura.read();
								instrucao = new String(ch);
								if ((instrucao.equals("C2"))) {      // store DX
									System.out.println("store DX");
								} else if (instrucao.equals("C0")) {   // store AX
									System.out.println("store AX");
								}
								break;

							case "12":                                     // read input stream
								System.out.println("read input stream");
								break;

							case "08":                                     // write Output stream
								System.out.println("write Output stream");
								break;

							case "EE":                                     // hlt(fim do programa)
								System.out.println("hlt");
								break;

							case "EF":                                     // ret
								System.out.println("ret");
								break;

							default:
								JOptionPane.showMessageDialog(null, "Intrução [" + instrucao + "] não é aceita");
								break;
						}
					}
				}
				arquivo_leitura.close();
			} catch (IOException e) {
				System.out.println("Erro: ao manipular o arquivo");
			}
		}
	}
}