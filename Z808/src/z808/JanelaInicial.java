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
    private String caminhoArquivos = "'src\\z808\\resources\\macro3.txt'";
    private JTextArea textAreaArquivo;// = new JTextArea(45);
    private JLabel labAux;
    
    public JanelaInicial() {
        // janela
        setTitle("Z808 Emulator");
        setSize(600, 450);
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
        JLabel labArquivo = new JLabel("File(s) path:");
        labArquivo.setFont(new Font("Arial", Font.PLAIN, 14));
        labArquivo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 50);
        container.add(labArquivo, gbc);
        
        // input para o texto do caminho do arquivo
        textAreaArquivo = new JTextArea();
        textAreaArquivo.setColumns(100);
        textAreaArquivo.setRows(100);
        textAreaArquivo.setLineWrap(true);
        textAreaArquivo.setMinimumSize(new Dimension(450, 75));
        textAreaArquivo.setEditable(false);
        textAreaArquivo.setText(caminhoArquivos);
        textAreaArquivo.setCaretColor(Color.WHITE);
        textAreaArquivo.addKeyListener(this);
        gbc.gridy = 5;
        container.add(textAreaArquivo, gbc);
        
        // botão de procurar arquivo no pc
        JButton btnProcurar = new JButton("Search");
        btnProcurar.addActionListener((ActionEvent e) -> {
            JFileChooser seletor_arq = new JFileChooser(".\\src\\z808\\resources");
            seletor_arq.setMultiSelectionEnabled(true);
            int escolha = seletor_arq.showOpenDialog(this);
            if (escolha == JFileChooser.APPROVE_OPTION){
                File[] arquivos = seletor_arq.getSelectedFiles();
                String texto = "";
                for (File arq : arquivos) 
                    texto += "'" + arq + "' : ";
                textAreaArquivo.setText(texto);
            }
            btnProcurar.setFocusable(false);
        }); 
        gbc.gridy = 6;
        gbc.insets = new Insets(15, -100, 0, 0);
        container.add(btnProcurar, gbc);
       
        // botão pra confirmar que é aquele arquivo que a gente quer
        JButton btnConfirmar = new JButton("Confirm");
        btnConfirmar.addActionListener((ActionEvent e) -> {
            try {
                btnCarregar();
            } catch (IOException ex) {
                Logger.getLogger(JanelaInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        gbc.insets = new Insets(15, 100, 0, 0);
        container.add(btnConfirmar, gbc);
        
        // legenda auxiliar
        labAux = new JLabel("Press ENTER to continue.");
        labAux.setFont(new Font("Arial", Font.PLAIN, 11));
        labAux.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(50, 0, 5, 0);
        container.add(labAux, gbc);    
    }
    
    public void btnCarregar() throws IOException {
        caminhoArquivos = textAreaArquivo.getText();
        String[] arquivos = caminhoArquivos.split(" : ");
        File arq;
        int i, contador = 0;
        for (i = 0; i < arquivos.length; i++) {
        // for (String arq : arquivos) {
            arquivos[i] = arquivos[i].substring(1, arquivos[i].length() - 1);
            arq = new File(arquivos[i]);
            if (!arq.exists())
                btnCarregarErro("Some of the files weren't found. Try again."); 
            else {
                if (!arquivos[i].contains(".txt")) 
                    btnCarregarErro("All files must be of type .TXT. Try again.");
                else
                    contador += 1;
            }
        }
        if (contador == arquivos.length) {
            JanelaZ808 sistema = new JanelaZ808(arquivos);
            sistema.setVisible(true);
            this.dispose();
        } 
    }
    
    public void btnCarregarErro(String mensagem) {
        labAux.setFont(new Font("Arial", Font.BOLD, 11));
        labAux.setForeground(Color.RED);
        labAux.setText(mensagem);
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
