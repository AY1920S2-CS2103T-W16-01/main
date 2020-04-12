package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/** Clears the task list. */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_SUCCESS = "You have these tags:\n";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Set<Tag> tagSet = model.getTagSet();
        StringBuilder sb = new StringBuilder();
        for (Tag t : tagSet) {
            sb.append(t.toString() + "\n");
        }
        return new CommandResult(MESSAGE_SUCCESS + sb.toString());
    }
}
