package ru.krista;

import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


//TODO: Так делать нельзя, теряется весь смысл в конкретном случае. Не бойтесь создавать дополнительные классы и файлы
//public class Main implements CurrencyExchange

public class Main {
    
    //TODO: в Main классе этого быть не должно. Но в соответствии с другими комментариями отсюда это пропадёт
    public static ArrayList<String> arrayList;

    //TODO: Про это уже обсудили, см переписку
    public static Path getFilePath() {
        return filePath;
    }

    private static String path = "src/main/resources/favorites.txt";
    private static Path filePath = Paths.get(path);

    //TODO: Почему? Зачем?
    public static int value;

    public static void main(String[] args) {
        Main main = new Main(); // TODO: соответственно и это не надо делать
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
                    //TODO: Логику парсинга и преобразования строки тоже вынести в отдельный метод\класс
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
                        //TODO: Используем интерфейс Map в качестве типа объекта, а не частную реализацию
                        HashMap<Currency, Double> currenciesHashMap = main.getCurrencyRates(base, Arrays.stream(str).map(Currency::valueOf).toArray(Currency[]::new));//с помощью потоков преобразуем стринговые выражение в Currency, потому что их может быть несколько
                        //TODO: Тоже вынести куда-нибудь отдельно в метод\класс
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
                    break;//TODO: лишнее, даже идея подсказывает, что можно удалить
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
                            break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                        }
                        case 2 -> {
                            System.out.println("Выход в главное меню".toUpperCase());
                            break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                        }
                        default -> {
                            System.out.println("Некорректный ввод!!!");
                            break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                        }
                    }
                    break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                }
                default -> {
                    System.out.println("Некорректный ввод!!!");
                    break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                }
            }
        }
    }

    //TODO: Соответственно это вынести в реализацию интерфейса CurrencyExchange. Класс, реализующий этот интерфейс можно
    //      назвать, к примеру, CurrencyExchangeJsoup
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
//        TODO: так нельзя делать. Можно использовать условный оператор без лишнего выкидывания Исключения,
//              которое лишь усложняет код
//        try {
//            int num = enter.nextInt();
//            return num;
//        } catch (Exception e) {
//            System.err.println("Ошибка: " + e.getMessage() + "\nПопробуйте ввести ещё раз!!!");
//            return enterNumber();
//        }
    }

    //TODO: В чём принципиальная разница и отличие от метода enterNumber?
    public static int enterNumberForDelete() {
        var enter = new Scanner(System.in);
//        TODO: так нельзя делать. Можно использовать условный оператор без лишнего выкидывания Исключения,
//              которое лишь усложняет код
//        try {
//            Exception e = new Exception();
//            int num = enter.nextInt();
//            if (num < 0 || num > arrayList.size()) throw e;
//            return (num);
//        } catch (Exception e) {
//            System.err.println("Ошибка: " + e.getMessage() + "\nПопробуйте ввести ещё раз!!!");
//            return enterNumberForDelete();
//        }
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
//        TODO: так нельзя делать. Можно использовать условный оператор без лишнего выкидывания Исключения,
//              которое лишь усложняет код
//        try {
//            Exception e = new Exception();
//            String str = enter.nextLine().trim().toUpperCase();
//            if (str.equals("0")) {
//                return "0";
//            } else if (str.split(" ").length < 4) {
//                throw e;
//            }
//            return str;
//        } catch (Exception e) {
//            System.err.println("Ошибка при вводе данных!!!" + e.getMessage() + "\n");
//            System.out.println();//Для вывода последующего sout, иначе из-за serr не выведется
//            return enterOfString();
//        }
    }


    //TODO: Всю работу с файлами вынести в отдельную сущность. Не надо городить статические методы
    public static void printFromFile(Path filePath) {

        System.out.println("Валюты и их значения, добавленные в избранное:".toUpperCase());
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
    }

    //TODO: Всю работу с файлами вынести в отдельную сущность. Не надо городить статические методы
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

    //TODO: Всю работу с файлами вынести в отдельную сущность. Не надо городить статические методы
    public static void editing(ArrayList<String> arrayList) {
        while (true) {
            System.out.println("""
                    Выберите действие:
                    0)Выход
                    1)Удалить запись""");
            int number = enterNumber();

            //TODO: почему для выбора действия сначала используется условный оператор if, а потом для 1 пункта
            //      используется switch. Определиться и выбрать что-то одно
            if (number == 0) {
                System.out.println("Завершение работы редактирования...".toUpperCase()); //TODO: зачем toUpperCase?
                System.out.println("""
                        Желаете сохранить изменения?
                        1)Да
                        2)Нет""");
                int action = enterNumber();
                switch (action) {
                    case 1 -> {
                        System.out.println("Сохранение файла...".toUpperCase()); //TODO: зачем toUpperCase?
                        try {
                            FileWriter fw = new FileWriter(path, false);
                            for (var x : arrayList) {
                                fw.write(x + "\n");
                            }
                            fw.close();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                        break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                    }
                    case 2 -> {
                        break; //TODO: лишнее, даже идея подсказывает, что можно удалить
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
                    break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                }
                default -> {
                    System.out.println("Что-то пошло не так!!!");
                    break; //TODO: лишнее, даже идея подсказывает, что можно удалить
                }
            }
        }
    }


}
