package com.grupo2.tpteo1grupo2.clases;

public class NodoConstanteString extends NodoExpresion {
    private final String valor;

    public NodoConstanteString(String valor) {
        super("CONST_STRING");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CONST_STRING: " + valor.toString();
    }
}
