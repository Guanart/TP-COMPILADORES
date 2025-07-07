package com.grupo2.tpteo1grupo2.clases;

public class NodoMayor extends NodoComparacion {
    public NodoMayor (NodoExpresion izquierda, NodoExpresion derecha) {
        super(">", izquierda, derecha);
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        System.out.println("Generando código ensamblador para la expresión: " + this.getDescripcionNodo());
        String code = "";
        String labelTrue = "LT_TRUE_" + this.getIdNodo();
        String labelEnd = "LT_END_" + this.getIdNodo();

        // Generar código de los hijos si no son hojas
        if (!izquierda.soyHoja()) {
            izquierda.generarAssembler(dataSection, codeSection);
        }
        if (!derecha.soyHoja()) {
            derecha.generarAssembler(dataSection, codeSection);
        }

        // Cargar valores en registros y comparar
        if (!izquierda.soyHoja()) {
            code += "MOV EAX, _@" + izquierda.getIdNodo() + "\n";
        } else {
            code += "MOV EAX, _" + izquierda.getDescripcion() + "\n";
        }

        if (!derecha.soyHoja()) {
            code += "CMP EAX, _@" + derecha.getIdNodo() + "\n";
        } else {
            code += "CMP EAX, _" + derecha.getDescripcion() + "\n";
        }

        // Instrucciones condicionales
        code += "JG " + labelTrue + "\n"; // salto si EAX > valor comparado
        code += "MOV DWORD PTR _@" + this.getIdNodo() + ", 0\n"; // falso
        code += "JMP " + labelEnd + "\n";
        code += labelTrue + ":\n";
        code += "MOV DWORD PTR _@" + this.getIdNodo() + ", 1\n"; // verdadero
        code += labelEnd + ":\n\n";

        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        codeSection.append(code);
    }
}
