package com.grupo2.tpteo1grupo2;

import java.io.*;
import java.util.*;

public class SymbolTableGenerator {
    // Estructura para representar un símbolo
    static class Symbol {
        String nombre;
        String valor;
        String token;
        int longitud;

        public Symbol(String nombre, String valor, String token, int longitud) {
            this.nombre = nombre;
            this.valor = valor;
            this.token = token;
            this.longitud = longitud;
        }
    }

    // Función para leer el archivo y extraer símbolos
    public List<Symbol> parseFile(String filePath) {
        Map<String, Symbol> symbolsMap = new LinkedHashMap<>(); // Utiliza LinkedHashMap para preservar el orden de inserción

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Verificar si la línea contiene un token de tipo "ID" o una constante
                if (line.contains("Token ID encontrado") || line.contains("Token CONST_INT encontrado")
                        || line.contains("Token CONST_STRING encontrado") || line.contains("Token CONST_REAL encontrado")
                        || line.contains("Token CONST_B encontrado")) {
                    String lexema = extractLexema(line);
                    String token = extractToken(line);
                    String nombre = lexema;

                    // Asignar nombres para las constantes según su valor
                    if (token.startsWith("CONST")) {
                        nombre = "_" + removeQuotes(lexema);
                    }

                    if (lexema != null && !symbolsMap.containsKey(nombre)) {
                        // Añadir un nuevo símbolo a la tabla
                        Symbol symbol = new Symbol(nombre, token.startsWith("CONST") ? lexema : "", token, lexema.length());
                        symbolsMap.put(nombre, symbol);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(symbolsMap.values());
    }

    public String removeQuotes(String input) {
        // Reemplaza todas las comillas dobles por una cadena vacía
        return input.replace("\"", "");
    }

    // Función para extraer el lexema de una línea
    private static String extractLexema(String line) {
        int index = line.indexOf("Lexema");
        if (index != -1) {
            return line.substring(index + 7).trim();
        }
        return null;
    }

    // Función para extraer el token de una línea
    private static String extractToken(String line) {
        int index = line.indexOf("Token");
        if (index != -1) {
            int endIndex = line.indexOf(" ", index + 6);
            return line.substring(index + 6, endIndex);
        }
        return null;
    }

    // Función para guardar la tabla de símbolos en un archivo CSV
    public void saveToCSV(List<Symbol> symbols, String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Escribir encabezado
            writer.println("NOMBRE,VALOR,TOKEN,LONGITUD");

            // Escribir cada símbolo
            for (Symbol symbol : symbols) {
                writer.printf("%s,%s,%s,%d%n",
                        symbol.nombre, symbol.valor, symbol.token, symbol.longitud);
            }

            System.out.println("Tabla de símbolos generada exitosamente.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}