package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.TaskList;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/** Contains utility methods for populating {@code TaskList} with sample data. */
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(
                    new Name("Alex Yeoh"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("friends")),
            new Task(
                    new Name("Bernice Yu"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("colleagues", "friends")),
            new Task(
                    new Name("Charlotte Oliveiro"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("neighbours")),
            new Task(
                    new Name("David Li"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("family")),
            new Task(
                    new Name("Irfan Ibrahim"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("classmates")),
            new Task(
                    new Name("Roy Balakrishnan"),
                    new Priority("1"),
                    new Description(""),
                    getTagSet("colleagues"))
        };
    }

    public static ReadOnlyTaskList getSampleTaskList() {
        TaskList sampleAb = new TaskList();
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }

    /** Returns a tag set containing the list of strings given. */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
