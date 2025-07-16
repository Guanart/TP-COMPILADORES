package com.grupo2.tpteo1grupo2.clases;

public class NodoSuma extends NodoExpresionBinaria {

    public NodoSuma(NodoExpresion izquierda, NodoExpresion derecha) {
        super("+", izquierda, derecha);
    }

    @Override
    public String toString() {
        return izquierda.toString() + "+" + derecha.toString();
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String code = "";

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
        code += "FADD\n"; // Suma los dos valores en la pila del coprocesador
        code += "FSTP _@" + this.getIdNodo() + "\n"; // Almacena el resultado de la suma en el nodo actual
        code += "\n"; // Nueva línea para separar instrucciones

        // Agregar variable auxiliar para el resultado de la suma a la data section
        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        // Agregar el resultado de la suma a la sección de código
        codeSection.append(code);
    }
}