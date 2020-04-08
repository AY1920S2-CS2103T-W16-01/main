package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CompletorResult;
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

    public CompletorResult completeCommand(String input) {
        String[] commaSeparatedFields = input.split("\\s+");
        String feedbackToUser = Messages.UNCHANGED_SUCCESS;

        ArrayList<String> acceptedFields = new ArrayList<>();
        acceptedFields.add("sort");
        for (int i = 1; i < commaSeparatedFields.length; i++) {
            String currWord = commaSeparatedFields[i];
            Optional<String> completedWord = StringUtil.getCompletedWord(currWord, SortCommand.ALLOWED_SORT_FIELDS);
            if (completedWord.isPresent()) {
                acceptedFields.add(completedWord.get());
                feedbackToUser = Messages.COMPLETE_SUCCESS;
            }
        }

        String newCommand = StringUtil.getCommaJoinedCommand(acceptedFields.toArray(new String[0]));
        return new CompletorResult(newCommand, feedbackToUser);
    }
}
