package com.example.helloapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;
import java.sql.*;

public class HelloController {

    @FXML private TableView<HelloController> tableView; // Lien avec la TableView dans FXML
    private final ObservableList<HelloController> data = FXCollections.observableArrayList();

    // Attributs de chaque ligne
    private LocalDate periode;  // Change ici de String à LocalDate
    private int total;
    private int logement;
    private int nourriture;
    private int sorties;
    private int voiture;
    private int voyage;
    private int impots;
    private int autres;

    // Constructeur
    public HelloController() {}

    public HelloController(LocalDate periode, int total, int logement, int nourriture, int sorties, int voiture, int voyage, int impots, int autres) {
        this.periode = periode;  // Utilise LocalDate ici
        this.total = total;
        this.logement = logement;
        this.nourriture = nourriture;
        this.sorties = sorties;
        this.voiture = voiture;
        this.voyage = voyage;
        this.impots = impots;
        this.autres = autres;
    }

    // Getter et Setters
    public LocalDate getPeriode() { return periode; }  // Change ici aussi pour retourner LocalDate
    public int getTotal() { return total; }
    public int getLogement() { return logement; }
    public int getNourriture() { return nourriture; }
    public int getSorties() { return sorties; }
    public int getVoiture() { return voiture; }
    public int getVoyage() { return voyage; }
    public int getImpots() { return impots; }
    public int getAutres() { return autres; }

    // Méthode pour ajouter la nouvelle ligne à la liste observable
    public void addLine(HelloController newLine) {
        data.add(newLine);  // Ajouter la nouvelle ligne
        tableView.setItems(data);  // Mettre à jour la TableView

        // Ajouter aussi cette ligne dans la base de données SQLite
        boolean success = Database.insertExpense(newLine.getPeriode().toString(), newLine.getLogement(), newLine.getNourriture(),
                newLine.getSorties(), newLine.getVoiture(), newLine.getVoyage(),
                newLine.getImpots(), newLine.getAutres());
        if (!success) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout dans la base de données.");
            alert.showAndWait();
        }
    }

    // Méthode pour récupérer les données de la base de données et les ajouter à la TableView
    public void loadDataFromDatabase() {
        String query = "SELECT * FROM expense";
        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Extraire les données de chaque ligne de la base
                String periodeString = resultSet.getString("date");

                // Convertir la chaîne en LocalDate
                LocalDate periode = LocalDate.parse(periodeString);

                int logement = resultSet.getInt("housing");
                int nourriture = resultSet.getInt("food");
                int sorties = resultSet.getInt("goingOut");
                int voiture = resultSet.getInt("transportation");
                int voyage = resultSet.getInt("travel");
                int impots = resultSet.getInt("tax");
                int autres = resultSet.getInt("other");

                // Calculer le total
                int total = logement + nourriture + sorties + voiture + voyage + impots + autres;

                // Créer une nouvelle ligne avec un LocalDate pour la période
                HelloController line = new HelloController(periode, total, logement, nourriture, sorties, voiture, voyage, impots, autres);
                data.add(line);  // Ajouter à l'ObservableList
            }

            tableView.setItems(data);  // Mettre à jour la TableView avec les données récupérées

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur de récupération des données.");
            alert.showAndWait();
        }
    }

    // Méthode pour ouvrir le dialogue d'ajout de ligne
    @FXML
    private void openAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
            Parent root = loader.load();

            AddDialogController addDialogController = loader.getController();
            addDialogController.setMainController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une ligne");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();  // Affiche l'erreur si le fichier FXML ne peut pas être chargé
        }
    }

    // Méthode pour initialiser les données lors du démarrage de l'application
    @FXML
    private void initialize() {
        loadDataFromDatabase();  // Charger les données depuis la base de données
    }
}
