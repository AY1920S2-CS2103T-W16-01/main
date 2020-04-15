package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import seedu.address.commons.core.Messages;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.DeleteCommandParser;
import seedu.address.logic.parser.DoneCommandParser;
import seedu.address.logic.parser.EditCommandParser;
import seedu.address.logic.parser.PomCommandParser;
import seedu.address.logic.parser.SortCommandParser;

/**
 * Contains a command completor that calls upon other command parsers to complete input provided by
 * users.
 */
public class CommandCompletor {
    private List<String> commands = new ArrayList<>();

    /** Add all available commands */
    public CommandCompletor() {
        this.commands.add(AddCommand.COMMAND_WORD);
        this.commands.add(EditCommand.COMMAND_WORD);
        this.commands.add(DoneCommand.COMMAND_WORD);
        this.commands.add(DeleteCommand.COMMAND_WORD);
        this.commands.add(PomCommand.COMMAND_WORD);
        this.commands.add(FindCommand.COMMAND_WORD);
        this.commands.add(ClearCommand.COMMAND_WORD);
        this.commands.add(ListCommand.COMMAND_WORD);
        this.commands.add(HelpCommand.COMMAND_WORD);
        this.commands.add(ExitCommand.COMMAND_WORD);
        this.commands.add(SortCommand.COMMAND_WORD);
        this.commands.add(TagCommand.COMMAND_WORD);
        this.commands.add(SetCommand.COMMAND_WORD);
        this.commands.add(SwitchTabCommand.STATS_COMMAND_WORD);
        this.commands.add(SwitchTabCommand.TASKS_COMMAND_WORD);
        this.commands.add(SwitchTabCommand.SETTINGS_COMMAND_WORD);
    }

    /**
     * Provides auto complete for all partial command words: Auto completion happens when: 1. Edit
     * distance between target and input < 2 2. input matches the head of the target
     *
     * <p>For done, delete commands: remove indices that are out of range For add and edit commands:
     * Adds prefixes for priority and reminder For pom command: adds timer prefix For sort command:
     * auto completes recognized sort fields else removes other fields
     *
     * @param input raw user input
     * @return CompletorResult which contains both the completed message and feedback to display
     * @throws CompletorException throws an exception when a command is invalid
     */
    public CompletorResult getSuggestedCommand(String input, int listSize)
            throws CompletorException {
        String[] splitInput = input.split("\\s+");
        String feedbackToUser = Messages.COMPLETE_SUCCESS;

        if (splitInput.length <= 0 || splitInput[0].length() == 0) {
            throw new CompletorException(String.format(Messages.COMPLETE_UNFOUND_FAILURE, ""));
        }

        // Gets auto completed command based on the two criteria above
        Optional<String> suggestedCommandWord =
                StringUtil.getCompletedWord(splitInput[0], this.commands.toArray(new String[0]));

        if (suggestedCommandWord.isPresent()) {
            splitInput[0] = suggestedCommandWord.get();
        } else {
            throw new CompletorException(
                    String.format(Messages.COMPLETE_UNFOUND_FAILURE, splitInput[0]));
        }

        String newCommand = String.join(" ", splitInput);

        switch (splitInput[0]) {
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().completeCommand(newCommand);
        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().completeCommand(newCommand, listSize);
        case PomCommand.COMMAND_WORD:
            return new PomCommandParser().completeCommand(newCommand);
        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().completeCommand(newCommand);
        case DoneCommand.COMMAND_WORD:
            return new DoneCommandParser().completeCommand(newCommand, listSize);
        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().completeCommand(newCommand, listSize);
        }
        return new CompletorResult(newCommand, feedbackToUser);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandCompletor)) {
            return false;
        } else {
            CommandCompletor otherCommandCompletor = (CommandCompletor) other;
            return otherCommandCompletor.commands.equals(this.commands);
        }
    }
}
