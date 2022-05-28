package com.example.nbp_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

@Service
public class NBPService {
    private final URICreator uriCreator;
    private static final DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public NBPService() {
        uriCreator = URICreator.getInstance();
    }

    public String getResponseString(URI uri) {
        String responseBody;

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return responseBody;
    }

    public Double getTodayGoldPrice() {
        Double goldPrice = 0d;
        String responseBody;
        JsonArray responseArray;

        URI uri = uriCreator.createURI(1);

        responseBody = getResponseString(uri);
        responseArray = JsonParser.parseString(responseBody).getAsJsonArray();

        for (JsonElement element : responseArray) {
            JsonObject object = element.getAsJsonObject();
            goldPrice = object.get("cena").getAsDouble();
        }

        return goldPrice;
    }

    public Double getAvgGoldPrice(int days) {
        Double avgGoldPrice = 0d;
        String responseBody;
        JsonArray responseArray;

        uriCreator.addDaysParameter(days);

        URI uri = uriCreator.createURI(2);

        responseBody = getResponseString(uri);
        responseArray = JsonParser.parseString(responseBody).getAsJsonArray();

        for (JsonElement element : responseArray) {
            JsonObject object = element.getAsJsonObject();
            avgGoldPrice += object.get("cena").getAsDouble();
        }

        avgGoldPrice /= days;

        return Double.valueOf(df.format(avgGoldPrice));
    }

    public String getExchangeRates(String currencyCode, int days) {
        SortedMap<LocalDate, Double> exchangeRates = new TreeMap<>();
        StringBuilder returnExchangeRates = new StringBuilder();
        String responseBody;
        JsonArray responseArray;
        JsonObject jsonObject;

        uriCreator.addCurrencyCodeParameter(currencyCode);
        uriCreator.addDaysParameter(days);

        URI uri = uriCreator.createURI(3);

        responseBody = getResponseString(uri);
        jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        responseArray = jsonObject.get("rates").getAsJsonArray();

        for (JsonElement element : responseArray) {
            JsonObject object = element.getAsJsonObject();

            LocalDate date = LocalDate.parse(object.get("effectiveDate").getAsString());
            Double exchangeRate = object.get("mid").getAsDouble();

            exchangeRates.put(date, exchangeRate);
        }

        for (var entry : exchangeRates.entrySet()) {
            returnExchangeRates.append("(").append(entry.getKey().getDayOfWeek().toString().toLowerCase(Locale.ROOT))
                    .append(") ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return returnExchangeRates.toString();
    }
}
