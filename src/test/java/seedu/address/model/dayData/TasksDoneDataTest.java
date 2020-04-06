package seedu.address.model.dayData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedDayDataTest.INVALID_TASKS_DONE_DATA;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TasksDoneDataTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TasksDoneData(null));
    }

    @Test
    public void constructor_invalidTasksDoneData_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class, () -> new TasksDoneData(INVALID_TASKS_DONE_DATA));
    }

    @Test
    public void isValidTasksDoneData() {
        // invalid tasksDoneData
        assertFalse(TasksDoneData.isValidTasksDoneData("")); // empty string
        assertFalse(TasksDoneData.isValidTasksDoneData("cat")); // not integer
        assertFalse(TasksDoneData.isValidTasksDoneData("12-12-1997")); // date
        assertFalse(TasksDoneData.isValidTasksDoneData("-3")); // negative number

        // valid tasksDoneData
        assertTrue(TasksDoneData.isValidTasksDoneData("50"));
    }
}
