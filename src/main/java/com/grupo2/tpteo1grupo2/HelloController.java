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
    public void initialize() {

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tempDir, "prueba.txt");

        // Crear el archivo si no existe
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
                this.utils.mostrarAlertError();
                return;
            }
        }

        StringBuilder content = new StringBuilder();

        // Leer el archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.utils.mostrarAlertError();
        }

        String fileContent = content.toString();
        codeTextArea.setText(fileContent);

        /*
        Platform.runLater(() -> {
            Stage stage = (Stage) codeTextArea.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(600);
        });

         */
        // currentStage = (Stage) codeTextArea.getScene().getWindow();
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

            String archivoAux = "nombres_tipos.csv";
            File file = new File(archivoAux);
            if (file.exists()) {
                if (!file.delete()) {
                    System.err.println("No se pudo eliminar el archivo existente: " + archivoAux);
                    return;
                }
            }

            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            FileReader f = new FileReader(new File(tempDir, "prueba.txt"));
            Lexico Lexer = new Lexico(f);
            Parser sintaxis = new Parser(Lexer);
            sintaxis.parse();
            String reglas = sintaxis.getReglas();
            reglas = reglas.replace("null", "");
            Resultado.getInstance().setContenido(reglas);
            //Lexer.next_token();

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
        //slideOut.setFromX(0);
        //slideOut.setToX(-currentStage.getScene().getWidth());

        // Crear la transición de deslizamiento desde la derecha para la nueva escena
        //newRoot.translateXProperty().set(currentStage.getScene().getWidth());
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newRoot);
        //slideIn.setFromX(currentStage.getScene().getWidth());
        //slideIn.setToX(0);

        // Configurar el cambio de escena después de la transición de deslizamiento de salida
        slideOut.setOnFinished(event -> {
            currentStage.setScene(resultadoCompilacion);
            slideIn.play();
        });
        currentStage.setOnCloseRequest(event -> this.exitApplication(null));

        slideOut.play();
        SymbolTableGenerator prueba = new SymbolTableGenerator();
        prueba.editTypes("nombres_tipos.csv","tabla_de_simbolos.csv");
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