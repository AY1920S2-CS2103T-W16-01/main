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
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.PomCommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.Statistics;
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

    public enum PROMPT_STATE {
        NONE,
        CHECK_DONE,
        CHECK_TAKE_BREAK,
        CHECK_DONE_MIDPOM;
    }

    public final String CHECK_DONE_MESSAGE =
            "Did you manage to finish the last task?\n"
                    + "(Y) - Task will be set to done. (N) - No changes.";

    public final String CHECK_TAKE_BREAK_MESSAGE =
            "Shall we take a 5-min break?\n" + "(Y) - 5-min timer begins. (N) - App goes neutral.";

    public final String CHECK_DONE_MIDPOM_MESSAGE =
            "Great! Would you like to continue with another task\n"
                    + "(pom <index>) - next task pommed with remaining time. (N) - App goes neutral.";

    private PROMPT_STATE promptState;

    public PomodoroManager(Model model) {
        promptState = PROMPT_STATE.NONE;
        this.model = model;
    }

    public void setPomodoroDisplay(PomodoroDisplay pomodoroDisplay) {
        this.pomodoroDisplay = pomodoroDisplay;
    }

    public PomodoroDisplay getPomodoroDisplay() {
        return pomodoroDisplay;
    }

    public void setResultDisplay(ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public String getDefaultStartTimeAsString() {
        int secondsRemaining = defaultStartTime;
        int minutePortion = secondsRemaining / 60;
        int secondPortion = secondsRemaining % 60;
        return String.format("%02d:%02d", minutePortion, secondPortion);
    }

    public Integer getDefaultStartTime() {
        return defaultStartTime;
    }

    public Integer getRestTime() {
        return restTime;
    }

    public void setDefaultStartTime(float defaultStartTimeInMin) {
        this.defaultStartTime = (int) (defaultStartTimeInMin * 60);
        model.setPomodoroDefaultTime(defaultStartTimeInMin);
    }

    public void setRestTime(float restTimeInMin) {
        this.restTime = (int) (restTimeInMin * 60);
        model.setPomodoroRestTime(restTimeInMin);
    }

    public void setTimerLabel(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    public void start(float time) {
        startTime = (int) (time);
        timeSeconds = new SimpleIntegerProperty(startTime);
        configureUi();
        configureTimer();
        promptState = PROMPT_STATE.NONE;
    }

    public void pause() throws NullPointerException {
        try {
            timeline.pause();
        } catch (NullPointerException ne) {
            throw ne;
        }
    }

    public void unpause() throws NullPointerException {
        try {
            timeline.play();
        } catch (NullPointerException ne) {
            throw ne;
        }
    }

    public void reset() {
        timerLabel.textProperty().unbind();
        pomodoroDisplay.reset();
    }

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
                    updateStatistics(model); // Update pom duration
                    pomodoroDisplay.playDone();
                });
    }

    public void updateStatistics(Model model) {
        requireNonNull(startDateTime);
        endDateTime = LocalDateTime.now();
        model.getStatistics().updateDataDates();
        List<DayData> newDayDatas = generateUpdatedDayData(startDateTime, endDateTime);
        newDayDatas.forEach(dayData -> model.getStatistics().updatesDayData(dayData));
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

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
            DayData currDayData = model.getStatistics().getDayDataFromDate(date);
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
        DayData currDayData = model.getStatistics().getDayDataFromDate(date);
        PomDurationData updatedPomDuration =
                new PomDurationData("" + (currDayData.getPomDurationData().value + minutes));
        DayData updatedDayData =
                new DayData(date, updatedPomDuration, currDayData.getTasksDoneData());
        out.add(updatedDayData);

        return out;
    }

    public void startTrackTask(Task task) {
        startDateTime = LocalDateTime.now();
        endDateTime = null;
    }

    public PROMPT_STATE getPromptState() {
        return this.promptState;
    }

    public void setPromptState(PROMPT_STATE promptState) {
        this.promptState = promptState;
    }

    public void checkBreakActions() {
        this.setPromptState(PROMPT_STATE.CHECK_TAKE_BREAK);
    }

    public void checkMidPomDoneActions() {
        this.setPromptState(PROMPT_STATE.CHECK_DONE_MIDPOM);
        mainWindow.setPomCommandExecutor();
    }

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

    public void setDoneParams(Model model, List<Task> originList, int taskIndex) {
        this.model = model;
        this.originList = originList;
        this.taskIndex = taskIndex;
    }

    private void clearDoneParams() {
        // this.model = null;
        this.originList = null;
        this.taskIndex = -1;
    }

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

        LocalDateTime now = LocalDateTime.now();
        Date dateOnDone = new Date(now.format(Date.dateFormatter));
        Statistics stats = model.getStatistics();
        DayData dayData = stats.getDayDataFromDate(dateOnDone);
        DayData updatedDayData =
                new DayData(
                        dateOnDone,
                        dayData.getPomDurationData(),
                        new TasksDoneData("" + (dayData.getTasksDoneData().value + 1)));
        stats.updatesDayData(updatedDayData);
        clearDoneParams();
    }

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
                    mainWindow.setDefaultCommandExecutor();
                    return commandResult;
                }
                try {
                    // PomCommand pc = (PomCommand) (new
                    // TaskListParser().parseCommand(commandText));
                    // if continuedPom was created, user put in a valid pom request. Execute as per
                    // normal
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
}
