package seedu.address.model.settings;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class PomDuration implements Comparable {

    public static final String MESSAGE_CONSTRAINTS = "Pomodoro duration should not exceed 1 hour";
    public static final String VALIDATION_REGEX = "[1-60]";
    public final String value;

    public PomDuration(String pomDuration) {
        requireNonNull(pomDuration);
        checkArgument(isValidPomDuration(pomDuration), MESSAGE_CONSTRAINTS);
        value = pomDuration;
    }

    /** Returns true if a given string is a valid pomDuration number. */
    public static boolean isValidPomDuration(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PomDuration // instanceof handles nulls
                        && value.equals(((PomDuration) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof PomDuration)) {
            return 0;
        }
        PomDuration otherPriority = (PomDuration) other;
        return otherPriority.value.compareTo(this.value);
    }
}
