package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class rateApp
{
    class Currency {
        private int currencyCodeA;
        private int currencyCodeB;
        private int date;
        private double rateBuy;
        private double rateCross;
        private double rateSell;

        @Override
        public String toString() {
            return "Currency{" +
                    "currencyCodeA=" + currencyCodeA +
                    ", currencyCodeB=" + currencyCodeB +
                    ", date=" + date +
                    ", rateBuy=" + rateBuy +
                    ", rateCross=" + rateCross +
                    ", rateSell=" + rateSell +
                    '}';
        }
    }

    static String JSON = getJSON("https://api.monobank.ua/bank/currency");

    public static void main( String[] args ) throws FileNotFoundException {
        int cur = 1;
        while(cur != 0) {
            System.out.println("Enter code of currency, for example 840 (for USD) or 978 (for EUR) etc. or 0 for exit :");
            var sc = new Scanner(System.in);
            cur = sc.nextInt();
            printRate(cur);
        }

    }

    public static String getJSON(String spec) {
        String json = "";
        try {
            URL url = new URL(spec);
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            json = br.readLine();

        } catch (IOException e) {
            System.out.println(spec + " is non-existent link");
        }
        System.out.println(json);
        return json;
    }

    public static void printRate (int cur) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Currency[] allRates = gson.fromJson(JSON, Currency[].class);
        for (Currency rate: allRates) {
            if(cur == rate.currencyCodeA && rate.currencyCodeB == 980) {
                String letterCode = defineCurrencyLetterCode(cur);
                Field[] fields = rate.getClass().getDeclaredFields();
                System.out.println("Exchange rate UAH for sell " + letterCode + " = " + rate.rateBuy
                        + System.lineSeparator()
                        + "Exchange rate UAH for buy " + letterCode + " = " + rate.rateSell);
            }
        }
    }

    public static String defineCurrencyLetterCode (int code) throws FileNotFoundException {
        String letterCode = "";
        File file = new File("code.csv");
        Scanner sc = new Scanner(file);
        for (; sc.hasNextLine();) {
            String[] dataFile = sc.nextLine().split(";");
            if(dataFile[0].equals(Integer.toString(code))){
                letterCode = dataFile[1];
            }
        }
        return letterCode;
    }

}


