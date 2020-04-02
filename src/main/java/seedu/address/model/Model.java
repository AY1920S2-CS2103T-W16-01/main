package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.model.task.Task;

/** The API of the Model component. */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Task> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Replaces user prefs data with the data in {@code userPrefs}. */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /** Returns the user prefs. */
    ReadOnlyUserPrefs getUserPrefs();

    /** Returns the user prefs' GUI settings. */
    GuiSettings getGuiSettings();

    /** Sets the user prefs' GUI settings. */
    void setGuiSettings(GuiSettings guiSettings);

    /** Returns the user prefs' address book file path. */
    Path getTaskListFilePath();

    /** Sets the user prefs' address book file path. */
    void setTaskListFilePath(Path taskListFilePath);

    /** Replaces address book data with the data in {@code taskList}. */
    void setTaskList(ReadOnlyTaskList taskList);

    /** Returns the TaskList */
    ReadOnlyTaskList getTaskList();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasTask(Task task);

    /** Deletes the given person. The person must exist in the address book. */
    void deleteTask(Task target);

    /** Adds the given person. {@code person} must not already exist in the address book. */
    void addTask(Task task);

    /**
     * Replaces the given person {@code target} with {@code editedTask}. {@code target} must exist
     * in the address book. The person identity of {@code editedTask} must not be the same as
     * another existing person in the address book.
     */
    void setTask(Task target, Task editedTask);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    void setComparator(Comparator<Task>[] compare);
    
    void sortList();

    ReadOnlyPet getPet();

    void setPetName(String name);

    void incrementExp();

    void incrementPomExp();

    void updateMoodWhenDone();

    ReadOnlyPomodoro getPomodoro();

    void setPomodoroTask(Task task);

    // void setPomodoroTime(String time);

    void setPomodoroManager(PomodoroManager pomodoroManager);

    void setPetManager(PetManager PetManager);

    PomodoroManager getPomodoroManager();

    Statistics getStatistics();

    public void updateDataDatesStatistics();
}
