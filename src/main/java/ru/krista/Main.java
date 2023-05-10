package ru.krista;

import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


public class Main implements CurrencyExchange {
    public static ArrayList<String> arrayList;

    public static Path getFilePath() {
        return filePath;
    }

    private static String path = "src/main/resources/favorites.txt";
    private static Path filePath = Paths.get(path);
    public static int value;

    public static void main(String[] args) {
        Main main = new Main();
        while (true) {
            System.out.println("""
                                        
                    ГЛАВНОЕ МЕНЮ
                    Выберите вариант, указав цифру:
                        0) Выход из программы
                        1) Конвертация валют
                        2) История запросов и значений конвертации""");
            int number = enterNumber();
            if (number == 0) {
                System.out.println("Завершение работы приложения".toUpperCase());
                break;
            }
            switch (number) {
                case 1 -> {
                    String convertation = enterOfString();

                    if (convertation.equals("0")) break;

                    String[] str = convertation.split(" ");


                    Currency base = Currency.valueOf(str[1]);//устанавливаем базовую валюту
                    value = Integer.valueOf(str[0]);//устанавливаем значение базовой валюты, которое хотим посчитать в других валютах
                    System.out.println();

                    for (int i = 0; i < 3; i++) {//удаляем число, rub, to
                        convertation = convertation.replace(str[i], "");
                    }

                    convertation = convertation.trim();//удаляем лишние пробелы в начале и в конце
                    str = convertation.split(" ");

                    try {
                        HashMap<Currency, Double> currenciesHashMap = main.getCurrencyRates(base, Arrays.stream(str).map(Currency::valueOf).toArray(Currency[]::new));//с помощью потоков преобразуем стринговые выражение в Currency, потому что их может быть несколько
                        System.out.println("Желаете добавить в избранное?\n" + "1)Да\n" + "2)Нет\n");
                        int enterAction = enterNumber();
                        if (enterAction == 1) {
                            FileWriter fw = new FileWriter(path, true);
                            for (Map.Entry<Currency, Double> entry : currenciesHashMap.entrySet()) {
                                fw.write(value + " " + base.name() + " TO " + entry.getKey() + " = " + entry.getValue() + "\n");
                            }
                            fw.close();
                            System.out.println("Данные успешно добавлены".toUpperCase());
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка!!!" + e.getMessage());
                    }
                    break;
                }
                case 2 -> {

                    printFromFile(getFilePath());

                    System.out.println("""
                            Желаете редактировать избранные валюты?
                            1)Да
                            2)Нет""");
                    int enterAction = enterNumber();
                    switch (enterAction) {
                        case 1 -> {
                            arrayList = getFromFile(getFilePath());
                            editing(arrayList);
                            break;
                        }
                        case 2 -> {
                            System.out.println("Выход в главное меню".toUpperCase());
                            break;
                        }
                        default -> {
                            System.out.println("Некорректный ввод!!!");
                            break;
                        }
                    }
                    break;
                }
                default -> {
                    System.out.println("Некорректный ввод!!!");
                    break;
                }
            }
        }
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

    public static int enterNumber() {
        var enter = new Scanner(System.in);
        try {
            int num = enter.nextInt();
            return num;
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage() + "\nПопробуйте ввести ещё раз!!!");
            return enterNumber();
        }
    }

    public static int enterNumberForDelete() {
        var enter = new Scanner(System.in);
        try {
            Exception e = new Exception();
            int num = enter.nextInt();
            if (num < 0 || num > arrayList.size()) throw e;
            return (num);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage() + "\nПопробуйте ввести ещё раз!!!");
            return enterNumberForDelete();
        }
    }

    public static String enterOfString() {
        var enter = new Scanner(System.in);
        System.out.print("""
                Введите выражения в правильной последовательности через пробел:
                    1)Указываем сумму
                    2)Указываем код базовой валюты(из которой будет производиться перевод), а после пишем \"to\"
                    3)Указываем код(коды через пробел) валюты(валют), в которую(которые) будет переводиться базовая валюта
                Пример: 300 rub to usd eur
                Введите своё выражение(0 - отмена):""" + " ");
        try {
            Exception e = new Exception();
            String str = enter.nextLine().trim().toUpperCase();
            if (str.equals("0")) {
                return "0";
            } else if (str.split(" ").length < 4) {
                throw e;
            }
            return str;
        } catch (Exception e) {
            System.err.println("Ошибка при вводе данных!!!" + e.getMessage() + "\n");
            System.out.println();//Для вывода последующего sout, иначе из-за serr не выведется
            return enterOfString();
        }
    }

    public static void printFromFile(Path filePath) {

        System.out.println("Валюты и их значения, добавленные в избранное:".toUpperCase());
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
    }

    public static ArrayList<String> getFromFile(Path filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(x -> lines.add(x));
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
        return lines;
    }

    public static void printArrayList(ArrayList<String> arrayList) {
        int i = 1;
        for (var elem : arrayList) {
            System.out.println(i + ")" + elem);
            i++;
        }
    }

    public static void editing(ArrayList<String> arrayList) {
        while (true) {
            System.out.println("""
                    Выберите действие: 
                    0)Выход
                    1)Удалить запись""");
            int number = enterNumber();

            if (number == 0) {
                System.out.println("Завершение работы редактирования...".toUpperCase());
                System.out.println("""
                        Желаете сохранить изменения?
                        1)Да
                        2)Нет""");
                int action = enterNumber();
                switch (action) {
                    case 1 -> {
                        System.out.println("Сохранение файла...".toUpperCase());
                        try {
                            FileWriter fw = new FileWriter(path, false);
                            for (var x : arrayList) {
                                fw.write(x + "\n");
                            }
                            fw.close();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                        break;
                    }
                    case 2 -> {
                        break;
                    }
                    default -> {
                        System.out.println("Некорректный ввод!!!");
                        printArrayList(arrayList);
                        editing(arrayList);
                        break;
                    }
                }
                break;
            }
            switch (number) {
                case 1 -> {
                    System.out.println("Удаление записи:".toUpperCase() + "\nВыберите строку для удаления(0 - отмена):");
                    printArrayList(arrayList);
                    int lineForDelete = enterNumberForDelete();
                    if (lineForDelete == 0) {
                        break;
                    } else lineForDelete--;
                    try {
                        arrayList.remove(lineForDelete);
                        printArrayList(arrayList);
                    } catch (Exception e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }
                    break;
                }
                default -> {
                    System.out.println("Что-то пошло не так!!!");
                    break;
                }
            }
        }
    }


}
