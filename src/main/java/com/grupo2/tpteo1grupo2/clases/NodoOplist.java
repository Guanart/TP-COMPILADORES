package com.grupo2.tpteo1grupo2.clases;

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

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        asignacion.generarAssembler(dataSection, codeSection);
    }
}
