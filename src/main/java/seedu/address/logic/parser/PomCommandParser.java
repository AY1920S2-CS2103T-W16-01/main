package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class PomCommandParser implements Parser<PomCommand> {

    public PomCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(
                args, PREFIX_TIMER);

        Optional<String> optTimerString = argMultimap.getValue(PREFIX_TIMER);
        float timerAmount = optTimerString.isEmpty()
            ? PomCommand.DEFAULT_TIMER
            : Float.parseFloat(optTimerString.get());

        try {
            String preamble = argMultimap.getPreamble();
            if (preamble.toLowerCase().equals("pause")) {
                return new PomCommand(true, false);
            } else if (preamble.toLowerCase().equals("continue")) {
                return new PomCommand(false, true);
            } else {
                // Look for an index to call Pom on
                Index index = ParserUtil.parseIndex(preamble);
                return new PomCommand(index, timerAmount);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE), pe);
        }
    }
}
