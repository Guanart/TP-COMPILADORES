package com.grupo2.tpteo1grupo2.clases;

import java.util.List;

public class NodoPrograma extends Nodo {
    private final List<NodoSentencia> sentencias;
    private final List<NodoDeclaracion> declaraciones;

    public NodoPrograma(List<NodoSentencia> sentencias, List<NodoDeclaracion> declaraciones) {
        super("PGM");
        this.sentencias = sentencias;
        this.declaraciones = declaraciones;
    }

    public String graficar() {
        // Acá se dispara la invocación a los métodos graficar() de los nodos.
        // Como un NodoPrograma no tiene padre, se inicia pasando null.
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_programa";

        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");

        resultado.append(miId + " [label=\"Programa\"]\n");
        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        if (this.declaraciones != null) {
            for (NodoDeclaracion declaracion : this.declaraciones) {
                resultado.append(declaracion.graficar(miId));
            }
        }

        resultado.append("}");

        return resultado.toString();
    }
}

