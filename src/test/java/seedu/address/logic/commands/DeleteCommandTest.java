package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.Statistics;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit
 * tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model =
            new ModelManager(
                    getTypicalTaskList(),
                    new Pet(),
                    new Pomodoro(),
                    new Statistics(),
                    new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] {INDEX_FIRST_PERSON});

        StringBuilder expectedMessage =
                new StringBuilder(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        expectedMessage.append(String.format("%n%s", taskToDelete));

        ModelManager expectedModel =
                new ModelManager(
                        model.getTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage.toString(), expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] {outOfBoundIndex});

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] {INDEX_FIRST_PERSON});

        StringBuilder expectedMessage =
                new StringBuilder(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        expectedMessage.append(String.format("%n%s", taskToDelete));

        Model expectedModel =
                new ModelManager(
                        model.getTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.deleteTask(taskToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage.toString(), expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskList().getTaskList().size());

        DeleteCommand deleteCommand = new DeleteCommand(new Index[] {outOfBoundIndex});

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(new Index[] {INDEX_FIRST_PERSON});
        DeleteCommand deleteSecondCommand = new DeleteCommand(new Index[] {INDEX_SECOND_PERSON});

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(new Index[] {INDEX_FIRST_PERSON});
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /** Updates {@code model}'s filtered list to show no one. */
    private void showNoPerson(Model model) {
        model.updateFilteredTaskList(p -> false);

        assertTrue(model.getFilteredTaskList().isEmpty());
    }
}
