package com.example.nbp_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NBPController {
    NBPService nbpService = new NBPService();

    @GetMapping(value = "/api/gold-price/today")
    public String getTodayGoldPrice() {
        OutputParameters outputParameters = new OutputParameters("Today's gold price: ");
        return outputParameters.getMessage() + nbpService.getTodayGoldPrice() + " PLN";
    }

    @GetMapping(value = "api/gold-price/average/last/{days}")
    public String getAvgGoldPrice(@PathVariable int days) {
        OutputParameters outputParameters = new OutputParameters("Average gold price from last ");
        outputParameters.setDays(days);

        try {
            if (days > 255) throw  new IndexOutOfBoundsException("Over");
            else if (days <= 0) throw new IndexOutOfBoundsException("Below");

            return outputParameters.getMessage() +
                    outputParameters.getDays() + " business days: " +
                    nbpService.getAvgGoldPrice(outputParameters.getDays()) + " PLN";
        } catch (IndexOutOfBoundsException e) {
            handleDaysOutOfBoundsException(days, e, outputParameters);
        }

        if (outputParameters.getDays() == 0) return getTodayGoldPrice();

        return outputParameters.getMessage() +
                outputParameters.getDays() + " business days: " +
                nbpService.getAvgGoldPrice(outputParameters.getDays()) + " PLN";
    }

    @GetMapping(value = "api/gold-price/average")
    public String getAvgGoldPrice() {
        return getAvgGoldPrice(14);
    }

    @GetMapping(value = "api/exchange-rates/{currencyCode}/{days}")
    public String getExchangeRates(@PathVariable String currencyCode, @PathVariable int days) {
        OutputParameters outputParameters = new OutputParameters("Exchange rates for PLN to ");
        outputParameters.setDays(days);
        outputParameters.setCurrency(currencyCode);

        try {
            if (days > 255) throw  new IndexOutOfBoundsException("Over");
            else if (days <= 0) throw new IndexOutOfBoundsException("Below");

            return outputParameters.getMessage() +
                    outputParameters.getCurrency() + " from last " + outputParameters.getDays() + " business days: \n" +
                    nbpService.getExchangeRates(outputParameters.getCurrency(), outputParameters.getDays());
        } catch (IndexOutOfBoundsException e) {
            handleDaysOutOfBoundsException(days, e, outputParameters);
        }

        return outputParameters.getMessage() +
                outputParameters.getCurrency() + " from last " + outputParameters.getDays() + " business days: \n" +
                nbpService.getExchangeRates(outputParameters.getCurrency(), outputParameters.getDays());
    }

    @GetMapping(value = "api/exchange-rates/{currencyCode}")
    public String getExchangeRates(@PathVariable String currencyCode) {
        return getExchangeRates(currencyCode, 5);
    }

    private void handleDaysOutOfBoundsException(int days, IndexOutOfBoundsException e, OutputParameters outputParameters) {

        outputParameters.appendMessage("ERROR\n");
        outputParameters.appendMessage("Amount of days must be within 0 and 255\n");
        if (e.getMessage().equals("Over")) {
            outputParameters.setDays(255);
        } else {
            days = days*-1;
            if (days == 0) {
                outputParameters.setDays(0);
            } else if (days < 255) {
                outputParameters.setDays(days);
            } else { //days > 255
                outputParameters.setDays(255);
            }
        }
    }
}