package seedu.address.ui;

import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.exceptions.ParseException;

/** The UI component that is responsible for receiving user command inputs. */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    public static final String WARNING_STYLE_CLASS = "warning";
    public static final String SUCCESS_STYLE_CLASS = "success";
    private static final String FXML = "CommandBox.fxml";
    private Timer scheduler;

    private final CommandExecutor commandExecutor;
    private final CommandSuggestor commandSuggestor;

    @FXML private TextField commandTextField;

    public CommandBox(CommandExecutor commandExecutor, CommandSuggestor commandSuggestor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.commandSuggestor = commandSuggestor;
        scheduler = new Timer();
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField
                .textProperty()
                .addListener(
                        (unused1, unused2, unused3) -> {
                            setStyleToDefault();
                        });

        commandTextField.setOnKeyPressed(getTabKeyEventHandler());
    }

    public EventHandler getTabKeyEventHandler() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB
                        && !event.isShiftDown()
                        && !event.isControlDown()) {

                    try {
                        String suggestion =
                                commandSuggestor.suggestCommand(commandTextField.getText());
                        commandTextField.setText(suggestion);
                        // cancels all previous timers so that we won't have a case of previous 
                        // timers setting colors in before the 1 second of success style
                        refreshTimer(); 
                        setStyleToIndicateCompletorSuccess();
                        scheduler.schedule(getSetStyleToDefaultTimerTask(), 1000);
                    } catch (CompletorException e) {
                        refreshTimer();
                        setStyleToIndicateCompletorFailure();
                        scheduler.schedule(getSetStyleToDefaultTimerTask(), 1000);
                    }
                    // Below is required as event.consume() does not prevent tab from unfocussing the text field
                    commandTextField.requestFocus();
                    commandTextField.forward();
                    return;
                }
            }
        };
    }

    private void refreshTimer() {
        scheduler.cancel();
        scheduler.purge();
        scheduler = new Timer();
    }

    /** Handles the Enter button pressed event. */
    @FXML
    private void handleCommandEntered() {
        try {
            commandExecutor.execute(commandTextField.getText());
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /** TimerTask that sets the command box style to the default style. */
    private TimerTask getSetStyleToDefaultTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                setStyleToDefault();
            }
        };
    }

    /** Remvoes all possible styles applied */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
        commandTextField.getStyleClass().remove(WARNING_STYLE_CLASS);
        commandTextField.getStyleClass().remove(SUCCESS_STYLE_CLASS);
    }
    

    /** Sets the command box style to indicate a failed command. */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a failed auto complete. */
    private void setStyleToIndicateCompletorFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(WARNING_STYLE_CLASS)) {
            return;
        }

        styleClass.add(WARNING_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a successful auto complete. */
    private void setStyleToIndicateCompletorSuccess() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(SUCCESS_STYLE_CLASS)) {
            return;
        }

        styleClass.add(SUCCESS_STYLE_CLASS);
    }

    /** Represents a function that can execute commands. */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    /** Represents a function that can complete commands. */
    @FunctionalInterface
    public interface CommandSuggestor {
        /**
         * Performs an auto complete and returns the completed command or an exception.
         * 
         * @see seedu.address.logic.commmands.CommandCompletor#getSuggestedCommand
         */
        String suggestCommand(String commandText) throws CompletorException;
    }
}
