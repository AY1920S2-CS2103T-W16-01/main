package seedu.address.ui;

import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.PomodoroManager.PROMPT_STATE;
import seedu.address.logic.commands.CommandCompletor;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.commands.PomCommandResult;
import seedu.address.logic.commands.SwitchTabCommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.TaskListParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.DayData;
import seedu.address.model.task.Reminder;

import static seedu.address.logic.commands.SwitchTabCommand.STATS_TAB_INDEX;
import static seedu.address.logic.commands.SwitchTabCommand.TASKS_TAB_INDEX;

/**
 * The Main Window. Provides the basic application layout containing a menu bar and space where
 * other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private CommandCompletor commandCompletor;
    private PomodoroManager pomodoro;
    private PetManager petManager;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private PetDisplay petDisplay;
    private PomodoroDisplay pomodoroDisplay;
    private StatisticsDisplay statisticsDisplay;
    private SettingsDisplay settingsDisplay;

    private CommandBox commandBox;

    @FXML private StackPane commandBoxPlaceholder;

    @FXML private MenuItem helpMenuItem;

    @FXML private StackPane personListPanelPlaceholder;

    @FXML private StackPane resultDisplayPlaceholder;

    @FXML private StackPane statusbarPlaceholder;

    @FXML private StackPane petPlaceholder;

    @FXML private StackPane pomodoroPlaceholder;

    @FXML private StackPane statisticsPlaceholder;

    @FXML private StackPane settingsPlaceholder;

    @FXML private TabPane tabPanePlaceholder;

    public MainWindow(
            Stage primaryStage, Logic logic, PomodoroManager pomodoro, PetManager petManager) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.pomodoro = pomodoro;
        this.petManager = petManager;
        this.commandCompletor = new CommandCompletor();

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public StatisticsDisplay getStatisticsDisplay() {
        return statisticsDisplay;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666 is fixed in later version of
         * SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will not
         * work when the focus is in them because the key event is consumed by the
         * TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is in
         * CommandBox or ResultDisplay.
         */
        getRoot()
                .addEventFilter(
                        KeyEvent.KEY_PRESSED,
                        event -> {
                            if (event.getTarget() instanceof TextInputControl
                                    && keyCombination.match(event)) {
                                menuItem.getOnAction().handle(new ActionEvent());
                                event.consume();
                            }
                        });
    }

    /** Fills up all the placeholders of this window. */
    void fillInnerParts() {
        personListPanel = new TaskListPanel(logic.getFilteredTaskList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        petManager.updateMoodWhenLogIn();
        petDisplay = new PetDisplay();
        petManager.setPetDisplay(petDisplay);
        petManager.updatePetDisplay();
        petPlaceholder.getChildren().add(petDisplay.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getTaskListFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand, this::suggestCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        pomodoroDisplay = new PomodoroDisplay();
        pomodoroPlaceholder.getChildren().add(pomodoroDisplay.getRoot());
        pomodoro.setTimerLabel(pomodoroDisplay.getTimerLabel());
        pomodoro.setResultDisplay(resultDisplay);
        pomodoro.setMainWindow(this);

        statisticsDisplay = new StatisticsDisplay();
        statisticsPlaceholder.getChildren().add(statisticsDisplay.getRoot());

        settingsDisplay = new SettingsDisplay(petManager, logic.getPomodoro());
        settingsPlaceholder.getChildren().add(settingsDisplay.getRoot());

        // tabPanePlaceholder.getSelectionModel().select(1);

    }

    /** Sets the default size based on {@code guiSettings}. */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
        primaryStage.setResizable(false);
    }

    /** Opens the help window or focuses on it if it's already opened. */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /** Closes the application. */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings =
                new GuiSettings(
                        primaryStage.getWidth(),
                        primaryStage.getHeight(),
                        (int) primaryStage.getX(),
                        (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public TaskListPanel getTaskListPanel() {
        return personListPanel;
    }

    /** */
    private String suggestCommand(String commandText) {
        String suggestion = commandCompletor.getSuggestedCommand(commandText);
        if (suggestion.equals(commandText)) {
            resultDisplay.setFeedbackToUser(commandCompletor.getFailureMessage());
        } else {
            resultDisplay.setFeedbackToUser(commandCompletor.getSuccessMessage());
        }
        return suggestion;
    }

    public void setTabFocusTasks() {
        tabPanePlaceholder.getSelectionModel().select(0);
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText)
            throws CommandException, ParseException {

        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            // Swap to tasks tab
            tabPanePlaceholder
                    .getSelectionModel()
                    .select(TASKS_TAB_INDEX);

            // Switch tabs related results
            try {
                SwitchTabCommandResult switchTabCommandResult =
                        (SwitchTabCommandResult) commandResult;
                tabPanePlaceholder
                        .getSelectionModel()
                        .select(switchTabCommandResult.getTabToSwitchIndex());
                if (switchTabCommandResult.getTabToSwitchIndex() == STATS_TAB_INDEX) {
                    ObservableList<DayData> customQueue = logic.getCustomQueue();
                    statisticsDisplay.updateGraphs(customQueue);
                }
            } catch (ClassCastException ce) {
            }

            // Pomodoro related results
            try {
                PomCommandResult pomCommandResult = (PomCommandResult) commandResult;

                if (!pomCommandResult.getIsPause() && !pomCommandResult.getIsContinue()) {
                    pomodoroDisplay.setTaskInProgressText(pomCommandResult.getPommedTask());
                    pomodoro.start(pomCommandResult.getTimerAmountInMin());
                    pomodoro.setDoneParams(
                            pomCommandResult.getModel(),
                            pomCommandResult.getOriginList(),
                            pomCommandResult.getTaskIndex());
                }
            } catch (ClassCastException ce) {

            } catch (NullPointerException ne) {
                resultDisplay.setFeedbackToUser("Sorry, you've got no tasks being POMmed.");
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                petManager.handleExit();
                handleExit();
            }
            petManager.updatePetDisplay();
            // update because sorting returns a new list

            // * Old implementation for sort
            // personListPanel = new TaskListPanel(logic.getFilteredTaskList());
            // personListPanelPlaceholder.getChildren().clear();
            // personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    public void setPomCommandExecutor() {
        commandBox = new CommandBox(this::pomExecuteCommand, this::suggestCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    public void setDefaultCommandExecutor() {
        commandBox = new CommandBox(this::executeCommand, this::suggestCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    private CommandResult pomExecuteCommand(String commandText)
            throws CommandException, ParseException {

        PomodoroManager.PROMPT_STATE pomPromptState = pomodoro.getPromptState();
        switch (pomPromptState) {
            case CHECK_DONE:
                petManager.updatePetDisplay();
                if (commandText.toLowerCase().equals("y")) {
                    CommandResult commandResult =
                            new CommandResult(
                                    "Good job! " + pomodoro.CHECK_TAKE_BREAK_MESSAGE, false, false);
                    resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                    pomodoro.doneTask();
                    pomodoro.checkBreakActions();
                    // logic.incrementPomExp();
                    return commandResult;
                    // Continue to next prompt from break-timer
                } else if (commandText.toLowerCase().equals("n")) {
                    CommandResult commandResult =
                            new CommandResult(
                                    "Alright, lets try again the next round! "
                                            + pomodoro.CHECK_TAKE_BREAK_MESSAGE,
                                    false,
                                    false);
                    resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                    pomodoro.checkBreakActions();
                    // logic.incrementPomExp();
                    return commandResult;
                } else {
                    throw new ParseException(
                            "(Please confirm) Did you manage to finish the last task?\n"
                                    + "(Y) - Task will be set to done. (N) - No changes");
                }
            case CHECK_TAKE_BREAK:
                if (commandText.toLowerCase().equals("y")) {
                    CommandResult commandResult =
                            new CommandResult("Okie doke! Rest easy now...", false, false);
                    resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                    pomodoro.takeABreak();
                    pomodoro.setPromptState(PROMPT_STATE.NONE);
                    return commandResult;
                    // Continue to next prompt from break-timer
                } else if (commandText.toLowerCase().equals("n")) {
                    CommandResult commandResult =
                            new CommandResult("Alright, back to neutral!", false, false);
                    resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                    pomodoro.setPromptState(PROMPT_STATE.NONE);
                    pomodoro.reset();
                    this.setDefaultCommandExecutor();
                    return commandResult;
                } else {
                    throw new ParseException(
                            "(Please confirm) Shall we take a 5-min break?\n"
                                    + "(Y) - 5-min timer begins. (N) - App goes neutral.");
                }
            case CHECK_DONE_MIDPOM:
                if (commandText.toLowerCase().equals("n")) {
                    CommandResult commandResult =
                            new CommandResult("Alright, back to neutral!", false, false);
                    resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
                    pomodoro.setPromptState(PROMPT_STATE.NONE);
                    pomodoro.reset();
                    this.setDefaultCommandExecutor();
                    return commandResult;
                }
                try {
                    PomCommand pc = (PomCommand) (new TaskListParser().parseCommand(commandText));
                    // if continuedPom was created, user put in a valid pom request. Execute as per
                    // normal
                    PomCommandResult pomCommandResult =
                            (PomCommandResult) logic.execute(commandText);
                    logger.info("Result: " + pomCommandResult.getFeedbackToUser());
                    resultDisplay.setFeedbackToUser(pomCommandResult.getFeedbackToUser());
                    if (pomCommandResult.getIsPause()) {
                        pomodoro.pause();
                    } else if (pomCommandResult.getIsContinue()) {
                        pomodoro.unpause();
                    } else {
                        pomodoroDisplay.setTaskInProgressText(pomCommandResult.getPommedTask());
                        // pomodoro.start(pomCommandResult.getTimerAmountInMin());
                        pomodoro.unpause();
                        pomodoro.setDoneParams(
                                pomCommandResult.getModel(),
                                pomCommandResult.getOriginList(),
                                pomCommandResult.getTaskIndex());
                    }
                    pomodoro.setPromptState(PROMPT_STATE.NONE);
                    this.setDefaultCommandExecutor();
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

    @FXML
    public static void triggerReminder(Reminder reminder, String name, String description) {
        long delay = reminder.getDelay();
        Timeline timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(delay),
                                ae -> {
                                    MainWindow.showReminder(name, description);
                                    reminder.setHasFired();
                                }));
        timeline.play();
    }

    @FXML
    /**
     * Is triggered at the delayed time in Duke itself.
     * https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#informational-alert
     */
    public static void showReminder(String name, String description) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder");
        alert.setHeaderText(name);
        alert.setContentText(description);
        alert.show();
    }
}
