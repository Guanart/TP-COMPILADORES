package com.grupo2.tpteo1grupo2.clases;

public abstract class NodoExpresion extends Nodo {

    public NodoExpresion(String nombre) {
        super(nombre);
    }

    public abstract String getTipoValorExpresion();

    @Override
    public String generarAssembler() {
        return "";
        //StringBuilder assembler = new StringBuilder();
        //assembler.append("MOV ").append(this.getId()).append(", ").append(this.getValor()).append("\n");
        //return assembler.toString();
    }

}
