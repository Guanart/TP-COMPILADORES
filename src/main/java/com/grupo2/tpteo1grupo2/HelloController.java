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

import com.grupo2.tpteo1grupo2.clases.*;

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
    }

    @FXML
    protected void onCompilar() throws IOException {
        if (codeTextArea == null) {
            return;
        }
        Stage currentStage = (Stage) codeTextArea.getScene().getWindow();
        String contenidoACompilar = codeTextArea.getText();

        if (!contenidoACompilar.isEmpty()) {
            this.utils.crearArchivo(contenidoACompilar);
            try {
                // Cargar prueba.txt
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                FileReader f = new FileReader(new File(tempDir, "prueba.txt"));

                // Obtener Léxico a partir del texto a compilar
                Lexico Lexer = new Lexico(f);

                // Instanciar parser
                Parser parser = new Parser(Lexer);

                // Parsear y obtener el AST
                NodoPrograma programa = (NodoPrograma) parser.parse().value;

                // Graficar el AST en un archivo .dot
                try (PrintWriter pw = new PrintWriter(new FileWriter("arbol.dot"))) {
                    pw.println(programa.graficar());
                }

                // Generar imagen PNG usando Graphviz
                String cmd = "dot -Tpng arbol.dot -o arbol.png";
                Process p = Runtime.getRuntime().exec(cmd);

                // Esperar a que termine la ejecución de "dot"
                int exitCode = p.waitFor();

                // Verificar si se creó el archivo
                File arbolPng = new File("arbol.png");
                if (exitCode == 0 && arbolPng.exists()) {
                    System.out.println("✅ Árbol AST generado correctamente: " + arbolPng.getAbsolutePath());
                } else {
                    System.err.println("❌ Error al generar el árbol AST.");
                }

                // Obtener reglas de sintaxis
                String reglas = parser.getReglas();
                reglas = reglas.replace("null", "");
                Resultado.getInstance().setContenido(reglas);

            } catch (FileNotFoundException ex) {
                this.utils.mostrarAlertError();
            } catch (SintaxException ex) {
                this.utils.mostrarSintaxError();
                throw ex;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Mostrar reglas
            FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/com/grupo2/tpteo1grupo2/ResultadoCompilacion.fxml"));
            Parent newRoot = fxmlLoader.load();
            Scene resultadoCompilacion = new Scene(newRoot, 600, 700);
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), currentStage.getScene().getRoot());
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newRoot);
            slideOut.setOnFinished(event -> {
                currentStage.setScene(resultadoCompilacion);
                slideIn.play();
            });
            currentStage.setOnCloseRequest(event -> this.exitApplication(null));
            slideOut.play();
        } else {
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