package com.grupo2.tpteo1grupo2.clases;

public class NodoIgual extends NodoComparacion{
    public NodoIgual (NodoExpresion izquierda, NodoExpresion derecha) {
        super("=", izquierda, derecha);
    }

    public static Boolean isValid(NodoExpresion izquierda, NodoExpresion derecha) {
        String izType = izquierda.getTipoValorExpresion();
        String derType = derecha.getTipoValorExpresion();

        boolean ambosNumericos = (izType.equals("INTEGER") || izType.equals("FLOAT")) &&
                (derType.equals("INTEGER") || derType.equals("FLOAT"));

        boolean ambosStrings = izType.equals("STRING") && derType.equals("STRING");

        return ambosNumericos || ambosStrings;
    }
}
