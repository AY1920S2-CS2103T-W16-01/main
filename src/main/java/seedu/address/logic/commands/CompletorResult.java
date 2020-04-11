package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/** Represents the result of a command execution. */
public class CompletorResult {

    private final String feedbackToUser;
    private final String suggestedCommand;

    /** Constructs a {@code CompletorResult} with the specified fields. */
    public CompletorResult(String suggestedCommand, String feedbackToUser) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.suggestedCommand = requireNonNull(suggestedCommand);
    }

    /** Appends space to all suggeseted commands */
    public String getSuggestion() {
        return suggestedCommand + " ";
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CompletorResult)) {
            return false;
        }

        CompletorResult result = (CompletorResult) other;
        return feedbackToUser.equals(result.feedbackToUser)
                && suggestedCommand == result.suggestedCommand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, suggestedCommand);
    }
}
