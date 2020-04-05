package seedu.address.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import seedu.address.model.task.exceptions.InvalidReminderException;

/**
 * Represents a Task's reminder number in the task list. Guarantees: immutable; is valid as declared
 * in {@link #isValidReminder(String)}
 */
public class Reminder implements Comparable {

    public static final String MESSAGE_CONSTRAINTS =
            "Reminder should be in format DD/MM/YY@HH:mm eg 04/11/20@10:30";
    public static final String MESSAGE_CONSTRAINTS_PAST =
            "Operation would result in invalid reminder due to time set being in the past.";
    public static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy@HH:mm");
    private final LocalDateTime reminderDateTime;
    private boolean hasFired;
    private long delay;

    /**
     * Constructs a reminder with the corresponding time, description and name.
     *
     * @param reminderDateTime
     * @param Description
     * @param Name
     * @throws InvalidReminderException
     */
    public Reminder(LocalDateTime reminderDateTime) throws InvalidReminderException {
        this.reminderDateTime = reminderDateTime;
        this.hasFired = false;
        this.delay = Integer.MAX_VALUE;
        setDelay();
    }

    public Reminder(String dateTimeString) throws InvalidReminderException {
        this.reminderDateTime = stringFormatter.parse(dateTimeString, LocalDateTime::from);
        this.hasFired = false;
        this.delay = Integer.MAX_VALUE;
        setDelay();
    }

    public static long calculateDelay(LocalDateTime currentTime, LocalDateTime reminderDateTime) {
        Duration duration = Duration.between(currentTime, reminderDateTime);
        long delay = duration.getSeconds();
        return delay;
    }

    public void setHasFired() {
        this.hasFired = true;
    }

    public static boolean isValidReminder(String dateTimeString) {
        try {
            stringFormatter.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Sets the reminder based on the time delay calculated. If it has fired then nothing is
     * triggered.
     *
     * @throws InvalidReminderException if the time delay is negative and has not been fired before.
     */
    private void setDelay() throws InvalidReminderException {
        long timeDelay = calculateDelay(LocalDateTime.now(), reminderDateTime);
        if (timeDelay < 0) {
            if (!hasFired) {
                throw new InvalidReminderException();
            }
        } else {
            this.delay = timeDelay;
        }
    }

    /** @return the delay */
    public long getDelay() {
        return delay;
    }

    public String displayReminder() {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd LLLL 'at' h:ma");
        return reminderDateTime.format(customFormatter);
    }

    @Override
    public String toString() {
        return reminderDateTime.format(stringFormatter);
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof Reminder)) {
            return 0;
        }
        Reminder otherReminder = (Reminder) other;
        LocalDateTime currentDateTime = LocalDateTime.now();
        long diffFromToday = calculateDelay(currentDateTime, this.reminderDateTime);
        long otherDiffFromToday = calculateDelay(currentDateTime, otherReminder.reminderDateTime);
        System.out.println(diffFromToday);
        System.out.println("=====================");

        if (diffFromToday < otherDiffFromToday) {
            if (diffFromToday < 0) { // if already over, put it lower in the list
                return 1;
            }
            return -1;
        } else if (otherDiffFromToday < diffFromToday) {
            if (otherDiffFromToday < 0) { // if already over put it lower in the list
                return -1;
            }
            return 1;
        } else {
            return 0;
        }
    }
}
