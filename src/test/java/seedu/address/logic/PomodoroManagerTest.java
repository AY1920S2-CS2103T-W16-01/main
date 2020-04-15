package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.storage.JsonPetStorage;
import seedu.address.storage.JsonPomodoroStorage;
import seedu.address.storage.JsonStatisticsStorage;
import seedu.address.storage.JsonTaskListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class PomodoroManagerTest {

    @TempDir public Path temporaryFolder;

    private Model model = new ModelManager();
    private PetManager petManager;
    private PomodoroManager pomodoroManager;

    @BeforeEach
    public void setUp() {
        JsonTaskListStorage taskListStorage =
                new JsonTaskListStorage(temporaryFolder.resolve("taskList.json"));
        JsonPetStorage petStorage = new JsonPetStorage(temporaryFolder.resolve("pet.json"));
        JsonPomodoroStorage pomodoroStorage =
                new JsonPomodoroStorage(temporaryFolder.resolve("pomodoro.json"));
        JsonStatisticsStorage statisticsStorage =
                new JsonStatisticsStorage(temporaryFolder.resolve("statistics.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage =
                new StorageManager(
                        taskListStorage,
                        petStorage,
                        pomodoroStorage,
                        statisticsStorage,
                        userPrefsStorage);

        pomodoroManager = new PomodoroManager(model);

        petManager = new PetManager();

        pomodoroManager.setDefaultStartTime(25f);

        pomodoroManager.setRestTime(5f);

        model.setPomodoroManager(pomodoroManager);

        model.setPetManager(petManager);
    }

    @Test
    public void produceCorrectTimeAsString() {
        String timeString = pomodoroManager.getDefaultStartTimeAsString();
        assertTrue(timeString.equals("25:00"));
    }

    @Test
    public void setPomodoroTimes_updateModel() {
        String newDefaultTime;
        String newRestTime;
        pomodoroManager.setDefaultStartTime(42f);
        pomodoroManager.setRestTime(13f);
        newDefaultTime = model.getPomodoro().getDefaultTime();
        newRestTime = model.getPomodoro().getRestTime();
        boolean defaultMatches = newDefaultTime.equals("42.0");
        boolean restMatches = newRestTime.equals("13.0");
        assertTrue(defaultMatches);
        assertTrue(restMatches);
    }

    @Test
    public void generateUpdatedDayDataTest() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusHours(48);
        List<DayData> dayDatas = pomodoroManager.generateUpdatedDayData(start, end);
        assertTrue(dayDatas.size() == 3);
    }

    @Test
    public void updateStatisticsTest() {
        final int AMOUNT_TO_TEST = 42;
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMinutes(AMOUNT_TO_TEST);
        DayData d =
                model.getDayDataFromDateStatistics(
                        new Date(LocalDateTime.now().toLocalDate().toString()));
        assertTrue(d.getPomDurationData().value == 0);
        pomodoroManager.setStartDateTime(start);
        pomodoroManager.updateStatistics(model);
        List<DayData> dayDatas = pomodoroManager.generateUpdatedDayData(start, end);
        int totalMinutes = 0;
        for (DayData dayData : dayDatas) {
            DayData a = model.getDayDataFromDateStatistics(dayData.getDate());
            totalMinutes += a.getPomDurationData().value;
        }
        assertTrue(totalMinutes == AMOUNT_TO_TEST);
    }
}
