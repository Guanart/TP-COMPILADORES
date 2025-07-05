package com.grupo2.tpteo1grupo2.clases;

public class NodoConstanteReal extends NodoExpresion {
    private final double valor;

    public NodoConstanteReal(double valor) {
        super(Double.toString(valor));
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CONST_REAL: " + Double.toString(valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }

    @Override
    public String getTipoValorExpresion() {
        return "";
    }

    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    }
}

