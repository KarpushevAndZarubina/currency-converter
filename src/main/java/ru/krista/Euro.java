package ru.krista;

import org.jsoup.Jsoup;

public class Euro {

    private static double euroValue;

    public static double getEuroValue() {
        return euroValue;
    }

    public static void parseEuroValue() {
        double dblValueEuro = 0;
        try {
            var document = Jsoup.connect("https://yandex.ru/search/?text=курс+евро+на+сегодня&clid=2270455&banerid=0758004987%3A63bad38ea09d0a8cf9cccbe0&win=575&lr=10839").get();
            var selector = document.selectFirst("#a11y-search-result-converter > div.Converter-Inputs > div:nth-child(3) > span.Textinput.ConverterTextinput > input");
            dblValueEuro = Double.valueOf(selector.attr("value").replace(",","."));
            System.out.println("1Euro = " + dblValueEuro + "руб");
        } catch (Exception e) {
            System.err.println("Произошла ошибка." +
                    "\nНазвание ошибки: " + e.getMessage());
        }
        euroValue = dblValueEuro;
    }
}
