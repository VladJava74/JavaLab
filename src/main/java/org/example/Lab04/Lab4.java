package org.example.Lab04;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Lab4 {
    static void main() {

        try(InputStream inputStream = Lab4.class.getResourceAsStream("/lab_4.txt")) {

            if (inputStream == null) {
                System.out.println("Файл не найден");
                return;
            }

            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            ArrayList<LogEntry> arr = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                arr.add(convert_to_LogEntry(line));
            }

        } catch (Exception e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
    }

    public static LogEntry convert_to_LogEntry(String line) {

        String dateTime = line.substring(0, 23);

        int firstOpen = line.indexOf('[');
        int firstClose = line.indexOf(']', firstOpen);
        int secondOpen = line.indexOf('[', firstClose);
        int secondClose = line.indexOf(']', secondOpen);

        String level = line.substring(firstOpen + 1, firstClose);
        String category = line.substring(secondOpen + 1, secondClose);
        String message = line.substring(secondClose + 1).trim();

        return new LogEntry(dateTime, level, category, message);
    }

    public static ArrayList<LogEntry> parseLog(String[] lines) {
        ArrayList<LogEntry> res = new ArrayList<>();
        for(String lin : lines) {
            res.add(convert_to_LogEntry(lin));
        }
        return res;
    }

    public static ArrayList<LogEntry> filterByDate(ArrayList<LogEntry> entries, LocalDateTime date) {
        ArrayList<LogEntry> res = new ArrayList<>();
        for (LogEntry lin : entries) {
            if(lin.Timestamp.toLocalDate().equals(date.toLocalDate())) res.add(lin);
        }
        return res;
    }

    public static ArrayList<LogEntry> filterByLevel(LogEntry[] entries, String level) {
        ArrayList<LogEntry> res = new ArrayList<>();
        for (LogEntry lin : entries) {
            if(lin.Level.equals(level)) res.add(lin);
        }
        return res;
    }

    public static ArrayList<LogEntry> FilterByCategory(LogEntry[] entries, String category) {
        ArrayList<LogEntry> res = new ArrayList<>();
        for (LogEntry lin : entries) {
            if(lin.Category.equals(category)) res.add(lin);
        }
        return res;
    }

    public static ArrayList<LogEntry> search(LogEntry[] entries, String text) {
        ArrayList<LogEntry> result = new ArrayList<>();
        String searchText = text.toLowerCase();
        for (LogEntry entry : entries) {
            if (entry.Message.toLowerCase().contains(searchText)) {
                result.add(entry);
            }
        }
        return result;
    }

    public static int CountByLevel(LogEntry[] entries, String level) {
        int N = 0;
        for (LogEntry lin : entries) {
            if(lin.Level.equals(level)) N++;
        }
        return N;
    }

    public static String getServerStatus(ArrayList<LogEntry> entries) {
        boolean hasError = false;

        for (LogEntry lin : entries) {
            if ("Fatal".equals(lin.Level) && "Server".equals(lin.Category)) {
                return "КРИТИЧЕСКАЯ ОШИБКА: сервер остановлен";
            }

            if ("Error".equals(lin.Level)) {
                hasError = true;
            }
        }

        if (hasError) {
            return "Есть ошибки: требуется проверка";
        }

        return "Сервер работает штатно";
    }
}

