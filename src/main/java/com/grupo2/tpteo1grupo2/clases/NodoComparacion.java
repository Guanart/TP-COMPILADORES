package com.grupo2.tpteo1grupo2.clases;


import com.grupo2.tpteo1grupo2.InvalidTypeException;

public class NodoComparacion extends NodoExpresionBooleana {
    protected final NodoExpresion  izquierda;
    protected final NodoExpresion  derecha;

    public NodoComparacion (String nombre, NodoExpresion izquierda, NodoExpresion derecha) {
        super(nombre);
        this.izquierda = izquierda;
        this.derecha = derecha;
        if (!(isValid(izquierda, derecha))) {
            throw new InvalidTypeException("Comparación inválida: No se pueden comparar variables STRING");
        }
    }

    public static Boolean isValid(NodoExpresion izquierda, NodoExpresion derecha) {
        String izType = izquierda.getTipoValorExpresion();
        String derType = derecha.getTipoValorExpresion();
        return !izType.equals("STRING") && !derType.equals("STRING");
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                izquierda.graficar(miId) +
                derecha.graficar(miId);
    }
    
    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {

    }
}
