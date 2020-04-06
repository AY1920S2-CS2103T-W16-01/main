package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

public class DoneCommandResult extends CommandResult {

    public DoneCommandResult(String feedbackToUser) {
        super(requireNonNull(feedbackToUser));
    }
}
