package com.grupo2.tpteo1grupo2.clases;

import java.util.List;

public class NodoIf extends NodoSentencia {
    private final NodoExpresionBooleana condicion;
    private final List<NodoSentencia> sentenciasThen;
    private final List<NodoSentencia> sentenciasElse;

    public NodoIf(NodoExpresionBooleana condicion, List<NodoSentencia> sentenciasThen, List<NodoSentencia> sentenciasElse) {
        super("IF");
        this.condicion = condicion;
        this.sentenciasThen = sentenciasThen;
        this.sentenciasElse = sentenciasElse;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();

        // Grafica el nodo IF
        resultado.append(super.graficar(idPadre));

        // Grafica la condición colgando directamente del nodo IF
        resultado.append(condicion.graficar(miId));

        // Agrega un nodo ficticio THEN colgando del nodo IF
        Nodo nodoThen = new Nodo("Then");
        resultado.append(nodoThen.graficar(miId));

        // Grafica las sentencias asociadas al "then" colgando del nodo ficticio THEN
        String idNodoThen = nodoThen.getIdNodo();
        for (NodoSentencia sentencia: sentenciasThen) {
            resultado.append(sentencia.graficar(idNodoThen));
        }

        // Si hay sentencias asociadas al "else"...
        if (sentenciasElse != null) {
            // Agrega un nodo ficticio "ELSE" colgando del nodo IF
            Nodo nodoElse = new Nodo("Else");
            resultado.append(nodoElse.graficar(miId));

            // Grafica las sentencias asociadas al "else" colgando del nodo ficticio ELSE
            String idNodoElse = nodoElse.getIdNodo();
            for (NodoSentencia sentencia: sentenciasElse) {
                resultado.append(sentencia.graficar(idNodoElse));
            }
        }

        return resultado.toString();
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        StringBuilder code = new StringBuilder();

        // Etiquetas únicas para el salto
        String labelElse = "ELSE_" + this.getIdNodo();
        String labelEndIf = "ENDIF_" + this.getIdNodo();

        // Generar assembler de la condición booleana
        StringBuilder condicionCode = new StringBuilder();
        condicion.generarAssembler(dataSection, condicionCode);
        code.append(condicionCode);

        code.append("MOV EAX, _@" + condicion.getIdNodo() + "\n");
        code.append("CMP EAX, 0\n");

        boolean tieneElse = sentenciasElse != null && !sentenciasElse.isEmpty();
        if (tieneElse) {
            code.append("JE " + labelElse + "\n");
        } else {
            code.append("JE " + labelEndIf + "\n");
        }

        // THEN
        StringBuilder thenCode = new StringBuilder();
        for (NodoSentencia sentencia : sentenciasThen) {
            sentencia.generarAssembler(dataSection, thenCode);
        }
        code.append(thenCode);

        if (tieneElse) {
            code.append("JMP " + labelEndIf + "\n");
            code.append(labelElse + ":\n");

            StringBuilder elseCode = new StringBuilder();
            for (NodoSentencia sentencia : sentenciasElse) {
                sentencia.generarAssembler(dataSection, elseCode);
            }
            code.append(elseCode);
        }

        code.append(labelEndIf + ":\n\n");
        codeSection.append(code);
    }

    @Override
    public String getDescripcionNodo() {
        return "IF";
    }

    @Override
    public boolean soyHoja() {
        return false; // Un nodo IF no es una hoja, ya que tiene hijos (condición y sentencias)
    }
}
