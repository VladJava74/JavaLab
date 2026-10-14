package org.example.Lab05;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab5 {
    public static void main(String[] args) {
        final String root = "src/main/resources/resour5";
        final String indexPath = root + "/index.json";
        final String savesDirectory = root + "/saves";
        final String backupsDirectory = root + "/backups";

        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Scanner scanner = new Scanner(System.in);
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);


        System.out.println("Путь к индексу: " +
                new File(indexPath).getAbsolutePath());
        System.out.println("Индекс найден: " + new File(indexPath).exists());
        System.out.println("Загружено записей: " + entries.size());

        while (true) {
            System.out.println("\n=== Меню сохранений ===");
            System.out.println("1. Показать список сохранений");
            System.out.println("2. Посмотреть данные сохранения");
            System.out.println("3. Изменить персонажа");
            System.out.println("4. Создать резервную копию");
            System.out.println("5. Восстановить резервную копию");
            System.out.println("6. Удалить сохранение");
            System.out.println("7. Выйти");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число от 1 до 7.");
                continue;
            }

            if (choice == 7) {
                System.out.println("Выход.");
                break;
            }

            if (choice < 1 || choice > 7) {
                System.out.println("Введите число от 1 до 7.");
                continue;
            }

            if (choice == 1) {
                if (entries.isEmpty()) {
                    System.out.println("Сохранений нет.");
                } else {
                    for (int i = 0; i < entries.size(); i++) {
                        SaveEntry entry = entries.get(i);
                        System.out.printf(
                                "%d. %s | файл: %s | изменено: %s%n",
                                i + 1,
                                entry.get_name(),
                                entry.get_file(),
                                entry.get_updatedAt()
                        );
                    }
                }
                continue;
            }

            if (entries.isEmpty()) {
                System.out.println("Сохранений нет.");
                continue;
            }

            for (int i = 0; i < entries.size(); i++) {
                SaveEntry entry = entries.get(i);
                System.out.printf(
                        "%d. %s | файл: %s%n",
                        i + 1,
                        entry.get_name(),
                        entry.get_file()
                );
            }

            System.out.print("Введите номер сохранения (0 — отмена): ");

            int saveNumber;
            try {
                saveNumber = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число.");
                continue;
            }

            if (saveNumber == 0) {
                continue;
            }

            if (saveNumber < 1 || saveNumber > entries.size()) {
                System.out.println("Сохранения с таким номером нет.");
                continue;
            }

            SaveEntry entry = entries.get(saveNumber - 1);

            switch (choice) {
                case 2: {
                    SaveData save = Lab5.LoadSave(savesDirectory, entry);

                    if (save != null) {
                        Lab5.PrintSave(entry, save);
                    }
                    break;
                }

                case 3: {
                    SaveData save = Lab5.LoadSave(savesDirectory, entry);

                    if (save == null) {
                        break;
                    }

                    System.out.println("Текущий персонаж: " + save.get_Character());
                    System.out.print("Введите новое имя персонажа: ");
                    String character = scanner.nextLine().trim();

                    if (character.isEmpty()) {
                        System.out.println("Имя не изменено.");
                        break;
                    }

                    save.save_Character(character);
                    entry.saves_updatedAt(
                            LocalDateTime.now().format(formatter)
                    );

                    Lab5.SaveGame(savesDirectory, entry, save);
                    Lab5.SaveIndex(indexPath, entries);
                    System.out.println("Данные персонажа обновлены.");
                    break;
                }

                case 4:
                    Lab5.CreateBackup(
                            savesDirectory,
                            backupsDirectory,
                            entry
                    );
                    break;

                case 5:
                    Lab5.RestoreBackup(
                            savesDirectory,
                            backupsDirectory,
                            entry
                    );
                    break;

                case 6: {
                    System.out.print(
                            "Удалить сохранение \"" +
                                    entry.get_name() +
                                    "\"? (да/нет): "
                    );

                    String answer = scanner.nextLine().trim();

                    if (answer.equalsIgnoreCase("да")) {
                        Lab5.DeleteSave(
                                savesDirectory,
                                indexPath,
                                entries,
                                entry
                        );
                    } else {
                        System.out.println("Удаление отменено.");
                    }
                    break;
                }

                default:
                    break;
            }
        }

        scanner.close();
    }

    public static ArrayList<SaveEntry> LoadIndex(String indexPath) {
        ArrayList<SaveEntry> entries = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            SaveEntry[] loadedEntries =
                    objectMapper.readValue(new File(indexPath), SaveEntry[].class);

            if (loadedEntries != null) {
                for (SaveEntry entry : loadedEntries) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения индекса: " + e.getMessage());
        }

        return entries;
    }

    public static SaveData LoadSave(String savesDirectory, SaveEntry entry) {
        ObjectMapper objectMapper = new ObjectMapper();
        File saveFile = new File(savesDirectory, entry.get_file());

        try {
            return objectMapper.readValue(saveFile, SaveData.class);
        } catch (IOException e) {
            System.out.println("Ошибка чтения сохранения: " + e.getMessage());
            return null;
        }
    }

    public static void PrintSave(SaveEntry entry, SaveData save) {
        if (entry == null || save == null) {
            System.out.println("Невозможно показать данные сохранения.");
            return;
        }

        System.out.println("Имя сохранения: " + entry.get_name());
        System.out.println("Персонаж: " + save.get_Character());
        System.out.println("Карта: " + save.get_Map());

        if (save.get_Position() != null) {
            System.out.println(
                    "Координаты: " +
                            save.get_Position().X + " " +
                            save.get_Position().Y
            );
        } else {
            System.out.println("Координаты: не указаны");
        }

        System.out.println("Здоровье: " + save.get_Health());
        System.out.println("Предметы: " + save.get_Inventory());
    }

    public static SaveEntry FindByName(
            ArrayList<SaveEntry> entries,
            String name
    ) {
        for (SaveEntry entry : entries) {
            if (entry.get_name() != null &&
                    entry.get_name().equalsIgnoreCase(name)) {
                return entry;
            }
        }

        return null;
    }

    public static void SaveGame(
            String savesDirectory,
            SaveEntry entry,
            SaveData save
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File directory = new File(savesDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Не удалось создать папку сохранений.");
            return;
        }

        File saveFile = new File(directory, entry.get_file());

        try {
            objectMapper.writeValue(saveFile, save);
        } catch (IOException e) {
            System.out.println("Ошибка записи сохранения: " + e.getMessage());
        }
    }

    public static void SaveIndex(String indexPath, List<SaveEntry> entries) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File indexFile = new File(indexPath);
        File parentDirectory = indexFile.getParentFile();

        if (parentDirectory != null &&
                !parentDirectory.exists() &&
                !parentDirectory.mkdirs()) {
            System.out.println("Не удалось создать папку для индекса.");
            return;
        }

        try {
            objectMapper.writeValue(indexFile, entries);
        } catch (IOException e) {
            System.out.println("Ошибка записи индекса: " + e.getMessage());
        }
    }

    public static void CreateBackup(
            String savesDirectory,
            String backupsDirectory,
            SaveEntry entry
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File saveFile = new File(savesDirectory, entry.get_file());
        File backupsFolder = new File(backupsDirectory);

        if (!backupsFolder.exists() && !backupsFolder.mkdirs()) {
            System.out.println("Не удалось создать папку резервных копий.");
            return;
        }

        File backupFile = new File(backupsFolder, entry.get_file());

        try {
            SaveData save = objectMapper.readValue(saveFile, SaveData.class);
            objectMapper.writeValue(backupFile, save);
            System.out.println("Резервная копия создана.");
        } catch (IOException e) {
            System.out.println("Ошибка создания резервной копии: " + e.getMessage());
        }
    }

    public static void RestoreBackup(
            String savesDirectory,
            String backupsDirectory,
            SaveEntry entry
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File backupFile = new File(backupsDirectory, entry.get_file());
        File savesFolder = new File(savesDirectory);

        if (!savesFolder.exists() && !savesFolder.mkdirs()) {
            System.out.println("Не удалось создать папку сохранений.");
            return;
        }

        File saveFile = new File(savesFolder, entry.get_file());

        try {
            SaveData save = objectMapper.readValue(backupFile, SaveData.class);
            objectMapper.writeValue(saveFile, save);
            System.out.println("Сохранение восстановлено.");
        } catch (IOException e) {
            System.out.println("Ошибка восстановления: " + e.getMessage());
        }
    }

    public static void DeleteSave(String savesDirectory, String indexPath, List<SaveEntry> entries,
            SaveEntry entry) {
        File saveFile = new File(savesDirectory, entry.get_file());

        if (saveFile.exists() && !saveFile.delete()) {
            System.out.println("Не удалось удалить файл сохранения.");
            return;
        }

        for (int i = entries.size() - 1; i >= 0; i--) {
            if (entries.get(i).get_file().equals(entry.get_file())) {
                entries.remove(i);
            }
        }

        SaveIndex(indexPath, entries);
        System.out.println("Сохранение удалено.");
    }
}