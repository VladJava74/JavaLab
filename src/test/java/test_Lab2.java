import org.example.Lab2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class test_Lab2 {
    @Test
    void t1() {
        String result = Lab2.checkConfiguration(50, 8, false);

        assertEquals("Запуск возможен", result);
    }

    @Test
    void t2() {
        String result = Lab2.checkConfiguration(0, 8, false);

        assertEquals(
                "Запуск невозможен: количество игроков должно быть больше нуля.",
                result
        );
    }

    @Test
    void t3() {
        String result = Lab2.checkConfiguration(10, 4, false);

        assertEquals(
                "Запуск невозможен: серверу недостаточно оперативной памяти.",
                result
        );
    }

    @Test
    void t4() {
        String result = Lab2.checkConfiguration(56, 8, false);

        assertTrue(result.contains(
                "Запуск возможен с предупреждением: для такого количества игроков рекомендуется больше оперативной памяти."
        ));

        assertTrue(result.contains("Запуск возможен"));
    }

    @Test
    void t5() {
        String result = Lab2.checkConfiguration(50, 8, true);

        assertTrue(result.contains(
                "Запуск возможен с предупреждением: публичный сервер защищён паролем."
        ));

        assertTrue(result.contains("Запуск возможен"));
    }
}
