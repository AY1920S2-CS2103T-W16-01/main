package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class PomodoroDisplay extends UiPart<Region> {

    private static final String FXML = "PomodoroDisplay.fxml";

    public String taskInProgressText; // mutable
    public String timerText; // mutable

    @FXML private HBox pomodoroPane;
    @FXML private Label taskInProgress;
    @FXML private Label timer;

    public PomodoroDisplay(String taskInProgressText, String timerText) {
        super(FXML);
        this.taskInProgressText = taskInProgressText;
        this.timerText = timerText;

        taskInProgress.setText(taskInProgressText);
        timer.setText(timerText);
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
