package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import seedu.address.model.Model;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;

/** Adds a person to the address book. */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String[] ALLOWED_SORT_FIELDS = {"priority", "date", "name"};

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
                case "date":
                    temp.add(getReminderComparator());
                case "name":
                    temp.add(getNameComparator());
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
