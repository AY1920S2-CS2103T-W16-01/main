package seedu.address.logic.commands;

public class SortCommandResult extends CommandResult {
    private final String sortOrder;

    public SortCommandResult(String feedbackToUser, String sortOrder) {
        super(feedbackToUser);
        this.sortOrder = sortOrder;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }
}
