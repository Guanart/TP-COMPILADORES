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
        System.out.println("Generando código ensamblador para la expresión: " + this.getDescripcionNodo());
        String code = "";
        // Asignar el valor de la expresión al identificador
        // Primero, generar el código ensamblador para la expresión
        expresion.generarAssembler(dataSection, codeSection);
        // Luego, almacenar el resultado de la expresión en el identificador
        // Se asume que el identificador ya tiene su variable auxiliar creada en la data section

        code += "FLD _@" + expresion.getIdNodo() + "\n";
        code += "FSTP _" + identificador.getId() + "\n";
        codeSection.append(code);
    }
}
