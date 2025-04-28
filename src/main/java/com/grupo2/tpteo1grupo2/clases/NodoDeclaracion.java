package com.grupo2.tpteo1grupo2.clases;

public class NodoDeclaracion extends Nodo {
    private NodoIdentificador identificador;
    private NodoTipo tipo;

    public NodoDeclaracion(NodoIdentificador identificador, NodoTipo tipo) {
        super(":=");
        this.identificador = identificador;
        this.tipo = tipo;
    }
}
