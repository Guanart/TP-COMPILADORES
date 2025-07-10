package com.grupo2.tpteo1grupo2.clases;

public class NodoIgual extends NodoComparacion{
    public NodoIgual (NodoExpresion izquierda, NodoExpresion derecha) {
        super("=", izquierda, derecha);
    }

    public static Boolean isValid(NodoExpresion izquierda, NodoExpresion derecha) {
        String izType = izquierda.getTipoValorExpresion();
        String derType = derecha.getTipoValorExpresion();

        boolean ambosNumericos = (izType.equals("INTEGER") || izType.equals("FLOAT")) &&
                (derType.equals("INTEGER") || derType.equals("FLOAT"));

        boolean ambosStrings = izType.equals("STRING") && derType.equals("STRING");

        return ambosNumericos || ambosStrings;
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        System.out.println("Generando código ensamblador para la comparación: " + this.getDescripcionNodo());
        StringBuilder code = new StringBuilder();

        String etiquetaTrue = "CMP_TRUE_" + this.getIdNodo();
        String etiquetaEnd = "CMP_END_" + this.getIdNodo();

        // Evaluar lado izquierdo
        if (!izquierda.soyHoja()) {
            izquierda.generarAssembler(dataSection, codeSection);
            code.append("FLD _@" + izquierda.getIdNodo() + "\n");
        } else {
            code.append("FLD _" + izquierda.getDescripcion() + "\n");
        }

        // Comparar con lado derecho
        if (!derecha.soyHoja()) {
            derecha.generarAssembler(dataSection, codeSection);
            code.append("FCOM _@" + derecha.getIdNodo() + "\n");
        } else {
            code.append("FCOM _" + derecha.getDescripcion() + "\n");
        }

        // Procesar flags del coprocesador
        code.append("FSTSW AX\n");
        code.append("SAHF\n");

        // Si izquierda = derecha --> salta a etiquetaTrue
        code.append("JE " + etiquetaTrue + "\n");

        // Falso: poner 0
        code.append("MOV _@" + this.getIdNodo() + ", 0\n");
        code.append("JMP " + etiquetaEnd + "\n");

        // Verdadero: poner 1
        code.append(etiquetaTrue + ":\n");
        code.append("MOV _@" + this.getIdNodo() + ", 1\n");

        // Fin de la comparación
        code.append(etiquetaEnd + ":\n\n");

        // Declarar variable auxiliar donde se guarda el booleano
        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");

        // Añadir a la sección de código final
        codeSection.append(code);
    }
}
