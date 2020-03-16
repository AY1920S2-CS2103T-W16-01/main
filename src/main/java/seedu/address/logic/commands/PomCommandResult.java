package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.task.Task;

public class PomCommandResult extends CommandResult {

    private final String pommedTask;

    private final float timerAmount;

    private final boolean isPause;

    private final boolean isContinue;

    private final Model model;

    private final int taskIndex;

    private List<Task> originList;

    /** Constructs a {@code PomCommandResult} with the specified fields. */
    public PomCommandResult(String feedbackToUser, String pommedTask, float timerAmount,
        Model model, int taskIndex, List<Task> originList, boolean isPause, boolean isContinue) {
        super(requireNonNull(feedbackToUser), false, false);
        this.pommedTask = pommedTask;
        this.timerAmount = timerAmount;
        this.model = requireNonNull(model);
        this.isPause = requireNonNull(isPause);
        this.isContinue = requireNonNull(isContinue);
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
        return isPause;
    }

    public boolean getIsContinue() {
        return isContinue;
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
