package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Optional;
import seedu.address.commons.core.Messages;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CompletorDeletionResult;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new SortCommand object */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand and returns a
     * SortCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String[] uniqueWords = ParserUtil.parseUniqueKeyWords(args);
        if (uniqueWords.length == 0) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String[] validFields =
                Arrays.stream(uniqueWords)
                        .filter(s -> Arrays.asList(SortCommand.ALLOWED_SORT_FIELDS).contains(s))
                        .toArray(String[]::new);

        if (validFields.length == 0) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(validFields); // should be
    }

    public CompletorResult completeCommand(String input) {
        String[] splitFields = input.split("\\s+");
        String feedbackToUser = Messages.COMPLETE_SUCCESS;
        StringBuilder removedFields = new StringBuilder();
        StringBuilder acceptedFields = new StringBuilder("sort ");

        for (int i = 1; i < splitFields.length; i++) {
            String currWord = splitFields[i];
            Optional<String> completedWord =
                    StringUtil.getCompletedWord(currWord, SortCommand.ALLOWED_SORT_FIELDS);
            if (completedWord.isPresent()) {
                acceptedFields.append(completedWord.get() + " ");
            } else {
                removedFields.append(currWord + ", ");
            }
        }

        acceptedFields.setLength(acceptedFields.length() - 1);
        String newCommand = acceptedFields.toString();

        if (removedFields.length() > 0) {
            removedFields.setLength(removedFields.length() - 2);
            String errorFeedback =
                    String.format(Messages.COMPLETE_UNKNOWN_SORT_FIELDS, removedFields.toString());
            return new CompletorDeletionResult(newCommand, errorFeedback);
        }

        feedbackToUser =
                String.format(
                        "%s\nHere are some other fields:\n%s",
                        feedbackToUser, SortCommand.DISPLAY_POSSIBLE_FIELDS);
        return new CompletorResult(newCommand, feedbackToUser);
    }
}
