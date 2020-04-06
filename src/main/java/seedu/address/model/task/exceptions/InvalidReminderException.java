package seedu.address.model.task.exceptions;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Reminder;

/** Signals that the reminder's time is before the current time */
public class InvalidReminderException extends ParseException {
    public InvalidReminderException() {
        super(Reminder.MESSAGE_CONSTRAINTS_PAST);
    }
}
