package com.example.bot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println ("Повідомлення отримано " +
                update.getMessage().getText());

    }

    @Override
    public String getBotUsername() {
        return "tester210_bot";
    }

   @Override
   public String getBotToken(){
        return "6487675074:AAGjmTeI4SegWMPdQnKHBusVwQWRrKZrZ10";
   }
}
