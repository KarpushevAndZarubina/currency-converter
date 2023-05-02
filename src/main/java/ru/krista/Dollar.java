package ru.krista;

import org.jsoup.Jsoup;

public class Dollar {
    public static double getDollarsValue() {
        return dollarsValue;
    }

    private static double dollarsValue;
    public static void parseDollarValue() {
        double dblValueDollar = 0;
        try {
            var document = Jsoup.connect("https://yandex.ru/search/?text=курс+доллара+на+сегодня&lr=10839&clid=2270455&win=575&src=suggest_B").get();
            var selector = document.selectFirst("#a11y-search-result-converter > div.Converter-Inputs > div:nth-child(3) > span.Textinput.ConverterTextinput > input");
            dblValueDollar = Double.valueOf(selector.attr("value").replace(",","."));
            System.out.println("1$ = " + dblValueDollar + "руб");
        } catch (Exception e) {
            System.err.println("Произошла ошибка." +
                    "\nНазвание ошибки: " + e.getMessage());
        }
        dollarsValue = dblValueDollar;
    }
    }
