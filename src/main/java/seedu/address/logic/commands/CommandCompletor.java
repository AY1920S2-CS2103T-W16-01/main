package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Reminder;

/** Represents a command with hidden internal logic and the ability to be executed. */
public class CommandCompletor {
    private ArrayList<String> commands = new ArrayList<>();

    /** Add all available commands */
    public CommandCompletor() {
        this.commands.add(AddCommand.COMMAND_WORD);
        this.commands.add(EditCommand.COMMAND_WORD);
        this.commands.add(DoneCommand.COMMAND_WORD);
        this.commands.add(DeleteCommand.COMMAND_WORD);
        this.commands.add(FindCommand.COMMAND_WORD);
        this.commands.add(ClearCommand.COMMAND_WORD);
        this.commands.add(PomCommand.COMMAND_WORD);
        this.commands.add(ExitCommand.COMMAND_WORD);
        this.commands.add(SwitchTabCommand.STATS_COMMAND_WORD);
        this.commands.add(SwitchTabCommand.TASKS_COMMAND_WORD);
    }

    public String getSuggestedCommand(String input) {
        String[] trimmedInputWords = input.split("\\s+");

        if (trimmedInputWords.length <= 0) {
            return input;
        }

        trimmedInputWords[0] = getCompletedCommand(trimmedInputWords[0]);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);

        switch (trimmedInputWords[0]) {
            case "add":
            case "edit":
                for (int i = trimmedInputWords.length - 1; i > 0; i--) {
                    String currentArgument = trimmedInputWords[i];
                    if (Reminder.isValidReminder(currentArgument) && !hasReminder) {
                        trimmedInputWords[i] =
                                addPrefix(CliSyntax.PREFIX_REMINDER.toString(), currentArgument);
                        hasReminder = true;
                    } else if (Priority.isValidPriority(currentArgument) && !hasPriority) {
                        // prevent autoComplete from setting task index with a priority
                        if (trimmedInputWords[0] == "edit" && i < 2) { 
                            continue;
                        } 
                        trimmedInputWords[i] =
                                addPrefix(CliSyntax.PREFIX_PRIORITY.toString(), currentArgument);
                        hasPriority = true;
                    }
                }
                
                return String.join(" ", trimmedInputWords);
            case "done":
            case "delete":
                String[] commaSeparatedWords = input.split("\\s*,\\s*|\\s+");
                String[] indexes = getCommandArguments(commaSeparatedWords);
                String commaJoinedIndexes = String.join(", ", indexes);
                return String.format("%s %s", commaSeparatedWords[0], commaJoinedIndexes);
            case "pom":
                ArgumentMultimap pomArgMap = ArgumentTokenizer.tokenize(input, PREFIX_TIMER);
                boolean hasTimer = ParserUtil.arePrefixesPresent(pomArgMap, PREFIX_TIMER);
                if (! hasTimer) {
                    trimmedInputWords[2] = addPrefix(CliSyntax.PREFIX_TIMER.toString(), trimmedInputWords[2]);
                }
            default:
                return String.join(" ", trimmedInputWords);
        }
    }

    public String getSuccessMessage() {
        return "Command auto completed!";
    }

    public String getFailureMessage() {
        return "No command auto complete found :(";
    }

    private String getCompletedCommand(String firstWord) {
        for (String commandWord : this.commands) {
            Pattern commandPattern = Pattern.compile(String.format("^%s", commandWord));
            Matcher commandMatcher = commandPattern.matcher(firstWord.toLowerCase());
            if (commandMatcher.matches() || commandMatcher.hitEnd()) {
                return commandWord;
            } 
        }
        return firstWord;
    }

    private String[] getCommandArguments(String[] splitWords) {
        return Arrays.copyOfRange(splitWords, 1, splitWords.length);
    }

    private String addPrefix(String prefix, String arg) {
        return String.format("%s%s", prefix, arg);
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
