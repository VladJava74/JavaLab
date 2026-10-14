
import org.example.Lab05.Lab5;
import org.example.Lab05.SaveData;
import org.example.Lab05.SaveEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class test_Lab5 {

    @TempDir
    Path tempDirectory;

    private String indexPath;
    private String savesDirectory;
    private String backupsDirectory;

    @BeforeEach
    void setUp() throws Exception {
        indexPath = tempDirectory.resolve("index.json").toString();
        savesDirectory = tempDirectory.resolve("saves").toString();
        backupsDirectory = tempDirectory.resolve("backups").toString();

        Files.createDirectories(Path.of(savesDirectory));
        Files.createDirectories(Path.of(backupsDirectory));

        String indexJson =
                "[{\"name\":\"slot-01\",\"file\":\"save-01.json\"," +
                        "\"updatedAt\":\"2026-04-18 10:15:00.000\"}]";

        String saveJson =
                "{\"Character\":\"Mira\",\"Map\":\"Ashen Coast\"," +
                        "\"Position\":{\"X\":128,\"Y\":47},\"Health\":76," +
                        "\"Inventory\":[\"iron sword\"]}";

        Files.write(
                Path.of(indexPath),
                indexJson.getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
                Path.of(savesDirectory, "save-01.json"),
                saveJson.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void loadIndexReadsSaveEntry() {
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);

        assertEquals(1, entries.size());
        assertEquals("slot-01", entries.get(0).get_name());
        assertEquals("save-01.json", entries.get(0).get_file());
    }

    @Test
    void findByNameIsCaseInsensitive() {
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);

        SaveEntry entry = Lab5.FindByName(entries, "SLOT-01");

        assertNotNull(entry);
        assertEquals("slot-01", entry.get_name());
    }

    @Test
    void findByNameReturnsNullIfSaveDoesNotExist() {
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);

        assertNull(Lab5.FindByName(entries, "missing"));
    }

    @Test
    void loadSaveReadsSaveData() {
        SaveEntry entry = Lab5.LoadIndex(indexPath).get(0);

        SaveData save = Lab5.LoadSave(savesDirectory, entry);

        assertNotNull(save);
        assertEquals("Mira", save.get_Character());
        assertEquals("Ashen Coast", save.get_Map());
        assertEquals(128, save.get_Position().X);
        assertEquals(47, save.get_Position().Y);
        assertEquals(76, save.get_Health());
        assertTrue(save.get_Inventory().contains("iron sword"));
    }

    @Test
    void saveGameWritesEditedDataThatCanBeLoadedAgain() {
        SaveEntry entry = Lab5.LoadIndex(indexPath).get(0);
        SaveData save = Lab5.LoadSave(savesDirectory, entry);

        assertNotNull(save);

        save.save_Map("Crystal Cavern");
        save.get_Position().X = 10;
        save.get_Position().Y = 25;
        save.save_Health(50);
        save.get_Inventory().add("crystal key");

        Lab5.SaveGame(savesDirectory, entry, save);

        SaveData loaded = Lab5.LoadSave(savesDirectory, entry);

        assertNotNull(loaded);
        assertEquals("Crystal Cavern", loaded.get_Map());
        assertEquals(10, loaded.get_Position().X);
        assertEquals(25, loaded.get_Position().Y);
        assertEquals(50, loaded.get_Health());
        assertTrue(loaded.get_Inventory().contains("crystal key"));
    }

    @Test
    void saveIndexWritesEntriesThatCanBeLoadedAgain() {
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);
        entries.add(new SaveEntry(
                "manual",
                "manual.json",
                "2026-05-01 12:30:00.000"
        ));

        Lab5.SaveIndex(indexPath, entries);

        ArrayList<SaveEntry> loaded = Lab5.LoadIndex(indexPath);

        assertEquals(2, loaded.size());
        assertEquals("manual", loaded.get(1).get_name());
        assertEquals("manual.json", loaded.get(1).get_file());
    }

    @Test
    void createBackupCreatesReadableBackupFile() {
        SaveEntry entry = Lab5.LoadIndex(indexPath).get(0);

        Lab5.CreateBackup(savesDirectory, backupsDirectory, entry);

        File backupFile = Path.of(backupsDirectory, entry.get_file()).toFile();
        assertTrue(backupFile.exists());

        SaveData backup = Lab5.LoadSave(backupsDirectory, entry);

        assertNotNull(backup);
        assertEquals("Ashen Coast", backup.get_Map());
    }

    @Test
    void restoreBackupRestoresOriginalSave() throws Exception {
        SaveEntry entry = Lab5.LoadIndex(indexPath).get(0);

        Lab5.CreateBackup(savesDirectory, backupsDirectory, entry);

        Files.write(
                Path.of(savesDirectory, entry.get_file()),
                "{\"Map\":\"Changed\"}".getBytes(StandardCharsets.UTF_8)
        );

        Lab5.RestoreBackup(savesDirectory, backupsDirectory, entry);

        SaveData restored = Lab5.LoadSave(savesDirectory, entry);

        assertNotNull(restored);
        assertEquals("Ashen Coast", restored.get_Map());
    }

    @Test
    void deleteSaveRemovesFileAndIndexEntry() {
        ArrayList<SaveEntry> entries = Lab5.LoadIndex(indexPath);
        SaveEntry entry = entries.get(0);

        Lab5.DeleteSave(savesDirectory, indexPath, entries, entry);

        assertFalse(Path.of(savesDirectory, entry.get_file()).toFile().exists());
        assertTrue(Lab5.LoadIndex(indexPath).isEmpty());
    }
}