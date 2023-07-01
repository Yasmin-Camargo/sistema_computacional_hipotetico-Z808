/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author claud
 */
public class GUI extends JFrame{
	
	JTextField nome_arquivo = new JTextField(45);
	
	public GUI(){
		
		//Cria a janela em x por y
		JFrame frame = new JFrame("Z808 Emulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		//Cria o painel de seleção de arquivo fonte
		JPanel painel_selecao = new JPanel();
		JLabel instrucoes = new JLabel("Arquivo fonte:");
		JButton escolher_arquivo = new JButton("...");
		JButton confirmar = new JButton ("Confirmar");
		painel_selecao.add(instrucoes);
		painel_selecao.add(nome_arquivo);
		painel_selecao.add(escolher_arquivo);
		painel_selecao.add(confirmar);
		
		//Seleciona o arquivo pra virar a fonte
		escolher_arquivo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser arquivo = new JFileChooser("src");
				int escolha = arquivo.showOpenDialog(GUI.this);
				if (escolha == JFileChooser.APPROVE_OPTION){
					File arquivo_selecionado = arquivo.getSelectedFile();
					nome_arquivo.setText(arquivo_selecionado.getPath());
				}
				
				if(escolha == JFileChooser.CANCEL_OPTION){
					nome_arquivo.setText("Nenhum arquivo selecionado");
				}
			}
		});
		
		
		
		//Posiciona painel
		frame.getContentPane().add(BorderLayout.SOUTH, painel_selecao);
		frame.setVisible(true);
	}
}
