package seedu.address.model.dayData;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a DayData's PomDurationData number. Guarantees: immutable; is valid as
 * declared in {@link #isValidPomDurationData(String)}
 */
public class PomDurationData {

    public static final String MESSAGE_CONSTRAINTS =
            "PomDurationData is an integer greater than or equals to 0 and less than 1440";
    public final String value;

    /**
     * Constructs a {@code PomDurationData}.
     *
     * @param pomDurationData A valid priority number.
     */
    public PomDurationData(String pomDurationData) {
        requireNonNull(pomDurationData);
        checkArgument(isValidPomDurationData(pomDurationData), MESSAGE_CONSTRAINTS);
        value = pomDurationData;
    }

    /** Returns true if a given string is a valid priority number. */
    public static boolean isValidPomDurationData(String test) {
        try {
            int i = Integer.parseInt(test);
            return (i >= 0 && i < 1440);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PomDurationData // instanceof handles nulls
                && value.equals(((PomDurationData) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
