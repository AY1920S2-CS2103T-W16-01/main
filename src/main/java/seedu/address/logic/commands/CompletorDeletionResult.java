package seedu.address.logic.commands;

/** Represents the result of a command execution. */
public class CompletorDeletionResult extends CompletorResult {

    /** Constructs a {@code CompletorResult} with the specified fields. */
    public CompletorDeletionResult(String suggestedCommand, String feedbackToUser) {
        super(suggestedCommand, feedbackToUser);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof CompletorDeletionResult)) {
            return false;
        }

        return super.equals(obj);
    }
}
