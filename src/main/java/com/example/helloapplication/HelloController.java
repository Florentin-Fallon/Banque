package com.example.helloapplication;

import com.example.helloapplication.components.CurrencySwitchButton;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private ChoiceBox<String> monthChoiceBox;
    @FXML private TableView<ExpenseRecord> tableView;

    @FXML private TableColumn<ExpenseRecord, String> periodeColumn;
    @FXML private TableColumn<ExpenseRecord, Double> totalColumn;
    @FXML private TableColumn<ExpenseRecord, Double> logementColumn;
    @FXML private TableColumn<ExpenseRecord, Double> nourritureColumn;
    @FXML private TableColumn<ExpenseRecord, Double> sortiesColumn;
    @FXML private TableColumn<ExpenseRecord, Double> voitureColumn;
    @FXML private TableColumn<ExpenseRecord, Double> voyageColumn;
    @FXML private TableColumn<ExpenseRecord, Double> impotsColumn;
    @FXML private TableColumn<ExpenseRecord, Double> autresColumn;

    @FXML public CurrencySwitchButton currencySwitchButton;

    private final DateTimeFormatter dbMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final DateTimeFormatter displayMonthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

    private double currentExchangeRate = 1.0;
    private String currentCurrency = "EUR";
    private Animation ExchangeRateFetcher;

    @FXML
    public void initialize() {
        periodeColumn.setCellValueFactory(new PropertyValueFactory<>("periode"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAffiche"));
        logementColumn.setCellValueFactory(new PropertyValueFactory<>("logementAffiche"));
        nourritureColumn.setCellValueFactory(new PropertyValueFactory<>("nourritureAffiche"));
        sortiesColumn.setCellValueFactory(new PropertyValueFactory<>("sortiesAffiche"));
        voitureColumn.setCellValueFactory(new PropertyValueFactory<>("voitureAffiche"));
        voyageColumn.setCellValueFactory(new PropertyValueFactory<>("voyageAffiche"));
        impotsColumn.setCellValueFactory(new PropertyValueFactory<>("impotsAffiche"));
        autresColumn.setCellValueFactory(new PropertyValueFactory<>("autresAffiche"));

        initMonthChoiceBox();

        monthChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chargerDonneesPourMois(newVal);
            }
        });

        if (!monthChoiceBox.getItems().isEmpty()) {
            monthChoiceBox.setValue(monthChoiceBox.getItems().get(0));
        }

        currencySwitchButton.setOnCurrencyChanged((nouvelleDevise, nouveauTaux) -> {
            currentCurrency = nouvelleDevise;
            currentExchangeRate = nouveauTaux;
            mettreAJourTableauAvecTaux(nouveauTaux);
        });
    }

    private void initMonthChoiceBox() {
        List<String> last12Months = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 12; i++) {
            LocalDate month = now.minusMonths(i);
            String formatted = month.format(dbMonthFormatter);
            last12Months.add(formatted);
        }

        ObservableList<String> months = FXCollections.observableArrayList(last12Months);
        monthChoiceBox.setItems(months);
    }

    private void chargerDonneesPourMois(String yearMonth) {
        ObservableList<ExpenseRecord> data = FXCollections.observableArrayList();

        String query = """
                SELECT 
                    strftime('%Y-%m', date) AS periode,
                    SUM(housing + food + goingOut + transportation + travel + tax + other) AS total,
                    SUM(housing) AS logement,
                    SUM(food) AS nourriture,
                    SUM(goingOut) AS sorties,
                    SUM(transportation) AS voiture,
                    SUM(travel) AS voyage,
                    SUM(tax) AS impots,
                    SUM(other) AS autres
                FROM expense
                WHERE strftime('%Y-%m', date) = ?
                GROUP BY strftime('%Y-%m', date)
                """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, yearMonth);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String periode = rs.getString("periode");
                    double total = rs.getDouble("total");
                    double logement = rs.getDouble("logement");
                    double nourriture = rs.getDouble("nourriture");
                    double sorties = rs.getDouble("sorties");
                    double voiture = rs.getDouble("voiture");
                    double voyage = rs.getDouble("voyage");
                    double impots = rs.getDouble("impots");
                    double autres = rs.getDouble("autres");

                    ExpenseRecord record = new ExpenseRecord(
                            periode, total, logement, nourriture, sorties,
                            voiture, voyage, impots, autres
                    );
                    data.add(record);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(data);
        mettreAJourTableauAvecTaux(currentExchangeRate);
    }

    private void mettreAJourTableauAvecTaux(double taux) {
        for (ExpenseRecord record : tableView.getItems()) {
            record.setTotalAffiche(record.getTotal() * taux);
            record.setLogementAffiche(record.getLogement() * taux);
            record.setNourritureAffiche(record.getNourriture() * taux);
            record.setSortiesAffiche(record.getSorties() * taux);
            record.setVoitureAffiche(record.getVoiture() * taux);
            record.setVoyageAffiche(record.getVoyage() * taux);
            record.setImpotsAffiche(record.getImpots() * taux);
            record.setAutresAffiche(record.getAutres() * taux);
        }

        String suffixe = currentCurrency.equals("EUR") ? " (€)" : " (" + currentCurrency + ")";
        totalColumn.setText("Total" + suffixe);
        logementColumn.setText("Logement" + suffixe);
        nourritureColumn.setText("Nourriture" + suffixe);
        sortiesColumn.setText("Sorties" + suffixe);
        voitureColumn.setText("Voiture/Transport" + suffixe);
        voyageColumn.setText("Voyage" + suffixe);
        impotsColumn.setText("Impôts" + suffixe);
        autresColumn.setText("Autres" + suffixe);

        tableView.refresh();
    }

    public void addLine(ExpenseRecord newRecord) {
        tableView.getItems().add(newRecord);
    }

    @FXML
    private void openAddDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
            Parent root = loader.load();

            AddDialogController dialogController = loader.getController();
            dialogController.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter une dépense");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (monthChoiceBox.getValue() != null) {
                chargerDonneesPourMois(monthChoiceBox.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
