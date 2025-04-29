package com.grupo2.tpteo1grupo2.clases;

import java.util.ArrayList;

public class NodoPrograma extends Nodo {
    private final ArrayList<NodoDeclaracion> declaraciones;
    private final ArrayList<NodoSentencia> sentencias;

    public NodoPrograma(ArrayList<NodoDeclaracion> declaraciones, ArrayList<NodoSentencia> sentencias) {
        super("PGM");
        this.declaraciones = declaraciones;
        this.sentencias = sentencias;
    }

    public String graficar() {
        // Acá se dispara la invocación a los métodos graficar() de los nodos.
        // Como un NodoPrograma no tiene padre, se inicia pasando null.
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();

        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");

        resultado.append(miId + " [label=\"Programa\"]\n");

        if (this.declaraciones != null) {
            for (NodoDeclaracion declaracion : this.declaraciones) {
                resultado.append(declaracion.graficar(miId));
            }
        }

        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        resultado.append("}");

        return resultado.toString();
    }
}