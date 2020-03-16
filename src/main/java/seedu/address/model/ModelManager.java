package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

// TODO Set Pet attributes via ModelManager

/** Represents the in-memory model of the address book data. */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskList taskList;
    private final Pet pet;
    private final UserPrefs userPrefs;
    private final FilteredList<Task> filteredTasks;

    /** Initializes a ModelManager with the given taskList and userPrefs. */
    public ModelManager(ReadOnlyTaskList taskList, ReadOnlyPet pet, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(taskList, userPrefs);

        logger.fine("Initializing with Task List: " + taskList + " and user prefs " + userPrefs);

        this.taskList = new TaskList(taskList);
        this.pet = new Pet(pet); // initialize a pet as a model
        logger.info(String.format("Initializing with Pet: %s", this.pet.toString()));

        this.userPrefs = new UserPrefs(userPrefs);
        filteredTasks = new FilteredList<>(this.taskList.getTaskList());
    }

    public ModelManager() {
        this(new TaskList(), new Pet(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getTaskListFilePath() {
        return userPrefs.getTaskListFilePath();
    }

    @Override
    public void setTaskListFilePath(Path taskListFilePath) {
        requireNonNull(taskListFilePath);
        userPrefs.setTaskListFilePath(taskListFilePath);
    }

    // =========== TaskList
    // ================================================================================

    @Override
    public void setTaskList(ReadOnlyTaskList taskList) {
        this.taskList.resetData(taskList);
    }

    @Override
    public ReadOnlyTaskList getTaskList() {
        return taskList;
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return taskList.hasTask(task);
    }

    @Override
    public void deleteTask(Task target) {
        taskList.removeTask(target);
    }

    @Override
    public void addTask(Task task) {
        taskList.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        taskList.setTask(target, editedTask);
    }

    // =========== Filtered Task List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of
     * {@code versionedTaskList}
     */
    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return filteredTasks;
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return taskList.equals(other.taskList)
                && userPrefs.equals(other.userPrefs)
                && filteredTasks.equals(other.filteredTasks);
    }

    // TODO Add a manager for pets

    public ReadOnlyPet getPet() {
        return pet;
    }

    public void setPetName(String name) {
        this.pet.setName(name);
    }
}
