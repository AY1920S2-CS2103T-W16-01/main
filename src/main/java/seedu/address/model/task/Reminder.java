package seedu.address.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import seedu.address.model.task.exceptions.InvalidReminderException;

/**
 * Represents the task's reminder and its functionality.
 *
 * @author Arthur Lee
 * @version 1.4
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
     * Constructs a reminder with the corresponding LocalDateTime. By default its hasFired is set to
     * false and delay is the maximum value possible. On construction, delay is calculated and set.
     *
     * @param reminderDateTime
     * @throws InvalidReminderException
     */
    public Reminder(LocalDateTime reminderDateTime) throws InvalidReminderException {
        this.reminderDateTime = reminderDateTime;
        this.hasFired = false;
        this.delay = Integer.MAX_VALUE;
        setDelay();
    }

    /**
     * Constructs a reminder with the dateTimeString read from user input. On construction, delay is
     * calculated and set.
     *
     * @param dateTimeString
     * @throws InvalidReminderException
     */
    public Reminder(String dateTimeString) throws InvalidReminderException {
        this.reminderDateTime = stringFormatter.parse(dateTimeString, LocalDateTime::from);
        this.hasFired = false;
        this.delay = Integer.MAX_VALUE;
        setDelay();
    }

    /**
     * Calculates the time delay between the current time and the time indicated by user for the
     * reminder.
     *
     * @param currentTime
     * @param reminderDateTime
     * @return
     */
    public static long calculateDelay(LocalDateTime currentTime, LocalDateTime reminderDateTime) {
        Duration duration = Duration.between(currentTime, reminderDateTime);
        long delay = duration.getSeconds();
        return delay;
    }

    /** Setter for hasFired. */
    public void setHasFired() {
        this.hasFired = true;
    }

    /** Gets the hasFired boolean attribute */
    public boolean getHasFired() {
        return hasFired;
    }

    /**
     * Boolean check valid reminder when parsing the string input from the user.
     *
     * @param dateTimeString
     */
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

    /** Getter for reminder's LocalDateTime. */
    public LocalDateTime getDateTime() {
        return reminderDateTime;
    }

    /** Getter for reminder's time delay. */
    public long getDelay() {
        return delay;
    }

    /**
     * Formats the string for displaying on the taskcard.
     *
     * @return String
     */
    public String displayReminder() {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd LLLL 'at' h:mma");
        return reminderDateTime.format(customFormatter);
    }

    @Override
    public String toString() {
        return reminderDateTime.format(stringFormatter);
    }

    /** Overrides compareTo method to use time delay as point of comparison. */
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
