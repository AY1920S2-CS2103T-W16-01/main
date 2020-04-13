package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class PomodoroDisplay extends UiPart<Region> {

    private static final String FXML = "PomodoroDisplay.fxml";
    private static final String DEFAULT_TASK_IN_PROGRESS = "No task in progress.";
    private static final String DEFAULT_TIMER = "25:00";

    private String DONE_SOUND_FILEPATH = "/sounds/pom_ding.mp3";
    private String default_timer = DEFAULT_TIMER;
    public String taskInProgressText; // mutable
    public String timerText; // mutable

    @FXML private HBox pomodoroPane;
    @FXML private Label taskInProgress;
    @FXML private Label timer;

    public PomodoroDisplay() {
        super(FXML);
        this.taskInProgressText = DEFAULT_TASK_IN_PROGRESS;
        this.timerText = DEFAULT_TIMER;

        taskInProgress.setText(taskInProgressText);
        timer.setText(timerText);
    }

    public void setTaskInProgressText(String toDisplay) {
        taskInProgress.setText(toDisplay);
    }

    public void setDefaultTimeText(String defaultTimeText) {
        this.default_timer = defaultTimeText;
    }

    public void reset() {
        this.taskInProgressText = DEFAULT_TASK_IN_PROGRESS;
        this.timerText = default_timer;

        taskInProgress.setText(taskInProgressText);
        timer.setText(timerText);
    }

    public void setTimerText(String toDisplay) {
        timer.setText(toDisplay);
    }

    public Label getTimerLabel() {
        return timer;
    }

    public void playDone() {
        Media media = new Media(this.getClass().getResource(DONE_SOUND_FILEPATH).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
