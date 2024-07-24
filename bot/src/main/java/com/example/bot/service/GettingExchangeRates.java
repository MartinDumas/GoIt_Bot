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
    public String getExchangeRates(String bank) throws IOException {
        Bank service;
        switch (bank) {
            case "/privat24" -> service = new PrivatBankService();
            case "/mono" -> service = new MonoBankService();
            case "/nbu" -> service = new NbuService();
            default -> throw new IllegalArgumentException("Unsupported bank: " + bank);
        }
        return service.getExchangeRates();
    }
}
