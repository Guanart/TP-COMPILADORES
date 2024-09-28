package com.grupo2.tpteo1grupo2;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class HelloController extends Component {
    private Stage stage;
    @FXML
    private Label welcomeText;

    @FXML
    private TextArea codeTextArea;

    @FXML
    private Button outerButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onCompilar() throws IOException {
        if (codeTextArea == null) {
            System.out.println("codeTextArea no está inicializado.");
            return;
        }

        Stage currentStage = (Stage) codeTextArea.getScene().getWindow();

        String contenidoACompilar = codeTextArea.getText();

        try {
            if (!contenidoACompilar.isEmpty()){
                this.crearArchivo(contenidoACompilar);
            }
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            FileReader f = new FileReader(new File(tempDir, "prueba.txt"));
            Lexico Lexer = new Lexico(f);
            Lexer.next_token();

        } catch (FileNotFoundException ex) {
            System.out.println("No se encontró el archivo");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/com/grupo2/tpteo1grupo2/ResultadoCompilacion.fxml"));
        Parent newRoot = fxmlLoader.load();
        Scene resultadoCompilacion = new Scene(newRoot, 550, 550);

// Crear la transición de deslizamiento hacia la izquierda para la escena actual
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), currentStage.getScene().getRoot());
        slideOut.setFromX(0);
        slideOut.setToX(-currentStage.getScene().getWidth());

// Crear la transición de deslizamiento desde la derecha para la nueva escena
        newRoot.translateXProperty().set(currentStage.getScene().getWidth());
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(05), newRoot);
        slideIn.setFromX(currentStage.getScene().getWidth());
        slideIn.setToX(0);

// Configurar el cambio de escena después de la transición de deslizamiento de salida
        slideOut.setOnFinished(event -> {
            currentStage.setScene(resultadoCompilacion);
            slideIn.play();
        });

        slideOut.play();

    }

    private void crearArchivo(String contenidoACompilar) {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File outputFile = new File(tempDir, "prueba.txt");
        try (FileWriter f = new FileWriter(outputFile)) {
            f.write(contenidoACompilar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUploadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");

        // Opcional: agregar filtros de archivo
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);

        // Abrir el explorador de archivos
        File selectedFile = fileChooser.showOpenDialog(this.stage);
        if (selectedFile != null) {
            FileUpload fileUpload = new FileUpload();
            String contenido = fileUpload.readFile(selectedFile.getAbsolutePath());
            codeTextArea.setText(contenido);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

}