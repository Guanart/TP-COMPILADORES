package com.grupo2.tpteo1grupo2.clases;

import com.grupo2.tpteo1grupo2.TablaSimbolos;

public class NodoAsignacion extends NodoSentencia {
    private final NodoIdentificador identificador;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoIdentificador identificador, NodoExpresion expresion) {
        super("::=");
        this.identificador = identificador;
        this.expresion = expresion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                identificador.graficar(miId) +
                expresion.graficar(miId);
    }

    public void chequearValidezTipos(NodoIdentificador identificador, NodoExpresion expresion, TablaSimbolos tablaSimbolos) {

    }

    @Override
    public String generarAssembler() {
        StringBuilder assembler = new StringBuilder();

        // Generar código ensamblador para la asignación
        assembler.append("MOV ").append(identificador.getId()).append(", ").append(expresion.generarAssembler()).append("\n");

        return assembler.toString();
    }
}
