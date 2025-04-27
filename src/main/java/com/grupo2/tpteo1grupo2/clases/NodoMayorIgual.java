package com.grupo2.tpteo1grupo2;

public class NodoMayorIgual extends NodoComparacion {
    public NodoMayorIgual (NodoExpresion izquierda, NodoExpresion derecha) {
        super(">=", izquierda, derecha);
    }
}
