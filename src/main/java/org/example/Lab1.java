package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Lab1 {
    static void main() {
        try (InputStream inputStream =
                     Lab1.class.getResourceAsStream("/lab_1.txt")) {

            if (inputStream == null) {
                System.out.println("Файл не найден");
                return;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            String line;

            ArrayList<String> inf = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                inf.add(line);
            }

            System.out.println("=== Сводка состояния сервера ===\n" +
                    "Состояние:" + inf.get(0) + "\n" +
                    "Игроки: " + inf.get(1) + "\n" +
                    "Свободные места: " + inf.get(2) + "\n" +
                    "Заполненность: " + inf.get(3) + "\n" +
                    "Процессор: " + inf.get(4) + "\n" +
                    "Память: " + inf.get(5) + "\n" +
                    "Свободное место: " + inf.get(6) + "\n" +
                    "Задержка сети: " + inf.get(7)
            );



        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
    }
}
