package com.grupo2.tpteo1grupo2.clases;


public class NodoAnd extends NodoExpresionBooleana {
    private final NodoExpresionBooleana izquierda;
    private final NodoExpresionBooleana derecha;

    public NodoAnd (NodoExpresionBooleana izquierda, NodoExpresionBooleana derecha) {
        super("AND");
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                izquierda.graficar(miId) +
                derecha.graficar(miId);
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String code = "";

        if (!izquierda.soyHoja()) {
            izquierda.generarAssembler(dataSection, codeSection);
        }
        if (!derecha.soyHoja()) {
            derecha.generarAssembler(dataSection, codeSection);
        }

        // Cargar operandos y aplicar AND lógico
        if (!izquierda.soyHoja()) {
            code += "MOV EAX, _@" + izquierda.getIdNodo() + "\n";
        } else {
            code += "MOV EAX, _" + izquierda.getDescripcion() + "\n";
        }

        if (!derecha.soyHoja()) {
            code += "MOV EBX, _@" + derecha.getIdNodo() + "\n";
        } else {
            code += "MOV EBX, _" + derecha.getDescripcion() + "\n";
        }

        code += "AND EAX, EBX\n"; // AND lógico
        code += "MOV _@" + this.getIdNodo() + ", EAX\n\n";

        dataSection.append("_@" + this.getIdNodo() + " DD ?\n");
        codeSection.append(code);
    }
}

