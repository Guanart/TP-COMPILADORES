package com.grupo2.tpteo1grupo2;

public class NodoMenorIgual extends NodoComparacion {
    public NodoMenorIgual (NodoExpresion izquierda, NodoExpresion derecha) {
        super("<=", izquierda, derecha);
    }
}
