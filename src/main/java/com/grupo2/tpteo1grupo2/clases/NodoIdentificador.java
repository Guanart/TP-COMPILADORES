package com.grupo2.tpteo1grupo2.clases;

public class NodoIdentificador extends NodoExpresion {
    private final String identificador;
    private String tipo;

    public NodoIdentificador(String identificador) {
        super("ID");
        this.identificador = identificador;
    }

    public NodoIdentificador(String identificador, String tipo) {
        super("ID");
        this.identificador = identificador;
        this.tipo = tipo;
    }

    public String getId() {
        return identificador;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }

    @Override
    public String toString() {
        return identificador;
    }

    @Override
    public String getTipoValorExpresion() {
        return this.getTipo();
    }

}
