package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

/**
 * Represents the Command result for a switch tab command
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class SwitchTabCommandResult extends CommandResult {

    private final int tabToSwitchIndex;

    /** Constructs a {@code SwitchTabCommandResult} with the specified fields. */
    public SwitchTabCommandResult(String feedbackToUser, int tabToSwitchIndex) {
        super(requireNonNull(feedbackToUser));
        this.tabToSwitchIndex = requireNonNull(tabToSwitchIndex);
    }

    /**
     * Getter for the tab index.
     *
     * @return integer representing the tab index.
     */
    public int getTabToSwitchIndex() {
        return tabToSwitchIndex;
    }
}
