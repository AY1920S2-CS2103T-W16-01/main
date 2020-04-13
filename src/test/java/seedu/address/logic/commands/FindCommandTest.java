package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.NameContainsKeywordsPredicate;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

/** Contains integration tests (interaction with the Model) for {@code FindCommand}. */
public class FindCommandTest {
    private Model model =
            new ModelManager(
                    getTypicalTaskList(),
                    new Pet(),
                    new Pomodoro(),
                    new Statistics(),
                    new UserPrefs());
    private Model expectedModel =
            new ModelManager(
                    getTypicalTaskList(),
                    new Pet(),
                    new Pomodoro(),
                    new Statistics(),
                    new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different task -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_tag_taskFound() {
        HashSet<Tag> testTags = new HashSet<>();
        String[] tagNames = model.getTagNames();
        Tag newTag = new Tag(tagNames[0]);
        testTags.add(newTag);
        NameContainsKeywordsPredicate predicate = preparePredicate(testTags);
        TaskList tl = new TaskList();
        for (Task t : model.getFilteredTaskList()) {
            if (t.hasTag(newTag)) {
                tl.addTask(t);
            }
        }
        model.updateFilteredTaskList(predicate);
        expectedModel.setTaskList(tl);
        assertEquals(expectedModel.getFilteredTaskList(), model.getFilteredTaskList());
    }

    @Test
    public void execute_tagName_taskFound() {
        HashSet<Tag> testTags = new HashSet<>();
        String[] tagNames = model.getTagNames();
        Tag newTag = new Tag(tagNames[0]);
        testTags.add(newTag);
        Task taskWithTag = new TaskBuilder().withName("Im a new task").withTags("forTest").build();
        NameContainsKeywordsPredicate predicate =
                preparePredicate(taskWithTag.getName().fullName, testTags);
        TaskList tl = new TaskList();
        tl.addTask(taskWithTag);
        for (Task t : model.getFilteredTaskList()) {
            if (t.hasTag(newTag)) {
                tl.addTask(t);
            }
        }
        model.addTask(taskWithTag);
        model.updateFilteredTaskList(predicate);
        expectedModel.setTaskList(tl);
        Command sort = new SortCommand(new String[] {"name"});
        try {
            sort.execute(model);
            sort.execute(expectedModel);
        } catch (CommandException e) {
        }

        assertEquals(expectedModel.getFilteredTaskList(), model.getFilteredTaskList());
    }

    @Test
    public void execute_zeroKeywords_noTaskFound() {
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredTaskList());
    }

    /** Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}. */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    private NameContainsKeywordsPredicate preparePredicate(String userInput, Set<Tag> tags) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")), tags);
    }

    private NameContainsKeywordsPredicate preparePredicate(Set<Tag> tags) {
        return new NameContainsKeywordsPredicate(Arrays.asList(new String[0]), tags);
    }
}
