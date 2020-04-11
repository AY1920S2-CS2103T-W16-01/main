package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Represents the Command result for a pom command
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class PomCommandResult extends CommandResult {

    private final String pommedTask;

    private final float timerAmount;

    private final Model model;

    private final int taskIndex;

    private List<Task> originList;

    private final PomCommand.POM_TYPE pomType;

    /** Constructs a {@code PomCommandResult} with the specified fields. */
    public PomCommandResult(
            String feedbackToUser,
            String pommedTask,
            float timerAmount,
            Model model,
            int taskIndex,
            List<Task> originList,
            PomCommand.POM_TYPE pomType) {
        super(requireNonNull(feedbackToUser), false, false);
        this.pommedTask = pommedTask;
        this.timerAmount = timerAmount;
        this.model = requireNonNull(model);
        this.pomType = requireNonNull(pomType);
        this.taskIndex = taskIndex;
        this.originList = originList;
    }

    public String getPommedTask() {
        return pommedTask;
    }

    public float getTimerAmountInMin() {
        return timerAmount;
    }

    public boolean getIsPause() {
        return pomType == PomCommand.POM_TYPE.PAUSE;
    }

    public boolean getIsContinue() {
        return pomType == PomCommand.POM_TYPE.CONTINUE;
    }

    public boolean getIsStop() {
        return pomType == PomCommand.POM_TYPE.STOP;
    }

    public boolean getIsNormal() {
        return pomType == PomCommand.POM_TYPE.NORMAL;
    }

    public Model getModel() {
        return model;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public List<Task> getOriginList() {
        return originList;
    }
}
