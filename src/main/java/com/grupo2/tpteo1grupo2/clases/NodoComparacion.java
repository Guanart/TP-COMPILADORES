package com.grupo2.tpteo1grupo2.clases;


public class NodoComparacion extends NodoExpresionBooleana {
protected final NodoExpresion  izquierda;
protected final NodoExpresion  derecha;

    public NodoComparacion (String nombre,NodoExpresion izquierda, NodoExpresion derecha) {
        super(nombre);
        this.izquierda = izquierda;
        this.derecha = derecha;

        if (!(isValid(izquierda, derecha))) {
            throw new RuntimeException("Comparación inválida");
        }
    }
     @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                izquierda.graficar(miId) +
                derecha.graficar(miId);
    }

    public static Boolean isValid(NodoExpresion izquierda, NodoExpresion derecha) {
        String izType = izquierda.getTipoValorExpresion();
        String derType = derecha.getTipoValorExpresion();

        if ( (izType == null) || (derType == null) ) {
            System.out.println("NODO COMPARACION: uno de los hijos devuelve tipo nulo");
            System.out.println("izType: " + izType);
            System.out.println("derType: " + derType);
            return false;
        }
        return !izType.equals("STRING") && !derType.equals("STRING");
    }
    
    @Override
    public void generarAssembler(StringBuilder dataSection, StringBuilder codeSection) {

    }
}
