package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PomCommand;

public class PomCommandParserTest {

    private PomCommandParser parser = new PomCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(
                parser,
                "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(
                parser,
                "1 tm/-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE));
        assertParseFailure(
                parser,
                "1 tm/0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, PomCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validIndex_success() {
        final int INDEX = 42;
        Index index = Index.fromOneBased(INDEX);
        PomCommand expectedCommand = new PomCommand(index);
        assertParseSuccess(parser, "" + INDEX, expectedCommand);
    }

    @Test
    public void parse_validTimer_success() {
        final int INDEX = 42;
        final float TIMER_AMOUNT = 17f;
        Index index = Index.fromOneBased(INDEX);
        PomCommand expectedCommand = new PomCommand(index, TIMER_AMOUNT);
        assertParseSuccess(parser, INDEX + " tm/17", expectedCommand);
    }
}
