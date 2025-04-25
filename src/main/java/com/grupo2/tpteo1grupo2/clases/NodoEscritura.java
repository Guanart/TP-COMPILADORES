package com.grupo2.tpteo1grupo2.clases;

public class NodoEscritura extends NodoSentencia {
    private final NodoExpresion parametro;

    public NodoEscritura(NodoExpresion parametro) {
        super("WRITE");
        this.parametro = parametro;
    }
}
