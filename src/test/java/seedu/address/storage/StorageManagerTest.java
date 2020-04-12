package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalDayDatas.getTypicalStatistics;
import static seedu.address.testutil.TypicalPet.getTypicalPet;
import static seedu.address.testutil.TypicalPomodoro.getTypicalPomodoro;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonTaskListStorage taskListStorage = new JsonTaskListStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonPomodoroStorage pomodoroStorage =
                new JsonPomodoroStorage(getTempFilePath("pomodoro.json"));
        JsonStatisticsStorage statisticsStorage =
                new JsonStatisticsStorage(getTempFilePath("statistics.json"));
        JsonPetStorage petStorage = new JsonPetStorage(getTempFilePath("pet.json"));

        storageManager =
                new StorageManager(
                        taskListStorage,
                        petStorage,
                        pomodoroStorage,
                        statisticsStorage,
                        userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskListReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonTaskListStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonTaskListStorageTest} class.
         */
        TaskList original = getTypicalTaskList();
        storageManager.saveTaskList(original);
        ReadOnlyTaskList retrieved = storageManager.readTaskList().get();
        assertEquals(original, new TaskList(retrieved));
    }

    @Test
    public void getTaskListFilePath() {
        assertNotNull(storageManager.getTaskListFilePath());
    }

    @Test
    public void pomodoroReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonPomodoroStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonTaskListStorageTest} class.
         */
        Pomodoro original = getTypicalPomodoro();
        storageManager.savePomodoro(original);
        ReadOnlyPomodoro retrieved = storageManager.readPomodoro().get();
        assertEquals(original, new Pomodoro(retrieved));
    }

    @Test
    public void petReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonTaskListStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonTaskListStorageTest} class.
         */
        Pet original = getTypicalPet();
        storageManager.savePet(original);
        ReadOnlyPet retrieved = storageManager.readPet().get();
        assertEquals(original, new Pet(retrieved));
    }

    @Test
    public void getPetFilePath() {
        assertNotNull(storageManager.getPetFilePath());
    }

    @Test
    public void statisticsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonStatisticsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonStatisticsStorageTest} class.
         */
        Statistics original = getTypicalStatistics();
        storageManager.saveStatistics(original);
        ReadOnlyStatistics retrieved = storageManager.readStatistics().get();
        assertEquals(original, new Statistics(retrieved));
    }

    @Test
    public void getStatisticsFilePath() {
        assertNotNull(storageManager.getStatisticsFilePath());
    }
}
