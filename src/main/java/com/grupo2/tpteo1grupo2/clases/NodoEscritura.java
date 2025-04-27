package com.grupo2.tpteo1grupo2;

public class NodoEscritura extends NodoSentencia {
    private final NodoExpresion parametro;

    public NodoEscritura(NodoExpresion parametro) {
        super("WRITE");
        this.parametro = parametro;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                parametro.graficar(miId);
    }
}
