package seedu.address.model.settings;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class DailyTarget implements Comparable {

    public static final String MESSAGE_CONSTRAINTS = "Daily Target should not exceed 720 mins";
    public static final String VALIDATION_REGEX = "[1-720]";
    public final String value;

    public DailyTarget(String dailyTarget) {
        requireNonNull(dailyTarget);
        checkArgument(isValidDailyTarget(dailyTarget), MESSAGE_CONSTRAINTS);
        value = dailyTarget;
    }

    /** Returns true if a given string is a valid pomDuration number. */
    public static boolean isValidDailyTarget(String test) {
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
                || (other instanceof DailyTarget // instanceof handles nulls
                        && value.equals(((DailyTarget) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof DailyTarget)) {
            return 0;
        }
        DailyTarget otherPriority = (DailyTarget) other;
        return otherPriority.value.compareTo(this.value);
    }
}
