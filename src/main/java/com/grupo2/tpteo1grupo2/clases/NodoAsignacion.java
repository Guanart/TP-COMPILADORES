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
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String code = "";
        // Asignar el valor de la expresión al identificador
        code += "FLD _@" + expresion.getIdNodo() + "\n";
        code += "FSTP _" + identificador.getIdNodo() + "\n";
        codeSection.append(code);
    }
}
