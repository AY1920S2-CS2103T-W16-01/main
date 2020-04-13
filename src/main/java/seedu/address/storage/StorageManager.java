package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.InvalidPetException;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/** Manages storage of TaskList data in local storage. */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskListStorage taskListStorage;
    private UserPrefsStorage userPrefsStorage;
    private PetStorage petStorage;
    private PomodoroStorage pomodoroStorage;
    private StatisticsStorage statisticsStorage;

    // NOTE these storage objects all correspond to Json-storagename, i.e. JsonTaskListStorage, ...
    public StorageManager(
            TaskListStorage taskListStorage,
            PetStorage petStorage,
            PomodoroStorage pomodoroStorage,
            StatisticsStorage statisticsStorage,
            UserPrefsStorage userPrefsStorage) {
        super();
        this.taskListStorage = taskListStorage;
        this.petStorage = petStorage;
        this.pomodoroStorage = pomodoroStorage;
        this.statisticsStorage = statisticsStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ TaskList methods ==============================

    @Override
    public Path getTaskListFilePath() {
        return taskListStorage.getTaskListFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskList> readTaskList() throws DataConversionException, IOException {
        return readTaskList(taskListStorage.getTaskListFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskList> readTaskList(Path filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskListStorage.readTaskList(filePath);
    }

    @Override
    public void saveTaskList(ReadOnlyTaskList taskList) throws IOException {
        saveTaskList(taskList, taskListStorage.getTaskListFilePath());
    }

    @Override
    public void saveTaskList(ReadOnlyTaskList taskList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskListStorage.saveTaskList(taskList, filePath);
    }
    // ================ Pet methods ==============================
    @Override
    public Path getPetFilePath() {
        return petStorage.getPetFilePath();
    }

    @Override
    public Optional<ReadOnlyPet> readPet()
            throws DataConversionException, IOException, InvalidPetException {
        return readPet(petStorage.getPetFilePath());
    }

    @Override
    public Optional<ReadOnlyPet> readPet(Path filePath)
            throws DataConversionException, IOException, InvalidPetException {
        logger.fine("Attempting to read data from file: " + filePath);
        return petStorage.readPet(filePath);
    }

    @Override
    public void savePet(ReadOnlyPet pet) throws IOException {
        savePet(pet, petStorage.getPetFilePath());
    }

    @Override
    public void savePet(ReadOnlyPet pet, Path filePath) throws IOException {
        logger.fine("Attempting to write pet data: " + filePath);
        petStorage.savePet(pet, filePath);
    }

    // ================ Pomodoro methods ==============================
    @Override
    public Optional<ReadOnlyPomodoro> readPomodoro() throws DataConversionException, IOException {
        return pomodoroStorage.readPomodoro();
    }

    @Override
    public void savePomodoro(ReadOnlyPomodoro pomodoro) throws IOException {
        logger.fine("Attempting to write pomodoro data: ");
        pomodoroStorage.savePomodoro(pomodoro);
    }

    // ================ Statistics methods ==============================

    @Override
    public Path getStatisticsFilePath() {
        return statisticsStorage.getStatisticsFilePath();
    }

    @Override
    public Optional<ReadOnlyStatistics> readStatistics()
            throws DataConversionException, IOException {
        return statisticsStorage.readStatistics(statisticsStorage.getStatisticsFilePath());
    }

    @Override
    public Optional<ReadOnlyStatistics> readStatistics(Path filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return statisticsStorage.readStatistics(filePath);
    }

    @Override
    public void saveStatistics(ReadOnlyStatistics statistics) throws IOException {
        logger.fine("Attempting to write Statistics data: ");
        statisticsStorage.saveStatistics(statistics);
    }

    @Override
    public void saveStatistics(ReadOnlyStatistics statistics, Path filePath) throws IOException {
        logger.fine("Attempting to write Statistics  data: " + filePath);
        statisticsStorage.saveStatistics(statistics, filePath);
    }
}
