package com.example.bot.service;

import com.google.gson.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class MyBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        System.out.println("Get message " + update.getMessage().getText());
        if(update.getMessage().getText().equals("/start")) {
            sendMessage(chatId, "Get message");
        }
        GettingExchangeRates bankRates = new GettingExchangeRates();
        String bank = update.getMessage().getText();
        try {
            String message = bankRates.getExchangeRates(bank);
            sendMessage(chatId, message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "tester210_bot";
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

   @Override
   public String getBotToken(){
        return "6487675074:AAGjmTeI4SegWMPdQnKHBusVwQWRrKZrZ10";
   }
}