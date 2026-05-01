/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * gets exchange rates from exchange-rate API. 
 * returns the exchange rate of the requested currency. 
 */
public class ExchangeRateService {

    private final String apiKey = "012a11ee928a3e83ad78dd1d";
    //im aware hardcoding the apikey isnt optimal and could instead be in a config file. 
    //hardcoding it anyways since this is more of a proof of concept than anything

    public double getRate(String targetCurrency) {
        if (targetCurrency.equals("USD")) {
            return 1.0;
        }

        try {
            String urlText = "https://v6.exchangerate-api.com/v6/"
                    + apiKey
                    + "/latest/USD";

            URL url = new URL(urlText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String json = response.toString();

            return extractRate(json, targetCurrency);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1.0;
    }

    private double extractRate(String json, String targetCurrency) {
        String searchText = "\"" + targetCurrency + "\":";
        int startIndex = json.indexOf(searchText);

        if (startIndex == -1) {
            return 1.0;
        }

        startIndex = startIndex + searchText.length();

        int endIndex = json.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }

        String numberText = json.substring(startIndex, endIndex).trim();

        return Double.parseDouble(numberText);
    }
}