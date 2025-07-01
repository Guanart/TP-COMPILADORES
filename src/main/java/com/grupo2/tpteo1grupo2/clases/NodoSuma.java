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

        // TO DO: Verificar que ambos hijos sean hojas (constantes o identificadores) antes de generar el código ensamblador. Si uno de los hijos no es una hoja, se debe llamar su metodo generarAssembler() para obtener su código ensamblador, y que incluya su variable tipo _@aux a la dataSection.
        // Verificar que ambos hijos sean hojas
        // if (!izquierda.soyHoja()) {
        //     code += izquierda.generarAssembler(dataSection, codeSection);
        // }
        // if (!derecha.soyHoja()) {
        //     code += derecha.generarAssembler(dataSection, codeSection);
        // }

        // Hijos hojas
        code += "FLD _" + izquierda.getIdNodo() + "\n"; // Carga el valor de la izquierda en la pila del coprocesador
        code += "FLD _" + derecha.getIdNodo() + "\n"; // Carga el valor de la derecha en la pila del coprocesador
        code += "FADD\n"; // Suma los dos valores en la pila del coprocesador
        code += "FSTP _@" + this.getIdNodo() + "\n"; // Almacena el resultado de la suma en el nodo actual
        code += "\n"; // Nueva línea para separar instrucciones

        // Agregar variable auxiliar para el resultado de la suma a la data section
        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        // Agregar el resultado de la suma a la sección de código
        codeSection.append(code);
    }
}