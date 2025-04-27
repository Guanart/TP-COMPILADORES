package com.grupo2.tpteo1grupo2;

public class NodoSuma extends NodoExpresionBinaria {

    public NodoSuma(NodoExpresion izquierda, NodoExpresion derecha) {
        super("+", izquierda, derecha);
    }
}