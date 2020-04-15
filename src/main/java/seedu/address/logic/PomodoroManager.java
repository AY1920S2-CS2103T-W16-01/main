package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.commands.PomCommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.TaskListParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.PomDurationData;
import seedu.address.model.dayData.TasksDoneData;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.ui.MainWindow;
import seedu.address.ui.PomodoroDisplay;
import seedu.address.ui.ResultDisplay;

/**
 * The manager app for the pomodoro feature.
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class PomodoroManager {

    private Integer defaultStartTime;
    private Integer startTime;
    private Integer restTime;
    private Timeline timeline;
    private Label timerLabel;
    private ResultDisplay resultDisplay;
    private PomodoroDisplay pomodoroDisplay;
    private MainWindow mainWindow;
    private IntegerProperty timeSeconds;
    private Model model;
    private List<Task> originList;
    private int taskIndex;

    private LocalDateTime startDateTime, endDateTime;

    private final int SECONDS_IN_A_DAY = 24 * 60 * 60;

    public enum PROMPT_STATE {
        NONE,
        CHECK_DONE,
        CHECK_TAKE_BREAK,
        CHECK_DONE_MIDPOM,
        CHECK_RESUME_LAST;
    }

    public final String CHECK_DONE_MESSAGE =
            "Did you manage to finish the last task?\n"
                    + "(Y) - Task will be set to done. (N) - No changes.";

    public final String CHECK_TAKE_BREAK_MESSAGE =
            "Shall we take a 5-min break?\n" + "(Y) - 5-min timer begins. (N) - App goes neutral.";

    public final String CHECK_DONE_MIDPOM_MESSAGE =
            "Great! Would you like to continue with another task\n"
                    + "(pom <index>) - next task pommed with remaining time. (N) - App goes neutral.";

    public final String CHECK_RESUME_LAST_MESSAGE =
            "Welcome back! You had a task mid-pom when you left. Carry on?\n"
                    + "(Y) - pomodoro will resume on that task. (N) - Pomodoro cancels. App neutral.";

    private PROMPT_STATE promptState;

    /**
     * PomodoroManager constructor.
     *
     * @param model of the app's current state.
     */
    public PomodoroManager(Model model) {
        promptState = PROMPT_STATE.NONE;
        this.model = model;
    }

    /**
     * Setter to the pomodoro's UI element.
     *
     * @param pomodoroDisplay representing the UI element.
     */
    public void setPomodoroDisplay(PomodoroDisplay pomodoroDisplay) {
        this.pomodoroDisplay = pomodoroDisplay;
    }

    /** Getter for the pomodoro's UI element. */
    public PomodoroDisplay getPomodoroDisplay() {
        return pomodoroDisplay;
    }

    /** Setter for the pomodoro's UI element. */
    public void setResultDisplay(ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
    }

    /** Setter for the app's MainWindow instance. */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /** Returns the currently set default pomodoro time as string in the mm:ss format. */
    public String getDefaultStartTimeAsString() {
        int secondsRemaining = defaultStartTime;
        int minutePortion = secondsRemaining / 60;
        int secondPortion = secondsRemaining % 60;
        return String.format("%02d:%02d", minutePortion, secondPortion);
    }

    /** Getter for the currently set default pomodoro time in seconds. */
    public Integer getDefaultStartTime() {
        return defaultStartTime;
    }

    /** Getter for the currently set rest time in seconds. */
    public Integer getRestTime() {
        return restTime;
    }

    /**
     * Setter for the default pomodoro time amount.
     *
     * @param defaultStartTimeInMin new value to be updated.
     */
    public void setDefaultStartTime(float defaultStartTimeInMin) {
        this.defaultStartTime = (int) (defaultStartTimeInMin * 60);
        model.setPomodoroDefaultTime(defaultStartTimeInMin);
    }

    /**
     * Setter for the pomodoro rest time amount.
     *
     * @param restTimeInMin new value to be updated.
     */
    public void setRestTime(float restTimeInMin) {
        this.restTime = (int) (restTimeInMin * 60);
        model.setPomodoroRestTime(restTimeInMin);
    }

    /**
     * Setter for the timer label UI element.
     *
     * @param timerLabel representing the UI element.
     */
    public void setTimerLabel(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    /**
     * Start a pomodoro cycle.
     *
     * @param timeInSec the pomodoro time amount for particular instance of the cycle.
     */
    public void start(float timeInSec) {
        startTime = (int) (timeInSec);
        startTime = startTime > SECONDS_IN_A_DAY ? SECONDS_IN_A_DAY : startTime;
        timeSeconds = new SimpleIntegerProperty(startTime);
        configureUi();
        configureTimer();
        promptState = PROMPT_STATE.NONE;
    }

    /**
     * Pauses a running pomodoro cycle. Timer is halted, not deleted. Task in progress is retained.
     *
     * @throws NullPointerException when no pomodoro cycle is running.
     */
    public void pause() throws NullPointerException {
        try {
            timeline.pause();
        } catch (NullPointerException ne) {
            throw ne;
        }
    }

    /**
     * Resumes a paused pomodoro cycle. Timer carries on with remaining time.
     *
     * @throws NullPointerException when no pomodoro cycle is running.
     */
    public void unpause() throws NullPointerException {
        try {
            timeline.play();
        } catch (NullPointerException ne) {
            throw ne;
        }
    }

    /**
     * Ceases the running pomodoro cycle. Timer is reset. App is set back to neutral.
     *
     * @throws NullPointerException when no pomodoro cycle is running.
     */
    public void stop() throws NullPointerException {
        try {
            timeline.stop();
            reset();
            timeline = null;
            model.setPomodoroTask(null);
        } catch (NullPointerException ne) {
            throw ne;
        }
    }

    /** Sets the pomodoro to its neutral state. */
    public void reset() {
        timerLabel.textProperty().unbind();
        pomodoroDisplay.reset();
    }

    /** Prepares the necessary UI elements for the a pomodoro cycle. */
    private void configureUi() {
        timerLabel
                .textProperty()
                .bind(
                        Bindings.createStringBinding(
                                () -> {
                                    if (timeSeconds.getValue() == null) {
                                        return "";
                                    } else {
                                        int secondsRemaining = timeSeconds.get();
                                        int minutePortion = secondsRemaining / 60;
                                        int secondPortion = secondsRemaining % 60;
                                        return String.format(
                                                "%02d:%02d", minutePortion, secondPortion);
                                    }
                                },
                                timeSeconds));
    }

    /** Prepares the timer system for a pomodoro cycle to happen. */
    private void configureTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(startTime);
        timeline = new Timeline();
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(startTime + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
        timeline.setOnFinished(
                event -> {
                    // Update the Pet at timer expiry.
                    PetManager petManager = model.getPetManager();
                    petManager.incrementPomExp();
                    petManager.updateDisplayElements();
                    mainWindow.updatePetDisplay();

                    this.setPromptState(PROMPT_STATE.CHECK_DONE);
                    resultDisplay.setFeedbackToUser(CHECK_DONE_MESSAGE);
                    mainWindow.setPomCommandExecutor();
                    mainWindow.setTabFocusTasks();
                    model.setPomodoroTask(null);
                    endDateTime = LocalDateTime.now();

                    // Update the Statistics at timer expiry.
                    updateStatistics(model);

                    pomodoroDisplay.playDone();
                });
    }

    /**
     * Updates the app's Statistics system.
     *
     * @param model of the app's current state.
     */
    public void updateStatistics(Model model) {
        requireNonNull(startDateTime);
        endDateTime = LocalDateTime.now();
        model.updateDataDatesStatistics();
        List<DayData> newDayDatas = generateUpdatedDayData(startDateTime, endDateTime);
        newDayDatas.forEach(dayData -> model.updatesDayDataStatistics(dayData));
    }

    /**
     * Setter for the date-time at the Start of a pomodoro cycle.
     *
     * @param startDateTime represented as a LocalDateTime object.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Setter for the date-time at the End of a pomodoro cycle.
     *
     * @param endDateTime represented as a LocalDateTime object.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Generates a list of DayData objects based on specified Start and End date-times.
     *
     * @param startDateTime represented as a LocalDateTime object.
     * @param endDateTime represented as a LocalDateTime object.
     * @return the collection of DayDatas that represent the entire period between startDateTime and
     *     endDateTime.
     */
    public List<DayData> generateUpdatedDayData(
            LocalDateTime startDateTime, LocalDateTime endDateTime) {

        List<DayData> out = new LinkedList<>();
        LocalDateTime tempDateTime = startDateTime;
        while (!tempDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            // get minutes from this temp date to its end of day
            int minutes =
                    (int)
                            tempDateTime.until(
                                    tempDateTime.toLocalDate().atTime(LocalTime.MAX),
                                    ChronoUnit.MINUTES);
            Date date = new Date(tempDateTime.format(Date.dateFormatter));
            System.out.println(date.toString());
            DayData currDayData = model.getDayDataFromDateStatistics(date);
            PomDurationData updatedPomDuration =
                    new PomDurationData("" + (currDayData.getPomDurationData().value + minutes));
            DayData updatedDayData =
                    new DayData(date, updatedPomDuration, currDayData.getTasksDoneData());
            out.add(updatedDayData);
            tempDateTime = tempDateTime.plusDays(1);
            tempDateTime = tempDateTime.toLocalDate().atStartOfDay();
        }
        // Handle last day
        int minutes = (int) tempDateTime.until(endDateTime, ChronoUnit.MINUTES);
        Date date = new Date(tempDateTime.format(Date.dateFormatter));
        DayData currDayData = model.getDayDataFromDateStatistics(date);
        PomDurationData updatedPomDuration =
                new PomDurationData("" + (currDayData.getPomDurationData().value + minutes));
        DayData updatedDayData =
                new DayData(date, updatedPomDuration, currDayData.getTasksDoneData());
        out.add(updatedDayData);

        return out;
    }

    /** Sets the necessary parameters when tracking a pommed task. */
    public void startTrackTask() {
        startDateTime = LocalDateTime.now();
        endDateTime = null;
    }

    /**
     * Getter for the current prompt state of the manager.
     *
     * @return the enum representing the prompt state.
     */
    public PROMPT_STATE getPromptState() {
        return this.promptState;
    }

    /**
     * Setter for the prompt state of the manager.
     *
     * @param promptState
     */
    public void setPromptState(PROMPT_STATE promptState) {
        this.promptState = promptState;
    }

    /** Actions to take when pomodoro is checking if user wants a break. */
    public void checkBreakActions() {
        this.setPromptState(PROMPT_STATE.CHECK_TAKE_BREAK);
    }

    /** Actions to take when user "done" a task in the middle of a cycle. */
    public void checkMidPomDoneActions() {
        this.setPromptState(PROMPT_STATE.CHECK_DONE_MIDPOM);
        mainWindow.setPomCommandExecutor();
    }

    /** Actions to take when user requests to take a break. */
    public void takeABreak() {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(restTime);
        timeline = new Timeline();
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(restTime + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
        timeline.setOnFinished(
                event -> {
                    resultDisplay.setFeedbackToUser("Breaks over! What shall we do next?");
                    this.setPromptState(PROMPT_STATE.NONE); // App back to neutral
                    pomodoroDisplay.playDone();
                });

        mainWindow.setDefaultCommandExecutor();
    }

    /**
     * Updates the parameters needed to "done" a task.
     *
     * @param model of the app's current state.
     * @param originList the list from which the task is referenced (list order is maintained).
     * @param taskIndex indicating the task's position in the originList.
     */
    public void setDoneParams(Model model, List<Task> originList, int taskIndex) {
        this.model = model;
        this.originList = originList;
        this.taskIndex = taskIndex;
    }

    /** Resets the parameters after a task is "done". */
    private void clearDoneParams() {
        this.originList = null;
        this.taskIndex = -1;
        model.setPomodoroTask(null);
    }

    /** Actions taken to "done" a task that was being pommed. */
    public void doneTask() {
        Task taskToEdit = originList.get(taskIndex);
        Name updatedName = taskToEdit.getName();
        Priority updatedPriority = taskToEdit.getPriority();
        Description updatedDescription = taskToEdit.getDescription();
        Set<Tag> updatedTags = taskToEdit.getTags();
        Task editedTask =
                new Task(
                        updatedName,
                        updatedPriority,
                        updatedDescription,
                        new Done("Y"),
                        updatedTags);
        model.setTask(taskToEdit, editedTask);
        // Update stats
        model.updateDataDatesStatistics();

        // timeline = null;

        LocalDateTime now = LocalDateTime.now();
        Date dateOnDone = new Date(now.format(Date.dateFormatter));
        DayData dayData = model.getDayDataFromDateStatistics(dateOnDone);
        DayData updatedDayData =
                new DayData(
                        dateOnDone,
                        dayData.getPomDurationData(),
                        new TasksDoneData("" + (dayData.getTasksDoneData().value + 1)));
        model.updatesDayDataStatistics(updatedDayData);
        clearDoneParams();
    }

    /**
     * Handles the prompting behaviour at the end of a pomodoro cycle or the "done" event of a
     * pommed task.
     *
     * @param commandText representing the user's response.
     * @param logic a reference to the app's logic manager.
     * @param logger a reference to the app's logging system.
     * @param petManager a reference to app's pet manager system.
     * @return the result of executing a special pomodoro command that handles prompts.
     */
    public CommandResult promptBehaviour(
            String commandText, Logic logic, Logger logger, PetManager petManager)
            throws CommandException, ParseException {

        String commandTextLower = commandText.toLowerCase();

        switch (this.getPromptState()) {
        case CHECK_DONE:
            petManager.updateDisplayElements();
            if (commandTextLower.equals("y")) {
                mainWindow.updateMoodWhenDoneTask();
                mainWindow.updatePetDisplay();
                CommandResult commandResult =
                        new CommandResult(
                                "Good job! " + CHECK_TAKE_BREAK_MESSAGE, false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                doneTask();
                timeline = null;
                checkBreakActions();
                return commandResult;
            } else if (commandTextLower.equals("n")) {
                CommandResult commandResult =
                        new CommandResult(
                                "ALright, let's try again the next round! "
                                        + CHECK_TAKE_BREAK_MESSAGE,
                                false,
                                false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                checkBreakActions();
                return commandResult;
            } else {
                throw new ParseException(
                        "(Please confirm) Did you manage to finish the last task?\n"
                                + "(Y) - Task will be set to done. (N) - no changes");
            }
        case CHECK_TAKE_BREAK:
            if (commandTextLower.equals("y")) {
                CommandResult commandResult =
                        new CommandResult("Okie doke! Rest easy now...", false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                takeABreak();
                setPromptState(PROMPT_STATE.NONE);
                return commandResult;
            } else if (commandTextLower.equals("n")) {
                CommandResult commandResult =
                        new CommandResult("Alright, back to neutral!", false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                setPromptState(PROMPT_STATE.NONE);
                reset();
                mainWindow.setDefaultCommandExecutor();
                return commandResult;
            } else {
                throw new ParseException(
                        "(Please confirm) Shall we take a 5-min break?\n"
                                + "(Y) - 5-min timer begins. (N) - App goes neutral.");
            }
        case CHECK_DONE_MIDPOM:
            if (commandTextLower.equals("n")) {
                CommandResult commandResult =
                        new CommandResult("Alright, back to neutral!", false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                setPromptState(PROMPT_STATE.NONE);
                reset();
                timeline = null;
                mainWindow.setDefaultCommandExecutor();
                return commandResult;
            }
            try {
                PomCommand pc = (PomCommand) (new TaskListParser().parseCommand(commandText));

                PomCommandResult pomCommandResult =
                        (PomCommandResult) logic.execute(commandText);
                logger.info("Result: " + pomCommandResult.getFeedbackToUser());
                resultDisplay.setFeedbackToUser(pomCommandResult.getFeedbackToUser());
                if (pomCommandResult.getIsPause()) {
                    pause();
                } else if (pomCommandResult.getIsContinue()) {
                    unpause();
                } else {
                    pomodoroDisplay.setTaskInProgressText(pomCommandResult.getPommedTask());
                    unpause();
                    setDoneParams(
                            pomCommandResult.getModel(),
                            pomCommandResult.getOriginList(),
                            pomCommandResult.getTaskIndex());
                }
                setPromptState(PROMPT_STATE.NONE);
                mainWindow.setDefaultCommandExecutor();
                return pomCommandResult;
            } catch (ParseException | CommandException | ClassCastException e) {
                String message =
                        "(Please confirm) Would you like to continue with another task (not done yet)\n"
                                + "(pom <index>) - next task pommed with remaining time. (N) - App goes neutral.";
                resultDisplay.setFeedbackToUser(message);
                throw new ParseException(message);
            }
        case CHECK_RESUME_LAST:
            if (commandTextLower.equals("y")) {
                CommandResult commandResult =
                        new CommandResult("Okie doke! Pom resuming...", false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                startFromLast();
                setPromptState(PROMPT_STATE.NONE);
                mainWindow.setDefaultCommandExecutor();
                return commandResult;
            } else if (commandTextLower.equals("n")) {
                CommandResult commandResult =
                        new CommandResult("Alright, back to neutral!", false, false);
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                setPromptState(PROMPT_STATE.NONE);
                model.setPomodoroTask(null);
                reset();
                mainWindow.setDefaultCommandExecutor();
                return commandResult;
            } else {
                throw new ParseException("(Please confirm)" + CHECK_RESUME_LAST_MESSAGE);
            }
        case NONE:
        default:
            break;
        }
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Actions taken when user wants to resume pomodoro cycle that was paused during a mid-pom exit.
     */
    public void handleResumeLastSession() {
        Task prevPomTask = model.getPomodoroTask();
        if (prevPomTask == null) {
            return;
        }
        resultDisplay.setFeedbackToUser(CHECK_RESUME_LAST_MESSAGE);
        this.setPromptState(PROMPT_STATE.CHECK_RESUME_LAST); // App back to neutral
        mainWindow.setPomCommandExecutor();
    }

    /** Actions to take start timer from previous session before mid-pom exit. */
    public void startFromLast() {
        startDateTime = LocalDateTime.now();
        String timeLeft = model.getPomodoro().getTimeLeft();
        Task taskToResume = model.getPomodoroTask();
        pomodoroDisplay.setTaskInProgressText(taskToResume.getName().fullName);
        start(Float.parseFloat(timeLeft) * 60);
        List<Task> originList = model.getFilteredTaskList();
        Index resumeIndex = Index.fromZeroBased(originList.indexOf(taskToResume));
        setDoneParams(model, originList, resumeIndex.getZeroBased());
    }

    /** Actions taken by pomodoro manager upon exit of the app. */
    public void handleExit() {
        if (timeSeconds == null) {
            return;
        }
        float timeInMinutes = timeSeconds.floatValue() / 60;

        model.setPomodoroTimeLeft(timeInMinutes);
        if (model.getPomodoroTask() != null) {
            // Update statistics so far
            updateStatistics(model);
        }
    }
}
