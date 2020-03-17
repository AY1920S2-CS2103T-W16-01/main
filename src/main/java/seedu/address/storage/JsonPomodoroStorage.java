package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyPomodoro;

/** A class to access TaskList data stored as a json file on the hard disk. */
public class JsonPomodoroStorage implements PomodoroStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonPomodoroStorage.class);

    private Path filePath;

    public JsonPomodoroStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getTaskListFilePath() {
        return filePath;
    }

    /**
     * Similar to {@link #readTaskList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyPomodoro> readPomodoro() throws DataConversionException {
        requireNonNull(this.filePath);

        Optional<JsonAdaptedPomodoro> jsonPomodoro =
                JsonUtil.readJsonFile(this.filePath, JsonAdaptedPomodoro.class);
        if (!jsonPomodoro.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonPomodoro.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Similar to {@link #saveTaskList(ReadOnlyPomodoro)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    @Override
    public void savePomodoro(ReadOnlyPomodoro pomodoro) throws IOException {
        requireNonNull(pomodoro);
        requireNonNull(this.filePath);

        FileUtil.createIfMissing(this.filePath);
        JsonUtil.saveJsonFile(new JsonAdaptedPomodoro(pomodoro), filePath);
    }
}
