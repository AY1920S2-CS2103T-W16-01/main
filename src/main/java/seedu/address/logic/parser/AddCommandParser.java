package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.Optional;
import java.util.Set;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidReminderException;

/** Parses input arguments and creates a new AddCommand object */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand and returns an
     * AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     * @throws InvalidReminderException
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_NAME,
                        PREFIX_PRIORITY,
                        PREFIX_DESCRIPTION,
                        PREFIX_TAG,
                        PREFIX_REMINDER,
                        PREFIX_RECURRING);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        Priority priority =
                (argMultimap.getValue(PREFIX_PRIORITY).isEmpty())
                        ? ParserUtil.parsePriority("1")
                        : ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).get());

        Description description =
                (argMultimap.getValue(PREFIX_DESCRIPTION).isEmpty())
                        ? ParserUtil.parseDescription("")
                        : ParserUtil.parseDescription(
                                argMultimap.getValue(PREFIX_DESCRIPTION).get());

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Optional<Reminder> optionalReminder =
                (argMultimap.getValue(PREFIX_REMINDER).isEmpty())
                        ? Optional.empty()
                        : Optional.of(
                                ParserUtil.parseReminder(
                                        argMultimap.getValue(PREFIX_REMINDER).get()));

        Optional<Recurring> optionalRecurring =
                (argMultimap.getValue(PREFIX_RECURRING).isEmpty())
                        ? Optional.empty()
                        : Optional.of(
                                ParserUtil.parseRecurring(
                                        argMultimap.getValue(PREFIX_RECURRING).get()));

        Task task =
                new Task(name, priority, description, tagList, optionalReminder, optionalRecurring);

        return new AddCommand(task);
    }

    /**
     * Uses argMultimap to detect existing prefixes used so that it won't add double prefixes. Adds
     * priority and reminder prefixes
     *
     * @param input input that has been trimmed
     * @return CompletorResult with suggested command and feedback to display
     */
    public CompletorResult completeCommand(String input) {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);

        StringBuilder prefixesBuilder = new StringBuilder();

        String[] splitInput = input.split("\\s+");

        for (int i = splitInput.length - 1; i > 0; i--) {
            String currentArgument = splitInput[i];
            if (Reminder.isValidReminder(currentArgument) && !hasReminder) {
                splitInput[i] = CliSyntax.PREFIX_REMINDER.toString() + currentArgument;
                hasReminder = true;
                prefixesBuilder.append(CliSyntax.PREFIX_REMINDER.toString() + " ");
            } else if (Priority.isValidPriority(currentArgument) && !hasPriority) {
                splitInput[i] = CliSyntax.PREFIX_PRIORITY.toString() + currentArgument;
                hasPriority = true;
                prefixesBuilder.append(CliSyntax.PREFIX_PRIORITY.toString() + " ");
            }
        }

        String newCommand = String.join(" ", splitInput);
        String prefixesAdded = prefixesBuilder.length() == 0 ? "nil" : prefixesBuilder.toString();
        String feedbackToUser = String.format(Messages.COMPLETE_PREFIX_SUCCESS, prefixesAdded);

        return new CompletorResult(newCommand, feedbackToUser);
    }
}
