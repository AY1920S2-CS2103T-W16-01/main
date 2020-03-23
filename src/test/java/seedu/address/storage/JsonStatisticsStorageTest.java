package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDayDatas.dayNew;
import static seedu.address.testutil.TypicalDayDatas.dayNew2;
import static seedu.address.testutil.TypicalDayDatas.getTypicalStatistics;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.Statistics;

public class JsonStatisticsStorageTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonStatisticsStorageTest");

    @TempDir public Path testFolder;

    @Test
    public void readStatistics_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readStatistics(null));
    }

    private java.util.Optional<ReadOnlyStatistics> readStatistics(String filePath) throws Exception {
        return new JsonStatisticsStorage(Paths.get(filePath))
                .readStatistics(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readStatistics("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(
                DataConversionException.class, () -> readStatistics("notJsonFormatStatistics.json"));
    }

    @Test
    public void readTaskList_invalidAndValidDayDataStatistics_throwDataConversionException() {
        assertThrows(
                DataConversionException.class,
                () -> readStatistics("invalidAndValidDayDataStatistics.json"));
    }

    @Test
    public void readAndSaveStatistics_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempStatistics.json");
        Statistics original = getTypicalStatistics();
        JsonStatisticsStorage jsonStatisticsStorage = new JsonStatisticsStorage(filePath);

        // Save in new file and read back
        jsonStatisticsStorage.saveStatistics(original, filePath);
        ReadOnlyStatistics readBack = jsonStatisticsStorage.readStatistics(filePath).get();
        assertEquals(original, new Statistics(readBack));

        // Modify data, overwrite exiting file, and read back
        original.pop(); // Statistics table constraints
        original.addDayData(dayNew); // Statistics table constraints
        jsonStatisticsStorage.saveStatistics(original, filePath);
        readBack = jsonStatisticsStorage.readStatistics(filePath).get();
        assertEquals(original, new Statistics(readBack));

        // Save and read without specifying file path
        original.pop(); // Statistics table constraints
        original.addDayData(dayNew2); // Statistics table constraints
        jsonStatisticsStorage.saveStatistics(original); // file path not specified
        readBack = jsonStatisticsStorage.readStatistics().get(); // file path not specified
        assertEquals(original, new Statistics(readBack));
    }

    @Test
    public void saveTaskList_nullTaskList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveStatistics(null, "SomeFile.json"));
    }

    /** Saves {@code Statistics} at the specified {@code filePath}. */
    private void saveStatistics(ReadOnlyStatistics statistics, String filePath) {
        try {
            new JsonStatisticsStorage(Paths.get(filePath))
                    .saveStatistics(statistics, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveStatistics_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveStatistics(new Statistics(), null));
    }

}

