package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import java.util.Timer;
import java.util.TimerTask;
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

    private final CommandExecutor commandExecutor;

    @FXML private TextField commandTextField;

    public CommandBox(CommandExecutor commandExecutor, CommandSuggestor commandSuggestor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField
                .textProperty()
                .addListener((unused1, unused2, unused3) -> setStyleToDefault());

        commandTextField.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.TAB
                                && !event.isShiftDown()
                                && !event.isControlDown()) {
                            event.consume();
                            TimerTask tempTask = getDelayedDefaultStyleTimer();

                            try {
                                String suggestion =
                                        commandSuggestor.suggestCommand(commandTextField.getText());
                                commandTextField.setText(suggestion);
                                setStyleToIndicateCompletorSuccess();
                                new Timer().schedule( tempTask, 1000 );
                            } catch (CompletorException e) {
                                setStyleToIndicateCompletorFailure();
                                new Timer().schedule( tempTask, 1000 );
                            }
                            // event.consume doesn't seem to work, the below is thus a workaround
                            commandTextField.requestFocus();
                            commandTextField.forward();
                            return;
                        }
                    }
                });
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

    private TimerTask getDelayedDefaultStyleTimer() {
        return new TimerTask() {
            @Override
            public void run() {
                setStyleToDefault();
            }
        };
    }

    /** Sets the command box style to use the default style. */
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

    private void setStyleToIndicateCompletorFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(WARNING_STYLE_CLASS)) {
            return;
        }

        styleClass.add(WARNING_STYLE_CLASS);
    }

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

    @FunctionalInterface
    public interface CommandSuggestor {
        String suggestCommand(String commandText) throws CompletorException;
    }
}
