package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by .isSameTask comparison)
 */
public class TaskList implements ReadOnlyTaskList {

    private final UniqueTaskList tasklist;
    private final SortedList<Task> sortedTaskList;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        tasklist = new UniqueTaskList();
        sortedTaskList = new SortedList<Task>(this.tasklist.asUnmodifiableObservableList());
    }

    public TaskList() {}

    /** Creates an TaskList using the Tasks in the {@code toBeCopied} */
    public TaskList(ReadOnlyTaskList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    public void sort(Comparator<Task> comparator) {
        this.sortedTaskList.setComparator(comparator);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the task list with {@code tasks}. {@code tasks} must not contain
     * duplicate tasks.
     */
    public void setTasks(List<Task> tasklist) {
        this.tasklist.setTasks(tasklist);
    }

    /** Resets the existing data of this {@code TaskList} with {@code newData}. */
    public void resetData(ReadOnlyTaskList newData) {
        requireNonNull(newData);

        setTasks(newData.getTaskList());
    }

    //// task-level operations

    /** Returns true if a task with the same identity as {@code task} exists in the task list. */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasklist.contains(task);
    }

    /** Adds a task to the task list. The task must not already exist in the task list. */
    public void addTask(Task t) {
        tasklist.add(t);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}. {@code target}
     * must exist in the task list. The task identity of {@code editedTask} must not be the same as
     * another existing task in the task list.
     */
    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasklist.setTask(target, editedTask);
    }

    /** Removes {@code key} from this {@code TaskList}. {@code key} must exist in the task list. */
    public void removeTask(Task key) {
        tasklist.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return tasklist.asUnmodifiableObservableList().size() + " tasks";
        // TODO: refine later
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return sortedTaskList;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskList // instanceof handles nulls
                        && tasklist.equals(((TaskList) other).tasklist));
    }

    @Override
    public int hashCode() {
        return tasklist.hashCode();
    }
}
