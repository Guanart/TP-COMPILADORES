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
        return  "";
    }
}