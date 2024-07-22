package com.example.bot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GettingPrivat24 {
    private static final String TEST_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5 ";

    public String readingJsonFile() throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(TEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
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
        else {
            System.out.println("GET request not worked");
        }
        return result.toString();
    }
}