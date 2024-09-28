package com.grupo2.tpteo1grupo2;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ResultadoCompilacion {
    @FXML
    private TextArea codeTextArea;
    private final Utils utils = new Utils();

    @FXML
    public void initialize() {
        codeTextArea.setText(Resultado.getInstance().getContenido());
    }
    @FXML
    public void onNavBack(MouseEvent mouseEvent) throws IOException {

        Stage currentStage = (Stage) codeTextArea.getScene().getWindow();
        String contenidoACompilar = codeTextArea.getText();
        FXMLLoader fxmlLoader = new FXMLLoader(ResultadoCompilacion.class.getResource("/com/grupo2/tpteo1grupo2/hello-view.fxml"));
        Parent newRoot = fxmlLoader.load();
        Scene resultadoCompilacion = new Scene(newRoot, 600, 700);

        // Crear la transición de deslizamiento hacia la derecha para la escena actual
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), currentStage.getScene().getRoot());
        slideOut.setFromX(0);
        slideOut.setToX(currentStage.getScene().getWidth());

// Crear la transición de deslizamiento desde la izquierda para la nueva escena
        newRoot.translateXProperty().set(-currentStage.getScene().getWidth());
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newRoot);
        slideIn.setFromX(-currentStage.getScene().getWidth());
        slideIn.setToX(0);

// Configurar el cambio de escena después de la transición de deslizamiento de salida
        slideOut.setOnFinished(event -> {
            currentStage.setScene(resultadoCompilacion);
            slideIn.play();
        });
        currentStage.setOnCloseRequest(event -> this.exitApplication(null));

        slideOut.play();


    }

    private void exitApplication(Object o) {
        this.utils.borrarArchivoTemporal();
    }
}
