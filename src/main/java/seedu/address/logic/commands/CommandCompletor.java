package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.EditCommandParser;
import seedu.address.logic.parser.PomCommandParser;
import seedu.address.logic.parser.SortCommandParser;

/** Represents a command with hidden internal logic and the ability to be executed. */
public class CommandCompletor {
    private Set<String> commands = new HashSet<>();

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
        this.commands.add(SwitchTabCommand.STATS_COMMAND_WORD);
        this.commands.add(SwitchTabCommand.TASKS_COMMAND_WORD);
        this.commands.add(SwitchTabCommand.SETTINGS_COMMAND_WORD);
    }

    /**
     * Provides auto Complete for all partial command words
     *
     * For done, delete commands: Converts indexes given in a mixture of spaces and commas into
     * comma separated indexes For add and edit commands: Adds prefixes for priority and reminder
     * For pom command: adds timer prefix
     * 
     * @param input raw user input
     * @return CompletorResult which contains both the completed message and feedback to display
     * @throws CompletorException throws an exception when a command is invalid or 
     */
    public CompletorResult getSuggestedCommand(String input) throws CompletorException {
        String[] trimmedInputs = input.split("\\s+");
        String feedbackToUser = Messages.COMPLETE_UNCHANGED_SUCCESS;

        if (trimmedInputs.length <= 0) {
            throw new CompletorException(String.format(Messages.COMPLETE_UNFOUND_FAILURE, ""));
        }

        Optional<String> suggestedCommand =
                StringUtil.getCompletedWord(trimmedInputs[0], this.commands.toArray(new String[0]));

        if (suggestedCommand.isPresent()) {
            if (trimmedInputs[0].equals(suggestedCommand.get())) {
                feedbackToUser = Messages.COMPLETE_UNCHANGED_SUCCESS;
            } else {
                feedbackToUser = Messages.COMPLETE_SUCCESS;
            }
            trimmedInputs[0] = suggestedCommand.get();
        } else {
            throw new CompletorException(
                    String.format(Messages.COMPLETE_UNFOUND_FAILURE, trimmedInputs[0]));
        }

        String newCommand = String.join(" ", trimmedInputs);

        switch (trimmedInputs[0]) {

            case AddCommand.COMMAND_WORD:
                return new AddCommandParser().completeCommand(newCommand);
            case EditCommand.COMMAND_WORD:
                return new EditCommandParser().completeCommand(newCommand);
            case PomCommand.COMMAND_WORD:
                return new PomCommandParser().completeCommand(newCommand);
            case SortCommand.COMMAND_WORD:
                return new SortCommandParser().completeCommand(newCommand);
        }
        return new CompletorResult(newCommand + " ", feedbackToUser);
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
