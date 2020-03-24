package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new SortCommand object */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * SortCommand and returns a SortCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String[] uniqueWords = ParserUtil.parseUniqueKeyWords(args);
        if (uniqueWords.length == 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String[] validFields = Arrays.stream(uniqueWords)
                .filter(s -> Arrays.asList(SortCommand.ALLOWED_SORT_FIELDS).contains(s)).toArray(String[]::new);

        if (validFields.length == 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(validFields); // should be
    }
}
