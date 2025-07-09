package com.grupo2.tpteo1grupo2.clases;

import java.util.List;

public class NodoCiclo extends NodoSentencia {
    private final NodoExpresionBooleana condicion;
    private final List<NodoSentencia> cuerpo;
  

    public NodoCiclo(NodoExpresionBooleana condicion, List<NodoSentencia> cuerpo) {
        super("WHILE");
        this.condicion = condicion;
        this.cuerpo = cuerpo;
       
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();

        // Grafica el nodo IF
        resultado.append(super.graficar(idPadre));

        // Grafica la condición colgando directamente del nodo CUERPO
        resultado.append(condicion.graficar(miId));

        // Agrega un nodo ficticio CUERPO colgando del nodo WHILE
        Nodo nodoThen = new Nodo("Cuerpo");
        resultado.append(nodoThen.graficar(miId));

        // Grafica las sentencias asociadas al "then" colgando del nodo ficticio THEN
        String idNodoThen = nodoThen.getIdNodo();
        for (NodoSentencia sentencia: cuerpo) {
            resultado.append(sentencia.graficar(idNodoThen));
        }

        /* Si hay sentencias asociadas al "else"...
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
*/
        return resultado.toString();
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        System.out.println("Generando código assembler para WHILE");
        String code = "";

        // Etiquetas únicas por nodo
        String labelInicio = "WHILE_START_" + this.getIdNodo();
        String labelFin = "WHILE_END_" + this.getIdNodo();

        code += labelInicio + ":\n";

        // Generar assembler de la condición
        StringBuilder condicionCode = new StringBuilder();
        condicion.generarAssembler(dataSection, condicionCode); // genera código para evaluar y guardar resultado
        code += condicionCode.toString();

        // Evaluar la condición (resultado debe estar en _@condicion.getIdNodo())
        code += "MOV EAX, _@" + condicion.getIdNodo() + "\n";
        code += "CMP EAX, 0\n";
        code += "JE " + labelFin + "\n"; // Si la condición es falsa, salta al final

        // Generar el código del cuerpo del ciclo
        StringBuilder cuerpoCode = new StringBuilder();
        for (NodoSentencia sentencia : cuerpo) {
            sentencia.generarAssembler(dataSection, cuerpoCode);
        }
        code += cuerpoCode.toString();

        // Volver al inicio del ciclo
        code += "JMP " + labelInicio + "\n";

        // Etiqueta de fin
        code += labelFin + ":\n\n";

        codeSection.append(code);
    }
}


