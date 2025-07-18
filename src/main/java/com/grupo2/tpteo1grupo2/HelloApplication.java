package com.grupo2.tpteo1grupo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/grupo2/tpteo1grupo2/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            stage.setTitle("Compilador Grupo 2");
            stage.setScene(scene);
            stage.show(); // Mostrar la escena primero
            HelloController controller = fxmlLoader.getController(); // Obtener el controlador desde el FXMLLoader
            stage.setOnCloseRequest(event -> controller.exitApplication(null));
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}