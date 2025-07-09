package com.grupo2.tpteo1grupo2.clases;

public class NodoConstanteReal extends NodoExpresion {
    private final double valor;

    public NodoConstanteReal(double valor) {
        super(Double.toString(valor).replace(".", "_"));
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
        return "FLOAT";
    }

    @Override
    public boolean soyHoja() {
        return true; // Una constante real es una hoja en el árbol de sintaxis
    }

    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    }
}

