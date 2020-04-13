package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.PetManager;
import seedu.address.logic.StatisticsManager;
import seedu.address.model.ReadOnlyPomodoro;

public class SettingsDisplay extends UiPart<Region> {
    private static final String FXML = "SettingsDisplay.fxml";

    private static final String DEFAULT_PET_NAME = "BB";
    private static final String DEFAULT_POMODORO_DURATION = "25";
    private static final String DEFAULT_DAILY_CHALLENGE = "100";

    private ReadOnlyPomodoro pomodoro;
    private PetManager petManager;
    private StatisticsManager statisticsManager;

    @FXML private VBox settingsPane;
    @FXML private Label petNameLabel;
    @FXML private TextField petNameTextField;
    @FXML private Label pomodoroLabel;
    @FXML private TextField pomodoroTextField;
    @FXML private Label challengeLabel;
    @FXML private TextField challengeTextField;

    public SettingsDisplay(
            PetManager petManager, ReadOnlyPomodoro pomodoro, StatisticsManager statisticsManager) {
        super(FXML);
        this.pomodoro = pomodoro;
        this.petManager = petManager;
        this.statisticsManager = statisticsManager;

        update();
    }

    public void update() {
        String name = petManager.getPetName();
        petNameTextField.setText(name);

        String duration = pomodoro.getDefaultTime();
        pomodoroTextField.setText(duration);

        String dailyChallenge = statisticsManager.getDailyTargetText();
        challengeTextField.setText(dailyChallenge);
    }
}
