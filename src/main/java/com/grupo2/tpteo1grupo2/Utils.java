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
            } else {
            }
        } else {
        }
    }

    void mostrarAlertError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, seleccione un archivo o escriba lo que desea compilar");
        alert.showAndWait();
    }

    void mostrarSintaxisError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error de sintaxis. Por favor, comprueba la sintaxis");
        alert.showAndWait();
    }

    void mostrarBadToken() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Caracter no permitido");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, revise el texto ingresado");
        alert.showAndWait();
    }

    void mostrarStringLimit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Limite CTE_STRING");
        alert.setHeaderText(null);
        alert.setContentText("Una constante de string puede tener hasta 30 caracteres unicamente");
        alert.showAndWait();
    }

    void mostrarRealLimit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Limite CTE_REAL");
        alert.setHeaderText(null);
        alert.setContentText("Una constante real puede valer hasta 1000.0");
        alert.showAndWait();
    }

    void mostrarIntLimit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Limite CTE_INT");
        alert.setHeaderText(null);
        alert.setContentText("Una constante natural puede valer hasta 1000");
        alert.showAndWait();
    }

    void mostrarVariableInvalida(String variable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Variable inválida");
        alert.setHeaderText(null);
        alert.setContentText("La variable " + variable + " no ha sido declarada");
        alert.showAndWait();
    }
}
