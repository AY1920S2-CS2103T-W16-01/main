package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import seedu.address.model.Model;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;

/** Adds a task to the task list. */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String[] ALLOWED_SORT_FIELDS = {
        "r-priority", "r-date", "r-name", "r-done", "priority", "date", "name", "done"
    };
    public static final String DISPLAY_POSSIBLE_FIELDS =
            "1.(r-)priority\n2.(r-)name\n3.(r-)done\n4.(r-)date";

    public static final String MESSAGE_SUCCESS = "TaskList sorted by: %1$s";
    public static final String MESSAGE_SORT_UNKNOWN = "No such field to sort by %1$s!";
    public static final String MESSAGE_USAGE =
            String.format(
                    "%1$s -> Sorts tasklist by one or multiple fields form these choices:\n%2$s \nExample: %1$s done priority",
                    COMMAND_WORD, DISPLAY_POSSIBLE_FIELDS);

    private String[] fields;

    public SortCommand(String[] fields) {
        this.fields = fields;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // NOTE: Incorrect sort fields has been handled in SortCommandParser already
        ArrayList<Comparator<Task>> comparatorList = new ArrayList<>();
        for (String field : fields) {
            switch (field) {
                case "priority":
                    comparatorList.add(getPriorityComparator());
                    break;
                case "date":
                    comparatorList.add(getReminderComparator());
                    break;
                case "name":
                    comparatorList.add(getNameComparator());
                    break;
                case "done":
                    comparatorList.add(getDoneComparator());
                    break;
                case "r-priority":
                    comparatorList.add(getPriorityComparator().reversed());
                    break;
                case "r-date":
                    comparatorList.add(getReminderComparator().reversed());
                    break;
                case "r-name":
                    comparatorList.add(getNameComparator().reversed());
                    break;
                case "r-done":
                    comparatorList.add(getDoneComparator().reversed());
                    break;
            }
        }

        Comparator<Task> aggregateComparator = comparatorList.get(0);
        for (int i = 1; i < comparatorList.size(); i++) {
            aggregateComparator = aggregateComparator.thenComparing(comparatorList.get(i));
        }

        model.setComparator(aggregateComparator);

        String commandFeedback = String.format(MESSAGE_SUCCESS, String.join(" ", fields));

        return new SortCommandResult(commandFeedback, fields[0]);
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
        };
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
