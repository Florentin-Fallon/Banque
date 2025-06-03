package com.example.helloapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            LocalDate periode = periodePicker.getValue(); // c’est une vraie date

            double logement = Double.parseDouble(logementField.getText());
            double nourriture = Double.parseDouble(nourritureField.getText());
            double sorties = Double.parseDouble(sortiesField.getText());
            double voiture = Double.parseDouble(voitureField.getText());
            double voyage = Double.parseDouble(voyageField.getText());
            double impots = Double.parseDouble(impotsField.getText());
            double autres = Double.parseDouble(autresField.getText());

            double total = logement + nourriture + sorties + voiture + voyage + impots + autres;

            ExpenseRecord newRecord = new ExpenseRecord(
                    periode.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    total,
                    logement,
                    nourriture,
                    sorties,
                    voiture,
                    voyage,
                    impots,
                    autres
            );

            mainController.addLine(newRecord);

            Stage stage = (Stage) periodePicker.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Entrée invalide");
            alert.setContentText("Veuillez vérifier les valeurs des champs numériques.");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Date manquante");
            alert.setContentText("Veuillez sélectionner une date valide.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) periodePicker.getScene().getWindow();
        stage.close();
    }
}
