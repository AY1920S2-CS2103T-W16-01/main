package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HELP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import seedu.address.model.TaskList;
import seedu.address.model.task.Task;

/** A utility class containing a list of {@code Task} objects to be used in tests. */
public class TypicalTasks {

    public static final Task HOMEWORK10 =
            new TaskBuilder()
                    .withName("Homework 10")
                    .withDescription("Pages 100 - 112")
                    .withPriority("1")
                    .withTags("MA1521")
                    .withReminder("15/03/21@16:07")
                    .build();
    public static final Task LAB_3 =
            new TaskBuilder()
                    .withName("Lab 3")
                    .withDescription("Introduction to UDP")
                    .withPriority("2")
                    .withTags("help")
                    .withReminder("10/03/21@16:07")
                    .build();
    public static final Task TUTORIAL_1 =
            new TaskBuilder()
                    .withName("mum tells me to do Tutorial 1")
                    .withPriority("3")
                    .withDescription("Introduction to Calculus")
                    .withTags("MA1521")
                    .withReminder("05/03/21@16:07")
                    .build();
    public static final Task BUY_MILK =
            new TaskBuilder()
                    .withName("Buy milk")
                    .withPriority("2")
                    .withDescription("Very hungry")
                    .withRecurring("w", LocalDateTime.now())
                    .build();
    public static final Task FEED_CAT =
            new TaskBuilder()
                    .withName("mum tells me to feed cat")
                    .withPriority("3")
                    .withDescription("Cat is making noise")
                    .withReminder("08/03/21@16:07")
                    .withRecurring("d", LocalDateTime.now())
                    .build();
    public static final Task RUN =
            new TaskBuilder()
                    .withName("mum tells me to run")
                    .withPriority("1")
                    .withDescription("Complete 2.4km run!")
                    .withRecurring("w", LocalDateTime.now())
                    .build();
    public static final Task GYM =
            new TaskBuilder()
                    .withName("Gym")
                    .withPriority("3")
                    .withDescription("Complete 100 sets of pushups")
                    .withRecurring("w", LocalDateTime.now())
                    .build();

    // Manually added
    public static final Task OPTIONAL_LAB =
            new TaskBuilder()
                    .withName("Bonus Lab")
                    .withPriority("1")
                    .withDescription("Implement Google")
                    .build();
    public static final Task OPTIONAL_HOMEWORK =
            new TaskBuilder()
                    .withName("Homework 11")
                    .withPriority("2")
                    .withDescription("Fourier Analysis")
                    .withTags("Optional")
                    .build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task TASK1 =
            new TaskBuilder()
                    .withName(VALID_NAME_TASK1)
                    .withPriority(VALID_PRIORITY_TASK1)
                    .withDescription(VALID_DESCRIPTION_TASK1)
                    .withTags(VALID_TAG_HELP)
                    .build();
    public static final Task TASK2 =
            new TaskBuilder()
                    .withName(VALID_NAME_TASK2)
                    .withPriority(VALID_PRIORITY_TASK2)
                    .withDescription(VALID_DESCRIPTION_TASK2)
                    .withTags(VALID_TAG_MA1521, VALID_TAG_HELP)
                    .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalTasks() {} // prevents instantiation

    /** Returns an {@code TaskList} with all the typical tasks. */
    public static TaskList getTypicalTaskList() {
        TaskList ab = new TaskList();
        for (Task task : getTypicalTasks()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(
                Arrays.asList(HOMEWORK10, LAB_3, TUTORIAL_1, BUY_MILK, FEED_CAT, RUN, GYM));
    }
}
