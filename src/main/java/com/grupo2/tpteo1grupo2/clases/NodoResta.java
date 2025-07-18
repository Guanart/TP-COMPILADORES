package com.grupo2.tpteo1grupo2.clases;

public class NodoResta extends NodoExpresionBinaria {

    public NodoResta(NodoExpresion izquierda, NodoExpresion derecha) {
        super("-", izquierda, derecha);
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String code = "";

        // TO DO: Verificar que ambos hijos sean hojas (constantes o identificadores) antes de generar el código ensamblador. 
        // Si uno de los hijos no es una hoja, se debe llamar su metodo generarAssembler() para obtener su código ensamblador, 
        // y que incluya su variable tipo _@aux a la dataSection.

        // Verificar que ambos hijos sean hojas
        if (!izquierda.soyHoja()) {
            izquierda.generarAssembler(dataSection, codeSection);
            code += "FLD _@" + izquierda.getIdNodo() + "\n"; // Carga el valor de la izquierda en la pila del coprocesador
        } else {
            // Si es una hoja, se carga directamente su valor
            code += "FLD _" + izquierda.getDescripcion() + "\n";
        }

        if (!derecha.soyHoja()) {
            derecha.generarAssembler(dataSection, codeSection);
            code += "FLD _@" + derecha.getIdNodo() + "\n"; // Carga el valor de la derecha en la pila del coprocesador
        } else {
            // Si es una hoja, se carga directamente su valor
            code += "FLD _" + derecha.getDescripcion() + "\n";
        }

        // Hijos hojas
        code += "FSUB\n"; // Resta los dos valores en la pila del coprocesador
        code += "FSTP _@" + this.getIdNodo() + "\n"; // Almacena el resultado de la resta en el nodo actual
        code += "\n"; // Nueva línea para separar instrucciones

        // Agregar variable auxiliar para el resultado de la resta a la data section
        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        // Agregar el resultado de la resta a la sección de código
        codeSection.append(code);
    }
}