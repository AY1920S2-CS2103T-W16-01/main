package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TaskList;
import seedu.address.model.task.NameContainsKeywordsPredicate;
import seedu.address.model.task.Task;
import seedu.address.testutil.EditTaskDescriptorBuilder;

/** Contains helper methods for testing commands. */
public class CommandTestUtil {

    public static final String VALID_NAME_TASK1 = "Homework 1";
    public static final String VALID_NAME_TASK2 = "Lab 2";
    public static final String VALID_PRIORITY_TASK1 = "1";
    public static final String VALID_PRIORITY_TASK2 = "2";
    public static final String VALID_DESCRIPTION_TASK1 = "Pages 10 - 12";
    public static final String VALID_DESCRIPTION_TASK2 = "Introduction to TCP";
    public static final String VALID_TAG_MA1521 = "MA1521";
    public static final String VALID_TAG_HELP = "HELP";
    public static final String VALID_REMINDER = "15/03/21@16:07";
    public static final String VALID_RECURRING = "d";

    public static final String NAME_DESC_TASK1 = " " + PREFIX_NAME + VALID_NAME_TASK1;
    public static final String NAME_DESC_TASK2 = " " + PREFIX_NAME + VALID_NAME_TASK2;
    public static final String PRIORITY_DESC_TASK1 = " " + PREFIX_PRIORITY + VALID_PRIORITY_TASK1;
    public static final String PRIORITY_DESC_TASK2 = " " + PREFIX_PRIORITY + VALID_PRIORITY_TASK2;
    public static final String DESCRIPTION_DESC_TASK1 =
            " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_TASK1;
    public static final String DESCRIPTION_DESC_TASK2 =
            " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_TASK2;
    public static final String TAG_DESC_HELP = " " + PREFIX_TAG + VALID_TAG_HELP;
    public static final String TAG_DESC_MA1521 = " " + PREFIX_TAG + VALID_TAG_MA1521;
    public static final String REMINDER = " " + PREFIX_REMINDER + VALID_REMINDER;
    public static final String RECURRING = " " + PREFIX_RECURRING + VALID_RECURRING;

    public static final String INVALID_NAME_DESC =
            " " + PREFIX_NAME + "math&"; // '&' not allowed in names
    public static final String INVALID_PRIORITY_DESC =
            " " + PREFIX_PRIORITY + "99"; // any number not 1,2 or 3 not allowed in priority
    public static final String INVALID_TAG_DESC =
            " " + PREFIX_TAG + "tricky*"; // '*' not allowed in tags
    public static final String INVALID_REMINDER =
            " " + PREFIX_REMINDER + "15/03/20@@@16:07"; // invalid date
    public static final String INVALID_REMINDER_PAST =
            " " + PREFIX_REMINDER + "15/03/20@16:07"; // invalid date
    public static final String INVALID_RECURRING = " " + PREFIX_RECURRING + "m"; // invalid format

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditTaskDescriptor DESC_TASK1;
    public static final EditCommand.EditTaskDescriptor DESC_TASK2;

    static {
        DESC_TASK1 =
                new EditTaskDescriptorBuilder() // KIV might need to change this to include reminder
                        .withName(VALID_NAME_TASK1)
                        .withPriority(VALID_PRIORITY_TASK1)
                        .withDescription(VALID_DESCRIPTION_TASK1)
                        .withTags(VALID_TAG_HELP)
                        .build();
        DESC_TASK2 =
                new EditTaskDescriptorBuilder()
                        .withName(VALID_NAME_TASK2)
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .withTags(VALID_TAG_MA1521, VALID_TAG_HELP)
                        .build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(
            Command command,
            Model actualModel,
            CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(
            Command command, Model actualModel, String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the task list, filtered task list and selected task in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(
            Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        TaskList expectedTaskList = new TaskList(actualModel.getTaskList());
        List<Task> expectedFilteredList = new ArrayList<>(actualModel.getFilteredTaskList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedTaskList, actualModel.getTaskList());
        assertEquals(expectedFilteredList, actualModel.getFilteredTaskList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex}
     * in the {@code model}'s task list.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] name = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(name)));
        assertEquals(task, model.getFilteredTaskList().get(0));
    }
}
