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
        System.out.println("Generando código assembler para IF");
        String code = "";

        // Etiquetas únicas para el salto
        String labelElse = "ELSE_" + this.getIdNodo();
        String labelEndIf = "ENDIF_" + this.getIdNodo();

        // Generar assembler de la condición booleana
        condicion.generarAssembler(dataSection, codeSection);

        // La condición dejó su resultado en _@condicion.getIdNodo()
        code += "MOV EAX, _@" + condicion.getIdNodo() + "\n";
        code += "CMP EAX, 0\n";
        if (sentenciasElse != null && !sentenciasElse.isEmpty()) {
            code += "JE " + labelElse + "\n"; // Si es falso, va al else
        } else {
            code += "JE " + labelEndIf + "\n"; // Si no hay else, salta al fin del if
        }

        // THEN: generar assembler de cada sentencia en la lista
        for (NodoSentencia sentencia : sentenciasThen) {
            sentencia.generarAssembler(dataSection, codeSection);
        }

        // Si hay ELSE, se hace salto incondicional al final
        if (sentenciasElse != null && !sentenciasElse.isEmpty()) {
            code += "JMP " + labelEndIf + "\n";
            code += labelElse + ":\n";
            for (NodoSentencia sentencia : sentenciasElse) {
                sentencia.generarAssembler(dataSection, codeSection);
            }
        }

        code += labelEndIf + ":\n\n";

        codeSection.append(code);
    }
}
