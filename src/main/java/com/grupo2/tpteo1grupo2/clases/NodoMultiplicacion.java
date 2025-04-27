package com.grupo2.tpteo1grupo2;

public class NodoMultiplicacion extends NodoExpresionBinaria {

    public NodoMultiplicacion(NodoExpresion izquierda, NodoExpresion derecha) {
        super("*", izquierda, derecha);
    }
}
