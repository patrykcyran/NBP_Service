package com.example.nbp_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NBPController {
    NBPService nbpService = new NBPService();

    @GetMapping(value = "/api/gold-price/today")
    public String getTodayGoldPrice() {
        return "Today's gold price: " + nbpService.getTodayGoldPrice() + " PLN";
    }

    @GetMapping(value = "api/gold-price/average/last/{days}")
    public String getAvgGoldPrice(@PathVariable int days) {
        return "Average gold price from last " + days + " days: " + nbpService.getAvgGoldPrice(days) + " PLN";
    }

    @GetMapping(value = "api/gold-price/average")
    public String getAvgGoldPrice() {
        return getAvgGoldPrice(14);
    }

    @GetMapping(value = "api/exchange-rates/{currencyCode}/{days}")
    public String getExchangeRates(@PathVariable String currencyCode, @PathVariable int days) {

        return "Exchange rates for PLN to " + currencyCode + " from last " + days + " business days: \n" +
                nbpService.getExchangeRates(currencyCode, days);
    }

    @GetMapping(value = "api/exchange-rates/{currencyCode}")
    public String getExchangeRates(@PathVariable String currencyCode) {
        return getExchangeRates(currencyCode, 5);
    }
}