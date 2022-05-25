package com.example.nbp_service;

import java.net.URI;
import java.util.ArrayList;

public class URICreator {
    private static final String nbpUri = "http://api.nbp.pl/api";

    public static URI createURI(int endpointCode, ArrayList<String> parameterList) {
        URI createdURI = null;

        switch (endpointCode) {
            case 1:
                createdURI = URI.create(nbpUri + "/cenyzlota ");
                break;
            case 2:
                String days = parameterList.get(0);
                createdURI = URI.create(nbpUri + "/cenyzlota/last/" + days);
                break;
            case 3:
                String currencyCode = parameterList.get(0);
                days = parameterList.get(1);
                createdURI = URI.create(nbpUri + "/exchangerate/rates/a/" + currencyCode + "/last/" + days);
                break;
            default:
                break;
        }

        return createdURI;
    }
}