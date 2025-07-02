package util;

import java.io.FileWriter;
import java.io.IOException;

public class Writter {

    public static void escreverNoArquivo(String caminho, String texto) {
        try (FileWriter writer = new FileWriter(caminho, true)) {
            writer.write(texto + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}
