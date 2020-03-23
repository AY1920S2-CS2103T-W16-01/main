package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.InvalidTableException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyStatistics;

/** A class to access TaskList data stored as a json file on the hard disk. */
public class JsonStatisticsStorage implements StatisticsStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonStatisticsStorage.class);

    private Path filePath;

    public JsonStatisticsStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getStatisticsFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyStatistics> readStatistics() throws DataConversionException {
        return readStatistics(filePath);
    }

    /** Similar to {@link #readStatistics()}. */
    public Optional<ReadOnlyStatistics> readStatistics(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableDayDataList> jsonSerializableDayDataList =
                JsonUtil.readJsonFile(filePath, JsonSerializableDayDataList.class);
        if (!jsonSerializableDayDataList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonSerializableDayDataList.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        } catch (InvalidTableException ite) {
            logger.info("Invalid table constraints found in " + filePath + ": " + ite.getMessage());
            throw new DataConversionException(ite);
        }
    }

    @Override
    public void saveStatistics(ReadOnlyStatistics statistics) throws IOException {
        saveStatistics(statistics, filePath);
    }

    /**
     * Similar to {@link #saveStatistics(ReadOnlyStatistics)}.
     *
     * @param statistics location of the data. Cannot be null.
     */
    public void saveStatistics(ReadOnlyStatistics statistics, Path filePath) throws IOException {
        requireNonNull(statistics);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableDayDataList(statistics), filePath);
    }
}
