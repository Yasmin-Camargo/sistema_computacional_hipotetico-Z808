/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author claud
 */
public class GUI extends JFrame{
	
	private JTextField barra_texto = new JTextField(45);
	private String nome_arquivo = "";
	
	@SuppressWarnings("empty-statement")
	public GUI(){
		
		//Cria a janela em x por y
		JFrame frame = new JFrame("Z808 Emulator");
		frame.setBackground(Color.GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		
		JTextArea instrucoes = new JTextArea();

		//Cria o painel de seleção de arquivo fonte
		JPanel painel_principal = new JPanel();
		JPanel painel_selecao = new JPanel();
		JPanel painel_instrucoes = new JPanel();
		
		painel_principal.setBounds(560, 55, 200, 450);
		painel_principal.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
		
		painel_instrucoes.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

		instrucoes.setBounds(10,30,50,250);
		painel_principal.add(instrucoes);
		JLabel label = new JLabel("Arquivo fonte:");
		label.setForeground(Color.white);
		JButton escolher_arquivo = new JButton("...");
		JButton confirmar = new JButton ("Confirmar");
		
		painel_selecao.add(label);
		painel_selecao.add(barra_texto);
		painel_selecao.add(escolher_arquivo);
		painel_selecao.add(confirmar);
		painel_principal.add(new JLabel());
		
		//Seleciona o arquivo pra virar a fonte
		escolher_arquivo.addActionListener((ActionEvent e) -> {
			JFileChooser arquivo = new JFileChooser("src\\z808");
			int escolha = arquivo.showOpenDialog(GUI.this);
			if (escolha == JFileChooser.APPROVE_OPTION){
				File arquivo_selecionado = arquivo.getSelectedFile();
				barra_texto.setText(arquivo_selecionado.getPath());
			}
			
			if(escolha == JFileChooser.CANCEL_OPTION){
				barra_texto.setText("Nenhum arquivo selecionado");
			}
		});
		
		//Atribui o retorno do caminho do arquivo fonte ao botão confirmar
		confirmar.addActionListener((ActionEvent e) -> {
			nome_arquivo = barra_texto.getText();
		});
		
		painel_principal.setBackground(Color.red);
		painel_selecao.setBackground(Color.black);
		//Posiciona painel de seleção no topo
//		frame.getContentPane().add(BorderLayout.EAST, painel_visual);
		frame.add(painel_principal);
		frame.add( painel_selecao);
		frame.setVisible(true);
		
	}
	
	public String getCaminho(){
		return nome_arquivo;
	}
}
