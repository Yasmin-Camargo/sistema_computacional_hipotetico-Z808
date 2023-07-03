/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
public class JanelaZ808 extends JFrame{
    private JTextField barra_texto = new JTextField(45);

    @SuppressWarnings("empty-statement")
    public JanelaZ808(String nome_arquivo){        
        barra_texto.setText(nome_arquivo);
              
        //Cria a janela em x por y
        JFrame frame = new JFrame("Z808 Emulator");
        frame.setMinimumSize(new Dimension(900, 600)); 
        frame.setLocationRelativeTo(null); // Centralizando como padrão.
        frame.setLocationRelativeTo(null);
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
        label.setForeground(Color.GRAY);
        barra_texto.setEditable(false); // read only
        barra_texto.setCaretColor(Color.WHITE);
        barra_texto.setFont(new Font("Arial", Font.PLAIN, 14));
        barra_texto.setForeground(Color.GRAY);

        painel_selecao.add(label);
        painel_selecao.add(barra_texto);

        painel_principal.add(new JLabel());

        painel_principal.setBackground(Color.red);
        painel_selecao.setBackground(Color.black);
        frame.add(painel_principal);
        frame.add( painel_selecao);
        frame.setVisible(true);
    }
}
