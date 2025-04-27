package com.grupo2.tpteo1grupo2;

public class NodoConstanteEntera extends NodoExpresion {
    private final int valor;

    public NodoConstanteEntera(int valor) {
        super("CONST_INT");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CONST_INT: " + Integer.toString(valor);
    }
}
