package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.StringUtil;
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
        String feedbackToUser = UNCHANGED_SUCCESS;

        if (trimmedInputs.length <= 0) {
            throw new CompletorException(COMPLETE_FAILURE_COMMAND);
        }

        Optional<String> suggestedCommand =
                getCompletedWord(trimmedInputs[0], this.commands.toArray(new String[0]));

        if (suggestedCommand.isPresent()) {
            if (trimmedInputs[0].equals(suggestedCommand.get())) {
                feedbackToUser = UNCHANGED_SUCCESS;
            } else {
                feedbackToUser = COMPLETE_SUCCESS;
            }
            trimmedInputs[0] = suggestedCommand.get();
        } else {
            throw new CompletorException(
                    String.format(COMMAND_UNFOUND_FAILURE, trimmedInputs[0]));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);
        String prefixesAdded = "";
        
        String newCommand = String.join(" ", trimmedInputs);

        switch (trimmedInputs[0]) {
            case AddCommand.COMMAND_WORD:
            case EditCommand.COMMAND_WORD:
                for (int i = trimmedInputs.length - 1; i > 0; i--) {
                    String currentArgument = trimmedInputs[i];
                    if (Reminder.isValidReminder(currentArgument) && !hasReminder) {
                        trimmedInputs[i] =
                                addPrefix(CliSyntax.PREFIX_REMINDER.toString(), currentArgument);
                        hasReminder = true;
                        prefixesAdded += CliSyntax.PREFIX_REMINDER.toString();
                    } else if (Priority.isValidPriority(currentArgument) && !hasPriority) {
                        // prevent autoComplete from setting task index with a priority
                        if (trimmedInputs[0].equals("edit") && i < 2) {
                            continue;
                        }
                        trimmedInputs[i] =
                                addPrefix(CliSyntax.PREFIX_PRIORITY.toString(), currentArgument);
                        hasPriority = true;
                        prefixesAdded += CliSyntax.PREFIX_PRIORITY.toString();
                    }
                }
                newCommand = String.join(" ", trimmedInputs);
                prefixesAdded = prefixesAdded.length() == 0 ? "nil" : prefixesAdded;
                feedbackToUser = String.format(COMPLETE_PREFIX_SUCCESS, prefixesAdded);
                break;

            case DoneCommand.COMMAND_WORD:
            case DeleteCommand.COMMAND_WORD:
                // Converts indexes that are not comma separated into comma separated
                String[] commaSeparatedIndices = input.split("\\s*,\\s*|\\s+");
                String[] indexes = getCommandArguments(commaSeparatedIndices);
                String commaJoinedIndexes = String.join(", ", indexes);
                newCommand = String.format("%s %s", trimmedInputs[0], commaJoinedIndexes);
                feedbackToUser = String.format(COMPLETE_SUCCESS);
                break;

            case PomCommand.COMMAND_WORD:
                ArgumentMultimap pomArgMap = ArgumentTokenizer.tokenize(input, PREFIX_TIMER);
                boolean hasTimer = ParserUtil.arePrefixesPresent(pomArgMap, PREFIX_TIMER);
                if (!hasTimer && trimmedInputs.length > 2) {
                    trimmedInputs[2] =
                            addPrefix(CliSyntax.PREFIX_TIMER.toString(), trimmedInputs[2]);
                    prefixesAdded += CliSyntax.PREFIX_TIMER.toString();
                }
                newCommand = String.join(" ", trimmedInputs);
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
    private Optional<String> getCompletedWord(String word, String[] possibilities) {
        for (String matcher : possibilities) {
            if (StringUtil.keywordMatchPhrase(word, matcher)) {
                return Optional.of(matcher);
            }
            if (StringUtil.keywordMatchStartOfPhrase(word, matcher)) {
                return Optional.of(matcher);
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
