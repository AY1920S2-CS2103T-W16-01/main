package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedDayData.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDayDatas.day0;

import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.PomDurationData;
import seedu.address.model.dayData.TasksDoneData;

public class JsonAdaptedDayDataTest {
    private static final String INVALID_DATE = "2020-03-176";
    private static final String INVALID_POM_DURATION_DATA = "50000";
    private static final String INVALID_TASKS_DONE_DATA = "-1";

    private static final String VALID_DATE = day0.getDate().toString();
    private static final String VALID_POM_DURATION_DATA = day0.getPomDurationData().toString();
    private static final String VALID_TASKS_DONE_DATA = day0.getTasksDoneData().toString();

    @Test
    public void toModelType_validDayDataDetails_returnsDayData() throws Exception {
        JsonAdaptedDayData dayData = new JsonAdaptedDayData(day0);
        assertEquals(day0, dayData.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(
                        INVALID_DATE, VALID_POM_DURATION_DATA, VALID_TASKS_DONE_DATA);
        String expectedMessage = Date.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(
                        null, VALID_POM_DURATION_DATA, VALID_TASKS_DONE_DATA);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }

    @Test
    public void toModelType_invalidPomDurationData_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(
                        VALID_DATE, INVALID_POM_DURATION_DATA, VALID_TASKS_DONE_DATA);
        String expectedMessage = PomDurationData.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }

    @Test
    public void toModelType_nullPomDurationData_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(VALID_DATE, null, VALID_TASKS_DONE_DATA);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, PomDurationData.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }

    @Test
    public void toModelType_nullTasksDoneData_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(VALID_DATE, VALID_POM_DURATION_DATA, null);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, TasksDoneData.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }

    @Test
    public void toModelType_invalidTasksDoneData_throwsIllegalValueException() {
        JsonAdaptedDayData dayData =
                new JsonAdaptedDayData(
                        VALID_DATE, VALID_POM_DURATION_DATA, INVALID_TASKS_DONE_DATA);
        String expectedMessage = TasksDoneData.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, dayData::toModelType);
    }
}
