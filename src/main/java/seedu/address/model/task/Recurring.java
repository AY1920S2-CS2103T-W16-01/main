package seedu.address.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;

public class Recurring {
    private final RecurType type;
    private final LocalDateTime referenceDateTime;

    public static final String MESSAGE_CONSTRAINTS =
            "Recurring should be in the format d or w, for eg: rec/d";
    public static final String MESSAGE_RECURRING_TASK_SUCCESS = "Recurring Task: %1$s";

    public static final String VALIDATION_REGEX = "[dw]";
    public static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy@HH:mm");

    public Recurring(String recurringStringStorage) throws ParseException {
        String recurTypeString = recurringStringStorage.substring(0, 1);
        String dateTimeString = recurringStringStorage.substring(1);
        this.type = parseRecurType(recurTypeString);
        this.referenceDateTime = parseDateTime(dateTimeString);
    }

    public Recurring(String recurringString, LocalDateTime referenceDateTime)
            throws ParseException {
        this.type = parseRecurType(recurringString);
        this.referenceDateTime = referenceDateTime;
    }

    public LocalDateTime parseDateTime(String dateTimeString) {
        return stringFormatter.parse(dateTimeString, LocalDateTime::from);
    }

    public RecurType parseRecurType(String recurringString) throws ParseException {
        if (recurringString.equals("d")) {
            return RecurType.DAILY;
        } else if (recurringString.equals("w")) {
            return RecurType.WEEKLY;
        } else {
            throw new ParseException(Recurring.MESSAGE_CONSTRAINTS);
        }
    }

    public boolean shouldUpdateReminder(LocalDateTime reminderDateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), reminderDateTime);
        boolean hasPassed = duration.getSeconds() < 0;
        return hasPassed;
    }

    public LocalDateTime getUpdatedReminderTime(Reminder currentReminder) {
        LocalDateTime currentDateTime = currentReminder.getDateTime();
        if (shouldUpdateReminder(currentDateTime)) {
            currentDateTime = currentDateTime.plusDays(type.getDayInterval());
        }
        return currentDateTime;
    }

    /** Returns true if a given string is a valid name. */
    public static boolean isValidRecurring(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /** Returns Daily or Weekly for display on the card. */
    public String displayRecurring() {
        return StringUtil.getTitleCase(type.name());
    }

    public long getDelayToFirstTrigger() {
        // long delay = Duration.between(
        //                 LocalDateTime.now(),
        //                 referenceDateTime.plusDays(type.getDayInterval()))
        //         .getSeconds();
        // return delay >= 0 ? delay * 1000 : 0;
        return 60000l; // for testing
    }

    public long getPeriod() {
        return type.getInterval();
    }

    @Override
    public String toString() {
        String typeString = type.name().substring(0, 1).toLowerCase();
        String dateTimeString = referenceDateTime.format(stringFormatter);
        return typeString + dateTimeString;
    }
}
