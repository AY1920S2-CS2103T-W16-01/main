package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
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
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

public class SetCommandTest {

    @Test
    public void setPetName_successful() {
        Pet pet = new Pet();
        ModelStubWithPet modelStub = new ModelStubWithPet(pet);
        modelStub.setPetName("Momu");
        assertTrue(pet.getName().equals("Momu"));
    }

    @Test
    public void setPomodoroDefaultTime_successful() {
        Pomodoro pomodoro = new Pomodoro();
        ModelStubWithPomodoro modelStub = new ModelStubWithPomodoro(pomodoro);
        modelStub.setPomodoroDefaultTime(5);
        assertTrue(pomodoro.getDefaultTime().equals("5.0"));
    }

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
        public boolean hasTag(Tag t) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Set<Tag> getTagSet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void showAllTasks() {
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
        public void setComparator(Comparator<Task> compare) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortSearchByRelevance(Comparator<Task> comparator) {
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
        public Statistics getStatistics() {
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
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStatisticsManager(StatisticsManager statisticsManager) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void notifyMainWindow(String inpuString) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addObserver(Observer observer) {
            throw new AssertionError("This method should not be called.");
        }
    }

    private class ModelStubWithPomodoro extends ModelStub {
        private final Pomodoro pomodoro;

        ModelStubWithPomodoro(Pomodoro pomodoro) {
            this.pomodoro = pomodoro;
        }

        @Override
        public void setPomodoroDefaultTime(float defaultTimeInMin) {
            this.pomodoro.setDefaultTime(Float.toString(defaultTimeInMin));
        }
    }

    private class ModelStubWithPet extends ModelStub {
        private final Pet pet;

        ModelStubWithPet(Pet pet) {
            this.pet = pet;
        }

        @Override
        public void setPetName(String name) {
            this.pet.setName(name);
        }
    }
}
