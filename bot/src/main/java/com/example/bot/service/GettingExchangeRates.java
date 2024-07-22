package com.example.bot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GettingExchangeRates {
    private static final String PRIVATBANK_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
    private static final String MONOBANK_URL = "https://api.monobank.ua/bank/currency";
    private static final String NBU_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public String getExchangeRates(String bank) throws IOException {
        String apiUrl = switch (bank) {
            case "/privat24" -> PRIVATBANK_URL;
            case "/mono" -> MONOBANK_URL;
            case "/nbu" -> NBU_URL;
            default -> throw new IllegalArgumentException("Unsupported bank: " + bank);
        };

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("GET request not worked. Response code: " + responseCode);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return parseResponse(bank, response.toString());
    }

    private String parseResponse(String bank, String jsonResponse) {
        StringBuilder result = new StringBuilder();

        switch (bank) {
            case "/privat24" -> {
                JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    String ccy = jsonElement.getAsJsonObject().get("ccy").getAsString();
                    String baseCcy = jsonElement.getAsJsonObject().get("base_ccy").getAsString();
                    String buy = jsonElement.getAsJsonObject().get("buy").getAsString();
                    String sale = jsonElement.getAsJsonObject().get("sale").getAsString();
                    result.append("Currency: ").append(ccy).append("/").append(baseCcy).append("\n")
                            .append("Buy: ").append(buy).append("\n")
                            .append("Sale: ").append(sale).append("\n")
                            .append("-----------\n");
                }
            }
            case "/mono" -> {
                JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    int currencyCodeA = jsonObject.get("currencyCodeA").getAsInt();
                    int currencyCodeB = jsonObject.get("currencyCodeB").getAsInt();
                    if ((currencyCodeA == 840 && currencyCodeB == 980) || (currencyCodeA == 978 && currencyCodeB == 980)) { // USD-UAH or EUR-UAH
                        String ccy = (currencyCodeA == 840) ? "USD" : "EUR";
                        String baseCcy = "UAH";
                        String rateBuy = jsonObject.get("rateBuy").getAsString();
                        String rateSell = jsonObject.get("rateSell").getAsString();
                        result.append("Currency: ").append(ccy).append("/").append(baseCcy).append("\n")
                                .append("Buy: ").append(rateBuy).append("\n")
                                .append("Sale: ").append(rateSell).append("\n")
                                .append("-----------\n");
                    }
                }
            }
            case "/nbu" -> {
                JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String ccy = jsonObject.get("cc").getAsString();
                    if (ccy.equals("USD") || ccy.equals("EUR")) {
                        String rate = jsonObject.get("rate").getAsString();
                        result.append("Currency: ").append(ccy).append("/UAH\n")
                                .append("Buy: ").append(rate).append("\n")
                                .append("Sale: ").append(rate).append("\n")
                                .append("-----------\n");
                    }
                }
            }
        }
        return result.toString();
    }
}
