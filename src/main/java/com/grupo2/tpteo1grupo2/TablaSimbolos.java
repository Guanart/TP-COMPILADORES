package com.grupo2.tpteo1grupo2;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TablaSimbolos {

    private static class Simbolo {
        String nombre;
        String valor;
        String token;
        String longitud;
        String tipo;

        public Simbolo(String nombre, String valor, String token, String longitud, String tipo) {
            this.nombre = nombre;
            this.valor = valor;
            this.token = token;
            this.longitud = longitud;
            this.tipo = tipo;
        }

        public String toCSVLine() {
            return nombre + "," + valor + "," + token + "," + longitud + "," + tipo;
        }
    }

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
        String nombre = "_" + valor;
        agregarConstante(nombre, String.valueOf(valor), "CONST_REAL");
    }

    // Agregar constante string
    public void agregarConstanteString(String valor) {
        String nombre = "_" + valor.replace(" ", "_");
        String longitud = String.valueOf(valor.length());
        if (!tabla.containsKey(nombre)) {
            tabla.put(nombre, new Simbolo(nombre, valor, "CONST_STRII", longitud, "-"));
        }
    }

    // Agregar constante binaria (ej: 0b1101 → 13)
    public void agregarConstanteBinaria(String binario) {
        int decimal = Integer.parseInt(binario.substring(2), 2);
        String nombre = "_" + binario;
        agregarConstante(nombre, String.valueOf(decimal), "CONST_B");
    }

    // AgregarConstante  genérico
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
        return null; // o lanzar una excepción si querés controlar mejor
    }

}