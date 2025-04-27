package com.grupo2.tpteo1grupo2;

public class NodoOplist extends NodoSentencia {
    private final NodoAsignacion asignacion;

    public NodoOplist(NodoAsignacion asignacion) {
        super("OPLIST");
        this.asignacion = asignacion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                asignacion.graficar(miId);
    }
}
