package seedu.address.model.dayData;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a DayData's TasksDoneData number. Guarantees: immutable; is valid as declared in
 * {@link #isValidTasksDoneData(String)}
 */
public class TasksDoneData {

    public static final String MESSAGE_CONSTRAINTS =
            "TasksDoneData is an integer greater than or equals to 0";
    public final Integer value;

    /** Constructs a {@code TasksDoneData}. */
    public TasksDoneData() {
        value = 0;
    }

    /**
     * Constructs a {@code TasksDoneData}.
     *
     * @param taskDoneData A valid TasksDoneData.
     */
    public TasksDoneData(String taskDoneData) {
        requireNonNull(taskDoneData);
        checkArgument(isValidTasksDoneData(taskDoneData), MESSAGE_CONSTRAINTS);
        value = Integer.valueOf(taskDoneData);
    }

    /** Returns true if a given string is a valid TasksDoneData. */
    public static boolean isValidTasksDoneData(String test) {
        try {
            int i = Integer.parseInt(test);
            return i >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TasksDoneData // instanceof handles nulls
                        && value.equals(((TasksDoneData) other).value)); // state check
    }
}
