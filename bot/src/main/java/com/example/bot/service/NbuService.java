package com.example.bot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class NbuService extends Bank {
    private static final String NBU_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    @Override
    public String getExchangeRates() throws IOException {
        String jsonResponse = fetchData(NBU_URL);
        return parseResponse(jsonResponse);
    }

    @Override
    protected String parseResponse(String jsonResponse) {
        StringBuilder result = new StringBuilder();
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
        return result.toString();
    }
}
