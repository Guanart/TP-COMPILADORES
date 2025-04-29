package com.grupo2.tpteo1grupo2.clases;

public class NodoIdentificador extends NodoExpresion {
    private final String identificador;

    public NodoIdentificador(String identificador) {
        super("ID");
        this.identificador = identificador;
    }

    public String getId() {
        return identificador;
    }

    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }

    @Override
    public String toString() {
        return identificador;
    }
}
