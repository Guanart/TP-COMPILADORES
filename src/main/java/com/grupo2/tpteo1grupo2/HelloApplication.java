package com.grupo2.tpteo1grupo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/grupo2/tpteo1grupo2/hello-view.fxml"));
            //Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/grupo2/tpteo1grupo2/icono.png")));
            //stage.getIcons().add(icon);
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            stage.setTitle("Compilador Grupo 2");
            stage.setScene(scene);
            stage.show(); // Mostrar la escena primero

            HelloController controller = fxmlLoader.getController(); // Obtener el controlador desde el FXMLLoader
            stage.setOnCloseRequest(event -> controller.exitApplication(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}