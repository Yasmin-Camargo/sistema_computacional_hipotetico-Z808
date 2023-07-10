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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Claudio & Duda & Júlia Veiga & Júlia Junqueira
 */
public class JanelaZ808 extends JFrame{
    
    private final int TAM_MAX_TABELAS = 1000;
    
    private JTextField fieldRegAX, fieldRegDX, fieldRegCL, fieldRegRI, fieldRegSI, fieldRegIP, fieldRegSR, fieldRegSP;
    private JTextField fieldRegFlagCF, fieldRegFlagPF, fieldRegFlagIF, fieldRegFlagZF, fieldRegFlagSF, fieldRegFlagOF;
    private JTextField fieldSaida;
    
    Container container;
    private GridBagConstraints gbc;
    
    @SuppressWarnings("empty-statement")
    public JanelaZ808(String pathArquivo){ 
        // janela
        setTitle("Z808 Emulator");
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(1000, 600)); 
        setVisible(true);
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());
        
        // onde todos os itens são inseridos        
        gbc = new GridBagConstraints();
        container = getContentPane();
        
        // painel superior - onde o path do arquivo tá listado
        criarPainelArquivo(pathArquivo);
        
        criarRegistradores();
        
        criarPanelSaida();
        
        criarBotoes();
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
        
        JButton btnVoltar = new JButton("return");
        //btnVoltar.setName("return-button");
        btnVoltar.addActionListener((ActionEvent e) -> {
            btnVoltar();
        }); 
        panelArquivo.add(btnVoltar);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add( panelArquivo, gbc);
    }
    
    private void btnVoltar() {
        System.out.println("VOLTAR   -- Janela Inicial sera reaberta.");
        System.out.println("\t -- Janela do Emulador sera fechada.");
        System.out.println("\t -- Usuario podera escolher novo arquivo para ser aberto.");
    }
    
    private void criarRegistradores() {
        JPanel panelRegistr = new JPanel(new GridBagLayout());
        panelRegistr.setLayout(new GridBagLayout());
        
        JLabel labelRegistr = new JLabel("REGISTERS");
        //labelRegistr.setPreferredSize(new Dimension(150, 30));
        labelRegistr.setFont(new Font("Arial", Font.BOLD, 16));
        labelRegistr.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(1, 1, 1, 1);
        panelRegistr.add(labelRegistr, gbc);
        
        JLabel labelRegAX = new JLabel("AX");
        //gbc.fill = GridBagConstraints.HORIZONTAL;
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
        
        
        // registradores SR
        // FLAGS
        JLabel labelRegFlagCF = new JLabel("Flag CF");
        gbc.gridx = 0;
        gbc.gridy = 9;        
        panelRegistr.add(labelRegFlagCF, gbc);
        fieldRegFlagCF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagCF, gbc);
        
        JLabel labelRegFlagPF = new JLabel("Flag PF");
        gbc.gridx = 0;
        gbc.gridy = 10;
        panelRegistr.add(labelRegFlagPF, gbc);
        fieldRegFlagPF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagPF, gbc);
        
        JLabel labelRegFlagIF = new JLabel("Flag IF");
        gbc.gridx = 0;
        gbc.gridy = 11;
        panelRegistr.add(labelRegFlagIF, gbc);
        fieldRegFlagIF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagIF, gbc);
        
        JLabel labelRegFlagZF = new JLabel("Flag ZF");
        gbc.gridx = 0;
        gbc.gridy = 12;
        panelRegistr.add(labelRegFlagZF, gbc);
        fieldRegFlagZF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagZF, gbc);
        
        JLabel labelRegFlagSF = new JLabel("Flag SF");
        gbc.gridx = 0;
        gbc.gridy = 13;
        panelRegistr.add(labelRegFlagSF, gbc);
        fieldRegFlagSF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagSF, gbc);
        
        JLabel labelRegFlagOF = new JLabel("Flag OF");
        gbc.gridx = 0;
        gbc.gridy = 14;
        panelRegistr.add(labelRegFlagOF, gbc);
        fieldRegFlagOF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagOF, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
    
    public void atualizarFlagsSR(int[] regSR) {
        fieldRegFlagCF.setText(""+regSR[0]);
        fieldRegFlagPF.setText(""+regSR[6]);
        fieldRegFlagIF.setText(""+regSR[7]);
        fieldRegFlagZF.setText(""+regSR[8]);
        fieldRegFlagSF.setText(""+regSR[9]);
        fieldRegFlagOF.setText(""+regSR[12]);
    }
    
    public void criarTabelaCodigo(Object[][] data) {
        JPanel panelTabela = criarTabela(data, " CODE  AREA  MEMORY ");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void criarTabelaDados(Object[][] data, int tamCod) {        
        // tem que ignorar as posições que já foram usadas
        // talvez manter um contador global
        Object[][] aux;
        if (Array.getLength(data) > TAM_MAX_TABELAS) 
            aux = Arrays.copyOfRange(data, tamCod, 1000+tamCod);
        else
            aux = data;
        
        JPanel panelTabela = criarTabela(aux, " DATA  AREA  MEMORY ");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void criarTabelaPilha(Object[][] data) {
        Object[][] aux;
        if (Array.getLength(data) > TAM_MAX_TABELAS) {
            aux = Arrays.copyOfRange(data, Memoria.TAM_MAXIMO - TAM_MAX_TABELAS, Memoria.TAM_MAXIMO);
            aux = reverterVetor(aux);
        }
        else {
            aux = reverterVetor(data);
        }
        
        // MUDAR PARA CRIARTABELA(DATA) CASO QUEIRA TODAS AS LINHAS
        JPanel panelTabela = criarTabela(aux, " STACK  MEMORY ");
        gbc.gridx = 3;
        gbc.gridy = 1;
        //gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    private Object[][] reverterVetor(Object[][] data) {
        Object aux[];
        int tamanho = Array.getLength(data) - 1;
        for (int i = 0; i < Math.floor(tamanho/2); i++) {
            aux = data[i];
            data[i] = data[tamanho - i];
            data[tamanho - i] = aux;
        }
        return data;
    }
    
    private JPanel criarTabela(Object[][] data, String nomeTabela) {
        JPanel panelTabela = new JPanel();
        panelTabela.setName(nomeTabela);
        //panelTabela.setLayout(new GridBagLayout());
        
        JLabel labelTabela = new JLabel(nomeTabela);
        labelTabela.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;  
        gbc.gridheight = 1;
        panelTabela.add(labelTabela, gbc);
        
        String[] colunas = {"Address", "Value"};
        
        JTable tableMemoria = new JTable(data, colunas);
        
        tableMemoria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        //tableMemoria.setMaximumSize(new Dimension(200, 100));
        
        JScrollPane barraRolagem = new JScrollPane(tableMemoria); 
        barraRolagem.setPreferredSize(new Dimension(170, 280));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
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
    
    private void criarPanelSaida() {
        JPanel panelSaida = new JPanel();
        panelSaida.setLayout(new GridBagLayout());
        //panelWrite.setBackground(Color.WHITE);
        
        JLabel labelSaida = new JLabel("OUTPUT:");
        //labelSaida.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        
        panelSaida.add(labelSaida, gbc);
        
        fieldSaida = new JTextField(60);
        fieldSaida.setText("");
        fieldSaida.setEditable(false); // read only       
        fieldSaida.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldSaida.setBackground(Color.WHITE);
       
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        panelSaida.add(fieldSaida, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        container.add( panelSaida, gbc);
    }
    
    public void resetarSaida() {
        fieldSaida.setText("");
    }
    
    public void atualizarSaida(String novoTexto) {
        fieldSaida.setText(fieldSaida.getText() + novoTexto);
    }
    
    private void criarBotoes() {
        JPanel panelBotoes = new JPanel(new GridBagLayout());
        
        JButton btnIniciar = new JButton("start");
        btnIniciar.addActionListener((ActionEvent e) -> {
            btnIniciar();
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        panelBotoes.add(btnIniciar, gbc);
        
        JButton btnResetar = new JButton("reset");
        btnResetar.addActionListener((ActionEvent e) -> {
            btnResetar();
        });
        gbc.gridx = 3;
        panelBotoes.add(btnResetar, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        container.add(panelBotoes, gbc);
    }
    
    private void btnIniciar() {
        System.out.println("INICIAR -- Programa sera executado.");
    }
    
     private void btnResetar() {
        System.out.println("RESETAR -- Programa sera resetado para seu estado inicial.");
    } 
}
