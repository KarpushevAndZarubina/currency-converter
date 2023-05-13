package ru.krista;

public class Parser {
    public int value;
    public Currency base ;

    public String[] str;

    public void parseOfCurrencies() {
        String convertation = Main.enterOfString();

        if (convertation.equals("0")){

        }
        else {
            str = convertation.split(" ");


            this.base = Currency.valueOf(str[1]);//устанавливаем базовую валюту
            this.value = Integer.valueOf(str[0]);//устанавливаем значение базовой валюты, которое хотим посчитать в других валютах
            System.out.println();

            for (int i = 0; i < 3; i++) {//удаляем число, rub, to
                convertation = convertation.replace(str[i], "");
            }

            convertation = convertation.trim();//удаляем лишние пробелы в начале и в конце
            str = convertation.split(" ");
        }
    }
}
