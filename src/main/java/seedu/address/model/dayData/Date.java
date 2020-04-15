package seedu.address.model.dayData;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a DayDaya's date in the task list. Guarantees: immutable; is valid as declared in
 * {@link #isValidDate(String)}
 */
public class Date {
    public static final String MESSAGE_CONSTRAINTS =
            "Date must be in the form yyyy-mm-dd and is a valid date in the Gregorian calender";
    public static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
                    .withResolverStyle(ResolverStyle.STRICT);
    public final LocalDate value;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        LocalDate localDate = LocalDate.parse(date);
        value = localDate;
    }

    /** Returns true if a given string is a valid date. */
    public static boolean isValidDate(String test) {
        try {
            dateFormatter.parse(test);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /** Returns dateString to display for user output. */
    public String toPrint() {
        String dateString = value.toString();
        return dateString.substring(5, 7) + "/" + dateString.substring(8, 10);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                        && value.equals(((Date) other).value)); // state check
    }
}
