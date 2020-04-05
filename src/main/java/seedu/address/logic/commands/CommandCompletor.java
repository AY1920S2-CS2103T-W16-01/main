package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
    public static final String COMPLETE_SUCCESS = "Message auto completed: ";
    public static final String COMPLETE_PREFIX_SUCCESS =
            "Message auto completed with these prefixes %1$s";
    public static final String UNCHANGED_SUCCESS = "Command has nothing to complete :)";
    public static final String COMMAND_UNFOUND_FAILURE =
            "Auto complete not possible %1$s not found!";
    public static final String COMPLETE_FAILURE_COMMAND = "Auto complete not possible!";
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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);
        String prefixesAdded = "", feedbackToUser = UNCHANGED_SUCCESS;

        if (trimmedInputWords.length <= 0) {
            throw new CompletorException(COMPLETE_FAILURE_COMMAND);
        }

        Optional<String> suggestedCommand =
                getCompletedWord(trimmedInputWords[0], this.commands.toArray(new String[0]));

        if (suggestedCommand.isPresent()) {
            if (trimmedInputWords[0].equals(suggestedCommand.get())) {
                feedbackToUser = UNCHANGED_SUCCESS;
            } else {
                feedbackToUser = COMPLETE_SUCCESS;
            }
            trimmedInputWords[0] = suggestedCommand.get();
        } else {
            throw new CompletorException(
                    String.format(COMMAND_UNFOUND_FAILURE, trimmedInputWords[0]));
        }

        String newCommand = String.join(" ", trimmedInputWords);

        switch (trimmedInputWords[0]) {
            case AddCommand.COMMAND_WORD:
            case EditCommand.COMMAND_WORD:
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
                prefixesAdded = prefixesAdded.length() == 0 ? "nil" : prefixesAdded;
                feedbackToUser = String.format(COMPLETE_PREFIX_SUCCESS, prefixesAdded);
                break;

            case DoneCommand.COMMAND_WORD:
            case DeleteCommand.COMMAND_WORD:
                // Converts indexes that are not comma separated into comma separated
                String[] commaSeparatedIndices = input.split("\\s*,\\s*|\\s+");
                String[] indexes = getCommandArguments(commaSeparatedIndices);
                String commaJoinedIndexes = String.join(", ", indexes);
                newCommand = String.format("%s %s", trimmedInputWords[0], commaJoinedIndexes);
                feedbackToUser = String.format(COMPLETE_SUCCESS);
                break;

            case PomCommand.COMMAND_WORD:
                ArgumentMultimap pomArgMap = ArgumentTokenizer.tokenize(input, PREFIX_TIMER);
                boolean hasTimer = ParserUtil.arePrefixesPresent(pomArgMap, PREFIX_TIMER);
                if (!hasTimer && trimmedInputWords.length > 2) {
                    trimmedInputWords[2] =
                            addPrefix(CliSyntax.PREFIX_TIMER.toString(), trimmedInputWords[2]);
                    prefixesAdded += CliSyntax.PREFIX_TIMER.toString();
                }
                newCommand = String.join(" ", trimmedInputWords);
                feedbackToUser = String.format(COMPLETE_PREFIX_SUCCESS, prefixesAdded);
                break;

            case "sort":
                String[] commaSeparatedFields = input.split("\\s*,\\s*|\\s+");
                for (int i = 1; i < commaSeparatedFields.length; i++) {
                    String currWord = commaSeparatedFields[i];
                    Optional<String> completedWord =
                            getCompletedWord(currWord, SortCommand.ALLOWED_SORT_FIELDS);
                    if (completedWord.isPresent()) {
                        commaSeparatedFields[i] = completedWord.get();
                        feedbackToUser = COMPLETE_SUCCESS;
                    } else {
                        commaSeparatedFields[i] = "";
                    }
                }
                newCommand = getCommaJoinedCommand(commaSeparatedFields);
                break;
        }
        return new CompletorResult(newCommand + " ", feedbackToUser);
    }

    private String getCommaJoinedCommand(String[] words) {
        String commaArguments = String.join(", ", getCommandArguments(words));
        return String.format("%s %s", words[0], commaArguments);
    }

    /** Returns complete command if given partial command */
    private Optional<String> getCompletedWord(String firstWord, String[] possibilities) {
        for (String word : possibilities) {
            Pattern pattern = Pattern.compile(String.format("^%s", word));
            Matcher matcher = pattern.matcher(firstWord.toLowerCase());
            if (matcher.matches()) {
                return Optional.of(word);
            }
            if (matcher.hitEnd()) {
                return Optional.of(word);
            }
        }
        return Optional.empty();
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
