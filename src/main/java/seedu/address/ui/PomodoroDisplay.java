package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class PomodoroDisplay extends UiPart<Region> {

    private static final String FXML = "PomodoroDisplay.fxml";
    private static final String DEFAULT_TASK_IN_PROGRESS = "No task in progress.";
    private static final String DEFAULT_TIMER = "25:00";

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

    public void setTimerText(String toDisplay) {
        timer.setText(toDisplay);
    }

    public Label getTimerLabel() {
        return timer;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PomodoroDisplay)) {
            return false;
        }

        // state check
        PomodoroDisplay card = (PomodoroDisplay) other;
        return taskInProgress.getText().equals(card.taskInProgress.getText())
                && timer.getText().equals(card.timer.getText());
    }
}
