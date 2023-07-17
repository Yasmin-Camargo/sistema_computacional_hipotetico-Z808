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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
    private JTextField fieldRegFlagCF, fieldRegFlagPF, fieldRegFlagIF, fieldRegFlagZF, fieldRegFlagSF, fieldRegFlagOF;
    private JLabel labelSaida;
    private JTextArea areaSaida;
    
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
        
        criarPainelArquivo(pathArquivo); // cria painel superior com localização do arquivo
        criarRegistradores(); // cria JTextFields dos registradores
        // tabelas são criadas por chamadas em Z808.java
        criarPanelSaida(); // cria painel que mostra infos
        criarBotoes(); // cria botões na parte inferior
    }
    
    private void criarPainelArquivo(String pathArquivo) {
        JPanel panelArquivo = new JPanel();
        panelArquivo.setLayout(new GridBagLayout());
        //panelArquivo.setBackground(Color.WHITE);
        
        JLabel labelArquivo = new JLabel("FILE PATH: ");
        labelArquivo.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelArquivo.add(labelArquivo, gbc);
        
        JTextField fieldArquivo = new JTextField(30);
        fieldArquivo.setText(pathArquivo);
        fieldArquivo.setEditable(false); // read only
        fieldArquivo.setCaretColor(Color.WHITE);
        fieldArquivo.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldArquivo.setBackground(Color.WHITE);
        fieldArquivo.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        panelArquivo.add(fieldArquivo, gbc);
     
        JButton btnVoltar = new JButton("return");
        //btnVoltar.setName("return-button");
        btnVoltar.addActionListener((ActionEvent e) -> {
            btnVoltar();
        }); 
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        panelArquivo.add(btnVoltar, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;        
        container.add( panelArquivo, gbc);
    }
    
    private void btnVoltar() {
        System.out.println("VOLTAR   -- Janela Inicial sera reaberta.");
        System.out.println("\t -- Janela do Emulador sera fechada.");
        System.out.println("\t -- Usuario podera escolher novo arquivo para ser aberto.");
        JanelaInicial janelaInicial = new JanelaInicial();
        janelaInicial.setVisible(true);
        this.dispose();
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
        
        
        JLabel labelRegSP = new JLabel("SP");
        gbc.gridx = 0;
        gbc.gridy = 9;
        panelRegistr.add(labelRegSP, gbc);
        fieldRegSP = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegSP, gbc);
        
        
        // registradores SR
        // FLAGS
        JLabel labelRegFlagCF = new JLabel("Flag CF");
        gbc.gridx = 0;
        gbc.gridy = 10;        
        panelRegistr.add(labelRegFlagCF, gbc);
        fieldRegFlagCF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagCF, gbc);
        
        JLabel labelRegFlagPF = new JLabel("Flag PF");
        gbc.gridx = 0;
        gbc.gridy = 11;
        panelRegistr.add(labelRegFlagPF, gbc);
        fieldRegFlagPF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagPF, gbc);
        
        JLabel labelRegFlagIF = new JLabel("Flag IF");
        gbc.gridx = 0;
        gbc.gridy = 12;
        panelRegistr.add(labelRegFlagIF, gbc);
        fieldRegFlagIF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagIF, gbc);
        
        JLabel labelRegFlagZF = new JLabel("Flag ZF");
        gbc.gridx = 0;
        gbc.gridy = 13;
        panelRegistr.add(labelRegFlagZF, gbc);
        fieldRegFlagZF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagZF, gbc);
        
        JLabel labelRegFlagSF = new JLabel("Flag SF");
        gbc.gridx = 0;
        gbc.gridy = 14;
        panelRegistr.add(labelRegFlagSF, gbc);
        fieldRegFlagSF = new JTextField(5);
        gbc.gridx = 1;
        panelRegistr.add(fieldRegFlagSF, gbc);
        
        JLabel labelRegFlagOF = new JLabel("Flag OF");
        gbc.gridx = 0;
        gbc.gridy = 15;
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
    
    public void atualizarRegistradores(Registradores reg) {
        fieldRegCL.setText(""+reg.getCL());
        fieldRegRI.setText(""+reg.getRI());
        fieldRegIP.setText(""+reg.getIP());
        fieldRegSI.setText(""+reg.getSI());
        fieldRegAX.setText(""+reg.getAX());
        fieldRegDX.setText(""+reg.getDX());
        //fieldRegSR.setText(""+reg.getSR());
        fieldRegSP.setText(""+reg.getSP());
        int[] regSR = reg.getSR();
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
        
        JScrollPane barraRolagem = new JScrollPane(tableMemoria); 
        barraRolagem.setPreferredSize(new Dimension(170, 260));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;  
        gbc.gridheight = 3;
        panelTabela.add(barraRolagem, gbc);

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
    
    private void criarPanelInstrucoes() {
        JPanel panelInstr = new JPanel();
        panelInstr.setLayout(new GridBagLayout());
        //panelInstr.setSize(new Dimension)
    }
    
    private void criarPanelSaida() {
        JPanel panelSaida = new JPanel();
        panelSaida.setLayout(new GridBagLayout());
        //panelSaida.setSize(new Dimension(200, 400));
        
        JLabel labelSaidaTitulo = new JLabel("OUTPUT:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        
        panelSaida.add(labelSaidaTitulo, gbc);
        panelSaida.setPreferredSize(new Dimension(500, 200));
        
        areaSaida = new JTextArea();
        areaSaida.setLineWrap(true);
        areaSaida.setWrapStyleWord(true);
        areaSaida.setFont(new Font("Monospaced", 0, 10));
        areaSaida.setEditable(false);
        areaSaida.setSize(new Dimension(800, 50));        
       
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        panelSaida.add(areaSaida, gbc);        

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        container.add( panelSaida, gbc);
    }
    
    public void resetarSaida() {
        areaSaida.setText("");
    }
    
    public void atualizarSaida(String novoTexto) {
        //labelSaida.setText(labelSaida.getText() + "; " + novoTexto);
        areaSaida.append(novoTexto + '\n');
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
