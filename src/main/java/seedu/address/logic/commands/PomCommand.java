package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;

import java.util.List;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * This class represents the Command for the "pom" input by the user. It starts the pomodoro
 * function and sets a specified task in focus.
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class PomCommand extends Command {

    public static final String COMMAND_WORD = "pom";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Starts the pomodoro timer, focusing on "
                    + "the task identified by the index number used in the displayed task list.\n"
                    + "Parameters: 1-INDEXed (must be a positive integer)\n"
                    + "Time value must be greater than 0\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " 1 "
                    + PREFIX_TIMER
                    + " 10";

    public static final String PAUSE_MESSAGE = "Pomodoro paused.";

    public static final String CONTINUE_MESSAGE = "Pomodoro continuing.";

    public static final String STOP_MESSAGE = "Pomodoro stopped.";

    public static final String NO_POM = "Sorry, you've got no tasks being POMmed.";

    private final Index targetIndex;

    private final float timerAmount;

    private final POM_TYPE pomType;

    public enum POM_TYPE {
        NORMAL,
        PAUSE,
        CONTINUE,
        STOP;
    }

    /**
     * PomCommand constructor.
     *
     * @param targetIndex of the task to be focused on during pomodoro.
     */
    public PomCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.timerAmount = -1;
        this.pomType = POM_TYPE.NORMAL;
    }

    /**
     * PomCommand constructor.
     *
     * @param targetIndex of the task to be focused on during pomodoro.
     * @param timerAmount time in minutes for particular pomodoro cycle.
     */
    public PomCommand(Index targetIndex, float timerAmount) {
        this.targetIndex = targetIndex;
        this.timerAmount = timerAmount * 60;
        this.pomType = POM_TYPE.NORMAL;
    }

    /**
     * PomCommand constructor.
     *
     * @param pomType indicating the type of pomodoro command.
     */
    public PomCommand(POM_TYPE pomType) {
        this.targetIndex = null;
        this.timerAmount = 0;
        this.pomType = pomType;
    }

    /**
     * Executes Pom behaviour.
     *
     * @param model of the app's current state.
     * @return the CommandResult resulting for the execution of a PomCommand instance.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        PomodoroManager pm = model.getPomodoroManager();

        if (pomType == POM_TYPE.PAUSE) {
            try {
                pm.pause();
                return new PomCommandResult(PAUSE_MESSAGE, null, 0, model, -1, null, pomType);
            } catch (NullPointerException ne) {
                return new PomCommandResult(NO_POM, null, 0, model, -1, null, pomType);
            }
        }

        if (pomType == POM_TYPE.CONTINUE) {
            try {
                pm.unpause();
                return new PomCommandResult(CONTINUE_MESSAGE, null, 0, model, -1, null, pomType);
            } catch (NullPointerException ne) {
                return new PomCommandResult(NO_POM, null, 0, model, -1, null, pomType);
            }
        }

        if (pomType == POM_TYPE.STOP) {
            try {
                pm.stop();
                return new PomCommandResult(STOP_MESSAGE, null, 0, model, -1, null, pomType);
            } catch (NullPointerException ne) {
                return new PomCommandResult(NO_POM, null, 0, model, -1, null, pomType);
            }
        }

        List<Task> lastShownList = model.getFilteredTaskList();
        int index = targetIndex.getZeroBased();
        if (index >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToPom = lastShownList.get(index);

        pm.startTrackTask();

        if (taskToPom.getDone().getIsDone()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_TO_BE_DONED);
        }

        String resultText =
                model.getPomodoroTask() == null
                        ? "Pomming task: " + taskToPom.toString()
                        : "Switched pom task: " + taskToPom.toString();

        // Update the pomodoro model
        model.setPomodoroTask(taskToPom);

        System.out.println("Hardy: " + (pm.getDefaultStartTime()));

        return new PomCommandResult(
                resultText,
                taskToPom.getName().toString(),
                ((int) timerAmount) == -1 ? pm.getDefaultStartTime() : timerAmount,
                model,
                index,
                lastShownList,
                pomType);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PomCommand)) {
            return false;
        }

        // state check
        PomCommand e = (PomCommand) other;
        return targetIndex.equals(e.targetIndex)
                && ((int) timerAmount) == ((int) e.timerAmount)
                && (pomType == e.pomType);
    }
}
