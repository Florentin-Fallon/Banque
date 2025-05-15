package com.example.helloapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddDialogController {

    @FXML
    private DatePicker periodePicker;
    @FXML
    private TextField logementField;
    @FXML
    private TextField nourritureField;
    @FXML
    private TextField sortiesField;
    @FXML
    private TextField voitureField;
    @FXML
    private TextField voyageField;
    @FXML
    private TextField impotsField;
    @FXML
    private TextField autresField;

    private HelloController mainController;

    public AddDialogController() {}

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            LocalDate periode = periodePicker.getValue(); // ⚠️ c’est une vraie date maintenant

            int logement = Integer.parseInt(logementField.getText());
            int nourriture = Integer.parseInt(nourritureField.getText());
            int sorties = Integer.parseInt(sortiesField.getText());
            int voiture = Integer.parseInt(voitureField.getText());
            int voyage = Integer.parseInt(voyageField.getText());
            int impots = Integer.parseInt(impotsField.getText());
            int autres = Integer.parseInt(autresField.getText());

            int total = logement + nourriture + sorties + voiture + voyage + impots + autres;

            // ⚠️ Tu dois adapter ici selon ton code existant
            HelloController newLine = new HelloController(periode, total, logement, nourriture, sorties, voiture, voyage, impots, autres);

            mainController.addLine(newLine);

            Stage stage = (Stage) periodePicker.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Entrée invalide");
            alert.setContentText("Veuillez vérifier les valeurs des champs numériques.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) periodePicker.getScene().getWindow();
        stage.close();
    }
}
