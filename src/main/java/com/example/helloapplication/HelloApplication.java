package com.example.helloapplication;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 340);
        stage.setTitle( "Bienvenue sur mon tableau !");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/fire.png")));
        stage.show();
    }

    public static void main(String[] args) {
        if(!Database.isOK()){
            System.err.println("Erreur de connexion");
            System.exit(1);
            return;
        }
        launch();
    }
}