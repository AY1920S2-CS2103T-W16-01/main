package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Observer;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.StatisticsManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.dayData.DayData;
import seedu.address.model.task.Task;

/** Represents the in-memory model of the task list data. */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskList taskList;
    private final Pomodoro pomodoro;
    private final Statistics statistics;
    private final Pet pet;
    private final UserPrefs userPrefs;
    private FilteredList<Task> filteredTasks;
    private Comparator<Task>[] comparators = new Comparator[0];

    private PomodoroManager pomodoroManager;
    private PetManager petManager;
    private StatisticsManager statisticsManager;
    private ArrayList<Observer> observers;

    /** Initializes a ModelManager with the given taskList and userPrefs. */
    public ModelManager(
            ReadOnlyTaskList taskList,
            ReadOnlyPet pet,
            ReadOnlyPomodoro pomodoro,
            ReadOnlyStatistics statistics,
            ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(taskList, userPrefs);

        logger.fine("Initializing with Task List: " + taskList + " and user prefs " + userPrefs);

        this.taskList = new TaskList(taskList);
        this.pet = new Pet(pet); // initialize a pet as a model
        this.pomodoro = new Pomodoro(pomodoro); // initialize a pomodoro as a model
        this.statistics = new Statistics(statistics); // initialize a Statistics as a model
        logger.info(String.format("Initializing with Statistics: %s", this.statistics.toString()));

        this.petManager = new PetManager();
        this.petManager.setPet(this.pet);

        this.statisticsManager = new StatisticsManager();
        this.statisticsManager.setStatistics(this.statistics);

        this.userPrefs = new UserPrefs(userPrefs);
        filteredTasks = new FilteredList<>(this.taskList.getTaskList());
        this.observers = new ArrayList<>();
    }

    public ModelManager() {
        this(new TaskList(), new Pet(), new Pomodoro(), new Statistics(), new UserPrefs());
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

    /** sortList after task is added so that new task will follow the existing sort order */
    @Override
    public void addTask(Task task) {
        taskList.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    /** sortList after task is edited so that edited task will follow the existing sort order */
    @Override
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);
        taskList.setTask(target, editedTask);
        try {
            notifyObservers();
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }

    // =========== Subject Methods for Observer
    // ================================================================================
    public void notifyObservers() throws CommandException {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // =========== Filtered Task List Methods
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of @FXML
     * Serves as a reference point for TaskListPanel.java to update display {@code
     * versionedTaskList}
     */
    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return this.filteredTasks;
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    /** Used when a predicate is applied to show the more relevant serach results */
    @Override
    public void sortWithComparator(Comparator<Task> comparator) {
        this.taskList.sort(comparator);
    }

    // ================ Sort list methods

    /** Used when for the sort command when sorting by multiple fields */
    @Override
    public void setComparators(Comparator<Task>[] comparators) {
        requireNonNull(comparators);
        this.comparators = comparators;
        this.sortList();
    }

    @Override
    public void sortList() {
        for (int i = this.comparators.length - 1; i >= 0; i--) {
            this.taskList.sort(comparators[i]);
        }
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

    // ============================ Pet Manager

    @Override
    public ReadOnlyPet getPet() {
        return pet;
    }

    @Override
    public PetManager getPetManager() {
        return petManager;
    }

    @Override
    public void setPetName(String name) {
        this.pet.setName(name);
    }

    @Override
    public void setPetManager(PetManager petManager) {
        this.petManager = petManager;
        this.petManager.setPet(this.pet);
    }

    // ============================ Pomodoro Manager

    public ReadOnlyPomodoro getPomodoro() {
        return pomodoro;
    }

    public void setPomodoroTask(Task task) {
        this.pomodoro.setTask(task);
    }

    public Task getPomodoroTask() {
        return this.pomodoro.getRunningTask();
    }

    public void setPomodoroRestTime(float restTimeInMin) {
        this.pomodoro.setRestTime(Float.toString(restTimeInMin));
    }

    public void setPomodoroDefaultTime(float defaultTimeInMin) {
        this.pomodoro.setDefaultTime(Float.toString(defaultTimeInMin));
    }

    public void setPomodoroTimeLeft(float timeLeft) {
        this.pomodoro.setTimeLeft(Float.toString(timeLeft));
    }

    public void setPomodoroManager(PomodoroManager pomodoroManager) {
        this.pomodoroManager = pomodoroManager;
    }

    public PomodoroManager getPomodoroManager() {
        return pomodoroManager;
    }

    // ============================ Statistics Manager

    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
        this.statisticsManager.setStatistics(this.statistics);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public ObservableList<DayData> getCustomQueue() {
        return statistics.getCustomQueue();
    }

    public void updateDataDatesStatistics() {
        statistics.updateDataDates();
    }
}
