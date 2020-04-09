package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;

import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class PomCommandParser implements Parser<PomCommand> {

    public PomCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIMER);

        Optional<String> optTimerString = argMultimap.getValue(PREFIX_TIMER);

        try {
            String preamble = argMultimap.getPreamble();
            if (preamble.toLowerCase().equals("pause")) {
                return new PomCommand(true, false);
            } else if (preamble.toLowerCase().equals("continue")) {
                return new PomCommand(false, true);
            } else {
                // Look for an index to call Pom on
                Index index = ParserUtil.parseIndex(preamble);
                if (optTimerString.isEmpty()) {
                    return new PomCommand(index);
                } else {
                    float timerAmount = Float.parseFloat(optTimerString.get());
                    if (timerAmount <= 0) {
                        throw new ParseException("Invalid time");
                    }
                    return new PomCommand(index, timerAmount);
                }
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Uses argMultimap to detect existing prefixes used so that it won't add double prefixes.
     * Adds timer prefix
     * 
     * @param input input that has been trimmed
     * @return CompletorResult with suggested command and feedback to display
     */
    public CompletorResult completeCommand(String input) {
        ArgumentMultimap pomArgMap = ArgumentTokenizer.tokenize(input, PREFIX_TIMER);
        boolean hasTimer = ParserUtil.arePrefixesPresent(pomArgMap, PREFIX_TIMER);
        StringBuilder prefixesBuilder = new StringBuilder();

        String[] trimmedInputs = input.split("\\s+");

        if (!hasTimer && trimmedInputs.length > 2) {
            trimmedInputs[2] = CliSyntax.PREFIX_TIMER.toString() + trimmedInputs[2];
            prefixesBuilder.append(CliSyntax.PREFIX_TIMER.toString());
        }

        String newCommand = String.join(" ", trimmedInputs);
        String feedbackToUser = String.format(Messages.COMPLETE_PREFIX_SUCCESS, prefixesBuilder.toString());
        
        return new CompletorResult(newCommand, feedbackToUser);
    }
}
