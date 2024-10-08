package com.grupo2.tpteo1grupo2;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;

public class HelloController extends Component {
    private final Utils utils = new Utils();
    private Stage stage;
    @FXML
    private Label welcomeText;

    @FXML
    private TextArea codeTextArea;

    @FXML
    private Button outerButton;

    @FXML
    private HBox uploadButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onCompilar() throws IOException {
        if (codeTextArea == null) {
            return;
        }

        Stage currentStage = (Stage) codeTextArea.getScene().getWindow();

        String contenidoACompilar = codeTextArea.getText();
        if (!contenidoACompilar.isEmpty()){
            this.utils.crearArchivo(contenidoACompilar);
        try {

            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            FileReader f = new FileReader(new File(tempDir, "prueba.txt"));
            Lexico Lexer = new Lexico(f);
            Lexer.next_token();

        } catch (FileNotFoundException ex) {
            this.utils.mostrarAlertError();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/com/grupo2/tpteo1grupo2/ResultadoCompilacion.fxml"));
        Parent newRoot = fxmlLoader.load();
        Scene resultadoCompilacion = new Scene(newRoot, 600, 700);

        // Crear la transición de deslizamiento hacia la izquierda para la escena actual
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), currentStage.getScene().getRoot());
        slideOut.setFromX(0);
        slideOut.setToX(-currentStage.getScene().getWidth());

        // Crear la transición de deslizamiento desde la derecha para la nueva escena
        newRoot.translateXProperty().set(currentStage.getScene().getWidth());
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newRoot);
        slideIn.setFromX(currentStage.getScene().getWidth());
        slideIn.setToX(0);

        // Configurar el cambio de escena después de la transición de deslizamiento de salida
        slideOut.setOnFinished(event -> {
            currentStage.setScene(resultadoCompilacion);
            slideIn.play();
        });
        currentStage.setOnCloseRequest(event -> this.exitApplication(null));

        slideOut.play();
    }else {
            this.utils.mostrarAlertError();
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
        }
    }

    @FXML
    public void exitApplication(ActionEvent event){
        this.utils.borrarArchivoTemporal();
        Platform.exit();
    }


}