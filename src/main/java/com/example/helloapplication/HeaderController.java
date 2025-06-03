package com.example.helloapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class HeaderController {

    @FXML
    private void goToTableau(ActionEvent event) throws IOException {
        Stage stage = getStageFromEvent(event);
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Vue Tableau");
    }

    @FXML
    private void goToGraphique(ActionEvent event) throws IOException {
        Stage stage = getStageFromEvent(event);
        Parent root = FXMLLoader.load(getClass().getResource("graph-view.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Vue Graphique");
    }

    private Stage getStageFromEvent(ActionEvent event) {
        // Récupérer la Stage via le MenuItem déclencheur (car MenuItem n'est pas un Node)
        MenuItem menuItem = (MenuItem) event.getSource();
        return (Stage) menuItem.getParentPopup().getOwnerWindow();
    }

    @FXML
    private void handleQuitter() {
        Platform.exit();
    }
}
