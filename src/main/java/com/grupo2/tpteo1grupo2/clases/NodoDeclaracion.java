package com.grupo2.tpteo1grupo2.clases;

public class NodoDeclaracion extends Nodo {
    private NodoIdentificador identificador;
    private NodoTipo tipo;

    public NodoDeclaracion(NodoIdentificador identificador, NodoTipo tipo) {
        super(":=");
        this.identificador = identificador;
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador.getId();
    }

    public String getTipo() {
        return tipo.getType();
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                identificador.graficar(miId) +
                tipo.graficar(miId);
    }

}
