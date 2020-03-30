package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import seedu.address.model.Model;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.PomDurationData;
import seedu.address.model.dayData.TasksDoneData;
import seedu.address.model.dayData.Date;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.ui.MainWindow;
import seedu.address.ui.ResultDisplay;

public class PomodoroManager {

    private Integer startTime;
    private Integer restTime = 5; // 5 * 60;
    private Timeline timeline;
    private Label timerLabel;
    private ResultDisplay resultDisplay;
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

    public PomodoroManager() {
        promptState = PROMPT_STATE.NONE;
    }

    public void setResultDisplay(ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setTimerLabel(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    public void start(float timeInMinutes) {
        startTime = (int) (timeInMinutes * 60);
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
        timerLabel.setText("POM");
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
                    this.setPromptState(PROMPT_STATE.CHECK_DONE);
                    resultDisplay.setFeedbackToUser(CHECK_DONE_MESSAGE);
                    model.incrementPomExp();
                    mainWindow.setPomCommandExecutor();
                    mainWindow.setTabFocusTasks();
                    model.setPomodoroTask(null);
                    endDateTime = LocalDateTime.now();
                    updateStatistics(model); // Update pom duration
                });
    }

    public void updateStatistics(Model model) {
        requireNonNull(startDateTime);
        endDateTime = LocalDateTime.now();
        model.getStatistics().updateDataDates();
        List<DayData> newDayDatas = generateUpdatedDayData(startDateTime, endDateTime);
        newDayDatas.forEach(dayData -> model.getStatistics().updatesDayData(dayData));
        mainWindow.getStatisticsDisplay().updateGraphs(model.getStatistics().getCustomQueue()); // TODO code quality
    }


    public List<DayData> generateUpdatedDayData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<DayData> out = new LinkedList<>();
        LocalDateTime tempDateTime = startDateTime;
        while (!tempDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            // get minutes from this temp date to its end of day
            int minutes = (int)tempDateTime
                .until(tempDateTime.toLocalDate()
                .atTime(LocalTime.MAX), ChronoUnit.MINUTES);
            Date date = new Date(tempDateTime.format(Date.dateFormatter));
            System.out.println(date.toString());
            DayData currDayData = model.getStatistics().getDayDataFromDate(date);
            PomDurationData updatedPomDuration = 
                new PomDurationData("" +
                (currDayData.getPomDurationData().value + minutes));
            DayData updatedDayData =
                new DayData(
                    date,
                    updatedPomDuration,
                    currDayData.getTasksDoneData());
            out.add(updatedDayData);
            tempDateTime = tempDateTime.plusDays(1);
            tempDateTime = tempDateTime.toLocalDate().atStartOfDay();
        }
        // Handle last day
        int minutes = (int)tempDateTime
                .until(endDateTime, ChronoUnit.MINUTES);
        Date date = new Date(tempDateTime.format(Date.dateFormatter));
        DayData currDayData = model.getStatistics().getDayDataFromDate(date);
        PomDurationData updatedPomDuration = 
            new PomDurationData("" +
            (currDayData.getPomDurationData().value + minutes));
        DayData updatedDayData =
            new DayData(
                date,
                updatedPomDuration,
                currDayData.getTasksDoneData());
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
                });

        mainWindow.setDefaultCommandExecutor();
    }

    public void setDoneParams(Model model, List<Task> originList, int taskIndex) {
        this.model = model;
        this.originList = originList;
        this.taskIndex = taskIndex;
    }

    private void clearDoneParams() {
        this.model = null;
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
        // Update pet exp
        model.incrementExp();
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
}
