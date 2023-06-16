package ru.krista;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

//TODO: Написать комментарии к тому для чего нужен класс и что делает каждый метод
//      Комментарии оформлять как Javadoc: вводишь эти символы над методом или классом /** и нажимаешь Enter
//      Затем заполняешь все поля (Вверху сам комментарий, принимаемые значения, возвращаемое значение)
//      Шаблон как должен +- выглядеть Javadoc у метода getFromFile в этом классе.
public class Filer {
    public ArrayList<String> arrayList;

    public void corrected() {
        arrayList = getFromFile(Main.getFilePath());
        editing();
    }

    public void printFromFile(Path filePath) {

        System.out.println("Валюты и их значения, добавленные в избранное:".toUpperCase());
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
    }

    /**
     * Здесь пишешь что делает метод или для чего он нужен. Кратко, чтобы отразить всю суть
     * <p>
     * Метод с именем getFromFile предназначен для получения строк из файла для последующей работы с коллекцией,
     * а не с файлом напрямую, чтобы иметь возможность не вносить сохранения в случае ошибки
     *
     * @param filePath что за значение мы ожидаем (Например: путь до файла с избранными валютами)
     *                 ожидаем путь к txt файлу, где описаны избранные значения валют, внесённые ранее
     * @return что метод возвращает(Например: список строк полученного файла)
     * метод с именем getFromFile возвращает список строку, которые полученны из файла,
     * который находится по пути filePath и содержит избранные валюты.
     */
    public ArrayList<String> getFromFile(Path filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(x -> lines.add(x));
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
        return lines;
    }

    public void editing() {
        WorkOfEditing:
        {
            while (true) {
                System.out.println("""
                        Выберите действие:
                        0)Выход
                        1)Удалить запись""");
                int number = Main.enterNumber();

                switch (number) {
                    case 0 -> {
                        //TODO: Вынести закомментированный код в отдельный приватный метод
                        //      Снять комментарии комбинация клавиш Ctrl + / после выделения
                        //      необходимого закомментированного кода
                        endingAndSave();
                        break WorkOfEditing;
                    }
                    case 1 -> {
                        //TODO: Вынести закомментированный код в отдельный приватный метод
                        //      Снять комментарии комбинация клавиш Ctrl + / после выделения
                        //      необходимого закомментированного кода
                        deletingItem();

                    }
                    default -> {
                        System.out.println("Что-то пошло не так!!!");
                    }
                }
            }
        }
    }

    private void endingAndSave() {
        System.out.println("Завершение работы редактирования...");
        System.out.println("""
                Желаете сохранить изменения?
                1)Да
                2)Нет""");
        int action = Main.enterNumber();

        switch (action) {
            case 1 -> {
                System.out.println("Сохранение файла..."); //
                try {
                    FileWriter fw = new FileWriter(Main.getFilePath().toString(), false);
                    for (var x : arrayList) {
                        fw.write(x + "\n");
                    }
                    fw.close();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

            }
            case 2 -> {

            }
            default -> {
                System.out.println("Некорректный ввод!!!");
                Main.printArrayList(arrayList);
                editing();
            }
        }
    }

    private void deletingItem() {
        System.out.println("Удаление записи:\nВыберите строку для удаления(0 - отмена):");
        Main.printArrayList(arrayList);
        int lineForDelete = Main.enterNumber();
        if (lineForDelete < 0 || lineForDelete > arrayList.size()) {
            System.err.println("Пожалуйста введите корректные данные!!!");
            return;
        }
        if (lineForDelete != 0) {
            lineForDelete--;
            try {
                arrayList.remove(lineForDelete);
                Main.printArrayList(arrayList);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }


}
