package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/** Gets all tags currently in the list */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_SUCCESS = "You have these tags:\n";
    public static final String MESSAGE_NO_TAGS = "You have no tags\n";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String[] tagNames = model.getTagNames();
        if (tagNames.length == 0) {
            return new CommandResult(MESSAGE_NO_TAGS);
        }
        return new CommandResult(MESSAGE_SUCCESS + String.join(",\n", tagNames));
    }
}
