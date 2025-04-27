package com.grupo2.tpteo1grupo2;

public class NodoIdentificador extends NodoExpresion {
    private final String identificador;

    public NodoIdentificador(String identificador) {
        super("ID");
        this.identificador = identificador;
    }

    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }
}
