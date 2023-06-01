package ru.krista;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static Path getFilePath() {
        return Paths.get("src/main/resources/favorites.txt");
    }

    public static void main(String[] args) {
        AdditionToFavorites favorites = new AdditionToFavorites();

        var temporary = new Temporary();
        Filer filer = new Filer();
        Parser parser = new Parser();
        WorkOfApplication:
        {
            while (true) {
                System.out.println("""
                                            
                        ГЛАВНОЕ МЕНЮ
                        Выберите вариант, указав цифру:
                            0) Выход из программы
                            1) Конвертация валют
                            2) История запросов и значений конвертации""");
                int number = enterNumber();
                switch (number) {
                    case 0 -> {
                        System.out.println("Завершение работы приложения".toUpperCase());
                        break WorkOfApplication;
                    }
                    case 1 -> {
                        parser.parseOfCurrencies();
                        temporary.setValue(parser.value);
                        CurrencyExchange cej = new CurrencyExchangeJsoup(parser.value);
                        Map<Currency, Double> currenciesHashMap = cej.getCurrencyRates(parser.base, Arrays.stream(parser.str).map(Currency::valueOf).toArray(Currency[]::new));//с помощью потоков преобразуем стринговые выражение в Currency, потому что их может быть несколько
                        favorites.add(currenciesHashMap, parser);

                    }
                    case 2 -> {

                        filer.printFromFile(getFilePath());

                        System.out.println("""
                                Желаете редактировать избранные валюты?
                                1)Да
                                2)Нет""");
                        int enterAction = enterNumber();
                        switch (enterAction) {
                            case 1 -> {
                                filer.corrected();
                            }
                            case 2 -> {
                                System.out.println("Выход в главное меню".toUpperCase());
                            }
                            default -> {
                                System.out.println("Некорректный ввод!!!");
                            }
                        }
                    }
                    default -> {
                        System.out.println("Некорректный ввод!!!");
                    }
                }
            }
        }
    }

    public static int enterNumber() {
        var enter = new Scanner(System.in);
        String num = enter.next();

        if (num.matches("\\d+")) {
            return Integer.parseInt(num);
        } else {
            System.out.println("Ошибка!!! Введите число ещё раз");
            return enterNumber();
        }
    }

    public static String enterOfString() {
        var enter = new Scanner(System.in);
        System.out.print("""
                Введите выражения в правильной последовательности через пробел:
                    1)Указываем сумму
                    2)Указываем код базовой валюты(из которой будет производиться перевод), а после пишем "to"
                    3)Указываем код(коды через пробел) валюты(валют), в которую(которые) будет переводиться базовая валюта
                Пример: 300 rub to usd eur
                Введите своё выражение(0 - отмена):""" + " ");
        String str = enter.nextLine().trim().toUpperCase();
        if (str.equals("0")) {
            return "0";
        } else if (str.split(" ").length < 4) {
            return enterOfString();
        }
        return str;
    }


    public static void printArrayList(ArrayList<String> arrayList) {
        int i = 1;
        for (var elem : arrayList) {
            System.out.println(i + ")" + elem);
            i++;
        }
    }
}
