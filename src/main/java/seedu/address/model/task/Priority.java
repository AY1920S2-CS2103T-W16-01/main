package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Task's priority number in the task list. Guarantees: immutable; is valid as declared
 * in {@link #isValidPriority(String)}
 */
public class Priority implements Comparable {

    public static final String MESSAGE_CONSTRAINTS =
            "Priority can only be one of these numbers: {1,2,3}";
    public static final String VALIDATION_REGEX = "[1-3]";
    public final String value;

    /**
     * Constructs a {@code Priority}.
     *
     * @param priority A valid priority number.
     */
    public Priority(String priority) {
        requireNonNull(priority);
        checkArgument(isValidPriority(priority), MESSAGE_CONSTRAINTS);
        value = priority;
    }

    /** Returns true if a given string is a valid priority number. */
    public static boolean isValidPriority(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                        && value.equals(((Priority) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof Priority)) {
            return 0;
        }
        Priority otherPriority = (Priority) other;
        return otherPriority.value.compareTo(this.value);
    }
}
