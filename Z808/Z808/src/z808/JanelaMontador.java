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
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class JanelaMontador extends JFrame {
    JTextArea area; 
    public JanelaMontador(String[][] montador, int LC, int PC, Map<String,Integer> tabela) {
        // janela
        setTitle("Z808 Emulator: MONTADOR");
        setSize(400, 300);
        //setLocationRelativeTo(); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(600, 400)); 
        setVisible(true);
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());
        
        Container container = getContentPane();
        //GridBagConstraints gbc = new GridBagConstraints();
        
        //JLabel label = new JLabel("Montador");
        //label.setFont(new Font("Arial", Font.BOLD, 16));
        //container.add(label);
        
        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", 0, 10));
        area.setEditable(false);
        area.setMinimumSize(new Dimension(560, 400));
        area.setSize(560, 400);
       // area.setPreferredSize(new Dimension(250, 1000));
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setMinimumSize(new Dimension(580, 350));
        scroll.setMaximumSize(new Dimension(580, 350));
        
        area.append("\n   -----    MONTADOR    -----\n\n");
        area.append("linha\t\tendereco  |\tlabel\t\toperacao\toperando1\toperando2\n");
        int i, j;
        for (i = 0; i < montador.length; i++) {
            for (j = 0; j < montador[i].length; j++) {
                area.append(montador[i][j] + "\t\t");
            }
    
        }
        
        // Print dos contadores
        area.append("\n\nLC =  "+LC+"\nPC =  "+ PC);
        area.append("\n\nTabela de Simbolos \n");
        
        // Print da tabela de símbolos
        for (String chave : tabela.keySet()) {
            area.append(chave + ": " + tabela.get(chave)+"\n");
        }
        
        container.add(scroll);
    }
    
    public void addTexto(String novoTexto) {
        area.append("\n"+novoTexto);
    }
}
