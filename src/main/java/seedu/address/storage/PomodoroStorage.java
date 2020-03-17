package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyPomodoro;

/** Represents a storage for {@link seedu.address.model.TaskList}. */
public interface PomodoroStorage {

    /**
     * Returns TaskList data as a {@link ReadOnlyTaskList}. Returns {@code Optional.empty()} if
     * storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyPomodoro> readPomodoro() throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskList} to the storage.
     *
     * @param pomodoro cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void savePomodoro(ReadOnlyPomodoro pomodoro) throws IOException;
}
