package com.grupo2.tpteo1grupo2.clases;

public class NodoIdentificador extends NodoExpresion {
    private final String identificador;
    private String tipo;

    public NodoIdentificador(String identificador) {
        super(identificador);
        this.identificador = identificador;
    }

    public NodoIdentificador(String identificador, String tipo) {
        super(identificador);
        this.identificador = identificador;
        this.tipo = tipo;
    }

    public String getId() {
        return identificador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }

    @Override
    public String toString() {
        return identificador;
    }

    @Override
    public String getTipoValorExpresion() {
        return this.getTipo();
    }

    @Override
    public boolean soyHoja() {
        return true; // Un identificador es una hoja en el árbol de sintaxis
    }

    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
    }
}
