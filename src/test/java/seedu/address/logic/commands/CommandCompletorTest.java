package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CompletorException;

public class CommandCompletorTest {
    private final int DEFAULT_LIST_SIZE = 5;

    @Test
    public void execute_commandAutoComplete_successful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand =
                "add n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/forschool r/15/04/20@10:30 ";
        CompletorResult actualAddCommand =
                cc.getSuggestedCommand(
                        "ad n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/forschool r/15/04/20@10:30",
                        DEFAULT_LIST_SIZE);
        assertEquals(expectedAddCommand, actualAddCommand.getSuggestion());

        String expectedDoneCommand = "done 1 2 3 ";
        CompletorResult actualDoneCommand = cc.getSuggestedCommand("do 1 2 3", DEFAULT_LIST_SIZE);
        assertEquals(expectedDoneCommand, actualDoneCommand.getSuggestion());

        String expectedDeleteCommand = "delete 1 2 3 ";
        CompletorResult actualDeleteCommand =
                cc.getSuggestedCommand("del 1 2 3", DEFAULT_LIST_SIZE);
        assertEquals(expectedDeleteCommand, actualDeleteCommand.getSuggestion());

        String expectedFindCommand = "find hello world ";
        CompletorResult actualFindCommand =
                cc.getSuggestedCommand("fi hello world", DEFAULT_LIST_SIZE);
        assertEquals(expectedFindCommand, actualFindCommand.getSuggestion());
    }

    @Test
    public void execute_indexAutoComplete_successful() throws Exception {
        CommandCompletor cc = new CommandCompletor();

        String expectedDoneCommand = "done 1 2 ";
        CompletorResult actualDoneCommand =
                cc.getSuggestedCommand("do 1 2 7 8 9999 -10 0.5", DEFAULT_LIST_SIZE);
        assertEquals(expectedDoneCommand, actualDoneCommand.getSuggestion());
        assertEquals(actualDoneCommand instanceof CompletorDeletionResult, true);

        String expectedDeleteCommand = "delete 1 5 ";
        CompletorResult actualDeleteCommand =
                cc.getSuggestedCommand("del 0 01 5 6", DEFAULT_LIST_SIZE);
        assertEquals(expectedDeleteCommand, actualDeleteCommand.getSuggestion());
        assertEquals(actualDeleteCommand instanceof CompletorDeletionResult, true);
    }

    @Test
    public void execute_argsAutoComplete_successful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand =
                "add n/Math Homework des/Chapter 5, Pages 1 - 3 t/forschool p/3 r/15/04/20@10:30 ";
        String actualAddCommand =
                cc.getSuggestedCommand(
                                "add n/Math Homework des/Chapter 5, Pages 1 - 3  t/forschool 3 15/04/20@10:30",
                                DEFAULT_LIST_SIZE)
                        .getSuggestion();
        assertEquals(expectedAddCommand, actualAddCommand);

        String expectedEditCommand = "edit 2 r/15/04/20@10:30 p/3 ";
        String actualEditCommand =
                cc.getSuggestedCommand("edit 2 15/04/20@10:30 p/3", DEFAULT_LIST_SIZE)
                        .getSuggestion();
        assertEquals(expectedEditCommand, actualEditCommand);

        String expectedEditIndexUnchanged = "edit 2 r/15/04/20@10:30 ";
        String actualEditIndexUnchanged =
                cc.getSuggestedCommand("edit 2 15/04/20@10:30", DEFAULT_LIST_SIZE).getSuggestion();
        assertEquals(expectedEditIndexUnchanged, actualEditIndexUnchanged);
    }

    @Test
    public void execute_commandAutoComplete_unsuccessful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand = String.format(Messages.COMPLETE_UNFOUND_FAILURE, "asjjj");
        String actualAddCommand;
        try {
            actualAddCommand =
                    cc.getSuggestedCommand(
                                    "asjjj n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/for school r/15/04/20@10:30",
                                    DEFAULT_LIST_SIZE)
                            .getSuggestion();
        } catch (CompletorException e) {
            actualAddCommand = e.getMessage();
        }
        assertEquals(expectedAddCommand, actualAddCommand);

        String actualDoneCommand;
        try {
            actualDoneCommand =
                    cc.getSuggestedCommand("fffsdf 1,2,3", DEFAULT_LIST_SIZE).getSuggestion();
        } catch (CompletorException e) {
            actualDoneCommand = e.getMessage();
        }
        String expectedDoneCommand = String.format(Messages.COMPLETE_UNFOUND_FAILURE, "fffsdf");
        assertEquals(expectedDoneCommand, actualDoneCommand);
    }
}
