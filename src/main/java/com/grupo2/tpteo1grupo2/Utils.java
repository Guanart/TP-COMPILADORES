package com.grupo2.tpteo1grupo2;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    void crearArchivo(String contenidoACompilar) {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File outputFile = new File(tempDir, "prueba.txt");
        try (FileWriter f = new FileWriter(outputFile)) {
            f.write(contenidoACompilar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void borrarArchivoTemporal() {
        // Obtener el directorio temporal del sistema
        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        // Crear una referencia al archivo "prueba.txt" en la carpeta temporal
        File outputFile = new File(tempDir, "prueba.txt");

        // Verificar si el archivo existe y luego intentar borrarlo
        if (outputFile.exists()) {
            if (outputFile.delete()) {
                System.out.println("El archivo fue borrado con éxito.");
            } else {
                System.out.println("No se pudo borrar el archivo.");
            }
        } else {
            System.out.println("El archivo no existe.");
        }
    }

    void mostrarAlertError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, seleccione un archivo o escriba lo que desea compilar");
        alert.showAndWait();
    }

}
