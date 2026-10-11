import org.example.Lab04.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Lab4Test {
    private ArrayList<LogEntry> entries;
    private LogEntry[] entryArray;

    @BeforeEach
    void setUp() throws IOException {
        InputStream input = Lab4.class.getResourceAsStream("/lab_4.txt");
        assertNotNull(input, "Файл lab_4.txt не найден в ресурсах");

        String[] lines;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {
            lines = reader.lines().toArray(String[]::new);
        }

        entries = Lab4.parseLog(lines);
        entryArray = entries.toArray(new LogEntry[0]);
    }

    @Test
    void parseLogРазбираетПоляЗаписи() {
        LogEntry first = entries.get(0);

        assertEquals(
                LocalDateTime.of(2026, 9, 1, 12, 10, 1, 101_000_000),
                first.Timestamp
        );
        assertEquals("Info", first.Level);
        assertEquals("Server", first.Category);
        assertTrue(first.Message.contains("Событие"));
    }

    @Test
    void filterByDateВозвращаетЗаписиЗаУказанныйДень() {
        ArrayList<LogEntry> result = Lab4.filterByDate(
                entries,
                LocalDateTime.of(2026, 9, 2, 0, 0)
        );

        assertEquals(6, result.size());
    }

    @Test
    void filterByLevelВозвращаетЗаписиУказанногоУровня() {
        ArrayList<LogEntry> result =
                Lab4.filterByLevel(entryArray, "Error");

        assertEquals(3, result.size());
        assertEquals("Database", result.get(1).Category);
        assertEquals("Database", result.get(2).Category);
    }

    @Test
    void filterByCategoryВозвращаетЗаписиУказаннойКатегории() {
        ArrayList<LogEntry> result =
                Lab4.FilterByCategory(entryArray, "Combat");

        assertFalse(result.isEmpty());
        assertEquals("Combat", result.get(0).Category);
    }

    @Test
    void searchНеУчитываетРегистр() {
        ArrayList<LogEntry> result =
                Lab4.search(entryArray, "сердце эмберфанга");

        assertEquals(4, result.size());
    }

    @Test
    void countByLevelСчитаетЗаписиУказанногоУровня() {
        assertEquals(1, Lab4.CountByLevel(entryArray, "Fatal"));
    }

    @Test
    void getServerStatusВозвращаетКритическийСтатус() {
        assertEquals(
                "КРИТИЧЕСКАЯ ОШИБКА: сервер остановлен",
                Lab4.getServerStatus(entries)
        );
    }
}