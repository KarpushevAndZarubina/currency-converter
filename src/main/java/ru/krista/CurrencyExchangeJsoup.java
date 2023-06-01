package ru.krista;

import org.jsoup.Jsoup;

import java.util.HashMap;

public class CurrencyExchangeJsoup implements CurrencyExchange{
    private double value = 0;
    CurrencyExchangeJsoup(double value){
        this.value = value;
    }

    public HashMap<Currency, Double> getCurrencyRates(Currency base, Currency... symbols) {
        HashMap<Currency, Double> currencyRelations = new HashMap<>();
        {
            for (Currency symbol : symbols) {
                try {

                    System.out.println("Выполнение ковертации(" + base.name() + " TO " + symbol.name() + ")...");
                    double dblValue;
                    var document = Jsoup.connect("https://yandex.ru/search/?text=" + base.name() + "+to+" + symbol.name()).get();
                    var selector = document.selectFirst("#a11y-search-result-converter > div.Converter-Inputs > div:nth-child(3) > span.Textinput.ConverterTextinput > input");
                    dblValue = Double.valueOf(selector.attr("value").replace(",", "."));
                    dblValue = Math.round(value * dblValue * 100.0) / 100.0;
                    System.out.println(value + "\"" + base.name() + "\"" + " = " + dblValue + "\"" + symbol.name() + "\"");
                    currencyRelations.put(symbol, dblValue);
                } catch (Exception e) {
                    System.err.println("Произошла ошибка." + "\nНазвание ошибки: " + e.getMessage());
                }
            }
        }
        return currencyRelations;
    }
}
