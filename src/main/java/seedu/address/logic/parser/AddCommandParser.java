package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
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
                        PREFIX_REMINDER);

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

        Task task = new Task(name, priority, description, tagList, optionalReminder);

        return new AddCommand(task);
    }
}
