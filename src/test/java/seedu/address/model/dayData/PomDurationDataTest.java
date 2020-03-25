package seedu.address.model.dayData;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

public class PomDurationDataTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PomDurationData(null));
    }

    @Test
    public void constructor_invalidPomDurationData_throwsIllegalArgumentException() {
        String invalidPomDurationData = "hi";
        assertThrows(IllegalArgumentException.class, () -> new PomDurationData(invalidPomDurationData));
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
