package com.example.nbp_service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

//Singleton class
public class URICreator {

    private final String NBP_URI = "http://api.nbp.pl/api";
    private Map<String, String> parametersMap = new HashMap<>();
    private static URICreator instance;

    private URICreator() {}

    public static URICreator getInstance() {
        if (instance == null) {
            instance = new URICreator();
        }
        return instance;
    }

    public URI createURI(int endpointCode) {
        URI createdURI = null;

        switch (endpointCode) {
            case 1:
                createdURI = URI.create(NBP_URI + "/cenyzlota ");
                break;
            case 2:

                String days = parametersMap.get("Days");
                createdURI = URI.create(NBP_URI + "/cenyzlota/last/" + days);
                break;
            case 3:
                String currencyCode = parametersMap.get("CurrencyCode");
                days = parametersMap.get("Days");
                createdURI = URI.create(NBP_URI + "/exchangerates/rates/a/" + currencyCode + "/last/" + days);
                break;
            default:
                break;
        }
        clearParametersMap();

        return createdURI;
    }

    public void clearParametersMap() {
        parametersMap.clear();
    }

    public void addDaysParameter(int days) {
        parametersMap.put("Days", String.valueOf(days));
    }

    public void addCurrencyCodeParameter(String currencyCode) {
        parametersMap.put("CurrencyCode", currencyCode);
    }
}