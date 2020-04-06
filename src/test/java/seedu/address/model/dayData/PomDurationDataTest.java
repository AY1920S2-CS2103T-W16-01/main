package seedu.address.model.dayData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedDayDataTest.INVALID_POM_DURATION_DATA;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PomDurationDataTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PomDurationData(null));
    }

    @Test
    public void constructor_invalidPomDurationData_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PomDurationData(INVALID_POM_DURATION_DATA));
    }

    @Test
    public void isValidPomDurationData() {
        // invalid pomDurationData
        assertFalse(PomDurationData.isValidPomDurationData("")); // empty string
        assertFalse(TasksDoneData.isValidTasksDoneData("cat")); // not integer
        assertFalse(PomDurationData.isValidPomDurationData("12-12-1997")); // date
        assertFalse(PomDurationData.isValidPomDurationData("-3")); // negative number
        assertFalse(PomDurationData.isValidPomDurationData("123456")); // exceed minutes in a day

        // valid pomDurationData
        assertTrue(PomDurationData.isValidPomDurationData("50"));
    }
}
