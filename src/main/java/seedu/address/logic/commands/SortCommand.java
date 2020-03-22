package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;
import seedu.address.commons.core.Messages;


/** Adds a person to the address book. */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String[] ALLOWED_SORT_FIELDS = {"priority", "date"};

    public static final String MESSAGE_SUCCESS = "TaskList sorted by: %1$s";
    public static final String MESSAGE_SORT_UNKNOWN = "No such field to sort by %1$s!";
    public static final String MESSAGE_USAGE = String.format("%1$s: Sorts tasklist by certain field such as\n (priority|date)\n Example: %1$s priority", COMMAND_WORD);

    private String[] fields;

    public SortCommand(String [] fields) {
        this.fields = fields;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortFilteredTaskList(fields);
        return new CommandResult(
                String.format(
                        Messages.MESSAGE_FIELD_UNKNOWN,
                        String.join(" ", fields)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortCommand // instanceof handles nulls
                        && fields.equals(((SortCommand) other).fields)); // state check
    }
}
