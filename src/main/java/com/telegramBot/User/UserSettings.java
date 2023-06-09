package com.telegramBot.User;
import com.google.gson.Gson;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSettings {
    private final String settingsFile = "./files/userSettings.json";
    public void updateUserSettings(User user) {
        long chatId = user.getChatId();
        String[] banks = user.getBanks();
        String[] currencies = user.getCurrencies();
        int rounding = user.getRounding();
        String time = user.getTime();

        updateUserSettings(chatId, banks, currencies, rounding, time);
    }

    public void updateUserSettings(long chatId, String[] banks, String[] currencies, int rounding, String time) {
               try {
            List<User> users = getUsers();
            int index = getUserIndexByChatId(users, chatId);
            User user = new User(chatId, banks, currencies, rounding, time);
            if (index != -1) {
                users.set(index, user);
            } else {
                users.add(user);
            }
            saveUserSettings(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserSettingsByChatId(long chatId) {
        try {
            List<User> users = getUsers();
            for (User user : users) {
                if (user.getChatId() == chatId) {
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createDefaultSettings(long chatId) {
        String[] banks = {"Privat"};
        String[] currencies = {"USD"};
        int rounding = 2;
        String time = "10";

        try {
            List<User> users = getUsers();
            int index = getUserIndexByChatId(users, chatId);
            User user = new User(chatId, banks, currencies, rounding, time);
            if (index != -1) {
                users.set(index, user);
            } else {
                users.add(user);
            }
            saveUserSettings(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> getUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File storageFile = new File(settingsFile);
        if (storageFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(storageFile));
            users = new ArrayList<>(Arrays.asList(new Gson().fromJson(reader, User[].class)));
            reader.close();
        }
        return users != null ? users : new ArrayList<>();
    }

    private void saveUserSettings(List<User> users) throws IOException {
        FileWriter writer = new FileWriter(settingsFile);
        new Gson().toJson(users, writer);
        writer.close();
    }

    private int getUserIndexByChatId(List<User> users, long chatId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getChatId() == chatId) {
                return i;
            }
        }
        return -1;
    }
}
