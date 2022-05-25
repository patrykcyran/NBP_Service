package com.example.nbp_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class NBPService {
    private final String  nbpUri = "http://api.nbp.pl/api";
    private static final DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public String getResponseString(URI uri) {
        String responseBody = null;

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

        URI uri = URI.create(nbpUri + "/cenyzlota");

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

        URI uri = URI.create(nbpUri + "/cenyzlota/last/" + days);

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

        URI uri = URI.create(nbpUri + "/exchangerates/rates/a/" + currencyCode + "/last/" + days);

        System.out.println(uri);

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
