package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;

import java.util.Optional;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.commands.PomCommand.POM_TYPE;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PomCommand object.
 *
 * @author Hardy Shein
 * @version 1.4
 */
public class PomCommandParser implements Parser<PomCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PomCommand and returns a
     * Pom Command object for execution.
     *
     * @return the derived PomCommand for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public PomCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIMER);

        Optional<String> optTimerString = argMultimap.getValue(PREFIX_TIMER);

        try {
            String preamble = argMultimap.getPreamble();
            if (preamble.toLowerCase().equals("pause")) {
                return new PomCommand(POM_TYPE.PAUSE);
            } else if (preamble.toLowerCase().equals("continue")) {
                return new PomCommand(POM_TYPE.CONTINUE);
            } else if (preamble.toLowerCase().equals("stop")) {
                return new PomCommand(POM_TYPE.STOP);
            } else {
                // Look for an index to call Pom on
                Index index = ParserUtil.parseIndex(preamble);
                if (optTimerString.isEmpty()) {
                    return new PomCommand(index);
                } else {
                    try {
                        float timerAmount = Float.parseFloat(optTimerString.get());
                        if (timerAmount <= 0) {
                            throw new ParseException("Invalid time");
                        }
                        return new PomCommand(index, timerAmount);
                    } catch (NumberFormatException nfe) {
                        throw new ParseException("Invalid time");
                    }
                }
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Uses argMultimap to detect existing prefixes used so that it won't add double prefixes. Adds
     * timer prefix
     *
     * <p>Only attempts to add a prefix to the 3rd word in the command
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
        String feedbackToUser =
                String.format(Messages.COMPLETE_PREFIX_SUCCESS, prefixesBuilder.toString());

        return new CompletorResult(newCommand, feedbackToUser);
    }
}
