package com.grupo2.tpteo1grupo2.clases;

public class NodoTipo extends Nodo{
    public NodoTipo(String nombre) {
        super(nombre);
    }

    public String getType() {
        return this.getDescripcion();
    }
}
