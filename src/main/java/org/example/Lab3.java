package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Lab3 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Название события: ");
        String title = in.nextLine();

        System.out.print("Дата события (дд.мм.гггг): ");
        String dateText = in.nextLine();

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateText, inputFormat);

        System.out.print("Количество участников (можно пропустить): ");
        String participants = in.nextLine();

        System.out.print("Победитель (можно пропустить): ");
        String winner = in.nextLine();

        System.out.print("Комментарий (можно пропустить): ");
        String comment = in.nextLine();

        String report = "🎮 Отчёт о событии\n"
                + "Название: " + title + "\n"
                + "Дата проведения: " + date.format(inputFormat) + "\n";

        if (!participants.isEmpty()) {
            report += "Участников: " + participants + "\n";
        }

        if (!winner.isEmpty()) {
            report += "Победитель: " + winner + "\n";
        }

        if (!comment.isEmpty()) {
            report += "Комментарий: " + comment + "\n";
        }

        System.out.println("\nГотовый отчёт:\n");
        System.out.println(report);

        in.close();
    }
}