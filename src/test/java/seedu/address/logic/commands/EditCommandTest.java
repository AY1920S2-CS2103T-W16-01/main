package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.DESC_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.testutil.EditTaskDescriptorBuilder;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit
 * tests for EditCommand.
 */
public class EditCommandTest {

    private Model model =
            new ModelManager(getTypicalTaskList(), new Pet(), new Pomodoro(), new Statistics(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Task editedTask = new TaskBuilder().build();
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(editedTask).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel =
                new ModelManager(
                        new TaskList(model.getTaskList()),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTask(model.getFilteredTaskList().get(0), editedTask);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastTask = Index.fromOneBased(model.getFilteredTaskList().size());
        Task lastTask = model.getFilteredTaskList().get(indexLastTask.getZeroBased());

        TaskBuilder taskInList = new TaskBuilder(lastTask);
        Task editedTask =
                taskInList
                        .withName(VALID_NAME_TASK2)
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withTags(VALID_TAG_MA1521)
                        .build();

        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder()
                        .withName(VALID_NAME_TASK2)
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withTags(VALID_TAG_MA1521)
                        .build();
        EditCommand editCommand = new EditCommand(indexLastTask, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel =
                new ModelManager(
                        new TaskList(model.getTaskList()),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTask(lastTask, editedTask);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditTaskDescriptor());
        Task editedTask = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedTask);

        Model expectedModel =
                new ModelManager(
                        new TaskList(model.getTaskList()),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Task taskInFilteredList =
                model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        Task editedPerson = new TaskBuilder(taskInFilteredList).withName(VALID_NAME_TASK2).build();
        EditCommand editCommand =
                new EditCommand(
                        INDEX_FIRST_PERSON,
                        new EditTaskDescriptorBuilder().withName(VALID_NAME_TASK2).build());

        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel =
                new ModelManager(
                        new TaskList(model.getTaskList()),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTask(model.getFilteredTaskList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTaskUnfilteredList_failure() {
        Task firstTask = model.getFilteredTaskList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(firstTask).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Task taskInList = model.getTaskList().getTaskList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand =
                new EditCommand(
                        INDEX_FIRST_PERSON, new EditTaskDescriptorBuilder(taskInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder().withName(VALID_NAME_TASK2).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list, but smaller than size of
     * address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskList().getTaskList().size());

        EditCommand editCommand =
                new EditCommand(
                        outOfBoundIndex,
                        new EditTaskDescriptorBuilder().withName(VALID_NAME_TASK2).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_TASK1);

        // same values -> returns true
        EditTaskDescriptor copyDescriptor = new EditTaskDescriptor(DESC_TASK1);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_TASK1)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_TASK2)));
    }
}
