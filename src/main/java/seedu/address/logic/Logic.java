package seedu.address.logic;

import java.nio.file.Path;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.task.Task;

/** API of the Logic component */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Attempts to complete user's input
     *
     * @param userInput Input from user
     * @return the result of completion
     * @throws CompletorException If no command is detected or command is in the wrong format
     */
    CompletorResult suggestCommand(String userInput) throws CompletorException;

    /**
     * Returns the TaskList.
     *
     * @see seedu.address.model.Model#getTaskList()
     */
    ReadOnlyTaskList getTaskList();

    /** Returns an unmodifiable view of the filtered list of tasks */
    ObservableList<Task> getFilteredTaskList();

    /** Returns the user prefs' task list file path. */
    Path getTaskListFilePath();

    /** Returns the user prefs' GUI settings. */
    GuiSettings getGuiSettings();

    /** Set the user prefs' GUI settings. */
    void setGuiSettings(GuiSettings guiSettings);

    ReadOnlyPomodoro getPomodoro();
}
