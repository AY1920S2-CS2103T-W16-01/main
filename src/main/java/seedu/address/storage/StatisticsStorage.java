package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyStatistics;

/** Represents a storage for {@link seedu.address.model.Statistics}. */
public interface StatisticsStorage {

    /** Returns the file path of the data file. */
    Path getStatisticsFilePath();

    /**
     * Returns Statistics data as a {@link ReadOnlyStatistics}. Returns {@code Optional.empty()} if
     * storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyStatistics> readStatistics() throws DataConversionException, IOException;

    /** @see #getStatisticsFilePath() */
    Optional<ReadOnlyStatistics> readStatistics(Path filePath)
            throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyStatistics} to the storage.
     *
     * @param statistics cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveStatistics(ReadOnlyStatistics statistics) throws IOException;


    /** @see #saveStatistics(ReadOnlyStatistics) */
    void saveStatistics(ReadOnlyStatistics statistics, Path filePath) throws IOException;
}
