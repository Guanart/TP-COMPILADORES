package com.grupo2.tpteo1grupo2.clases;

public class NodoEscritura extends NodoSentencia {
    private final NodoExpresion parametro;

    public NodoEscritura(NodoExpresion parametro) {
        super("WRITE");
        this.parametro = parametro;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                parametro.graficar(miId);
    }

    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {
        String variable;
        String prefijo;
        if (!parametro.soyHoja()){
                parametro.generarAssembler(dataSection, codeSection);
                prefijo = "_@"; // Se utiliza el prefijo del nodo intermedio
        } else {
            prefijo = "_"; // Prefijo para variables o nodos hoja (constantes)
        }

        if (parametro instanceof NodoIdentificador)
            variable = prefijo + ((NodoIdentificador) parametro).getId(); // Si es una variable, se utiliza el nombre de la misma
        else
            variable = prefijo + parametro.getDescripcion();
        
        String tipo = parametro.getTipoValorExpresion();
        
            
        if (null == tipo) {
            throw new RuntimeException("Tipo no soportado para escritura: " + tipo);
        } else
            switch (tipo) {
                case "INTEGER":
                    codeSection.append("DisplayFloat ").append(variable).append(",2\n");
                    break;
                case "FLOAT":
                    codeSection.append("DisplayFloat ").append(variable).append(",2\n");
                    break;
                case "STRING":
                    codeSection.append("DisplayString ").append(variable).append("\n");
                    break;
                default:
                    throw new RuntimeException("Tipo no soportado para escritura: " + tipo);
            }
        codeSection.append("newLine 1").append("\n");
    }
}
