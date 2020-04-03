package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.Statistics;
import seedu.address.testutil.TypicalDayDatas;

public class JsonSerializableDayDataListTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableDayDataListTest");
    private static final Path TYPICAL_DAY_DATA_FILE =
            TEST_DATA_FOLDER.resolve("typicalDayDatas.json");
    private static final Path INVALID_DAY_DATA_FILE =
            TEST_DATA_FOLDER.resolve("invalidDayData.json");
    // private static final Path DUPLICATE_DAY_DATA_FILE =
    // TEST_DATA_FOLDER.resolve("duplicateTask.json");

    @Test
    public void toModelType_typicalDayDataFile_success() throws Exception {
        JsonSerializableDayDataList dataFromFile =
                JsonUtil.readJsonFile(TYPICAL_DAY_DATA_FILE, JsonSerializableDayDataList.class)
                        .get();
        Statistics statisticsFromFile = dataFromFile.toModelType();
        Statistics typicalStatistics = TypicalDayDatas.getTypicalStatistics();
        assertEquals(statisticsFromFile, typicalStatistics);
    }

    @Test
    public void toModelType_invalidDayDataFile_throwsIllegalValueException() throws Exception {
        JsonSerializableDayDataList dataFromFile =
                JsonUtil.readJsonFile(INVALID_DAY_DATA_FILE, JsonSerializableDayDataList.class)
                        .get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    // Todo
    /*
    @Test
    public void toModelType_duplicateTasks_throwsIllegalValueException() throws Exception {
        JsonSerializableTaskList dataFromFile =
                JsonUtil.readJsonFile(DUPLICATE_TASK_FILE, JsonSerializableTaskList.class).get();
        assertThrows(
                IllegalValueException.class,
                JsonSerializableTaskList.MESSAGE_DUPLICATE_TASK,
                dataFromFile::toModelType);
    } */
}
