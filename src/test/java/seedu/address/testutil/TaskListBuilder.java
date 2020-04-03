package seedu.address.testutil;

import seedu.address.model.TaskList;
import seedu.address.model.task.Task;

/**
 * A utility class to help with building TaskList objects. Example usage: <br>
 * {@code TaskList ab = new TaskListBuilder().withTask("John", "Doe").build();}
 */
public class TaskListBuilder {

    private TaskList taskList;

    public TaskListBuilder() {
        taskList = new TaskList();
    }

    public TaskListBuilder(TaskList taskList) {
        this.taskList = taskList;
    }

    /** Adds a new {@code Task} to the {@code TaskList} that we are building. */
    public TaskListBuilder withTask(Task task) {
        taskList.addTask(task);
        return this;
    }

    public TaskList build() {
        return taskList;
    }
}
