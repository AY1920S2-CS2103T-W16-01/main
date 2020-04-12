package seedu.address.model.settings;

import static java.util.Objects.requireNonNull;

public class DailyTarget implements Comparable {

    public static final String MESSAGE_CONSTRAINTS =
            "Daily Target should be more than 0 mins and does not exceed 720 mins, and it should not be blank";
    // 720/60 = 12. It is no longer productive to be doing work straight for more than 12 hours per
    // day

    public static final String VALIDATION_REGEX =
            "^([0-9]|[1-9][0-9]|[1-6][0-9][0-9]|7[0-1][0-9]|720)$";
    public final String value;

    public DailyTarget(String dailyTarget) {
        requireNonNull(dailyTarget);
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
