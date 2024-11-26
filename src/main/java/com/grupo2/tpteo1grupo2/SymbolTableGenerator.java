package com.grupo2.tpteo1grupo2;

import java.io.*;
import java.util.*;

public class SymbolTableGenerator {
    // Estructura para representar un símbolo
    static class Symbol {
        String nombre;
        String valor;
        String token;
        Integer longitud; // Cambiado a Integer para permitir valores nulos
        String tipo;

        public Symbol(String nombre, String valor, String token, Integer longitud, String tipo) {
            this.nombre = nombre;
            this.valor = valor;
            this.token = token;
            this.longitud = longitud;
            this.tipo = tipo != null? tipo : "-";
        }

        @Override
        public String toString() {
            return (nombre + valor + token + (longitud!=null? longitud.toString() : "") + tipo);
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

                    // Reemplazo los espacios en blanco por guion bajo en el nombre de las constantes string
                    if (token.startsWith("CONST_STRING")) {
                        nombre = nombre.replace(" ", "_");
                    }

                    // Calcular el valor de `valor` y `longitud` según el token
                    String valor = token.equals("CONST_B") ? convertBinaryToDecimal(lexema) : lexema;
                    Integer longitud = token.equals("CONST_STRING") ? lexema.length() : null;

                    if (lexema != null && !symbolsMap.containsKey(nombre)) {
                        // Añadir un nuevo símbolo a la tabla
                        Symbol symbol = new Symbol(nombre, valor, token, longitud, null);
                        symbolsMap.put(nombre, symbol);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(symbolsMap.values());
    }

    // Convierte un valor binario a su valor decimal
    private String convertBinaryToDecimal(String lexema) {
        if (lexema.startsWith("0b")) {
            try {
                return String.valueOf(Integer.parseInt(lexema.substring(2), 2));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return lexema;
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
            writer.println("NOMBRE,VALOR,TOKEN,LONGITUD,TIPO");

            if (symbols != null) {
                // Escribir cada símbolo
                for (Symbol symbol : symbols) {
                    writer.printf("%s,%s,%s,%s,%s%n",
                            symbol.nombre, symbol.valor, symbol.token,
                            symbol.longitud != null ? symbol.longitud : "",
                            symbol.tipo);
                }
            }

            System.out.println("Tabla de símbolos generada exitosamente.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void editTypes(String typesPath, String filePath) {
        try {
            // Leer el archivo CSV original
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));
            ArrayList<Symbol> id_types = new ArrayList<>();

            // Leer los tipos desde typesPath
            try (BufferedReader br = new BufferedReader(new FileReader(typesPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("NOMBRE,TIPO")) {
                        continue; // Saltar la cabecera
                    }
                    String[] partes = line.split(",");
                    if (partes.length >= 2) {
                        Symbol linea = new Symbol(partes[0], null, null, null, partes[1]);
                        id_types.add(linea);
                    }
                }
            }


            // Actualizar el archivo original con los nuevos tipos
            StringBuilder updatedContent = new StringBuilder();
            for (String line : lines) {
                if (line.contains("NOMBRE,VALOR,TOKEN,LONGITUD,TIPO")) {
                    updatedContent.append(line).append("\n"); // Mantener la cabecera
                    continue;
                }

                String[] partes = line.split(",");
                if (partes.length >= 5) { // Asegurar que hay suficientes columnas
                    String nombre = partes[0];
                    boolean actualizado = false;

                    // Buscar si hay un Symbol con el mismo nombre
                    for (Symbol symbol : id_types) {
                        if (symbol.nombre.equals(nombre)) {
                            partes[4] = symbol.tipo; // Actualizar el campo tipo
                            break;
                        } else {
                            partes[4] = "-";
                        }
                    }

                    line = String.join(",", partes);
                }
                updatedContent.append(line).append("\n");
            }

            // Escribir el contenido actualizado al archivo
            try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                writer.write(updatedContent.toString());
            }

            System.out.println("Archivo actualizado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
