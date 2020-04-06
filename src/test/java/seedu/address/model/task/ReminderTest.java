package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ReminderTest {
    @Test
    public void isValidReminder() {
        // valid reminders
        assertTrue(Reminder.isValidReminder("17/03/20@14:17")); // regular example of a date
        assertTrue(Reminder.isValidReminder("17/03/20@07:17")); // regular example but an AM date
        assertFalse(Reminder.isValidReminder("")); // empty string not allowed
        assertFalse(
                Reminder.isValidReminder("17th March at 7.17am")); // miscallenous date not allowed
        assertFalse(Reminder.isValidReminder("jfknsjvskvks")); // gibberish not allowed
    }

    @Test
    public void calculateDelay() {
        LocalDateTime pastTime =
                Reminder.stringFormatter.parse("17/03/20@14:17", LocalDateTime::from);
        LocalDateTime futureTime =
                Reminder.stringFormatter.parse("17/03/20@14:30", LocalDateTime::from);
        assertTrue(Reminder.calculateDelay(pastTime, futureTime) == 780);
    }
}
