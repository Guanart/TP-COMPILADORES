package org.grupo2;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author Eugenia
 */
public class EjemploFlex {

    public static void main(String[] args) throws Exception {
        try {
            FileReader f = new FileReader("src/main/java/org/grupo2/prueba.txt");
            Lexico Lexer = new Lexico(f);
            Lexer.next_token();
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontró el archivo");
        }
        
    }
    
}
