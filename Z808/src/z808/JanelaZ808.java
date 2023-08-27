package z808;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
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
import javax.swing.table.DefaultTableModel;

public class JanelaZ808 extends JFrame {
    private final int TAM_MAX_TABELAS = 1000;
    private final String[] panelMemorias = {"DataPanel", "StackPanel"};
    
    private JTextField fieldRegAX, fieldRegDX, fieldRegCL, fieldRegRI, fieldRegSI, fieldRegIP, fieldRegSP;
    private JTextField fieldRegFlagCF, fieldRegFlagPF, fieldRegFlagIF, fieldRegFlagZF, fieldRegFlagSF, fieldRegFlagOF;
    private JTextArea areaInstr, areaSaida;
    private JButton btnCarregarInstr, btnExecutar, btnResetar, btnPasso;
    private int chegouAoFim = 0;
    
    private final Z808 sistema;
    Container container;
    private final GridBagConstraints gbc;
    
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
        
        gbc = new GridBagConstraints();
        container = getContentPane();
        
        criarPanelArquivo(pathArquivo); // cria painel superior com localização do arquivo
        criarRegistradores(); // cria JTextFields dos registradores
        criarTextAreas();
        criarBotoes(); // cria botões na parte inferior
        btnCarregarInstr();
    }
    
    private void criarPanelArquivo(String pathArquivo) {
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
    
    public void attRegistradores(Registradores reg) {
        fieldRegCL.setText(""+reg.getCL());
        fieldRegRI.setText(""+reg.getRI());
        fieldRegIP.setText(""+reg.getIP());
        fieldRegSI.setText(""+reg.getSI());
        fieldRegAX.setText(""+reg.getAX());
        fieldRegDX.setText(""+reg.getDX());
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
        JPanel panelInstr = criar1TextArea(areaInstr, "INSTRUCTIONS");
        pack();
        JScrollPane scroll = (JScrollPane) panelInstr.getComponent(1);
        JViewport viewport = scroll.getViewport(); 
        areaInstr = (JTextArea) viewport.getView();
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        container.add( panelInstr, gbc);

        JPanel panelSaida = criar1TextArea(areaSaida, "OUTPUT");
        scroll = (JScrollPane) panelSaida.getComponent(1);
        viewport = scroll.getViewport(); 
        areaSaida = (JTextArea) viewport.getView();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        container.add( panelSaida, gbc);
        gbc.insets = new Insets(0, 10, 0, 0);
    }
    
    private JPanel criar1TextArea(JTextArea area, String title) {
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
        area.setCaretColor(Color.WHITE);
        area.setMinimumSize(new Dimension(250, 100));
        area.setSize(250, 100);
       // area.setPreferredSize(new Dimension(250, 1000));
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setMinimumSize(new Dimension(270, 100));
        scroll.setMaximumSize(new Dimension(270, 100));
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
    
    public void attInstrucao(String novoTexto) {
        areaInstr.append(novoTexto+"\n");
    }
    
    public void resetarSaida() {
        areaSaida.setText("");
    }
    
    public void attSaida(String novoTexto) {
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
        int[] memoria = sistema.carregarInstr();
        
        // gerando String da memória código (vai ser usada apenas 1x)
        int DS = sistema.getDSMemoria();
        String codigo[][] = new String[DS][2];
        for (int i = 0; i < DS; ++i) {
            codigo[i] = new String[] {"["+i+"]", ""+memoria[i]};
        }
        criarTabela(codigo, " CODE  AREA  MEMORY ", "Code", 1);
        criarTabela(criarMemoriaDados(memoria), " DATA  AREA  MEMORY ", "DataPanel", 2);
        criarTabela(criarMemoriaPilha(memoria), " STACK  MEMORY ", "StackPanel", 3);
        attRegistradores(sistema.getRegistr());
        
        btnCarregarInstr.setEnabled(false);
        btnPasso.setEnabled(true);
        btnExecutar.setEnabled(true);
        btnResetar.setEnabled(true);
        
        this.setVisible(true);
    }
    
    private void btnPasso() {           
        if (sistema.getRegistr().getIP() != sistema.getQuantInstr()) {
            String[] retorno = sistema.executarPasso();
            attRegistradores(sistema.getRegistr());
            attInstrucao(retorno[0]);
            attSaida(retorno[1]);
            if (retorno[2].equals("1")) 
                attTabelas(sistema.getDadosMemoria());
            this.setVisible(true);
        } else {
            if (chegouAoFim == 0) {
                chegouAoFim = 1;
                attInstrucao("-- FIM DAS INSTRUÇÕES --");
                btnPasso.setEnabled(false);
                btnExecutar.setEnabled(false);
            }
        }
    }
    
    private void btnExecutar() {
        if (chegouAoFim == 0) {
            String[] retorno = sistema.executar();
            attInstrucao(retorno[0] + "-- FIM DAS INSTRUÇÕES --");
            attSaida(retorno[1]);
            chegouAoFim = 1;
            attTabelas(sistema.getDadosMemoria());
            attRegistradores(sistema.getRegistr());
            btnPasso.setEnabled(false);
            btnExecutar.setEnabled(false);
            this.setVisible(true);
        }   
    }
    
    private void btnResetar() {
        String caminho = sistema.getCaminhoMacro();
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
    
    public String[][] criarMemoriaDados(int[] memoria) {
        int DS = sistema.getDSMemoria();
        String dados[][] = new String[TAM_MAX_TABELAS][2];
        int i = DS;
        for(; i < TAM_MAX_TABELAS; ++i) {
            if (memoria[i] != -1)
                dados[i-DS] = new String[] {"["+i+"]", ""+memoria[i]};
            else 
                if(i-DS < TAM_MAX_TABELAS)
                    dados[i-DS] = new String[] {"["+i+"]", "null"};
        }
        return dados;
    }
    
    public String[][] criarMemoriaPilha(int[] memoria) {
        int i, tamMaxMemoria = Memoria.TAM_MAXIMO; //65535
        int[] aux = Arrays.copyOfRange(memoria, tamMaxMemoria - TAM_MAX_TABELAS, tamMaxMemoria);
        
        String pilha[][] = new String[TAM_MAX_TABELAS][2];
        for (i = 0; i < TAM_MAX_TABELAS; ++i) {
            if (aux[i] != -1) 
                pilha[TAM_MAX_TABELAS-i-1] = new String[] {"["+(tamMaxMemoria-TAM_MAX_TABELAS+i)+"]", ""+(aux[i])};
            else 
                pilha[TAM_MAX_TABELAS-i-1] = new String[] {"["+(tamMaxMemoria-TAM_MAX_TABELAS+i)+"]", "null"};
        }
        return pilha;
    }
    
    private void attTabelas(int[] memoria) {
        JTable tables[] = new JTable[panelMemorias.length];
        
        for (Component comp : container.getComponents()) {
            for (int i = 0; i < panelMemorias.length; ++i) 
                if (comp instanceof JPanel panel) 
                    if (panel.getName() != null)
                        if (panel.getName().equals(panelMemorias[i]))  {
                            Component[] aux = panel.getComponents();
                            tables[i] = (JTable)((JScrollPane) aux[1]).getViewport().getView();
                            String[] colunas = {"Address", "Value"};
                            DefaultTableModel model = null;
                            switch (panelMemorias[i]) {
                                case "DataPanel" -> 
                                    model = new DefaultTableModel(criarMemoriaDados(memoria), colunas);
                                case "StackPanel" -> 
                                    model = new DefaultTableModel(criarMemoriaPilha(memoria), colunas);
                                default -> JOptionPane.showMessageDialog(
                                            null,
                                            "ERRO!",
                                            "Nome de painel não registrado. Favor, verificar linhas 503 a 530 da JanelaZ808.",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                            }
                            tables[i].setModel(model);
                        }  
        }
    }        
    
    private void criarTabela(Object[][] data, String titulo, String nomePanel, int panelGridX) {
        JPanel panel = new JPanel();
        panel.setName(nomePanel);
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;  
        gbc.gridheight = 1;
        panel.add(labelTitulo);
        
        String[] colunas = {"Address", "Value"};
        DefaultTableModel model = new DefaultTableModel(data, colunas);
        JTable table = new JTable();
        table.setModel(model);

        JScrollPane scroll = new JScrollPane(table); 
        scroll.setPreferredSize(new Dimension(170, 295));
       
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;  
        gbc.gridheight = 3;
        panel.add(scroll, gbc);
    
        gbc.gridx = panelGridX;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 1;
        container.add( panel, gbc);
    }
 
}
