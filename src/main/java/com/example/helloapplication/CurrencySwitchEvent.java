package com.example.helloapplication.components;

import javafx.event.Event;
import javafx.event.EventType;

public class CurrencySwitchEvent extends Event {
    public static final EventType<CurrencySwitchEvent> CURRENCY_SWITCH =
            new EventType<>(Event.ANY, "CURRENCY_SWITCH");

    private final String currency;

    public CurrencySwitchEvent(String currency) {
        super(CURRENCY_SWITCH);
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
