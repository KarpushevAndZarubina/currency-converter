package ru.krista;

import java.io.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/resources/favorites.txt";
        System.out.println("Курс валют:");
        Dollar.parseDollarValue();
        Euro.parseEuroValue();

        String ValueDollarForCalculate = "" + Dollar.getDollarsValue();
        String ValueEuroForCalculate = "" + Euro.getEuroValue();

        Scanner scanner = new Scanner(System.in);

        Byte number;

        double Sum = 0.0;

        double Result;

        while (true) {
            Result = 0;

            System.out.println("""
                                    
                    choose:
                        0)finish the program
                    	1)convert rubles in dollars
                    	2)convert dollars in rubles
                    	3)convert rub in euro
                    	4)convert euro in rubles
                    	5)open the list of favorite operations""");

            try {
                number = scanner.nextByte();
            } catch (Exception e) {
                System.out.println("Mistake!!! The requested data was probably entered!!! number assigned the value 0");
                number = 0;
            }
            if (number == 0) {
                break;
            }

            if (number != 5) {
                System.out.println("\nEnter the amount to transfer( 0 - finish the program): ");
                try {
                    Sum = scanner.nextDouble();
                } catch (Exception e) {
                    System.out.println("Mistake!!! The requested data was probably entered!!! Sum assigned the value 0");
                    Sum = 0;
                }

                if (Sum == 0) {
                    break;
                }
            }
            String forFavorite = "";

            switch (number) {
                case (1):
                    Result = Sum / Double.parseDouble(ValueDollarForCalculate);
                    forFavorite = number + "rub = " + Result + "$";

                    break;
                case (2):
                    Result = Double.parseDouble(ValueDollarForCalculate) * Sum;
                    forFavorite = number + "$ = " + Result + "rub";
                    break;
                case (3):
                    Result = Sum / Double.parseDouble(ValueEuroForCalculate);
                    forFavorite = number + "rub = " + Result + "euro";
                    break;
                case (4):
                    Result = Double.parseDouble(ValueEuroForCalculate) * Sum;
                    forFavorite = number + "euro = " + Result + "rub";
                    break;
                case (5):
                    System.out.println("Избранное:");
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(filePath));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    System.out.println("Mistake!!! It was necessary to enter a number from 1 to 4!!!");
                    break;
            }

            if (number != 5) {
                if (Result != 0) {
                    System.out.println("Currency conversion was successful!!! Result = " + Result);
                    System.out.println("would you like to add the operation to your favorites?(yes?)");
                    String answer = scanner.next().trim();
                    if (answer.equalsIgnoreCase("yes")) {
                        try {

                            FileWriter writer = new FileWriter(filePath, true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);
                            bufferWriter.newLine();
                            bufferWriter.write(forFavorite);
                            bufferWriter.close();
                            System.out.println("The addition was successful!");
                            System.out.println("The expression that was added: " + forFavorite);
                        } catch (IOException e) {
                            System.out.println("Ошибка при записи в файл: " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("The conversion did not happen!!!");
                }
            }
        }

    }


}

