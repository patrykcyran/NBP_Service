package com.example.nbp_service;

public class OutputParameters {
    StringBuilder message = new StringBuilder();
    String defaultMessage;
    int days;
    String currency;

    public OutputParameters(String message, int days, String currency) {
        defaultMessage = message;
        this.days = days;
        this.currency = currency;
    }

    public OutputParameters(String message, int days) {
        defaultMessage = message;
        this.days = days;
    }

    public OutputParameters(String message) {
        defaultMessage = message;
    }

    public OutputParameters() {
    }

    public String getMessage() {
        return defaultMessage == null ? message.toString() : message.toString() + defaultMessage;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void appendMessage(String message) {
        this.message.append(message);
    }

    public void prependMessage(String message) {
        this.message.insert(0, message);
    }
}
