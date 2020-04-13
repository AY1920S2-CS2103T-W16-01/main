package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/** A ui for the status bar that is displayed at the header of the application. */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private Timer scheduler;

    @FXML private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
        scheduler = new Timer();
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

    public void setWarning() {
        CssManipulator.setStyleToIndicateWarning(resultDisplay);
        scheduler.cancel();
        scheduler.purge();
        scheduler = new Timer();
        scheduler.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> CssManipulator.setStyleToDefault(resultDisplay));
                    }
                },
                1000);
    }
}
