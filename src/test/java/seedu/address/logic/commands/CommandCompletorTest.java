package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandCompletorTest {
    @Test
    public void execute_commandAutoComplete_successful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand =
                "add n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/forschool r/15/04/20@10:30";
        String actualAddCommand =
                cc.getSuggestedCommand(
                        "ad n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/forschool r/15/04/20@10:30");
        assertEquals(expectedAddCommand, actualAddCommand);

        String expectedDoneCommand = "done 1,2,3";
        String actualDoneCommand = cc.getSuggestedCommand("do 1,2,3");
        assertEquals(expectedDoneCommand, actualDoneCommand);

        String expectedDeleteCommand = "delete 1,2,3";
        String actualDeleteCommand = cc.getSuggestedCommand("del 1,2,3");
        assertEquals(expectedDeleteCommand, actualDeleteCommand);

        String expectedFindCommand = "find hello world";
        String actualFindCommand = cc.getSuggestedCommand("fi hello world");
        assertEquals(expectedFindCommand, actualFindCommand);
    }

    @Test
    public void execute_argsAutoComplete_successful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand =
                "add n/Math Homework des/Chapter 5, Pages 1 - 3 t/forschool p/3 r/15/04/20@10:30";
        String actualAddCommand =
                cc.getSuggestedCommand(
                        "add n/Math Homework des/Chapter 5, Pages 1 - 3  t/forschool 3 15/04/20@10:30");
        assertEquals(expectedAddCommand, actualAddCommand);

        String expectedEditCommand = "edit 2 r/15/04/20@10:30 p/3";
        String actualEditCommand = cc.getSuggestedCommand("edit 2 15/04/20@10:30 p/3");
        assertEquals(expectedEditCommand, actualEditCommand);

        String expectedEditIndexUnchanged = "edit 2 r/15/04/20@10:30";
        String actualEditIndexUnchanged = cc.getSuggestedCommand("edit 2 15/04/20@10:30");
        assertEquals(expectedEditIndexUnchanged, actualEditIndexUnchanged);

        String expectedDoneCommand = "done 1, 2, 3";
        String actualDoneCommand = cc.getSuggestedCommand("done 1   2  3");
        assertEquals(expectedDoneCommand, actualDoneCommand);

        String expectedDeleteCommand = "delete 1, 2, 3";
        String actualDeleteCommand = cc.getSuggestedCommand("delete 1  2  ,3");
        assertEquals(expectedDeleteCommand, actualDeleteCommand);
    }

    @Test
    public void execute_commandAutoComplete_unsuccessful() throws Exception {
        CommandCompletor cc = new CommandCompletor();
        String expectedAddCommand =
                "asjjj n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/for school r/15/04/20@10:30";
        String actualAddCommand =
                cc.getSuggestedCommand(
                        "asjjj n/Math Homework p/1 des/Chapter 5, Pages 1 - 3 t/for school r/15/04/20@10:30");
        assertEquals(expectedAddCommand, actualAddCommand);

        String expectedDoneCommand = "fffsdf 1,2,3";
        String actualDoneCommand = cc.getSuggestedCommand("fffsdf 1,2,3");
        assertEquals(expectedDoneCommand, actualDoneCommand);

        String expectedDeleteCommand = "dddeffe 1,2,3";
        String actualDeleteCommand = cc.getSuggestedCommand("dddeffe 1,2,3");
        assertEquals(expectedDeleteCommand, actualDeleteCommand);
    }
}
