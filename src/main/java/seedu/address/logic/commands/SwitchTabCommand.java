package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * This class represents the Command for the "tasks, stats and settings" input by the user.
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class SwitchTabCommand extends Command {

    public static final String TASKS_COMMAND_WORD = "tasks";

    public static final String STATS_COMMAND_WORD = "stats";

    public static final String SETTINGS_COMMAND_WORD = "settings";

    public static final int TASKS_TAB_INDEX = 1;

    public static final int STATS_TAB_INDEX = 2;

    public static final int SETTINGS_TAB_INDEX = 3;

    public static final String MESSAGE_SUCCESS = "View changed.";

    private final int tabIndexToSwitch;

    /**
     * SwitchTabCommand constructor.
     *
     * @param tabIndexToSwitch indicating the index of the UI element to swap to.
     */
    public SwitchTabCommand(int tabIndexToSwitch) {
        this.tabIndexToSwitch = tabIndexToSwitch;
    }

    /**
     * Executes switch tab behaviour.
     *
     * @param model of the app's current state.
     * @return the CommandResult resulting for the execution of a SwitchTabCommand instance.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        return new SwitchTabCommandResult(MESSAGE_SUCCESS, tabIndexToSwitch);
    }
}
