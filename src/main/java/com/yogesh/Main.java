package com.yogesh;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
    private static final String EXCHANGE_API_KEY =                                                                    "49472f2ddc0334daf0803d128390be9a";
    private static final String EXCHANGE_API_ENDPOINT = "https://api.exchangeratesapi.io/v1/latest?access_key=";
    private static final List<String> currency = Arrays.asList("ALL", "AFN", "ARS", "AWG", "AUD", "AZN", "BSD", "BBD", "BDT", "BYR", "BZD", "BMD", "BOB", "BAM", "BWP", "BGN", "BRL", "BND", "KHR", "CAD", "KYD", "CLP", "CNY", "COP", "CRC", "HRK", "CUP", "CZK", "DKK", "DOP", "XCD", "EGP", "SVC", "EEK", "EUR", "FKP", "FJD", "GHC", "GIP", "GTQ", "GGP", "GYD", "HNL", "HKD", "HUF", "ISK", "INR", "IDR", "IRR", "IMP", "ILS", "JMD", "JPY", "JEP", "KZT", "KPW", "KRW", "KGS", "LAK", "LVL", "LBP", "LRD", "LTL", "MKD", "MYR", "MUR", "MXN", "MNT", "MZN", "NAD", "NPR", "ANG", "NZD", "NIO", "NGN", "NOK", "OMR", "PKR", "PAB", "PYG", "PEN", "PHP", "PLN", "QAR", "RON", "RUB", "SHP", "SAR", "RSD", "SCR", "SGD", "SBD", "SOS", "ZAR", "LKR", "SEK", "CHF", "SRD", "SYP", "TWD", "THB", "TTD", "TRY", "TRL", "TVD", "UAH", "GBP", "USD", "UYU", "UZS", "VEF", "VND", "YER", "ZWD");
    private static String base;
    private static String target;
    private static double amount;
    private static CurrencyData currencyData;


    public static void main(String[] args) {
        inputData();
    }

    private static void displayDataAndOptions() {
        System.out.println("-------------------");
        System.out.println(amount + " " + base + " = " + (currencyData.getRates().get(target) / currencyData.getRates().get(base)) * amount + " " + target);
        System.out.println(1 + " " + base + " = " + (currencyData.getRates().get(target) / currencyData.getRates().get(base)) + " " + target);
        System.out.println(1 + " " + target + " = " + (currencyData.getRates().get(base) / currencyData.getRates().get(target)) + " " + base);
        System.out.println("-------------------");
        while (true) {
            System.out.println("1. Convert more with current data\n2. Convert more with real-time data\n3. exit");
            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();
            switch (input) {
                case 1 -> {
                    inputData();
                    return;
                }
                case 2 -> {
                    currencyData = null;
                    inputData();
                    return;
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("Select from 1,2 or 3");
            }
        }
    }

    private static void getCurrentRates() {
        if (currencyData == null) {
            System.out.println("Fetching real time currency data...");
        } else {
            System.out.println("Using last time fetched currency data");
            displayDataAndOptions();
            return;
        }
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URI(EXCHANGE_API_ENDPOINT+EXCHANGE_API_KEY).toURL().openConnection();
            connection.connect();

            byte[] byteContent = connection.getInputStream().readAllBytes();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(byteContent, 0, byteContent.length);
            String content = bos.toString(Charset.defaultCharset());
            currencyData = new Gson().fromJson(content, CurrencyData.class);
            displayDataAndOptions();
        } catch (Exception e) {
            System.out.println("Something went wrong, try again...");
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            getCurrentRates();
        }
    }

    private static void inputData() {
        while (true) {
            try {
                System.out.print("Enter base currency(eg. USD): ");
                Scanner baseInput = new Scanner(System.in);
                base = baseInput.next("\\w{3}").toUpperCase();
                if (!currency.contains(base)) throw new Exception("Invalid currency");
                break;
            } catch (Exception e) {
                System.out.println("\n\"" + base + "\" is invalid currency");

            }
        }
        while (true) {
            try {
                System.out.print("Enter target currency(eg. INR): ");
                Scanner targetInput = new Scanner(System.in);
                target = targetInput.next("\\w{3}").toUpperCase();
                if (!currency.contains(target)) throw new Exception("Invalid currency");
                break;
            } catch (Exception e) {
                System.out.println("\n\"" + target + "\" is invalid currency");
            }
        }

        while (true) {
            try {
                System.out.print("Enter amount: ");
                Scanner amountInput = new Scanner(System.in);
                amount = amountInput.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("\n\"" + amount + "\" is invalid amount");
            }
        }
        getCurrentRates();
    }
}

class CurrencyData {
    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean value) {
        this.success = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long value) {
        this.timestamp = value;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String value) {
        this.base = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> value) {
        this.rates = value;
    }
}