// CurrencySwitchButton.java
package com.example.helloapplication.components;

import javafx.scene.control.Button;
import java.util.function.BiConsumer;

public class CurrencySwitchButton extends Button {

    private String currentCurrency = "EUR";
    private double exchangeRate = 1.0;
    private BiConsumer<String, Double> onCurrencyChanged;

    public CurrencySwitchButton() {
        setText("Devise : EUR");
        setOnAction(event -> switchCurrency());
    }

    private void switchCurrency() {
        if (currentCurrency.equals("EUR")) {
            currentCurrency = "USD";
            exchangeRate = 1.08;
        } else {
            currentCurrency = "EUR";
            exchangeRate = 1.0;
        }

        setText("Devise : " + currentCurrency);
        if (onCurrencyChanged != null) {
            onCurrencyChanged.accept(currentCurrency, exchangeRate);
        }
    }

    public void setOnCurrencyChanged(BiConsumer<String, Double> listener) {
        this.onCurrencyChanged = listener;
    }
}
