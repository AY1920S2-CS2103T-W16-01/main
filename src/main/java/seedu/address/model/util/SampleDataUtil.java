package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.TaskList;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/** Contains utility methods for populating {@code TaskList} with sample data. */
public class SampleDataUtil {
    public static final Task TASK1 =
            new TaskBuilder()
                    .withName("Homework 10")
                    .withDescription("Pages 100 - 112")
                    .withPriority("1")
                    .withTags("MA1521")
                    .build();
    public static final Task TASK2 =
            new TaskBuilder()
                    .withName("Lab 3")
                    .withDescription("Introduction to UDP")
                    .withPriority("2")
                    .withTags("help")
                    .build();
    public static final Task TASK3 =
            new TaskBuilder()
                    .withName("mum tells me to do Tutorial 1")
                    .withPriority("3")
                    .withDescription("Introduction to Calculus")
                    .withTags("MA1521")
                    .build();
    public static final Task TASK4 =
            new TaskBuilder()
                    .withName("Buy milk")
                    .withPriority("2")
                    .withDescription("Very hungry")
                    .build();
    public static final Task TASK5 =
            new TaskBuilder()
                    .withName("mum tells me to feed cat")
                    .withPriority("3")
                    .withDescription("Cat is making noise")
                    .build();
    public static final Task TASK6 =
            new TaskBuilder()
                    .withName("mum tells me to run")
                    .withPriority("1")
                    .withDescription("Complete 2.4km run!")
                    .build();
    public static final Task TASK7 =
            new TaskBuilder()
                    .withName("Gym")
                    .withPriority("3")
                    .withDescription("Complete 100 sets of pushups")
                    .build();

    public static Task[] getSampleTasks() {
        return new Task[] {TASK1, TASK2, TASK3, TASK4, TASK5, TASK6, TASK7};
    }

    public static ReadOnlyTaskList getSampleTaskList() {
        TaskList sampleTaskList = new TaskList();
        for (Task sampleTask : getSampleTasks()) {
            sampleTaskList.addTask(sampleTask);
        }
        return sampleTaskList;
    }

    /** Returns a tag set containing the list of strings given. */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
