package com.telegramBot.bank.Privat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

public class Privatbank {

    private String baseCurrency;
    private String currency;
    private String purchaseRate;
    private String saleRate;


    public void Privatbank(String baseCurrency, String currency, String purchaseRate, String saleRate){
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.purchaseRate = purchaseRate;
        this.saleRate = saleRate;

    }

    public String get_base_currency() {
        return baseCurrency;
    }
    public String get_currency() {
        return currency;
    }
    public String get_purchase() {
        return purchaseRate;
    }

}


class GetCurrencyPrivatBank {

    public static void main(String[] args) throws IOException {

        Privatbank result = GetExchangePrivateBank("USD");

    }
    
        public static double getCurrencySell(BankEnum currency) throws IOException {

        double CurrencySell = Double.parseDouble(String.valueOf(GetExchangePrivateBank(currency).get_saleRate()));        
        
        System.out.println("CurrencySell: " + CurrencySell);
        return CurrencySell;
    }

    public static double getCurrencyBuy (BankEnum currency) throws IOException {
        double CurrencyBuy = Double.parseDouble(String.valueOf(GetExchangePrivateBank(currency).get_purchase()));

        System.out.println("CurrencyBuy: " + CurrencyBuy);
        return CurrencyBuy;
        
    }

    public static Privatbank GetExchangePrivateBank(String currency) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String URL_get_currency = "https://api.privatbank.ua/p24api/exchange_rates?json&date=" + dateFormat.format(new Date() );
//       String URL_get_currency = "https://api.privatbank.ua/p24api/exchange_rates?json&date=21.05.2023" ;

        Privatbank result = new Privatbank();

        URL url = new URL(URL_get_currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gsonR = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gsonR.fromJson(response.toString(), JsonObject.class);
            JsonArray exchangeRateArray = jsonObject.getAsJsonArray("exchangeRate");

            Privatbank[] currencyList = gsonR.fromJson(exchangeRateArray, Privatbank[].class);


            for (Privatbank currencyNeeded : currencyList) {
                if(currency.equals(currencyNeeded.get_currency())){
                    result = currencyNeeded ;
                }
            }
            int a = 5;

        } else {
            System.out.println("GetExchangePrivateBank request not worked");
        }
        return result;
    }
}
