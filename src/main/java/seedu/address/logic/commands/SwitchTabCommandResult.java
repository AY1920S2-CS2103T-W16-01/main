package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

public class SwitchTabCommandResult extends CommandResult {

    private final int tabToSwitchIndex;

    public SwitchTabCommandResult(String feedbackToUser, int tabToSwitchIndex) {
        super(requireNonNull(feedbackToUser));
        this.tabToSwitchIndex = requireNonNull(tabToSwitchIndex);
    }

    public int getTabToSwitchIndex() {
        return tabToSwitchIndex;
    }
}
