package com.example.helloapplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class GraphController {

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private ChoiceBox<String> monthChoiceBox;

    private final DateTimeFormatter dbMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("dd/MM");
    private final DateTimeFormatter dbDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private void initialize() {
        System.out.println("Initialize called");
        System.out.println("monthChoiceBox = " + monthChoiceBox);
        if (monthChoiceBox == null) {
            System.err.println("ERROR : monthChoiceBox est null !");
            return;
        }

        initMonthChoiceBox();

        System.out.println("monthChoiceBox.getItems() = " + monthChoiceBox.getItems());

        monthChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chargerDonneesPourMois(newVal);
            }
        });

        if (!monthChoiceBox.getItems().isEmpty()) {
            monthChoiceBox.setValue(monthChoiceBox.getItems().get(0));
        }
    }

    private void initMonthChoiceBox() {
        List<String> last12Months = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 12; i++) {
            LocalDate month = now.minusMonths(i);
            last12Months.add(month.format(dbMonthFormatter));
        }
        monthChoiceBox.getItems().setAll(last12Months);
    }

    private void chargerDonneesPourMois(String yearMonth) {
        if (yearMonth == null || yearMonth.isEmpty()) {
            System.err.println("Invalid yearMonth parameter in chargerDonneesPourMois");
            return;
        }
        chargerDonneesGraphique(yearMonth);
        chargerDonneesLineChart(yearMonth);
    }

    private void chargerDonneesGraphique(String yearMonth) {
        String query = """
            SELECT 
                COALESCE(SUM(housing),0) AS logement,
                COALESCE(SUM(food),0) AS nourriture,
                COALESCE(SUM(goingOut),0) AS sorties,
                COALESCE(SUM(transportation),0) AS transport,
                COALESCE(SUM(travel),0) AS voyage,
                COALESCE(SUM(tax),0) AS impots,
                COALESCE(SUM(other),0) AS autres
            FROM expense
            WHERE strftime('%Y-%m', date) = ?
        """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, yearMonth);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pieChart.getData().clear();
                    boolean hasData = false;

                    PieChart.Data logement = new PieChart.Data("Logement", rs.getDouble("logement"));
                    PieChart.Data nourriture = new PieChart.Data("Nourriture", rs.getDouble("nourriture"));
                    PieChart.Data sorties = new PieChart.Data("Sorties", rs.getDouble("sorties"));
                    PieChart.Data transport = new PieChart.Data("Transport", rs.getDouble("transport"));
                    PieChart.Data voyage = new PieChart.Data("Voyage", rs.getDouble("voyage"));
                    PieChart.Data impots = new PieChart.Data("Impôts", rs.getDouble("impots"));
                    PieChart.Data autres = new PieChart.Data("Autres", rs.getDouble("autres"));

                    for (PieChart.Data d : List.of(logement, nourriture, sorties, transport, voyage, impots, autres)) {
                        if (d.getPieValue() > 0) {
                            pieChart.getData().add(d);
                            hasData = true;
                        }
                    }
                    pieChart.setTitle(hasData ? "Répartition des dépenses pour " + yearMonth : "Aucune dépense pour ce mois");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerDonneesLineChart(String yearMonth) {
        String query = """
            SELECT date,
                   SUM(housing + food + goingOut + transportation + travel + tax + other) AS total_journalier
            FROM expense
            WHERE strftime('%Y-%m', date) = ?
            GROUP BY date
            ORDER BY date
        """;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Dépenses journalières");

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, yearMonth);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String dateStr = rs.getString("date");
                    if (dateStr == null) continue;

                    LocalDate date = LocalDate.parse(dateStr, dbDateFormatter);
                    String formattedDate = date.format(displayDateFormatter);
                    Number total = rs.getDouble("total_journalier");

                    XYChart.Data<String, Number> data = new XYChart.Data<>(formattedDate, total);
                    series.getData().add(data);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        lineChart.getData().clear();
        if (!series.getData().isEmpty()) {
            lineChart.getData().add(series);

            // Création des tooltips
            lineChart.applyCss();
            lineChart.layout();

            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + " : " + data.getYValue() + " €"));
                }
            }

            lineChart.setTitle("Dépenses journalières pour " + yearMonth);
        } else {
            lineChart.setTitle("Aucune dépense journalière pour ce mois");
        }
    }

    @FXML
    private void goToTableau(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Vue Tableau");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToGraphique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graph-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Vue Graphique");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuitter() {
        Platform.exit();
    }
}
