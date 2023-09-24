package z808;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/*
 * @authors  GRUPO: Adas & CG
 *  Bianca Beppler Dullius
 *  Caroline Souza Camargo
 *  Cláudio Luis da Silva Machado Junior
 *  Eduarda Abreu Carvalho
 *  Guilherme Braatz Stein
 *  Júlia da Rocha Junqueira
 *  Júlia Veiga da Silva
 *  Maria Julia Duarte Lorenzoni
 *  Yasmin Souza Camargo
 */

public class JanelaInicial extends JFrame implements KeyListener, ActionListener {
    private String arqPrincipal = "src\\z808\\resources\\macro3.txt";
    private String arqSecundarios = "";
    private JTextField fieldArqPrincipal;// = new JTextArea(45);
    private JTextArea textAreaArquivos;// = new JTextArea(45);
    private JLabel labelAux;
    
    public JanelaInicial() {
        // janela
        setTitle("Z808 Emulator");
        setSize(600, 500);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setMinimumSize(new Dimension(600, 450)); 
        setVisible(true);
        
        // onde todos os itens são inseridos
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // titulo principal
        JLabel labTitulo = new JLabel("WELCOME");
        labTitulo.setFont(new Font("Arial Black", Font.PLAIN, 36));
        labTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; // posicionamento na janela
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        container.add(labTitulo, gbc); // tipo appendChild(variavel) em JavaScript

        //subtitulo
        JLabel labSubtitulo = new JLabel("Z808 Emulator");
        labSubtitulo.setFont(new Font("Arial", Font.PLAIN, 24));
        labSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 30, 0);
        container.add(labSubtitulo, gbc);

        // label indicado "caminho do arquivo"
        JLabel labelArqPrincipal = new JLabel("Main file path:");
        labelArqPrincipal.setFont(new Font("Arial", Font.PLAIN, 14));
        labelArqPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        container.add(labelArqPrincipal, gbc);
        
        // input para o texto do caminho do arquivo
        fieldArqPrincipal = new JTextField(45);
        fieldArqPrincipal.addKeyListener(this);
        fieldArqPrincipal.setText(arqPrincipal);
        fieldArqPrincipal.setFocusable(false);
        fieldArqPrincipal.setEditable(false);
        fieldArqPrincipal.setBackground(Color.WHITE);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(fieldArqPrincipal, gbc);

        // botão de procurar arquivo no pc
        JButton btnProcurarPrincipal = new JButton("Search Main File");
        btnProcurarPrincipal.addActionListener((ActionEvent e) -> {
            JFileChooser seletor = new JFileChooser(".\\src\\z808\\resources");
            int escolha = seletor.showOpenDialog(this);
            if (escolha == JFileChooser.APPROVE_OPTION){
                File arquivo = seletor.getSelectedFile();
                fieldArqPrincipal.setText(arquivo.getPath());
            }
            btnProcurarPrincipal.setFocusable(false);
        }); 
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(btnProcurarPrincipal, gbc);
        
        // label indicado "caminho do arquivo"
        JLabel labelArqSecundario = new JLabel("Secondary file(s) path(s):");
        labelArqSecundario.setFont(new Font("Arial", Font.PLAIN, 14));
        labelArqSecundario.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        container.add(labelArqSecundario, gbc);
        
        // input para o texto do caminho do arquivo
        textAreaArquivos = new JTextArea();
        textAreaArquivos.setColumns(100);
        textAreaArquivos.setRows(100);
        textAreaArquivos.setLineWrap(true);
        textAreaArquivos.setMinimumSize(new Dimension(450, 75));
        textAreaArquivos.setEditable(false);
        textAreaArquivos.setText(arqSecundarios);
        textAreaArquivos.setCaretColor(Color.WHITE);
        textAreaArquivos.addKeyListener(this);
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(textAreaArquivos, gbc);
        
        // botão de procurar arquivo no pc
        JButton btnProcurarOutros = new JButton("Search Secondary Files");
        btnProcurarOutros.addActionListener((ActionEvent e) -> {
            JFileChooser seletor = new JFileChooser(".\\src\\z808\\resources");
            seletor.setMultiSelectionEnabled(true);
            int escolha = seletor.showOpenDialog(this);
            if (escolha == JFileChooser.APPROVE_OPTION){
                File[] arquivos = seletor.getSelectedFiles();
                String texto = "";
                for (File arq : arquivos) 
                    texto += "'" + arq + "' : ";
                textAreaArquivos.setText(texto);
            }
            btnProcurarOutros.setFocusable(false);
        }); 
        gbc.gridy = 9;
         gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, -150, 0, 0);
        container.add(btnProcurarOutros, gbc);
       
        // botão pra confirmar que é aquele arquivo que a gente quer
        JButton btnConfirmar = new JButton("Confirm");
        btnConfirmar.addActionListener((ActionEvent e) -> {
            try {
                btnCarregar();
            } catch (IOException ex) {
                Logger.getLogger(JanelaInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        gbc.insets = new Insets(15, 150, 0, 0);
        container.add(btnConfirmar, gbc);
        
        // legenda auxiliar
        labelAux = new JLabel("Press ENTER to continue.");
        labelAux.setFont(new Font("Arial", Font.PLAIN, 11));
        labelAux.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(50, 0, 5, 0);
        container.add(labelAux, gbc);    
    }
    
    public void btnCarregar() throws IOException {
        arqPrincipal = fieldArqPrincipal.getText();
        File arquivo = new File(arqPrincipal);
        if (!arquivo.exists()) 
            btnCarregarErro("Main file not found. Try again.");
        else 
            if (!arqPrincipal.contains(".txt"))   
                btnCarregarErro("Main file must be of type .txt. Try again.");
        
        ArrayList<String> arquivos = new ArrayList<>();
        int i, contador = 0;
        if (!arqSecundarios.equals("")) {
            String[] arqTemp = arqSecundarios.split(" : ");
            // arquivos = arqSecundarios.split(" : ");
            for (String arq : arqTemp) {
                arquivos.add(arq.substring(1, arq.length()-1));
                arquivo = new File(arq);
                if (!arquivo.exists())
                    btnCarregarErro(arquivo.getPath() + " wasn't found. Try again."); 
                    else 
                    if (!arq.contains(".txt"))
                        btnCarregarErro(arquivo.getPath() + " isn't of type .TXT. Try again."); 
                    else
                        contador += 1;
            }
        }
        if (contador == arquivos.size()) {
            JanelaZ808 sistema = new JanelaZ808(arqPrincipal, arquivos);
            sistema.setVisible(true);
            this.dispose();
        } 
    }
    
    public void btnCarregarErro(String mensagem) {
        labelAux.setFont(new Font("Arial", Font.BOLD, 11));
        labelAux.setForeground(Color.RED);
        labelAux.setText(mensagem);
    }
    
    @Override public void actionPerformed(ActionEvent event) {}
    @Override public void keyTyped(KeyEvent event) {}
    @Override public void keyPressed(KeyEvent event) {}
    @Override 
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == 10) { try {
            // ENTER
            event.consume();
            btnCarregar();
            } catch (IOException ex) {
                Logger.getLogger(JanelaInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JanelaInicial janela = new JanelaInicial();
                janela.setVisible(true);
            }
        });
    }

}
