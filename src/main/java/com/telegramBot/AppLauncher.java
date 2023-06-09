package com.telegramBot;

import com.telegramBot.telegram.CurrencyTelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AppLauncher  {
    public static void main(String[] args) {

            try {
                String username = "";
                String token = "";

                try (BufferedReader reader = new BufferedReader(new FileReader("files/botAPI.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("BOT_NAME")) {
                            username = line.split("=")[1].trim();
                        } else if (line.startsWith("BOT_TOKEN")) {
                            token = line.split("=")[1].trim();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the botAPI.txt file: " + e.getMessage());
                    return;
                }

                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                CurrencyTelegramBot bot = new CurrencyTelegramBot(username, token);
                botsApi.registerBot(bot);

                System.out.println("Bot successfully launched!");
            } catch (TelegramApiException e) {
                System.out.println("An error occurred while launching the bot: " + e.getMessage());
                e.printStackTrace();
        }
  }
}
