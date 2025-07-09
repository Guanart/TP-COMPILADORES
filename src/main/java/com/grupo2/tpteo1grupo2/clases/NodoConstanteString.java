package com.grupo2.tpteo1grupo2.clases;

import com.grupo2.tpteo1grupo2.TablaSimbolos;

public class NodoConstanteString extends NodoExpresion {
    private final String valor;

    public NodoConstanteString(String valor) {
        super(TablaSimbolos.limpiarNombreAssembler(valor));
        this.valor = valor;
    }

    @Override
    public String getTipoValorExpresion() {
        return "STRING";
    }

    @Override
    public String getDescripcionNodo() {
        return "CONST_STRING: " + valor.replace("\"", "'");
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }

    @Override
    public boolean soyHoja() {
        return true; // Una constante real es una hoja en el árbol de sintaxis
    }


    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    }


}
