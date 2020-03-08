package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Description;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Priority;
import seedu.address.model.tag.Tag;

/** Parses input arguments and creates a new AddCommand object */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand and returns an
     * AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_NAME,
                        PREFIX_PRIORITY,
                        PREFIX_EMAIL,
                        PREFIX_DESCRIPTION,
                        PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        // if (!arePrefixesPresent(
        // argMultimap, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_PRIORITY, PREFIX_EMAIL)
        // || !argMultimap.getPreamble().isEmpty()) {
        // throw new ParseException(
        // String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        // }

        System.out.println("Attempting parse.");
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        Priority priority =
                (argMultimap.getValue(PREFIX_PRIORITY).isEmpty())
                        ? ParserUtil.parsePriority("1")
                        : ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).get());

        Email email =
                (argMultimap.getValue(PREFIX_EMAIL).isEmpty())
                        ? ParserUtil.parseEmail("berniceyu@example.com")
                        : ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());

        Description description =
                (argMultimap.getValue(PREFIX_DESCRIPTION).isEmpty())
                        ? ParserUtil.parseDescription("")
                        : ParserUtil.parseDescription(
                                argMultimap.getValue(PREFIX_DESCRIPTION).get());

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person =
                new Person(
                        name,
                        priority,
                        email,
                        description,
                        tagList); // overloaded constructor which handles new Tasks

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
