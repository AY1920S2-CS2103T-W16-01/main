package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/** Deletes a task identified using it's displayed index from the task list. */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Deletes one or multiple tasks identified by the index number(s) used in the displayed tasks list.\n"
                    + "Parameters: INDEX1 INDEX2 (must be positive integers separated by a SPACE)\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " 1 2";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task(s): ";

    private final Index[] targetIndices;

    public DeleteCommand(Index[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        StringBuilder tasksDeleted = new StringBuilder(MESSAGE_DELETE_TASK_SUCCESS);
        HashSet<Task> toDeleteTasks = new HashSet<>();
        for (Index targetIndex : targetIndices) {
            targetIndex.getZeroBased();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }
            Task taskToDelete = lastShownList.get(targetIndex.getZeroBased());
            if (taskToDelete.equals(model.getPomodoroTask())) {
                throw new CommandException(Messages.MESSAGE_TASK_IN_PROGRESS);
            }
            toDeleteTasks.add(taskToDelete);
        }
        for (Task t : toDeleteTasks) {
            model.deleteTask(t);
            tasksDeleted.append(String.format("%n%s", t));
        }
        return new CommandResult(tasksDeleted.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof DeleteCommand) {
            Index[] myTargetIndices = targetIndices;
            Index[] otherTargetIndices = ((DeleteCommand) other).targetIndices;
            for (int i = 0; i < myTargetIndices.length; i++) {
                if (!myTargetIndices[i].equals(otherTargetIndices[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
