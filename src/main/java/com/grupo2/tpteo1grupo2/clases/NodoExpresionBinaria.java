package com.grupo2.tpteo1grupo2.clases;

import com.grupo2.tpteo1grupo2.InvalidTypeException;

public class NodoExpresionBinaria extends NodoExpresion {
    protected final NodoExpresion izquierda;
    protected final NodoExpresion derecha;

    public NodoExpresionBinaria(String nombre, NodoExpresion izquierda, NodoExpresion derecha) {
        super(nombre);
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.getTipoValorExpresion();
    }

    public String getTipoValorExpresion() {
        String tipo1 = izquierda.getTipoValorExpresion();
        String tipo2 = derecha.getTipoValorExpresion();
        if (tipo1.equals("STRING") || tipo2.equals("STRING")) {
            throw new InvalidTypeException("Expresión inválida: No se permiten operaciones matemáticas con STRING");
        } else if (tipo1.equals("FLOAT") || tipo2.equals("FLOAT")) {
            return "FLOAT";
        } else {
            return "INTEGER";
        }
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
