package com.grupo2.tpteo1grupo2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUpload {
    public String readFile(String path){
        String rutaArchivo = path;  // Coloca aquí la ruta del archivo
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String contenidoArchivo = contenido.toString();
        return contenidoArchivo;
    }
}
