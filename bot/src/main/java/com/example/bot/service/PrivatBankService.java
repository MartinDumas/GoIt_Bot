package com.example.bot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

public class PrivatBankService extends Bank {

    private static final String PRIVATBANK_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    @Override
    public String getExchangeRates() throws IOException {
        String jsonResponse = fetchData(PRIVATBANK_URL);
        return parseResponse(jsonResponse);
    }

    @Override
    protected String parseResponse(String jsonResponse) {
        StringBuilder result = new StringBuilder();
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
        return result.toString();
    }
}