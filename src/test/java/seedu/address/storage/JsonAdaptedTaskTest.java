package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.model.task.Done.DONE;
import static seedu.address.storage.JsonAdaptedTask.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTasks.LAB_3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Description;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

// TODO stub: arthur please put in valid reminder here to replace final nulls.

public class JsonAdaptedTaskTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PRIORITY = "+651234";
    // private static final String INVALID_DONE = "#A";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = LAB_3.getName().toString();
    private static final String VALID_PRIORITY = LAB_3.getPriority().toString();
    private static final String VALID_DESCRIPTION = LAB_3.getDescription().toString();
    private static final String VALID_DONE = DONE;
    private static final List<JsonAdaptedTag> VALID_TAGS =
            LAB_3.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList());

    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        JsonAdaptedTask task = new JsonAdaptedTask(LAB_3);
        assertEquals(LAB_3, task.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        INVALID_NAME,
                        VALID_PRIORITY,
                        VALID_DESCRIPTION,
                        VALID_DONE,
                        VALID_TAGS,
                        null,
                        null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        null,
                        VALID_PRIORITY,
                        VALID_DESCRIPTION,
                        VALID_DONE,
                        VALID_TAGS,
                        null,
                        null);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidPriority_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        VALID_NAME,
                        INVALID_PRIORITY,
                        VALID_DESCRIPTION,
                        VALID_DONE,
                        VALID_TAGS,
                        null,
                        null);
        String expectedMessage = Priority.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullPriority_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        VALID_NAME, null, VALID_DESCRIPTION, VALID_DONE, VALID_TAGS, null, null);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Priority.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        VALID_NAME, VALID_PRIORITY, null, VALID_DONE, VALID_TAGS, null, null);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedTask task =
                new JsonAdaptedTask(
                        VALID_NAME,
                        VALID_PRIORITY,
                        VALID_DESCRIPTION,
                        VALID_DONE,
                        invalidTags,
                        null,
                        null);
        assertThrows(IllegalValueException.class, task::toModelType);
    }
}
