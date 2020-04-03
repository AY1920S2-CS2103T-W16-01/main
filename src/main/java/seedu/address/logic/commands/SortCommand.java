package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import seedu.address.model.Model;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;

/** Adds a task to the address book. */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String[] ALLOWED_SORT_FIELDS = {
        "r-priority", "r-date", "r-name", "r-done", "priority", "date", "name", "done"
    }; // TODO replace with enum

    public static final String MESSAGE_SUCCESS = "TaskList sorted by: %1$s";
    public static final String MESSAGE_SORT_UNKNOWN = "No such field to sort by %1$s!";
    public static final String MESSAGE_USAGE =
            String.format(
                    "%1$s -> Sorts tasklist by certain field such as\n%2$s \nExample: %1$s priority",
                    COMMAND_WORD, String.join(" | ", ALLOWED_SORT_FIELDS));

    private String[] fields;

    public SortCommand(String[] fields) {
        this.fields = fields;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // NOTE: Incorrect sort fields has been handled in SortCommandParser already
        ArrayList<Comparator<Task>> temp = new ArrayList<>();
        for (String field : fields) {
            switch (field) {
                case "priority":
                temp.add(getPriorityComparator());
                break;
                case "date":
                temp.add(getReminderComparator());
                break;
                case "name":
                temp.add(getNameComparator());
                break;
                case "done":
                temp.add(getDoneComparator());
                break;
                case "r-priority":
                temp.add(getPriorityComparator().reversed());
                break;
                case "r-date":
                temp.add(getReminderComparator().reversed());
                break;
                case "r-name":
                temp.add(getNameComparator().reversed());
                break;
                case "r-done":
                temp.add(getDoneComparator().reversed());
                break;
            }
        }

        model.setComparator(temp.toArray(new Comparator[0]));
        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join(" ", fields)));
    }

    private Comparator<Task> getPriorityComparator() {
        return new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getPriority().compareTo(task2.getPriority());
            }
        };
    }

    private Comparator<Task> getDoneComparator() {
        return new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getDone().compareTo(task2.getDone());
            }
        };
    }

    private Comparator<Task> getNameComparator() {
        return new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getName().compareTo(task2.getName());
            }
        }.reversed();
    }

    private Comparator<Task> getReminderComparator() {
        return new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                Optional<Reminder> reminder1 = task1.getOptionalReminder();
                Optional<Reminder> reminder2 = task2.getOptionalReminder();
                if (reminder1.isPresent() && reminder2.isPresent()) {
                    return reminder1.get().compareTo(reminder2.get());
                }
                if (reminder1.isPresent()) {
                    return -1;
                }
                if (reminder2.isPresent()) {
                    return 1;
                }
                return 0;
            }
        };
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortCommand // instanceof handles nulls
                        && fields.equals(((SortCommand) other).fields)); // state check
    }
}
