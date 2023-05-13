package ru.krista;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Filer {
    public ArrayList<String> arrayList;

    public void corrected() {
        arrayList = getFromFile(Main.getFilePath());
        editing(arrayList);
    }

    public void printFromFile(Path filePath) {

        System.out.println("Валюты и их значения, добавленные в избранное:".toUpperCase());
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
    }

    public ArrayList<String> getFromFile(Path filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(x -> lines.add(x));
        } catch (Exception e) {
            System.err.println("Ошибка при работе с файлом!!!" + e.getMessage());
        }
        return lines;
    }

    public void editing(ArrayList<String> arrayList) {
        while (true) {
            System.out.println("""
                    Выберите действие:
                    0)Выход
                    1)Удалить запись""");
            int number = Main.enterNumber();

            switch (number) {
                case 0 -> {
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
                            editing(arrayList);
                        }
                    }
                    break;
                }
                case 1 -> {
                    System.out.println("Удаление записи:\nВыберите строку для удаления(0 - отмена):");
                    Main.printArrayList(arrayList);
                    int lineForDelete = Main.enterNumber();
                    if (lineForDelete < 0 || lineForDelete > arrayList.size()) {
                        System.err.println("Пожалуйста введите корректные данные!!!");
                    }
                    if (lineForDelete == 0) {
                        break;
                    } else lineForDelete--;
                    try {
                        arrayList.remove(lineForDelete);
                        Main.printArrayList(arrayList);
                    } catch (Exception e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }

                }
                default -> {
                    System.out.println("Что-то пошло не так!!!");

                }
            }
        }
    }
}
