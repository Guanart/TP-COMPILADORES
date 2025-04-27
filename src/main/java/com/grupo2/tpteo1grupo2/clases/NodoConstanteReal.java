package com.grupo2.tpteo1grupo2;

public class NodoConstanteReal extends NodoExpresion {
    private final double valor;

    public NodoConstanteReal(double valor) {
        super("CONST_REAL");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CONST_REAL: " + Double.toString(valor);
    }
}

