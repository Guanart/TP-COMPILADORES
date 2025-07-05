package com.grupo2.tpteo1grupo2.clases;

import com.grupo2.tpteo1grupo2.TablaSimbolos;

public class NodoAsignacion extends NodoSentencia {
    private final NodoIdentificador identificador;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoIdentificador identificador, NodoExpresion expresion) {
        super("::=");
        this.identificador = identificador;
        this.expresion = expresion;

        if (!(isValid(identificador, expresion))) {
            throw new RuntimeException("Asignacion inválida");
        }
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                identificador.graficar(miId) +
                expresion.graficar(miId);
    }

    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String code = "";
        // Asignar el valor de la expresión al identificador
        code += "FLD _@" + expresion.getIdNodo() + "\n";
        code += "FSTP _" + identificador.getIdNodo() + "\n";
        codeSection.append(code);
    }

    public static Boolean isValid(NodoExpresion id, NodoExpresion exp) {
        String idType = id.getTipoValorExpresion();
        String expType = exp.getTipoValorExpresion();

        if ( (idType == null) || (expType == null) ) {
            System.out.println("NODO ASIGNACION: uno de los hijos devuelve tipo nulo");
            System.out.println("idType: " + idType);
            System.out.println("expType: " + expType);
            return false;
        }
        return idType.equals(expType);
    }
}
