package seedu.address.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Task's Done in the task list. Guarantees: immutable; is valid as declared in {@link
 * #isValidDone(String)}
 */
public class Done implements Comparable {

    public static final String MESSAGE_CONSTRAINTS = "Done should be a simple Boolean ";

    public static final String DONE = "Y";
    public static final String NOT_DONE = "N";

    public static final String VALIDATION_REGEX = "Y|N";

    private final Boolean isDone;

    /**
     * Constructs a {@code Done}.
     *
     * @param isDone A valid Done.
     */
    public Done(String isDone) {
        requireNonNull(isDone);
        checkArgument(isValidDone(isDone), MESSAGE_CONSTRAINTS);
        this.isDone = isDone.equals(DONE);
    }

    public Done() {
        this.isDone = false;
    }

    /** Returns true if a given string is a valid name. */
    public static boolean isValidDone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public Boolean getIsDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return isDone ? DONE : NOT_DONE;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Done // instanceof handles nulls
                        && isDone == (((Done) other).isDone)); // state check
    }

    @Override
    public int hashCode() {
        return isDone.hashCode();
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof Done)) {
            return 0;
        }
        Done otherDone = (Done) other;
        return this.isDone.compareTo(otherDone.isDone);
    }
}
