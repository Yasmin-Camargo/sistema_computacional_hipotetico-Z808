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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        GridBagConstraints gbc = new GridBagConstraints();
        Container container = getContentPane();
        
        // painel superior - onde o path do arquivo tá listado
        JPanel panelArquivo = new JPanel();
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
        
        
        
        // painel superior esquerdo - dos registradores 
        JPanel panelRegistr = criarRegistradores(gbc);
        container.add(panelRegistr);
        
        
        JTable tableMemoria = new JTable();
        //String[] colunas = {"", ""};
        //Object[][] dadis = {
            //{"", ""},
            //{"", ""},
            //{"", ""}
        //};
        //container.tableMemoria;
        

        //JTable tablePilha = new JTable();
        
        
        
        /*
        // painel da direita
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBounds(560, 55, 200, 450);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        //panelPrincipal.add(areaInstrucoes);
        panelPrincipal.setBackground(Color.GREEN);
        
           
        container.add(panelPrincipal);
        */
    }
    
    JPanel criarRegistradores(GridBagConstraints gbc) {
        JPanel panelRegistr = new JPanel(new GridBagLayout());
        
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
}
