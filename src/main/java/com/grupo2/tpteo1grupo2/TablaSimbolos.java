package com.grupo2.tpteo1grupo2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Simbolo> tabla;

    public TablaSimbolos() {
        this.tabla = new LinkedHashMap<>();
    }

    // Agregar identificador
    public void agregarIdentificador(String nombre, String tipo) {
        if (!tabla.containsKey(nombre)) {
            tabla.put(nombre, new Simbolo(nombre, nombre, "ID", "", tipo));
        }
    }

    // Agregar constante entera
    public void agregarConstanteEntera(int valor) {
        String nombre = "_" + valor;
        agregarConstante(nombre, String.valueOf(valor), "CONST_INT");
    }

    // Agregar constante real
    public void agregarConstanteReal(double valor) {
        String nombre = "_" + String.valueOf(valor).replace(".", "_");
        agregarConstante(nombre, String.valueOf(valor), "CONST_REAL");
    }

    // Agregar constante string
    // Método estático para limpiar nombres de variables según las reglas de Turbo Assembler
    public static String limpiarNombreAssembler(String valor) {
        // Elimina las comillas si están presentes
        if (valor.startsWith("\"") && valor.endsWith("\"") && valor.length() >= 2) {
            valor = valor.substring(1, valor.length() - 1);
        }
        // Reemplaza caracteres no válidos por guion bajo
        String nombreLimpio = valor.replaceAll("[^A-Za-z0-9_?]", "_");
        // Asegura que el primer carácter sea letra, guion bajo o símbolo de
        // interrogación
        if (!nombreLimpio.matches("^[A-Za-z_?].*")) {
            nombreLimpio = "_" + nombreLimpio;
        }
        return nombreLimpio;
    }

    // Agregar constante string
    public void agregarConstanteString(String valor) {
        String nombre = "_" + limpiarNombreAssembler(valor);
        String longitud = String.valueOf(valor.length());
        if (!tabla.containsKey(nombre)) {
            tabla.put(nombre, new Simbolo(nombre, valor, "CONST_STRING", longitud, "-"));
        }
    }

    // Agregar constante binaria (ej: 0b1101 → 13)
    public void agregarConstanteBinaria(String binario) {
        int decimal = Integer.parseInt(binario.substring(2), 2);
        String nombre = "_" + binario;
        agregarConstante(nombre, String.valueOf(decimal), "CONST_B");
    }

    // AgregarConstante genérico
    private void agregarConstante(String nombre, String valor, String token) {
        if (!tabla.containsKey(nombre)) {
            tabla.put(nombre, new Simbolo(nombre, valor, token, "", "-"));
        }
    }

    // Guardar en CSV
    public void guardarComoCSV(String archivo) throws IOException {
        FileWriter writer = new FileWriter(archivo);
        writer.write("NOMBRE,VALOR,TOKEN,LONGITUD,TIPO\n");
        for (Simbolo simbolo : tabla.values()) {
            writer.write(simbolo.toCSVLine() + "\n");
        }
        writer.close();
    }

    public boolean contieneIdentificador(String nombre) {
        Simbolo s = tabla.get(nombre);
        return s != null && s.token.equals("ID");
    }

    public String obtenerTipo(String nombre) {
        Simbolo s = tabla.get(nombre);
        if (s != null && s.token.equals("ID")) {
            return s.tipo;
        }
        return null;
    }

    public Map<String, Simbolo> getTabla() {
        return Collections.unmodifiableMap(tabla);
    }

}