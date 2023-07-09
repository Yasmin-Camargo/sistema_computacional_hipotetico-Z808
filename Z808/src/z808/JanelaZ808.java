/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Claudio & Duda & Júlia Veiga & Júlia Junqueira
 */
public class JanelaZ808 extends JFrame{
    
    private JTextField fieldRegAX, fieldRegDX, fieldRegCL, fieldRegRI, fieldRegSI, fieldRegIP, fieldRegSR, fieldRegSP;
    private JTable tableMemCod;
    Container container;
    private GridBagConstraints gbc;
    
    @SuppressWarnings("empty-statement")
    public JanelaZ808(String pathArquivo){ 
        // janela
        setTitle("Z808 Emulator");
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(900, 600)); 
        setVisible(true);
        setBackground(Color.GRAY);
        
        // onde todos os itens são inseridos        
        gbc = new GridBagConstraints();
        container = getContentPane();
        
        // painel superior - onde o path do arquivo tá listado
        JPanel panelArquivo = new JPanel();
        panelArquivo.setLayout(new GridBagLayout());
        panelArquivo.setBackground(Color.WHITE);
        
        JLabel labelArquivo = new JLabel("Arquivo Fonte:");
        labelArquivo.setForeground(Color.GRAY);
        panelArquivo.add(labelArquivo);
        
        JTextField fieldArquivo = new JTextField(45);
        fieldArquivo.setText(pathArquivo);
        fieldArquivo.setEditable(false); // read only
        fieldArquivo.setCaretColor(Color.WHITE);
        fieldArquivo.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldArquivo.setForeground(Color.GRAY);
        panelArquivo.add(fieldArquivo);
        
        JButton btnReturn = new JButton("Return");
        btnReturn.setName("return-button");
        // PARA FAZER!!!
        //btnReturn.addActionListener(this);
        panelArquivo.add(btnReturn);
        
        container.add( panelArquivo);
        
        JPanel panelRegistr = criarRegistradores();
        container.add(panelRegistr);
        
        /*
        JPanel panelMemoriaCod = criarTabela(gbc);
        container.add(panelMemoriaCod);
        
        JPanel panelMemoriaDados = criarTabela(gbc);
        container.add(panelMemoriaDados);
        
        JPanel panelPilha = criarTabela(gbc);
        container.add(panelPilha);
        */
    }
    
    JPanel criarRegistradores() {
        JPanel panelRegistr = new JPanel(new GridBagLayout());
        panelRegistr.setLayout(new GridBagLayout());
        
        JLabel labelRegistr = new JLabel("REGISTRADORES");
        labelRegistr.setFont(new Font("Arial", Font.BOLD, 16));
        labelRegistr.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panelRegistr.add(labelRegistr, gbc);
        
        JLabel labelRegAX = new JLabel("AX");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelRegistr.add(labelRegAX, gbc);
        fieldRegAX = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegAX, gbc);
        
        JLabel labelRegDX = new JLabel("DX");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRegistr.add(labelRegDX, gbc);
        fieldRegDX = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegDX, gbc);
        
        JLabel labelRegCL = new JLabel("CL");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelRegistr.add(labelRegCL, gbc);
        fieldRegCL = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegCL, gbc);
        
        JLabel labelRegRI = new JLabel("RI");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelRegistr.add(labelRegRI, gbc);
        fieldRegRI = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegRI, gbc);
        
        JLabel labelRegSI = new JLabel("SI");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelRegistr.add(labelRegSI, gbc);
        fieldRegSI = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSI, gbc);
        
        JLabel labelRegIP = new JLabel("IP");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelRegistr.add(labelRegIP, gbc);
        fieldRegIP = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegIP, gbc);
        
        JLabel labelRegSR = new JLabel("SR");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panelRegistr.add(labelRegSR, gbc);
        fieldRegSR = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSR, gbc);
        
        JLabel labelRegSP = new JLabel("SP");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panelRegistr.add(labelRegSP, gbc);
        fieldRegSP = new JTextField(10);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSP, gbc);
        
        return panelRegistr;
    }
    
    public void atualizarRegCL(int value) {
        fieldRegCL.setText(Integer.toString(value));
    }
    
    public void atualizarRegRI(int value) {
        fieldRegRI.setText(Integer.toString(value));
    }
    
    public void atualizarRegIP(int value) {
        fieldRegIP.setText(Integer.toString(value));
    }
    
    public void atualizarRegSI(int value) {
        fieldRegSI.setText(Integer.toString(value));
    }
    
    public void atualizarRegAX(int value) {
        fieldRegAX.setText(Integer.toString(value));
    }
    
    public void atualizarRegDX(int value) {
        fieldRegDX.setText(Integer.toString(value));
    }
    
    public void atualizarRegSR(int value) {
        fieldRegSR.setText(Integer.toString(value));
    }
    
    public void atualizarRegSP(int value) {
        fieldRegSP.setText(Integer.toString(value));
    }
    
    public void criarTabela(Object[][] data) {
        JPanel panelTabela = new JPanel(new GridBagLayout());
        panelTabela.setLayout(new GridBagLayout());
        
        PrintStream stream = new PrintStream(System.out);
        for (Object[] data1 : data) {
            stream.print(data1[0]);
        }
        stream.flush();
        
        String[] colunas = {"Endereço", "Valor"};
        //Object[][] data = {
            /*{"Kathy", "Smith"},
            {"John", "Doe"},
            {"Sue", "Black"},
            {"Jane", "White"},*/
        //};
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;  
        gbc.gridheight = 3;
        JTable tableMemoria = new JTable(data, colunas);
        JScrollPane barraRolagem = new JScrollPane(tableMemoria);
        //panelTabela.add(tableMemoria, gbc);
        panelTabela.add(barraRolagem, gbc);
        container.add(panelTabela);

        //return panelTabela;
    }
    
    // a intenção era atualizar a memória conforme as instruções rodam
    // mas acho que tem uma função que tu seta de X a Y
    // e ele remove esses itens
    // mas pensando agora, a memória pode atualizar em qualquer lugar
    // então talvez seja necessário
    //public void atualizarMemCodigo(Object[][] data) {
        //tableMemCod.repaint();  
    //}
    
    
}
