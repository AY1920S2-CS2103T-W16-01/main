package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new DoneCommand object */
public class DoneCommandParser implements Parser<DoneCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DoneCommand and returns a
     * DoneCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DoneCommand parse(String args) throws ParseException {
        try {
            Index[] indices = ParserUtil.parseIndices(args);
            return new DoneCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE), pe);
        }
    }

    public CompletorResult completeCommand(String input, int listSize) {
        String[] splitInput = input.split("\\s+");
        StringBuilder newCommand = new StringBuilder("done ");
        StringBuilder removedIndices = new StringBuilder();
        String feedbackToUser = Messages.COMPLETE_UNCHANGED_SUCCESS;

        for (int i = 1; i < splitInput.length; i++) {
            if (!StringUtil.isNonZeroUnsignedInteger(splitInput[i])) {
                feedbackToUser = Messages.COMPLETE_INDEX_OUT_OF_RANGE;
                removedIndices.append(splitInput[i].toString());
                removedIndices.append(" ");
                continue;
            }
            int currNumber = Integer.parseInt(splitInput[i]);
            if (currNumber > listSize) {
                feedbackToUser = Messages.COMPLETE_INDEX_OUT_OF_RANGE;
                removedIndices.append(String.format("%d ", currNumber));
            } else {
                newCommand.append(String.format("%d ", currNumber));
            }
        }
        if (removedIndices.length() > 0) {
            return new CompletorResult(newCommand.toString(), String.format(feedbackToUser, removedIndices.toString()));
        } else {
            return new CompletorResult(newCommand.toString(), feedbackToUser);
        }
    }
}
