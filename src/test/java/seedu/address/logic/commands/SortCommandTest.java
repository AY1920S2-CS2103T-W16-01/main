package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.parser.FindCommandParser;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.TaskList;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class SortCommandTest {
    private static Task taskPriority1 =
            new TaskBuilder().withName("Task 1").withPriority("1").build();
    private static Task taskPriority2 =
            new TaskBuilder().withName("Task 2").withPriority("2").build();
    private static Task taskPriority3 =
            new TaskBuilder().withName("Task 3").withPriority("3").build();
    private static Task taskDate1 =
            new TaskBuilder()
                    .withName("Reminder Task 1")
                    .withPriority("2")
                    .withReminder(LocalDateTime.now().plusDays(4))
                    .build();
    private static Task taskDate2 =
            new TaskBuilder()
                    .withName("Reminder Task 2")
                    .withPriority("1")
                    .withReminder(LocalDateTime.now().plusDays(3))
                    .build();
    private static Task taskDate3 =
            new TaskBuilder()
                    .withName("Reminder Task 3")
                    .withPriority("3")
                    .withReminder(LocalDateTime.now().plusDays(1))
                    .build();
    private static Task taskDateDone3 =
            new TaskBuilder()
                    .withName("Reminder Task 3")
                    .withPriority("3")
                    .withReminder(LocalDateTime.now().plusDays(1))
                    .withDone()
                    .build();
    public static ModelStub modelStub;

    @BeforeEach
    public void constructor() {
        modelStub = new SortCommandTest().new ModelStub();
        TaskList stubList = new TaskList();
        stubList.addTask(taskPriority1);
        stubList.addTask(taskPriority2);
        stubList.addTask(taskPriority3);
        stubList.addTask(taskDate1);
        stubList.addTask(taskDate2);
        stubList.addTask(taskDate3);
        modelStub.setTaskList(stubList);
    }

    @Test
    public void execute_sortWithFindDone_successful() throws Exception {
        String[] prioritySort = {"priority"};

        new FindCommandParser().parse(" n/Reminder").execute(modelStub);
        CommandResult commandResult = new SortCommand(prioritySort).execute(modelStub);
        TaskList stubList = new TaskList();
        ModelStub tempModelStub = new ModelStub();
        stubList.addTask(taskDate3);
        stubList.addTask(taskDate1);
        stubList.addTask(taskDate2);
        tempModelStub.setTaskList(stubList);
        assertEquals(
                String.format(SortCommand.MESSAGE_SUCCESS, prioritySort[0]),
                commandResult.getFeedbackToUser());
        assertEquals(tempModelStub.getFilteredTaskList(), modelStub.getFilteredTaskList());
        Index[] doneIndices = {Index.fromZeroBased(0)};
        new DoneCommand(doneIndices);
        stubList = new TaskList();
        stubList.addTask(taskDateDone3);
        stubList.addTask(taskDate1);
        stubList.addTask(taskDate2);
        assertEquals(tempModelStub.getFilteredTaskList(), modelStub.getFilteredTaskList());
    }

    @Test
    public void execute_sortByPriority_successful() throws Exception {
        String[] prioritySort = {"priority", "name"};

        CommandResult commandResult = new SortCommand(prioritySort).execute(modelStub);
        TaskList stubList = new TaskList();
        ModelStub tempModelStub = new ModelStub();
        stubList.addTask(taskDate3);
        stubList.addTask(taskPriority3);
        stubList.addTask(taskDate1);
        stubList.addTask(taskPriority2);
        stubList.addTask(taskDate2);
        stubList.addTask(taskPriority1);
        tempModelStub.setTaskList(stubList);
        assertEquals(
                String.format(SortCommand.MESSAGE_SUCCESS, String.join(" ", prioritySort)),
                commandResult.getFeedbackToUser());
        assertEquals(tempModelStub.getFilteredTaskList(), modelStub.getFilteredTaskList());
    }

    @Test
    public void execute_sortByName_successful() throws Exception {
        String[] nameSort = {"name"};

        CommandResult commandResult = new SortCommand(nameSort).execute(modelStub);
        TaskList stubList = new TaskList();
        ModelStub tempModelStub = new ModelStub();
        stubList.addTask(taskDate1);
        stubList.addTask(taskDate2);
        stubList.addTask(taskDate3);
        stubList.addTask(taskPriority1);
        stubList.addTask(taskPriority2);
        stubList.addTask(taskPriority3);
        tempModelStub.setTaskList(stubList);
        assertEquals(
                String.format(SortCommand.MESSAGE_SUCCESS, nameSort[0]),
                commandResult.getFeedbackToUser());
        assertEquals(tempModelStub.getFilteredTaskList(), modelStub.getFilteredTaskList());
    }

    @Test
    public void execute_sortByDate_successful() throws Exception {
        String[] dateSort = {"date"};

        CommandResult commandResult = new SortCommand(dateSort).execute(modelStub);
        TaskList stubList = new TaskList();
        ModelStub tempModelStub = new ModelStub();
        stubList.addTask(taskDate3);
        stubList.addTask(taskDate2);
        stubList.addTask(taskDate1);
        stubList.addTask(taskPriority1);
        stubList.addTask(taskPriority2);
        stubList.addTask(taskPriority3);
        tempModelStub.setTaskList(stubList);
        assertEquals(
                String.format(SortCommand.MESSAGE_SUCCESS, dateSort[0]),
                commandResult.getFeedbackToUser());
        assertEquals(tempModelStub.getFilteredTaskList(), modelStub.getFilteredTaskList());
    }

    /** A default model stub that have all of the methods failing. */
    private class ModelStub extends ModelManager {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getTaskListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskListFilePath(Path taskListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyTaskList getTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTask(Task target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTask(Task target, Task editedTask) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyPet getPet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPetName(String name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyPomodoro getPomodoro() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public PomodoroManager getPomodoroManager() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPomodoroManager(PomodoroManager pomodoroManager) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPomodoroTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyStatistics getStatistics() {
            throw new AssertionError("This method should not be called.");
        }
    }

    // /** A Model stub that contains a single task. */
    // private class ModelStubWithTask extends ModelStub {
    //     private final Task task;

    //     ModelStubWithTask(Task task) {
    //         requireNonNull(task);
    //         this.task = task;
    //     }

    //     @Override
    //     public boolean hasTask(Task task) {
    //         requireNonNull(task);
    //         return this.task.isSameTask(task);
    //     }
    // }

    // /** A Model stub that always accept the task being added. */
    // private class ModelStubAcceptingTaskAdded extends ModelStub {
    //     final ArrayList<Task> tasksAdded = new ArrayList<>();

    //     @Override
    //     public boolean hasTask(Task task) {
    //         requireNonNull(task);
    //         return tasksAdded.stream().anyMatch(task::isSameTask);
    //     }

    //     @Override
    //     public void addTask(Task task) {
    //         requireNonNull(task);
    //         tasksAdded.add(task);
    //     }

    //     @Override
    //     public ReadOnlyTaskList getTaskList() {
    //         return new TaskList();
    //     }
    // }
}
