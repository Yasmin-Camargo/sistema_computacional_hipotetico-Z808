/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
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
	
	private JTextField barra_texto = new JTextField(45);
	private String nome_arquivo = "";
	
	@SuppressWarnings("empty-statement")
	public GUI(){
		
		//Cria a janela em x por y
		JFrame frame = new JFrame("Z808 Emulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		//Cria o painel de seleção de arquivo fonte
		JPanel painel_selecao = new JPanel();
		JLabel label = new JLabel("Arquivo fonte:");
		JButton escolher_arquivo = new JButton("...");
		JButton confirmar = new JButton ("Confirmar");
		painel_selecao.add(label);
		painel_selecao.add(barra_texto);
		painel_selecao.add(escolher_arquivo);
		painel_selecao.add(confirmar);
		
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
		
		//Posiciona painel de seleção no topo
		frame.getContentPane().add(BorderLayout.NORTH, painel_selecao);
		frame.setVisible(true);
		
	}
	
	public String getCaminho(){
		return nome_arquivo;
	}
}
