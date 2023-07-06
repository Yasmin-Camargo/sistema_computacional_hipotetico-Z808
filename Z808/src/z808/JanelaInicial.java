/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package z808;

/**
 *
 * @author Duda & Júlia Veiga & Júlia Junqueira
 */
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
import javax.swing.*;

public class JanelaInicial extends JFrame implements KeyListener, ActionListener {
    private String pathArquivo = "src\\z808\\arquivo_teste.txt";
    private JTextField fieldArquivo = new JTextField(45);
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
        JLabel labArquivo = new JLabel("File path:");
        labArquivo.setFont(new Font("Arial", Font.PLAIN, 14));
        labArquivo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        container.add(labArquivo, gbc);
        
        // input para o texto do caminho do arquivo
        fieldArquivo = new JTextField();
        fieldArquivo.setColumns(35);
        fieldArquivo.addKeyListener(this);
        fieldArquivo.setText(pathArquivo);
        gbc.gridy = 5;
        container.add(fieldArquivo, gbc);
        
        // botão de procurar arquivo no pc
        JButton btnProcurar = new JButton("Search");
        btnProcurar.addActionListener((ActionEvent e) -> {
            JFileChooser arquivo = new JFileChooser("src\\z808");
            int escolha = arquivo.showOpenDialog(this);
            if (escolha == JFileChooser.APPROVE_OPTION){
                File arquivo_selecionado = arquivo.getSelectedFile();
                fieldArquivo.setText(arquivo_selecionado.getPath());
            }
        }); 
        gbc.gridy = 6;
        gbc.insets = new Insets(15, -100, 0, 0);
        container.add(btnProcurar, gbc);
       
        // botão pra confirmar que é aquele arquivo que a gente quer
        JButton btnConfirmar = new JButton("Confirm");
        btnConfirmar.addActionListener((ActionEvent e) -> {
            carregar();
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
    
    public void carregar() {
        pathArquivo = fieldArquivo.getText();
        File arquivo = new File(pathArquivo);
        if (!arquivo.exists()) {
            //JOptionPane.showMessageDialog(null, "Erro: falha ao abrir o arquivo");
            labAux.setFont(new Font("Arial", Font.BOLD, 11));
            labAux.setForeground(Color.RED);
            labAux.setText("File not found. Try again.");
        }
        else {
            Z808 z808 = new Z808(pathArquivo); // chamar Z808
            this.setVisible(false); // matar janela atual
        }
    }
    
    @Override public void actionPerformed(ActionEvent event) {}
    @Override public void keyTyped(KeyEvent event) {}
    @Override public void keyPressed(KeyEvent event) {}
    @Override 
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == 10) { // ENTER
            carregar();
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
