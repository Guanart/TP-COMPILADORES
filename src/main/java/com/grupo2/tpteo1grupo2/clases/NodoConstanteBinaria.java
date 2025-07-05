package com.grupo2.tpteo1grupo2.clases;

public class NodoConstanteBinaria extends NodoExpresion {
    private final int valor;
    private final String literal;

    public NodoConstanteBinaria(int valor, String literal) {
        super("CONST_B");
        this.valor = valor;
        this.literal = literal;
    }

    @Override
    public String getDescripcionNodo() { return "CONST_B: " + literal + " (Valor decimal: " + Integer.toString(valor) + ")"; }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }

    @Override
    public String getTipoValorExpresion() {
        return "INTEGER";
    }
}
