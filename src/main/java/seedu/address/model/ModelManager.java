package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import seedu.address.model.task.NameContainsKeywordsPredicate;
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
    private HashMap<Task, TimerTask> recurringTimerTasks = new HashMap<>();
    private Timer recurringTimer = new Timer();
    private TaskSaver taskSaver;

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
        this.setRecurringTimers();
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
    public void setTaskSaver(TaskSaver taskSaver) {
        this.taskSaver = taskSaver;
    }

    private void setRecurringTimers() {
        this.recurringTimer.cancel();
        this.recurringTimer = new Timer();
        this.recurringTimerTasks.clear();
        for (Task t : this.taskList.getTaskList()) {
            if (t.getOptionalRecurring().isPresent()) {
                TimerTask tt = this.generateTimerTask(t);
                recurringTimerTasks.put(t, tt);
                this.recurringTimer.scheduleAtFixedRate(
                        tt, t.getDelayToFirstTrigger(), t.getRecurPeriod());
            }
        }
    }

    private TimerTask generateTimerTask(Task t) {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(
                        () -> {
                            setTask(t, t.getRecurredTask());
                        });
            }
        };
    }

    private void cancelTimerTask(Task t) {
        if (this.recurringTimerTasks.containsKey(t)) {
            this.recurringTimerTasks.get(t).cancel();
        }
    }

    private void setTimer(Task t) {
        if (t.getOptionalRecurring().isPresent()) {
            TimerTask tt = this.generateTimerTask(t);
            recurringTimerTasks.put(t, tt);
            this.recurringTimer.scheduleAtFixedRate(
                    tt, t.getDelayToFirstTrigger(), t.getRecurPeriod());
        }
    }

    @Override
    public void setTaskList(ReadOnlyTaskList taskList) {
        this.taskList.resetData(taskList);
        this.setRecurringTimers();
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
        this.cancelTimerTask(target);
        taskList.removeTask(target);
    }

    // @Override
    // public Index getIndexOfNewTask() {
    //     List<Task> lastShownTask = getFilteredTaskList();
    //     return Index.fromZeroBased(lastShownTask.size());
    // }

    @Override
    public void addTask(Task task) {
        taskList.addTask(task);
        this.sortList();
        setTimer(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);
        taskList.setTask(target, editedTask);
        cancelTimerTask(target);
        setTimer(editedTask);
        this.sortList();
        if (taskSaver != null) {
            this.taskSaver.saveTask(this.taskList);
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

    // =========== Filtered Task List Accessors
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
        filteredTasks.setPredicate(
                predicate); // predicate should now be applied and evaluate to true for certain
        // threshold
        if (predicate instanceof NameContainsKeywordsPredicate) {
            System.out.println("list called??");
            NameContainsKeywordsPredicate namePredicate = (NameContainsKeywordsPredicate) predicate;
            Comparator<Task> comparator =
                    new Comparator<>() {
                        @Override
                        public int compare(Task task1, Task task2) {
                            namePredicate.test(task1);
                            int score1 = namePredicate.getScore();
                            namePredicate.test(task2);
                            int score2 = namePredicate.getScore();
                            return score1 < score2 ? -1 : 1;
                        }
                    };
            this.taskList.sort(comparator);
        }
    }

    @Override
    public void setComparator(Comparator<Task>[] comparators) {
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
