package com.grupo2.tpteo1grupo2;

public class Resultado {
    // Variable estática para la instancia única
    private static Resultado resultado;

    // Atributo no estático para guardar el contenido
    private String contenido;

    // Constructor privado para evitar la instanciación directa
    private Resultado() {
    }

    // Método para obtener la única instancia de la clase
    public static Resultado getInstance() {
        if (resultado == null) {
            resultado = new Resultado();
        }
        return resultado;
    }

    // Métodos de acceso y modificación del contenido
    public void setContenido(String contenido) {
        this.contenido = contenido;  // Ahora es no estático
    }

    public String getContenido() {
        return this.contenido;  // Ahora es no estático
    }
}
