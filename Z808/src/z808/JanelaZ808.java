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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;
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
    
    private final int TAM_MAX_TABELAS = 1000;
    
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
        setLayout(new GridBagLayout());
        //setLayout(new GridLayout(2, 4, 25, 25));
        
        // onde todos os itens são inseridos        
        gbc = new GridBagConstraints();
        container = getContentPane();
        
        // painel superior - onde o path do arquivo tá listado
        criarPainelArquivo(pathArquivo);
        
        criarRegistradores();
    }
    
    private void criarPainelArquivo(String pathArquivo) {
        JPanel panelArquivo = new JPanel();
        panelArquivo.setLayout(new GridBagLayout());
        //panelArquivo.setBackground(Color.WHITE);
        
        JLabel labelArquivo = new JLabel("FILE PATH: ");
        labelArquivo.setForeground(Color.GRAY);
        panelArquivo.add(labelArquivo);
        
        JTextField fieldArquivo = new JTextField(30);
        fieldArquivo.setText(pathArquivo);
        fieldArquivo.setEditable(false); // read only
        fieldArquivo.setCaretColor(Color.WHITE);
        fieldArquivo.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldArquivo.setBackground(Color.WHITE);
        fieldArquivo.setForeground(Color.GRAY);
        panelArquivo.add(fieldArquivo);
        
        JButton btnReturn = new JButton("Return");
        btnReturn.setName("return-button");
        // PARA FAZER!!!
        //btnReturn.addActionListener(this);
        panelArquivo.add(btnReturn);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add( panelArquivo, gbc);
    }
    
    private void criarRegistradores() {
        JPanel panelRegistr = new JPanel(new GridBagLayout());
        panelRegistr.setLayout(new GridBagLayout());
        
        JLabel labelRegistr = new JLabel("REGISTERS");
        labelRegistr.setFont(new Font("Arial", Font.BOLD, 16));
        labelRegistr.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panelRegistr.add(labelRegistr, gbc);
        
        JLabel labelRegAX = new JLabel("AX");
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelRegistr.add(labelRegAX, gbc);
        fieldRegAX = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegAX, gbc);
        
        JLabel labelRegDX = new JLabel("DX");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRegistr.add(labelRegDX, gbc);
        fieldRegDX = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegDX, gbc);
        
        JLabel labelRegCL = new JLabel("CL");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelRegistr.add(labelRegCL, gbc);
        fieldRegCL = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegCL, gbc);
        
        JLabel labelRegRI = new JLabel("RI");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelRegistr.add(labelRegRI, gbc);
        fieldRegRI = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegRI, gbc);
        
        JLabel labelRegSI = new JLabel("SI");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelRegistr.add(labelRegSI, gbc);
        fieldRegSI = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSI, gbc);
        
        JLabel labelRegIP = new JLabel("IP");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelRegistr.add(labelRegIP, gbc);
        fieldRegIP = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegIP, gbc);
        
        JLabel labelRegSR = new JLabel("SR");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panelRegistr.add(labelRegSR, gbc);
        fieldRegSR = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSR, gbc);
        
        JLabel labelRegSP = new JLabel("SP");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panelRegistr.add(labelRegSP, gbc);
        fieldRegSP = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSP, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelRegistr, gbc);
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
    
    public void criarTabelaCodigo(Object[][] data) {
        JPanel panelTabela = criarTabela(data, "CODE AREA MEMORY");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void criarTabelaDados(Object[][] data) {        
        // tem que ignorar as posições que já foram usadas
        // talvez manter um contador global
        Object[][] aux;
        if (Array.getLength(data) > TAM_MAX_TABELAS) 
            aux = Arrays.copyOfRange(data, 0, 1000);
        else
            aux = data;
        
        JPanel panelTabela = criarTabela(aux, "DATA AREA MEMORY");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void criarTabelaPilha(Object[][] data) {
        Object[][] aux;
        if (Array.getLength(data) > TAM_MAX_TABELAS) 
            aux = Arrays.copyOfRange(data, Memoria.TAM_MAXIMO - TAM_MAX_TABELAS, Memoria.TAM_MAXIMO);
        else
            aux = data;
        
        JPanel panelTabela = criarTabela(aux, "STACK");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    private JPanel criarTabela(Object[][] data, String nomeTabela) {
        JPanel panelTabela = new JPanel();
        panelTabela.setName(nomeTabela);
        //panelTabela.setLayout(new GridBagLayout());
        
        JLabel labelTabela = new JLabel(nomeTabela);
        labelTabela.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;  
        gbc.gridheight = 1;
        panelTabela.add(labelTabela, gbc);
        
        String[] colunas = {"Address", "Value"};
        
        JTable tableMemoria = new JTable(data, colunas);
        //tableMemoria.getColumnModel().getColumn(0).setPreferredWidth(50);
        //tableMemoria.getColumnModel().getColumn(1).setPreferredWidth(50);
        JScrollPane barraRolagem = new JScrollPane(tableMemoria);
        //panelTabela.add(tableMemoria, gbc);      
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;  
        gbc.gridheight = 3;
        panelTabela.add(barraRolagem, gbc);
        
        
        //container.add(panelTabela);

        return panelTabela;
    }
    
    // a intenção era atualizar a memória conforme as instruções rodam
    // mas acho que tem uma função que tu seta de X a Y
    // e ele remove esses itens
    // mas pensando agora, a memória pode atualizar em qualquer lugar
    // então talvez seja necessário
    //public void atualizarMemCodigo(Object[][] data) {
        //tableMemCod.repaint();  
    //}
    
    // TALVEZ CRIAR UMA FUNÇÃO DE UPDATE!!!
    // VERIFICA O QUE FOI MUDADO
    // E ATUALIZA APENAS O QUE FOI MUDADO
    
    
}
