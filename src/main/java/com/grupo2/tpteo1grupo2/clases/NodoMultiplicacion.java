package com.grupo2.tpteo1grupo2.clases;

public class NodoMultiplicacion extends NodoExpresionBinaria {

    public NodoMultiplicacion(NodoExpresion izquierda, NodoExpresion derecha) {
        super("*", izquierda, derecha);
    }

    @Override
    public String toString() {
        return izquierda.toString() + "*" + derecha.toString();
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        System.out.println("Generando código ensamblador para la expresión: " + this.getDescripcionNodo());
        String code = "";

        if (!izquierda.soyHoja()) {
            izquierda.generarAssembler(dataSection, codeSection);
            code += "FLD _@" + izquierda.getIdNodo() + "\n";
        } else {
            code += "FLD _" + izquierda.getDescripcion() + "\n";
        }

        if (!derecha.soyHoja()) {
            derecha.generarAssembler(dataSection, codeSection);
            code += "FLD _@" + derecha.getIdNodo() + "\n";
        } else {
            code += "FLD _" + derecha.getDescripcion() + "\n";
        }

        code += "FMUL\n"; // Multiplicación
        code += "FSTP _@" + this.getIdNodo() + "\n";
        code += "\n";

        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        codeSection.append(code);
    }
}
