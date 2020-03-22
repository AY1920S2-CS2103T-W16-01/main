package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyTaskList_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(
                new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTaskList_success() {
        Model model =
                new ModelManager(
                        getTypicalTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        Model expectedModel =
                new ModelManager(
                        getTypicalTaskList(),
                        new Pet(),
                        new Pomodoro(),
                        new Statistics(),
                        new UserPrefs());
        expectedModel.setTaskList(new TaskList());

        assertCommandSuccess(
                new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
