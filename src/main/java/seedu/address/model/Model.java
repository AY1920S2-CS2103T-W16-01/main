package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Observer;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.StatisticsManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/** The API of the Model component. */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Task> PREDICATE_SHOW_ALL_TASKS = unused -> true;

    /** Replaces user prefs data with the data in {@code userPrefs}. */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /** Returns the user prefs. */
    ReadOnlyUserPrefs getUserPrefs();

    /** Returns the user prefs' GUI settings. */
    GuiSettings getGuiSettings();

    /** Sets the user prefs' GUI settings. */
    void setGuiSettings(GuiSettings guiSettings);

    /** Returns the user prefs' task list file path. */
    Path getTaskListFilePath();

    /** Sets the user prefs' task list file path. */
    void setTaskListFilePath(Path taskListFilePath);

    /** Replaces task list data with the data in {@code taskList}. */
    void setTaskList(ReadOnlyTaskList taskList);

    @FunctionalInterface
    public interface TaskSaver {
        void saveTask(TaskList tasklist);
    }

    void setTaskSaver(TaskSaver taskSaver);

    /** Returns the TaskList */
    ReadOnlyTaskList getTaskList();

    /** Returns true if a task with the same identity as {@code task} exists in the task list. */
    boolean hasTask(Task task);

    /** Deletes the given task. The task must exist in the task list. */
    void deleteTask(Task target);

    /** Adds the given task. {@code task} must not already exist in the task list. */
    void addTask(Task task);

    Set<Tag> getTagSet();

    boolean hasTag(Tag t);

    /**
     * Replaces the given task {@code target} with {@code editedTask}. {@code target} must exist in
     * the task list. The task identity of {@code editedTask} must not be the same as another
     * existing task in the task list.
     */
    void setTask(Task target, Task editedTask);

    /** Returns an unmodifiable view of the filtered task list */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    void showAllTasks();

    void setComparator(Comparator<Task> comparator);

    void sortList();

    void sortSearchByRelevance(Comparator<Task> comparator);

    ReadOnlyPet getPet();

    PetManager getPetManager();

    void setPetName(String name);

    ReadOnlyPomodoro getPomodoro();

    void setPomodoroTask(Task task);

    Task getPomodoroTask();

    void setPomodoroDefaultTime(float defaultTimeInMin);

    void setPomodoroRestTime(float restTimeInMin);

    void setPomodoroTimeLeft(float timeLeft);

    void setPomodoroManager(PomodoroManager pomodoroManager);

    void setPetManager(PetManager PetManager);

    void setStatisticsManager(StatisticsManager statisticsManager);

    PomodoroManager getPomodoroManager();

    ReadOnlyStatistics getStatistics();

    /**
     * Notifies observers when a change is made. Observer in this case is the MainWindow.
     *
     * @throws CommandException
     */
    void notifyMainWindow(String input) throws CommandException;

    void addObserver(Observer observer);

    void updateDataDatesStatistics();

    void updatesDayDataStatistics(DayData dayData);

    DayData getDayDataFromDateStatistics(Date date);
}
