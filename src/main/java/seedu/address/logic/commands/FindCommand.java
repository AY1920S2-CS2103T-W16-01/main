package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.task.NameContainsKeywordsPredicate;

/**
 * Finds and lists all tasks in task list whose name contains any of the argument keywords. Keyword
 * matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Finds all tasks whose names contain any of "
                    + "the specified keywords (case-insensitive) or tags.\n"
                    + "Parameters: n/PHRASE t/[TAG]...\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " n/alice bob charlie t/tag1 t/tag2";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Apart from filtering tasks that match the keywords based on edit distance derived from {@link
     * NameContainsKeywordsPredicate} also sorts tasks in ascending order of edit distance.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(predicate);

        model.setSearchResultOrder(predicate.getSearchOrderComparator());

        return new CommandResult(
                String.format(
                        Messages.MESSAGE_TASKS_LISTED_OVERVIEW,
                        model.getFilteredTaskList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                        && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
