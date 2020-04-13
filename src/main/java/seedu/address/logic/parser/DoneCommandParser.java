package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CompletorDeletionResult;
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

    /**
     * Removes all invalid indexes and lets user know which indexes have been removed
     *
     * @param input input that has been trimmed
     * @return CompletorResult with suggested command and feedback to display
     */
    public CompletorResult completeCommand(String input, int listSize) {
        String[] splitInput = input.split("\\s+");
        StringBuilder newCommand = new StringBuilder("done ");
        StringBuilder removedIndices = new StringBuilder();
        String feedbackToUser = Messages.COMPLETE_SUCCESS;

        for (int i = 1; i < splitInput.length; i++) {
            if (!StringUtil.isNonZeroUnsignedInteger(splitInput[i])) {
                feedbackToUser = Messages.COMPLETE_INDEX_OUT_OF_RANGE_REMOVAL;
                removedIndices.append(splitInput[i].toString() + " ");
                continue;
            }
            int currNumber = Integer.parseInt(splitInput[i]);
            if (currNumber > listSize) {
                feedbackToUser = Messages.COMPLETE_INDEX_OUT_OF_RANGE_REMOVAL;
                removedIndices.append(currNumber + " ");
            } else {
                newCommand.append(currNumber + " ");
            }
        }

        newCommand.setLength(newCommand.length() - 1);
        if (removedIndices.length() > 0) {
            removedIndices.setLength(removedIndices.length() - 1);
            return new CompletorDeletionResult(
                    newCommand.toString(),
                    String.format(feedbackToUser, removedIndices.toString()));
        } else {
            return new CompletorResult(newCommand.toString(), feedbackToUser);
        }
    }
}
