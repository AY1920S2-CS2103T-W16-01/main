package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.TasksDoneData;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/** Deletes a person identified using it's displayed index from the address book. */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Marks the task identified by the index number used in the displayed task list as done.\n"
                    + "Parameters: 1-INDEXed (must be a positive integer)\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Done Task(s): ";

    private final Index[] targetIndices;

    public DoneCommand(Index[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        StringBuilder tasksDone = new StringBuilder(MESSAGE_DONE_TASK_SUCCESS);
        HashSet<Task> toDoneTaskList = new HashSet<>();

        Task pommedTask = null;

        for (Index targetIndex : targetIndices) {
            targetIndex.getZeroBased();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Task taskToDelete = lastShownList.get(targetIndex.getZeroBased());
            if (taskToDelete.getDone().getIsDone()) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_TO_BE_DONED);
            }
            toDoneTaskList.add(taskToDelete);
        }

        for (Task taskToEdit : toDoneTaskList) {
            Task editedTask = createDoneTask(taskToEdit);

            // Normal statistics update

            // If task to be done is being pommed...
            if (taskToEdit.equals(model.getPomodoro().getRunningTask())) {
                pommedTask = taskToEdit;
                model.setPomodoroTask(null);
                updateStatisticsPomDone(model);
            } else {
                tasksDone.append(String.format("%n%s", editedTask));
                model.setTask(taskToEdit, editedTask);
                updateStatisticsRegularDone(model);
            }
            // increment Pet EXP after completing a task
            model.incrementExp();
            model.updateMoodWhenDone();
        }

        // The last task to show is the pommed task
        if (pommedTask != null) {
            tasksDone.append(String.format("\n----Pom Task Done----"));
            tasksDone.append(String.format("%n%s", pommedTask));
            PomodoroManager pm = model.getPomodoroManager();
            pm.pause();
            pm.doneTask();
            // Pause pom timer and check if wanna continue
            tasksDone.append("\n" + pm.CHECK_DONE_MIDPOM_MESSAGE);
            pm.checkMidPomDoneActions();
        }

        return new CommandResult(tasksDone.toString());
    }

    private static Date getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date(now.format(Date.dateFormatter));
        return date;
    }

    private static Task createDoneTask(Task taskToEdit) {
        assert taskToEdit != null;

        Name updatedName = taskToEdit.getName();
        Priority updatedPriority = taskToEdit.getPriority();
        Description updatedDescription = taskToEdit.getDescription();
        Set<Tag> updatedTags = taskToEdit.getTags();

        return new Task(
                updatedName, updatedPriority, updatedDescription, new Done("Y"), updatedTags);
    }

    private static void updateStatisticsRegularDone(Model model) {
        model.updateDataDatesStatistics();
        Date dateOnDone = getCurrentDate();
        Statistics stats = model.getStatistics();
        DayData dayData = stats.getDayDataFromDate(dateOnDone);
        DayData updatedDayData =
                new DayData(
                        dateOnDone,
                        dayData.getPomDurationData(),
                        new TasksDoneData("" + (dayData.getTasksDoneData().value + 1)));
        stats.updatesDayData(updatedDayData);
    }

    private static void updateStatisticsPomDone(Model model) {
        // Get the data from pomodoro manager
        model.getPomodoroManager().updateStatistics(model);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof DoneCommand) {
            Index[] myTargetIndices = targetIndices;
            Index[] otherTargetIndices = ((DoneCommand) other).targetIndices;
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
