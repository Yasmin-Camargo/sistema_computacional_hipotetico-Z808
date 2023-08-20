/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class JanelaMontador extends JFrame {
    GridBagConstraints gbc;
    Container container;
    JTextArea area; 
    
    public JanelaMontador(String[][] montador, int LC, int PC, Map<String,Integer> tabela) {
        // janela
        setTitle("Z808 Emulator: MONTADOR");
        setSize(700, 300);
        //setLocationRelativeTo(); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(700, 400)); 
        setVisible(true);
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());
        
        container = getContentPane();
        gbc = new GridBagConstraints();
        
        criarVisual(montador);
        
        // Print dos contadores
        /*area.append("\n\nLC =  "+LC+"\nPC =  "+ PC);
        area.append("\n\nTabela de Simbolos \n");
        
        // Print da tabela de símbolos
        for (String chave : tabela.keySet()) {
            area.append(chave + ": " + tabela.get(chave)+"\n");
        }
        
        container.add(scroll);*/
    }
    
    private void criarVisual(String[][] montador) {
        JLabel label = new JLabel("Montador");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(label, gbc);
        
        String[] colunas = {"Linha", " Endereço", "Label", "Operação", "Operando 1", "Operando 2"};
        JTable tabela = new JTable(montador, colunas);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        
        JScrollPane scroll = new JScrollPane(tabela); 
        scroll.setPreferredSize(new Dimension(475, 330));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(scroll, gbc);
        
        
        
    }

    public void addTexto(String novoTexto) {
        //area.append("\n"+novoTexto);
    }
}
