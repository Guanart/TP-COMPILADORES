package com.grupo2.tpteo1grupo2;

public class Simbolo {
    public final String nombre;
    public final String valor;
    public final String token;
    public final String longitud;
    public final String tipo;

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
