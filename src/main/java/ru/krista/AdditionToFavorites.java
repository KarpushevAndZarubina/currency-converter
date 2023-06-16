package ru.krista;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

public class AdditionToFavorites {

    public void add(Map<Currency, Double> currenciesHashMap, Parser parser) {
        try {
            System.out.println("Желаете добавить в избранное?\n" + "1)Да\n" + "2)Нет\n");
            int enterAction = Main.enterNumber();
            if (enterAction == 1) {
                FileWriter fw = new FileWriter(Main.getFilePath().toString(), true);
                for (Map.Entry<Currency, Double> entry : currenciesHashMap.entrySet()) {
                    fw.write(parser.value + " " + parser.base.name() + " TO " + entry.getKey() + " = " + entry.getValue() + "\n");
                }
                fw.close();
                System.out.println("Данные успешно добавлены".toUpperCase());
            }
        } catch (Exception e) {
            System.err.println("Ошибка!!!" + e.getMessage());
        }
    }
}