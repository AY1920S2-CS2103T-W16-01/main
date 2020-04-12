package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Observer;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.StatisticsManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.TaskList;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTaskAdded modelStub = new ModelStubAcceptingTaskAdded();
        Task validTask = new TaskBuilder().build();

        CommandResult commandResult = new AddCommand(validTask).execute(modelStub);

        assertEquals(
                String.format(AddCommand.MESSAGE_SUCCESS, validTask),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTask), modelStub.tasksAdded);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() {
        Task validTask = new TaskBuilder().build();
        AddCommand addCommand = new AddCommand(validTask);
        ModelStub modelStub = new ModelStubWithTask(validTask);

        assertThrows(
                CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_TASK,
                () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Task alice = new TaskBuilder().withName("Alice").build();
        Task bob = new TaskBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different task -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /** A default model stub that have all of the methods failing. */
    private class ModelStub implements Model {
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
        public Set<Tag> getTagSet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTag(Tag t) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskSaver(TaskSaver taskSaver) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskList(ReadOnlyTaskList newData) {
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
        public ObservableList<Task> getFilteredTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void showAllTasks() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyPet getPet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public PetManager getPetManager() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPetName(String name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setComparator(Comparator<Task> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortSearchByRelevance(Comparator<Task> compare) {
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
        public void setPetManager(PetManager PetManager) {
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

        @Override
        public void updateDataDatesStatistics() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatesDayDataStatistics(DayData dayData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public DayData getDayDataFromDateStatistics(Date date) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Task getPomodoroTask() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPomodoroDefaultTime(float defaultTimeInMin) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPomodoroRestTime(float restTimeInMin) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPomodoroTimeLeft(float timeLeft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setStatisticsManager(StatisticsManager statisticsManager) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyMainWindow(String inputString) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addObserver(Observer observer) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /** A Model stub that contains a single task. */
    private class ModelStubWithTask extends ModelStub {
        private final Task task;

        ModelStubWithTask(Task task) {
            requireNonNull(task);
            this.task = task;
        }

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return this.task.isSameTask(task);
        }
    }

    /** A Model stub that always accept the task being added. */
    private class ModelStubAcceptingTaskAdded extends ModelStub {
        final ArrayList<Task> tasksAdded = new ArrayList<>();

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return tasksAdded.stream().anyMatch(task::isSameTask);
        }

        @Override
        public void addTask(Task task) {
            requireNonNull(task);
            tasksAdded.add(task);
        }

        @Override
        public ReadOnlyTaskList getTaskList() {
            return new TaskList();
        }
    }
}
