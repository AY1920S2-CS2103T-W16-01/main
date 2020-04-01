package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Reminder;

/** Represents a command with hidden internal logic and the ability to be executed. */
public class CommandCompletor {
    private ArrayList<String> commands = new ArrayList<>();
    private final String COMPLETE_SUCCESS = "Message %1$s has been auto completed: ";
    private final String COMPLETE_SUCCESS_PREFIX = "Message %1$s has been auto completed and added these prefixes %2$s";
    private final String COMPLETE_FAILURE_COMMAND = "Auto complete not possible";
    private final String COMPLETE_FAILURE_UNCHANGED = "Command has nothing to complete :)";

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

    /**
     * Provides auto Complete for all partial command words
     *
     * <p>For done, delete commands: Converts indexes given in a mixture of spaces and commas into
     * comma separated indexes For add and edit commands: Adds prefixes for priority and reminder
     * For pom command: adds timer prefix
     *
     * @param input
     * @return returns command with completed command word, attached prefixes and convert indexes to
     *     comma separated ones
     */
    public CompletorResult getSuggestedCommand(String input) throws CompletorException {
        String[] trimmedInputWords = input.split("\\s+");

        if (trimmedInputWords.length <= 0) {
            return new CompletorResult(COMPLETE_FAILURE_COMMAND);
        }

        trimmedInputWords[0] = getCompletedCommand(trimmedInputWords[0]);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);
        String prefixesAdded = "", newCommand = "";

        switch (trimmedInputWords[0]) {
            case "add":
            case "edit":
                for (int i = trimmedInputWords.length - 1; i > 0; i--) {
                    String currentArgument = trimmedInputWords[i];
                    if (Reminder.isValidReminder(currentArgument) && !hasReminder) {
                        trimmedInputWords[i] =
                                addPrefix(CliSyntax.PREFIX_REMINDER.toString(), currentArgument);
                        hasReminder = true;
                        prefixesAdded += CliSyntax.PREFIX_REMINDER.toString();
                    } else if (Priority.isValidPriority(currentArgument) && !hasPriority) {
                        // prevent autoComplete from setting task index with a priority
                        if (trimmedInputWords[0].equals("edit") && i < 2) {
                            continue;
                        }
                        trimmedInputWords[i] =
                                addPrefix(CliSyntax.PREFIX_PRIORITY.toString(), currentArgument);
                        hasPriority = true;
                        prefixesAdded += CliSyntax.PREFIX_PRIORITY.toString();
                    }
                }
                newCommand = String.join(" ", trimmedInputWords);
                return new CompletorResult(newCommand, String.format(COMPLETE_SUCCESS_PREFIX, input, prefixesAdded));

            case "done":
            case "delete":
                // Converts indexes that are not comma separated into comma separated
                String[] commaSeparatedWords = input.split("\\s*,\\s*|\\s+");
                String[] indexes = getCommandArguments(commaSeparatedWords);
                String commaJoinedIndexes = String.join(", ", indexes);
                newCommand = String.format("%s %s", trimmedInputWords[0], commaJoinedIndexes);
                return new CompletorResult(newCommand, String.format(COMPLETE_SUCCESS, input));

            case "pom":
                ArgumentMultimap pomArgMap = ArgumentTokenizer.tokenize(input, PREFIX_TIMER);
                boolean hasTimer = ParserUtil.arePrefixesPresent(pomArgMap, PREFIX_TIMER);
                if (!hasTimer) {
                    trimmedInputWords[2] =
                            addPrefix(CliSyntax.PREFIX_TIMER.toString(), trimmedInputWords[2]);
                    prefixesAdded += CliSyntax.PREFIX_TIMER.toString();
                }
                return new CompletorResult(String.join(" ", trimmedInputWords), String.format(COMPLETE_SUCCESS_PREFIX, input, prefixesAdded));

            default:
                return new CompletorResult(String.format(COMPLETE_FAILURE_UNCHANGED, input, prefixesAdded));
        }
    }

    public String getSuccessMessage() {
        return "Command auto completed!";
    }

    public String getFailureMessage() {
        return "No command auto complete found :(";
    }

    /** Returns complete command if given partial command */
    private String getCompletedCommand(String firstWord) {
        for (String commandWord : this.commands) {
            Pattern commandPattern = Pattern.compile(String.format("^%s", commandWord));
            Matcher commandMatcher = commandPattern.matcher(firstWord.toLowerCase());
            if (!commandMatcher.matches() && commandMatcher.hitEnd()) {
                return commandWord;
            }
        }
        return firstWord;
    }

    /** Gets all non command arguments */
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
