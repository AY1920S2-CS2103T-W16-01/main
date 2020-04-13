package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HELP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTasks.HOMEWORK10;
import static seedu.address.testutil.TypicalTasks.TASK2;

import org.junit.jupiter.api.Test;
import seedu.address.testutil.TaskBuilder;

public class TaskTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Task task = new TaskBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> task.getTags().remove(0));
    }

    @Test
    public void isSameTask() {
        // same object -> returns true
        assertTrue(HOMEWORK10.isSameTask(HOMEWORK10));

        // null -> returns false
        assertFalse(HOMEWORK10.isSameTask(null));

        // different priority -> returns false
        Task editedAlice = new TaskBuilder(HOMEWORK10).withPriority(VALID_PRIORITY_TASK2).build();
        assertTrue(HOMEWORK10.isSameTask(editedAlice));

        // different name -> returns false
        editedAlice = new TaskBuilder(HOMEWORK10).withName(VALID_NAME_TASK2).build();
        assertFalse(HOMEWORK10.isSameTask(editedAlice));

        // same name, same priority, different attributes -> returns true
        editedAlice =
                new TaskBuilder(HOMEWORK10)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .withTags(VALID_TAG_MA1521)
                        .build();
        assertTrue(HOMEWORK10.isSameTask(editedAlice));

        // same name, different attributes -> returns true
        editedAlice =
                new TaskBuilder(HOMEWORK10)
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .withTags(VALID_TAG_MA1521)
                        .build();
        assertTrue(HOMEWORK10.isSameTask(editedAlice));

        // same name, same priority, different attributes -> returns true
        editedAlice =
                new TaskBuilder(HOMEWORK10)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .withTags(VALID_TAG_MA1521)
                        .build();
        assertTrue(HOMEWORK10.isSameTask(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Task aliceCopy = new TaskBuilder(HOMEWORK10).build();
        assertTrue(HOMEWORK10.equals(aliceCopy));

        // same object -> returns true
        assertTrue(HOMEWORK10.equals(HOMEWORK10));

        // null -> returns false
        assertFalse(HOMEWORK10.equals(null));

        // different type -> returns false
        assertFalse(HOMEWORK10.equals(5));

        // different task -> returns false
        assertFalse(HOMEWORK10.equals(TASK2));

        // different name -> returns false
        Task editedAlice = new TaskBuilder(HOMEWORK10).withName(VALID_NAME_TASK2).build();
        assertFalse(HOMEWORK10.equals(editedAlice));

        // different priority -> returns false
        editedAlice = new TaskBuilder(HOMEWORK10).withPriority(VALID_PRIORITY_TASK2).build();
        assertFalse(HOMEWORK10.equals(editedAlice));

        // different address -> returns false
        editedAlice = new TaskBuilder(HOMEWORK10).withDescription(VALID_DESCRIPTION_TASK2).build();
        assertFalse(HOMEWORK10.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new TaskBuilder(HOMEWORK10).withTags(VALID_TAG_HELP).build();
        assertFalse(HOMEWORK10.equals(editedAlice));
    }
}
