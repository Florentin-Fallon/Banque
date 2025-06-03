package com.example.helloapplication;

import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

public class TableMoneyController {
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML private TableView<ExpenseRecord> tableView;

    @FXML private TableColumn<ExpenseRecord, String> periodeColumn;
    @FXML private TableColumn<ExpenseRecord, Double> totalColumn;
    @FXML private TableColumn<ExpenseRecord, Double> salaireColumn;
    @FXML private TableColumn<ExpenseRecord, Double> aidesColumn;
    @FXML private TableColumn<ExpenseRecord, Double> autoentrepriseColumn;
    @FXML private TableColumn<ExpenseRecord, Double> revenupassifColumn;
    @FXML private TableColumn<ExpenseRecord, Double> autresColumn;

    @FXML public com.example.helloapplication.components.CurrencySwitchButton currencySwitchButton;

    private final DateTimeFormatter dbMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final DateTimeFormatter displayMonthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

    private double currentExchangeRate = 1.0;
    private String currentCurrency = "EUR";
    private Animation ExchangeRateFetcher;


}
