package seedu.address.ui;

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

    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final CommandSuggestor commandSuggestor;

    @FXML private TextField commandTextField;

    public CommandBox(CommandExecutor commandExecutor, CommandSuggestor commandSuggestor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.commandSuggestor = commandSuggestor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField
                .textProperty()
                .addListener(
                        (unused1, unused2, unused3) -> {
                            CssManipulator.setStyleToDefault(commandTextField);
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
                        CssManipulator.setStyleToIndicateSuccess(commandTextField);
                    } catch (CompletorException e) {
                        CssManipulator.setStyleToIndicateFailure(commandTextField);
                    }
                    // Below is required as event.consume() does not prevent tab from unfocussing
                    // the text field
                    commandTextField.requestFocus();
                    commandTextField.forward();
                    return;
                }
            }
        };
    }

    /** Handles the Enter button pressed event. */
    @FXML
    private void handleCommandEntered() {
        try {
            commandExecutor.execute(commandTextField.getText());
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            CssManipulator.setStyleToIndicateFailure(commandTextField);
        }
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
