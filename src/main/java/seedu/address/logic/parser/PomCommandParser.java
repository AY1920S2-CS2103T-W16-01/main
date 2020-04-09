package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMER;

import java.util.Optional;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PomCommand;
import seedu.address.logic.commands.PomCommand.POM_TYPE;
import seedu.address.logic.parser.exceptions.ParseException;

public class PomCommandParser implements Parser<PomCommand> {

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
}
