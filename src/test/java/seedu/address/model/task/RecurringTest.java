package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import seedu.address.logic.parser.exceptions.ParseException;

public class RecurringTest {

    @Test
    public void parseDateTime() {
        // valid parsing
        LocalDateTime dateTime = Recurring.parseDateTime("17/03/21@14:17");
        assertEquals(2021, dateTime.getYear());
        assertEquals(3, dateTime.getMonthValue());
        assertEquals(17, dateTime.getDayOfMonth());
        assertEquals(14, dateTime.getHour());
        assertEquals(17, dateTime.getMinute());
    }

    @Test
    public void parseRecurType() throws ParseException {
        // valid types
        assertEquals(RecurType.DAILY, Recurring.parseRecurType("d"));
        assertEquals(RecurType.WEEKLY, Recurring.parseRecurType("w"));

        // invalid and thus catch exception
        assertThrows(ParseException.class, () -> Recurring.parseRecurType("m"));
    }

    @Test
    public void shouldUpdateReminder() throws ParseException {
        // assert True
        Recurring testRecurring = new Recurring("d", LocalDateTime.of(2020, 03, 18, 14, 16));
        LocalDateTime testDateTime = LocalDateTime.of(2020, 03, 18, 14, 17);
        assertTrue(testRecurring.shouldUpdateReminder(testDateTime));

        // assert False
        LocalDateTime testFalseDateTime = LocalDateTime.of(2021, 03, 18, 14, 16);
        assertFalse(testRecurring.shouldUpdateReminder(testFalseDateTime));
    }
}
