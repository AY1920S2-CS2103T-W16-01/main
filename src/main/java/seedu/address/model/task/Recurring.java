package seedu.address.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents the task's recurring attribute and functionality.
 *
 * @author Arthur Lee
 * @version 1.4
 */
public class Recurring {
    private final RecurType type;
    private final LocalDateTime referenceDateTime;

    public static final String MESSAGE_CONSTRAINTS =
            "Recurring should be in the format d or w or t, for eg: rec/d";
    public static final String MESSAGE_RECURRING_TASK_SUCCESS = "Recurring task has been reset:\n";

    public static final String VALIDATION_REGEX = "[dwt]";
    public static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy@HH:mm");

    /** Constructor for recurring instance using String only read from JsonStorage. */
    public Recurring(String recurringStringStorage) throws ParseException {
        String recurTypeString = recurringStringStorage.substring(0, 1);
        String dateTimeString = recurringStringStorage.substring(1);
        this.type = parseRecurType(recurTypeString);
        this.referenceDateTime = parseDateTime(dateTimeString);
    }

    /**
     * Constructor for recurring instance using user input for recurring type as well as reference
     * LocalDateTime for when recurring is added.
     */
    public Recurring(String recurringString, LocalDateTime referenceDateTime)
            throws ParseException {
        this.type = parseRecurType(recurringString);
        this.referenceDateTime = referenceDateTime;
    }

    /** Parses dateTimeString to get LocalDateTime. */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return stringFormatter.parse(dateTimeString, LocalDateTime::from);
    }

    /** Parses recurring type string to return RecurType enum. */
    public static RecurType parseRecurType(String recurringString) throws ParseException {
        if (recurringString.equals("d")) {
            return RecurType.DAILY;
        } else if (recurringString.equals("w")) {
            return RecurType.WEEKLY;
        } else if (recurringString.equals("t")) {
            return RecurType.TEST;
        } else {
            throw new ParseException(Recurring.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Determines whether reminder should be updated during recurring behaviour based on whether the
     * reminder has been triggered yet.
     */
    public static boolean shouldUpdateReminder(LocalDateTime reminderDateTime) {
        Duration duration = Duration.between(LocalDateTime.now(), reminderDateTime);
        boolean hasPassed = duration.getSeconds() < 0;
        return hasPassed;
    }

    /** Returns boolean on whether the task should be updated or not */
    public static boolean shouldUpdateTask(Task t) {
        boolean isDone = t.getDone().getIsDone();
        Optional<Reminder> optReminder = t.getOptionalReminder();
        boolean updateReminder = false;
        if (optReminder.isPresent()) {
            LocalDateTime reminderDateTime = optReminder.get().getDateTime();
            updateReminder = shouldUpdateReminder(reminderDateTime);
        }
        return isDone || updateReminder;
    }

    /** Updates reminder time based on the time interval indicated by the user. */
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

    /**
     * Gets delay for the first trigger of the recurring based on the reference date time. For
     * testing the delay is set as 60 seconds.
     */
    public long getDelayToFirstTrigger() {
        if (type == RecurType.TEST) {
            return 60000l;
        }
        long delay = Duration.between(
                        LocalDateTime.now(),
                        referenceDateTime.plusDays(type.getDayInterval()))
                .getSeconds();
        return delay >= 0 ? delay * 1000 : 0;
    }

    /** Gets the time interval in milliseconds based on the recurring type. */
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
