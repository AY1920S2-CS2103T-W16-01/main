package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/** Manages storage of TaskList data in local storage. */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskListStorage taskListStorage;
    private UserPrefsStorage userPrefsStorage;
    private PetStorage petStorage;

    // NOTE these storage objects all correspond to Json-storagename, i.e. JsonTaskListStorage, ...
    public StorageManager(
            TaskListStorage taskListStorage,
            PetStorage petStorage,
            UserPrefsStorage userPrefsStorage) {
        super();
        this.taskListStorage = taskListStorage;
        this.petStorage = petStorage;
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
    // ================ TaskList methods ==============================
    @Override
    public Path getPetFilePath() {
        return petStorage.getPetFilePath();
    }

    @Override
    public Optional<ReadOnlyPet> readPet() throws DataConversionException, IOException {
        return readPet(petStorage.getPetFilePath());
    }

    @Override
    public Optional<ReadOnlyPet> readPet(Path filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return petStorage.readPet(filePath);
    }

    @Override
    public void savePet(ReadOnlyPet taskList) throws IOException {
        savePet(taskList, petStorage.getPetFilePath());
    }

    @Override
    public void savePet(ReadOnlyPet taskList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        petStorage.savePet(taskList, filePath);
    }
}
