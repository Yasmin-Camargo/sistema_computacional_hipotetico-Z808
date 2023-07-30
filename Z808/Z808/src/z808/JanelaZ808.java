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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

public class JanelaZ808 extends JFrame{
    
    private final int TAM_MAX_TABELAS = 1000;
    Z808 sistema;
    
    private JTextField fieldRegAX, fieldRegDX, fieldRegCL, fieldRegRI, fieldRegSI, fieldRegIP, fieldRegSR, fieldRegSP;
    private JTextField fieldRegFlagCF, fieldRegFlagPF, fieldRegFlagIF, fieldRegFlagZF, fieldRegFlagSF, fieldRegFlagOF;
    private JTextArea areaInstr, areaSaida;
    private JButton btnCarregarInstr, btnExecutar, btnResetar, btnPasso;
    int[] dadosMemoria;
    int chegouAoFim = 0, contInstr = 0;
    
    Container container;
    private GridBagConstraints gbc;
    
    @SuppressWarnings("empty-statement")
    public JanelaZ808(String pathArquivo) throws IOException { 
        sistema = new Z808(pathArquivo);
        
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
        criarTextAreas();
        criarBotoes(); // cria botões na parte inferior
        btnCarregarInstr();
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
        sistema.matarJanelaMontador();
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
    
    
    private void criarTextAreas() {
        JPanel panelInstr = criarTextArea(areaInstr, "INSTRUCTIONS");
        pack();
        JScrollPane scroll = (JScrollPane) panelInstr.getComponent(1);
        JViewport viewport = scroll.getViewport(); 
        areaInstr = (JTextArea) viewport.getView();
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        container.add( panelInstr, gbc);

        JPanel panelSaida = criarTextArea(areaSaida, "OUTPUT");
        scroll = (JScrollPane) panelSaida.getComponent(1);
        viewport = scroll.getViewport(); 
        areaSaida = (JTextArea) viewport.getView();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        container.add( panelSaida, gbc);
    }
    
    private JPanel criarTextArea(JTextArea area, String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel label = new JLabel(title);
        panel.add(label, gbc);
        
        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", 0, 10));
        area.setEditable(false);
        area.setMinimumSize(new Dimension(250, 100));
        area.setSize(250, 100);
       // area.setPreferredSize(new Dimension(250, 1000));
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setMinimumSize(new Dimension(270, 50));
        scroll.setMaximumSize(new Dimension(270, 50));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        panel.add(scroll, gbc);   
        
        return panel;
    }
    
    public void resetarInstr() {
        areaInstr.setText("");
    }
    
    public void atualizarInstr(String novoTexto) {
        areaInstr.append(novoTexto+"\n");
    }
    
    public void resetarSaida() {
        areaSaida.setText("");
    }
    
    public void atualizarSaida(String novoTexto) {
        if (!novoTexto.equals(""))
            areaSaida.append(novoTexto+"\n");
    }
    
    private void criarBotoes() {
        JPanel panelBotoes = new JPanel(new GridBagLayout());
        
        btnCarregarInstr = new JButton("load");
        btnCarregarInstr.addActionListener((ActionEvent e) -> {
            btnCarregarInstr();
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        panelBotoes.add(btnCarregarInstr, gbc);
        
        btnPasso = new JButton("step");
        btnPasso.setEnabled(false);
        btnPasso.addActionListener((ActionEvent e) -> {
            btnPasso();
        });
        gbc.gridx = 1;
        panelBotoes.add(btnPasso, gbc);
        
        btnExecutar = new JButton("execute");
        btnExecutar.setEnabled(false);
        btnExecutar.addActionListener((ActionEvent e) -> {
            btnExecutar();
        });
        gbc.gridx = 2;
        panelBotoes.add(btnExecutar, gbc);
        
        btnResetar = new JButton("reset");
        btnResetar.setEnabled(false);
        btnResetar.addActionListener((ActionEvent e) -> {
            btnResetar();
        });
        gbc.gridx = 3;
        panelBotoes.add(btnResetar, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        container.add(panelBotoes, gbc);
    }
    
    private void btnCarregarInstr() {
        int[] memoria = sistema.carregar_instrucoes();  
        int DS = sistema.getDSMemoria();
        
        String codigo[][] = new String[DS][2];
        int i;
        for (i = 0; i < DS; ++i) {
            codigo[i] = new String[] {"["+i+"]", ""+memoria[i]};
        }
        
        criarTabelaCodigo(codigo);
        atualizarMemoriaDados(memoria);
        atualizarMemoriaPilha(memoria);
        atualizarRegistradores(sistema.getRegistrador());
        
        btnCarregarInstr.setEnabled(false);
        btnPasso.setEnabled(true);
        btnExecutar.setEnabled(true);
        btnResetar.setEnabled(true);
        
        this.setVisible(true);
    }
    
    private void btnPasso() {           
        if (sistema.getRegistrador().getIP() != sistema.getQuantInstr()) {
            String[] retorno = sistema.executar_passo();
            atualizarRegistradores(sistema.getRegistrador());
            atualizarInstr(retorno[0]);
            atualizarSaida(retorno[1]);
            if (retorno[2].equals("1")) {
                dadosMemoria = sistema.getDadosMemoria();                
                atualizarMemoriaDados(dadosMemoria);
                atualizarMemoriaPilha(dadosMemoria);
                // TO COM ERRO AQUI?
            }
            this.setVisible(true);
        } else {
            if (chegouAoFim == 0) {
                chegouAoFim = 1;
                atualizarInstr("-- FIM DAS INSTRUÇÕES --");
                btnPasso.setEnabled(false);
                btnExecutar.setEnabled(false);
            }
        }
    }
    
    private void btnExecutar() {
        if (chegouAoFim == 0) {
            String[] retorno = sistema.executar_codigo();
            atualizarInstr(retorno[0] + "-- FIM DAS INSTRUÇÕES --");
            atualizarSaida(retorno[1]);
            chegouAoFim = 1;
            dadosMemoria = sistema.getDadosMemoria();
            atualizarMemoriaDados(dadosMemoria);
            atualizarMemoriaPilha(dadosMemoria);
            atualizarRegistradores(sistema.getRegistrador());
            btnPasso.setEnabled(false);
            btnExecutar.setEnabled(false);
            this.setVisible(true);
        }   
    }
    
     private void btnResetar() {
        String caminho = sistema.getCaminhoArq();
        try {
            sistema.matarJanelaMontador();
            JanelaZ808 novo = new JanelaZ808(caminho);
            novo.setVisible(true);
            this.dispose();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Exceção: " + e,
                "ERRO!",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
     
     public void criarTabelaCodigo(String[][] data) {
        JPanel panelTabela = criarTabela(data, " CODE  AREA  MEMORY ");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void atualizarMemoriaDados(int[] memoria) {
        int DS = sistema.getDSMemoria();
        //int tamMaxMemoria = Memoria.TAM_MAXIMO;
        String dados[][] = new String[TAM_MAX_TABELAS][2];
        int i = DS;
        for(; i < TAM_MAX_TABELAS; ++i) {
            if (memoria[i] != -1)
                dados[i-DS] = new String[] {"["+i+"]", ""+memoria[i]};
            else 
                if(i-DS < TAM_MAX_TABELAS)
                    dados[i-DS] = new String[] {"["+i+"]", "null"};
        }
        //criarTabelaDados(dados);
        JPanel panelTabela = criarTabela(dados, " DATA  AREA  MEMORY ");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
    }
    
    public void atualizarMemoriaPilha(int[] memoria) {
        int i, tamMaxMemoria = Memoria.TAM_MAXIMO;
        int[] aux = Arrays.copyOfRange(memoria, tamMaxMemoria - TAM_MAX_TABELAS, tamMaxMemoria);
        
        //int i = tamMaxMemoria;
        String pilha[][] = new String[TAM_MAX_TABELAS][2];

        for (i = 0; i < TAM_MAX_TABELAS; ++i) {
            if (aux[i] != -1)
                pilha[i] = new String[] {"["+(tamMaxMemoria-i-1)+"]", ""+aux[TAM_MAX_TABELAS-1-i]};
            else
                pilha[i] = new String[] {"["+(tamMaxMemoria-i-1)+"]", "null"};
        }
        
        JPanel panelTabela = criarTabela(pilha, " STACK  MEMORY ");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panelTabela, gbc);
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
 
}
