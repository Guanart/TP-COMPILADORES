package com.grupo2.tpteo1grupo2.clases;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Nodo {
    private String descripcion;

    public Nodo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    protected String getIdNodo() {
        return "nodo_" + this.hashCode();
    }

    public String getDescripcionNodo() {
        return descripcion;
    }

    protected String graficar(String idPadre) {
        return String.format("%1$s [label=\"%2$s\"]\n%3$s -- %1$s\n", getIdNodo(), getDescripcionNodo(), idPadre);
    }

    public boolean soyHoja() {
        return false; // Por defecto, un nodo no es una hoja
    }

    public String generarAssembler() {
        String assembler = "";
        return assembler;
    }

    // public String generarAssembler(AtomicInteger auxCount, ArrayList<SymbolMe> symbols_table, String tabs, Map<String, String> parameters) {
    //     ConstanteDataASM.data.append(this.getId()).append(" dd, ?\n");

    //     String assembler = "";

    //     if (!this.izquierda.soyHoja()) {
    //         assembler += this.izquierda.generarAssembler(auxCount, symbols_table, tabs, parameters);
    //     }

    //     if (!this.derecha.soyHoja()) {
    //         assembler += this.derecha.generarAssembler(auxCount, symbols_table, tabs, parameters);
    //     }

    //     assembler += tabs + "fld " + this.izquierda.getId() + "\n";
    //     assembler += tabs + "fld " + this.derecha.getId() + "\n";
    //     assembler += tabs + "fdiv\n";
    //     assembler += tabs + "fstp " + this.getId() + "\n";
    //     assembler += "\n";
    //     return assembler;
    // }
}