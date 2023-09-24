package z808;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ligador {
    private Map<String, Integer> tabela_global = new HashMap<>();
    private String diretorio;

    Ligador(String diretorio, String[] arquivos_simplif) {
        this.diretorio = diretorio;
        System.out.println("diretorio: " + diretorio);
        for (String arquivo : arquivos_simplif) {
            System.out.println("ARQUIVO " + arquivo);
            criarTabelaSimbolos(arquivo);
        }
        realizarLigacao(arquivos_simplif);
    }

    private void criarTabelaSimbolos(String arquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int enderecoRelativo = 0; // Inicializa o endereço relativo para o arquivo atual
            while ((linha = br.readLine()) != null) {
                // Analise a linha para extrair símbolos e endereços relativos
                String[] partes = linha.split(" "); // Assumindo que os símbolos estão separados por espaço
                if (partes.length == 2) {
                    String simbolo = partes[0];
                    // Não sei, aqui tinha que pegar o end relativo, mas de onde pega???
                    int enderecoSimbolo = enderecoRelativo;
                    // Verifica se o símbolo já existe na tabela global
                    if (!tabela_global.containsKey(simbolo)) {
                        // Se não existe, adiciona o símbolo com o endereço relativo
                        tabela_global.put(simbolo, enderecoSimbolo);
                    } else {
                        // Caso encontre um símbolo duplicado
                        System.err.println("Aviso: Símbolo duplicado encontrado - " + simbolo);
                    }
                }
                // Atualiza o endereço relativo para a próxima linha
                enderecoRelativo += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int buscarSimboloGlobal(String nome) {
        if (tabela_global.containsKey(nome)) {
            return tabela_global.get(nome); // Retorna o valor associado ao símbolo global
        } else {
            return -1; // Retorna -1 se o símbolo não for encontrado
        }
    }

    public void realizarLigacao(String[] arquivos_simplif) {
        System.out.println("-- segundo passo --");
        // Executa a ligação dos módulos usando a tabela de símbolos
        for (String arquivo : arquivos_simplif) {
            ligarModulo(arquivo);
        }
    }

    private void ligarModulo(String arquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int enderecoRelativo = 0; // Inicializa o endereço relativo para o arquivo atual
            while ((linha = br.readLine()) != null) {
                // Analise a linha para encontrar referências externas
                String[] partes = linha.split(" "); // Assumindo que as instruções estão separadas por espaço
                if (partes.length == 3 && partes[0].equals("EXTERN")) {
                    String simboloExtern = partes[1];
                    // Verifica se o símbolo externo existe na tabela global
                    if (tabela_global.containsKey(simboloExtern)) {
                        int enderecoGlobal = tabela_global.get(simboloExtern);
                        // Atualiza o endereço no módulo atual
                        int novoEndereco = enderecoRelativo + Integer.parseInt(partes[2]) + enderecoGlobal;
                        System.out.println("Ligando " + simboloExtern + " em " + novoEndereco);
                        // 
                        // Você pode fazer algo com o novoEndereco, como atualizar a instrução no módulo atual
                    } else {
                        System.err.println("Erro: Símbolo externo não encontrado - " + simboloExtern);
                    }
                }
                // Atualiza o endereço relativo para a próxima linha
                enderecoRelativo += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}