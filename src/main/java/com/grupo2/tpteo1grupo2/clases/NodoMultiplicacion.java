package com.grupo2.tpteo1grupo2.clases;

public class NodoMultiplicacion extends NodoExpresionBinaria {

    public NodoMultiplicacion(NodoExpresion izquierda, NodoExpresion derecha) {
        super("*", izquierda, derecha);
    }

    @Override
    public String toString() {
        return izquierda.toString() + "*" + derecha.toString();
    }
}
