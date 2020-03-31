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
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit
 * tests for {@code DoneCommand}.
 */
public class DoneCommandTest {

    private Model model =
            new ModelManager(
                    getTypicalTaskList(),
                    new Pet(),
                    new Pomodoro(),
                    new Statistics(),
                    new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Task taskToDone = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        Task doneTask = new TaskBuilder(taskToDone).withDone().build();

        DoneCommand doneCommand = new DoneCommand(new Index[] {INDEX_FIRST_PERSON});

        StringBuilder expectedMessage = new StringBuilder(DoneCommand.MESSAGE_DONE_TASK_SUCCESS);
        expectedMessage.append(String.format("%n%s", doneTask));

        ModelManager expectedModel =
                new ModelManager(
                        model.getTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTask(
                model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased()), doneTask);

        assertCommandSuccess(doneCommand, model, expectedMessage.toString(), expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DoneCommand doneCommand = new DoneCommand(new Index[] {outOfBoundIndex});

        assertCommandFailure(doneCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Task taskToDone = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        Task doneTask = new TaskBuilder(taskToDone).withDone().build();

        DoneCommand doneCommand = new DoneCommand(new Index[] {INDEX_FIRST_PERSON});

        StringBuilder expectedMessage = new StringBuilder(DoneCommand.MESSAGE_DONE_TASK_SUCCESS);
        expectedMessage.append(String.format("%n%s", doneTask));

        ModelManager expectedModel =
                new ModelManager(
                        model.getTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTask(
                model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased()), doneTask);

        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        assertCommandSuccess(doneCommand, model, expectedMessage.toString(), expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskList().getTaskList().size());

        DoneCommand doneCommand = new DoneCommand(new Index[] {outOfBoundIndex});

        assertCommandFailure(doneCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DoneCommand doneFirstCommand = new DoneCommand(new Index[] {INDEX_FIRST_PERSON});
        DoneCommand doneSecondCommand = new DoneCommand(new Index[] {INDEX_SECOND_PERSON});

        // same object -> returns true
        assertTrue(doneFirstCommand.equals(doneFirstCommand));

        // same values -> returns true
        DoneCommand deleteFirstCommandCopy = new DoneCommand(new Index[] {INDEX_FIRST_PERSON});
        assertTrue(doneFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(doneFirstCommand.equals(1));

        // null -> returns false
        assertFalse(doneFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(doneFirstCommand.equals(doneSecondCommand));
    }

    /** Updates {@code model}'s filtered list to show no one. */
    private void showNoPerson(Model model) {
        model.updateFilteredTaskList(p -> false);

        assertTrue(model.getFilteredTaskList().isEmpty());
    }
}
