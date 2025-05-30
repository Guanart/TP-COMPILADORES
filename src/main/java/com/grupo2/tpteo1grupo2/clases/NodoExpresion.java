package com.grupo2.tpteo1grupo2.clases;

public abstract class NodoExpresion extends Nodo {

    public NodoExpresion(String nombre) {
        super(nombre);
    }

    public abstract String getTipoValorExpresion();

}
