package com.grupo2.tpteo1grupo2.clases;

public class NodoMayorIgual extends NodoComparacion {
    public NodoMayorIgual (NodoExpresion izquierda, NodoExpresion derecha) {
        super(">=", izquierda, derecha);
    }
}
