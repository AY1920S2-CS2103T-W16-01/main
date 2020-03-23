package seedu.address.logic;

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
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.ui.ResultDisplay;

public class PomodoroManager {

    private Integer startTime;
    private Integer restTime = 5; // 5 * 60;
    private Timeline timeline;
    private Label timerLabel;
    private ResultDisplay resultDisplay;
    private IntegerProperty timeSeconds;
    private Model model;
    private List<Task> originList;
    private int taskIndex;

    public enum PROMPT_STATE {
        NONE,
        CHECK_DONE,
        CHECK_TAKE_BREAK;
    }

    public final String CHECK_DONE_MESSAGE =
            "Did you manage to finish the last task?\n"
                    + "(Y) - Task will be set to done. (N) - No changes.";

    public final String CHECK_TAKE_BREAK_MESSAGE =
            "Shall we take a 5-min break?\n" + "(Y) - 5-min timer begins. (N) - App goes neutral.";

    private PROMPT_STATE promptState;

    public PomodoroManager() {
        promptState = PROMPT_STATE.NONE;
    }

    public void setResultDisplay(ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
    }

    public void setTimerLabel(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    public void start(float timeInMinutes) {
        startTime = (int) (timeInMinutes * 60);
        timeSeconds = new SimpleIntegerProperty(startTime);
        configureUi();
        configureTimer();
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
                });
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
        clearDoneParams();
    }
}
