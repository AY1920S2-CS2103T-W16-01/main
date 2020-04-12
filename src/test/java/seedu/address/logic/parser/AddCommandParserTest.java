package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RECURRING;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_REMINDER_PAST;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.RECURRING;
import static seedu.address.logic.commands.CommandTestUtil.REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HELP;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_MA1521;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RECURRING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HELP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTasks.TASK1;
import static seedu.address.testutil.TypicalTasks.TASK2;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Task expectedTask = new TaskBuilder(TASK2).withTags(VALID_TAG_HELP).build();

        // whitespace only preamble
        assertParseSuccess(
                parser,
                PREAMBLE_WHITESPACE
                        + NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_HELP,
                new AddCommand(expectedTask));

        // multiple names - last name accepted
        assertParseSuccess(
                parser,
                NAME_DESC_TASK1
                        + NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_HELP,
                new AddCommand(expectedTask));

        // multiple prioritys - last priority accepted
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK1
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_HELP,
                new AddCommand(expectedTask));

        // multiple emails - last email accepted
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2 + PRIORITY_DESC_TASK2 + DESCRIPTION_DESC_TASK2 + TAG_DESC_HELP,
                new AddCommand(expectedTask));

        // multiple addresses - last address accepted
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK1
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_HELP,
                new AddCommand(expectedTask));

        // multiple tags - all accepted
        Task expectedTaskMultipleTags =
                new TaskBuilder(TASK2).withTags(VALID_TAG_HELP, VALID_TAG_MA1521).build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP,
                new AddCommand(expectedTaskMultipleTags));

        // with reminder and no tags
        Task expectedTaskReminder =
                new TaskBuilder(TASK2).withTags().withReminder(VALID_REMINDER).build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2 + PRIORITY_DESC_TASK2 + DESCRIPTION_DESC_TASK2 + REMINDER,
                new AddCommand(expectedTaskReminder));

        // with reminder and tags
        Task expectedTaskReminderTags =
                new TaskBuilder(TASK2)
                        .withTags(VALID_TAG_HELP, VALID_TAG_MA1521)
                        .withReminder(VALID_REMINDER)
                        .build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP
                        + REMINDER,
                new AddCommand(expectedTaskReminderTags));

        // with recurring
        Task expectedTaskRecurring =
                new TaskBuilder(TASK2)
                        .withTags()
                        .withRecurring(VALID_RECURRING, LocalDateTime.now())
                        .build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2 + PRIORITY_DESC_TASK2 + DESCRIPTION_DESC_TASK2 + RECURRING,
                new AddCommand(expectedTaskRecurring));

        // with recurring and reminder
        Task expectedTaskRecurRem =
                new TaskBuilder(TASK2)
                        .withTags()
                        .withReminder(VALID_REMINDER)
                        .withRecurring(VALID_RECURRING, LocalDateTime.now())
                        .build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + REMINDER
                        + RECURRING,
                new AddCommand(expectedTaskRecurRem));

        // with recurring, reminder and tags
        Task expectedTaskEverything =
                new TaskBuilder(TASK2)
                        .withTags(VALID_TAG_HELP, VALID_TAG_MA1521)
                        .withReminder(VALID_REMINDER)
                        .withRecurring(VALID_RECURRING, LocalDateTime.now())
                        .build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP
                        + REMINDER
                        + RECURRING,
                new AddCommand(expectedTaskEverything));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Task expectedTask = new TaskBuilder(TASK1).withTags().build();
        assertParseSuccess(
                parser,
                NAME_DESC_TASK1 + PRIORITY_DESC_TASK1 + DESCRIPTION_DESC_TASK1,
                new AddCommand(expectedTask));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(
                parser,
                VALID_NAME_TASK2 + PRIORITY_DESC_TASK2 + DESCRIPTION_DESC_TASK2,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(
                parser,
                INVALID_NAME_DESC
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP,
                Name.MESSAGE_CONSTRAINTS);

        // invalid priority
        assertParseFailure(
                parser,
                NAME_DESC_TASK2
                        + INVALID_PRIORITY_DESC
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP,
                Priority.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + INVALID_TAG_DESC
                        + VALID_TAG_HELP,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(
                parser, INVALID_NAME_DESC + INVALID_PRIORITY_DESC, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(
                parser,
                PREAMBLE_NON_EMPTY
                        + NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521
                        + TAG_DESC_HELP,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // invalid reminder due to wrong format
        assertParseFailure(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + VALID_TAG_HELP
                        + INVALID_REMINDER,
                Reminder.MESSAGE_CONSTRAINTS);

        // invalid reminder due time being in the past
        assertParseFailure(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + VALID_TAG_HELP
                        + INVALID_REMINDER_PAST,
                Reminder.MESSAGE_CONSTRAINTS_PAST);

        // invalid recurring due to wrong format
        assertParseFailure(
                parser,
                NAME_DESC_TASK2
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + VALID_TAG_HELP
                        + INVALID_RECURRING,
                Recurring.MESSAGE_CONSTRAINTS);
    }
}
