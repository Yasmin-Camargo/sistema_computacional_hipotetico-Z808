package z808;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JanelaMontador extends JFrame {
    GridBagConstraints gbc;
    Container container;
    JTextArea area; 
    
    public JanelaMontador(String[][] montador, int lc, int pc, Map<String,Integer> tabela, String nomeArq) {
        // janela
        setTitle("Z808 Emulator: MONTADOR " + nomeArq);
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(700, 400)); 
        setVisible(true);
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());
        
        container = getContentPane();
        gbc = new GridBagConstraints();
        
        criarVisual(montador, lc, pc, tabela, nomeArq);
    }
    
    private void criarVisual(String[][] montador, int lc, int pc, Map<String,Integer> tabela, String nomeArq) {
        JPanel panelTopo = new JPanel();
        panelTopo.setLayout(new GridBagLayout());
        
        JLabel labelMontador = new JLabel("Montador " + nomeArq);
        labelMontador.setFont(new Font("Arial", Font.BOLD, 16));
        labelMontador.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelTopo.add(labelMontador, gbc);

        JLabel labelLC = new JLabel("LC: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 5);
        panelTopo.add(labelLC, gbc);
        gbc.gridx = 1;
        
        JTextField fieldLC = new JTextField(10);
        fieldLC.setText(Integer.toString(lc));
        fieldLC.setEditable(false); // read only
        fieldLC.setCaretColor(Color.WHITE);
        fieldLC.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panelTopo.add(fieldLC, gbc);
                
        JLabel labelPC = new JLabel("PC: ");
        gbc.gridx = 2;
        panelTopo.add(labelPC, gbc);
        
        JTextField fieldPC = new JTextField(10);
        fieldPC.setText(Integer.toString(pc));
        fieldPC.setEditable(false); // read only
        fieldPC.setCaretColor(Color.WHITE);
        fieldPC.setBackground(Color.WHITE);
        gbc.gridx = 3;
        panelTopo.add(fieldPC, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        container.add(panelTopo, gbc);
        
        JPanel panelTabela = new JPanel();
        panelTabela.setLayout(new GridBagLayout());
        
        String[] colunas = {"Linha", " Endereço", "Label", "Operação", "Operando 1", "Operando 2"};
        JTable tabelaM = new JTable(montador, colunas);
        tabelaM.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        
        JScrollPane scroll = new JScrollPane(tabelaM); 
        scroll.setPreferredSize(new Dimension(475, 330));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelTabela.add(scroll, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(panelTabela, gbc);
        
        JPanel panelTSimbolos = new JPanel();
        panelTSimbolos.setLayout(new GridBagLayout());   
        
        JLabel labelTSimbolos = new JLabel("Tabela de Símbolos ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        panelTSimbolos.add(labelTSimbolos, gbc);
        
        int nivel = 1;
        gbc.insets = new Insets(2, 0, 0, 0);
        for (String chave : tabela.keySet()) {
            JLabel label = new JLabel(chave);
            gbc.gridx = 0;
            gbc.gridy = nivel;
            panelTSimbolos.add(label, gbc);

            JTextField field = new JTextField(10);
            field.setText(Integer.toString(tabela.get(chave)));
            field.setEditable(false); // read only
            field.setCaretColor(Color.WHITE);
            field.setBackground(Color.WHITE);
            gbc.gridx = 1;
            panelTSimbolos.add(field, gbc);
            
            nivel += 1;
        }
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        gbc.gridwidth = nivel;
        container.add(panelTSimbolos, gbc);
    }

    public void addTexto(String novoTexto) {
        //area.append("\n"+novoTexto);
    }
}
