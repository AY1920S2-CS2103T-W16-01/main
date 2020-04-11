package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTasks.HOMEWORK10;
import static seedu.address.testutil.TypicalTasks.LAB_3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.task.NameContainsKeywordsPredicate;
import seedu.address.testutil.TaskListBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new TaskList(), new TaskList(modelManager.getTaskList()));
        assertEquals(new Statistics(), modelManager.getStatistics());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setTaskListFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setTaskListFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setTaskListFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setTaskListFilePath(null));
    }

    @Test
    public void setTaskListFilePath_validPath_setsTaskListFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setTaskListFilePath(path);
        assertEquals(path, modelManager.getTaskListFilePath());
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasTask(null));
    }

    @Test
    public void hasTask_taskNotInTaskList_returnsFalse() {
        assertFalse(modelManager.hasTask(HOMEWORK10));
    }

    @Test
    public void hasTask_taskInTaskList_returnsTrue() {
        modelManager.addTask(HOMEWORK10);
        assertTrue(modelManager.hasTask(HOMEWORK10));
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> modelManager.getFilteredTaskList().remove(0));
    }

    @Test
    public void setStatisticsManager_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStatisticsManager(null));
    }

    @Test
    public void equals() {
        TaskList taskList = new TaskListBuilder().withTask(HOMEWORK10).withTask(LAB_3).build();
        TaskList differentTaskList = new TaskList();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager =
                new ModelManager(taskList, new Pet(), new Pomodoro(), new Statistics(), userPrefs);
        ModelManager modelManagerCopy =
                new ModelManager(taskList, new Pet(), new Pomodoro(), new Statistics(), userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different taskList -> returns false
        assertFalse(
                modelManager.equals(
                        new ModelManager(
                                differentTaskList,
                                new Pet(),
                                new Pomodoro(),
                                new Statistics(),
                                userPrefs)));

        // different filteredList -> returns false
        String[] keywords = HOMEWORK10.getName().fullName.split("\\s+");
        modelManager.updateFilteredTaskList(
                new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(
                modelManager.equals(
                        new ModelManager(
                                taskList, new Pet(), new Pomodoro(), new Statistics(), userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.showAllTasks();

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTaskListFilePath(Paths.get("differentFilePath"));
        assertFalse(
                modelManager.equals(
                        new ModelManager(
                                taskList,
                                new Pet(),
                                new Pomodoro(),
                                new Statistics(),
                                differentUserPrefs)));
    }
}
