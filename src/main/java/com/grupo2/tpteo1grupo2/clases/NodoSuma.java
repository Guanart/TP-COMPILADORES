package com.grupo2.tpteo1grupo2.clases;

public class NodoSuma extends NodoExpresionBinaria {

    public NodoSuma(NodoExpresion izquierda, NodoExpresion derecha) {
        super("+", izquierda, derecha);
    }

    @Override
    public String toString() {
        return izquierda.toString() + "+" + derecha.toString();
    }
}