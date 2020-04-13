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
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.RECURRING;
import static seedu.address.logic.commands.CommandTestUtil.REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HELP;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_MA1521;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RECURRING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMINDER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HELP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_TASK1, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_TASK1, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_TASK1, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(
                parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(
                parser,
                "1" + INVALID_PRIORITY_DESC,
                Priority.MESSAGE_CONSTRAINTS); // invalid priority
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid priority
        assertParseFailure(parser, "1" + INVALID_PRIORITY_DESC, Priority.MESSAGE_CONSTRAINTS);

        // valid priority followed by invalid priority. The test case for invalid
        // priority followed
        // by valid
        // priority
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(
                parser,
                "1" + PRIORITY_DESC_TASK2 + INVALID_PRIORITY_DESC,
                Priority.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code
        // Task} being
        // edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(
                parser, "1" + TAG_DESC_HELP + TAG_DESC_MA1521 + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(
                parser, "1" + TAG_DESC_HELP + TAG_EMPTY + TAG_DESC_MA1521, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(
                parser, "1" + TAG_EMPTY + TAG_DESC_HELP + TAG_DESC_MA1521, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(
                parser,
                "1" + INVALID_NAME_DESC + VALID_DESCRIPTION_TASK1 + VALID_PRIORITY_TASK1,
                Name.MESSAGE_CONSTRAINTS);

        // reminders, for invalid and for reminders set in the past
        assertParseFailure(parser, "1" + INVALID_REMINDER, Reminder.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_REMINDER_PAST, Reminder.MESSAGE_CONSTRAINTS_PAST);

        // recurring
        assertParseFailure(parser, "1" + INVALID_RECURRING, Recurring.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput =
                targetIndex.getOneBased()
                        + PRIORITY_DESC_TASK2
                        + TAG_DESC_MA1521
                        + DESCRIPTION_DESC_TASK1
                        + NAME_DESC_TASK1
                        + TAG_DESC_HELP
                        + REMINDER
                        + RECURRING;

        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder()
                        .withName(VALID_NAME_TASK1)
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withDescription(VALID_DESCRIPTION_TASK1)
                        .withTags(VALID_TAG_MA1521, VALID_TAG_HELP)
                        .withReminder(VALID_REMINDER)
                        .withRecurring(VALID_RECURRING, LocalDateTime.now())
                        .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + PRIORITY_DESC_TASK2;

        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder().withPriority(VALID_PRIORITY_TASK2).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + NAME_DESC_TASK1;
        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder().withName(VALID_NAME_TASK1).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // priority
        userInput = targetIndex.getOneBased() + PRIORITY_DESC_TASK1;
        descriptor = new EditTaskDescriptorBuilder().withPriority(VALID_PRIORITY_TASK1).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_TASK1;
        descriptor =
                new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_TASK1).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_HELP;
        descriptor = new EditTaskDescriptorBuilder().withTags(VALID_TAG_HELP).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // reminder
        userInput = targetIndex.getOneBased() + REMINDER;
        descriptor = new EditTaskDescriptorBuilder().withReminder(VALID_REMINDER).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // recurring
        userInput = targetIndex.getOneBased() + RECURRING;
        descriptor =
                new EditTaskDescriptorBuilder()
                        .withRecurring(VALID_RECURRING, LocalDateTime.now())
                        .build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput =
                targetIndex.getOneBased()
                        + PRIORITY_DESC_TASK1
                        + DESCRIPTION_DESC_TASK1
                        + TAG_DESC_HELP
                        + PRIORITY_DESC_TASK1
                        + DESCRIPTION_DESC_TASK1
                        + TAG_DESC_HELP
                        + PRIORITY_DESC_TASK2
                        + DESCRIPTION_DESC_TASK2
                        + TAG_DESC_MA1521;

        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder()
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .withTags(VALID_TAG_HELP, VALID_TAG_MA1521)
                        .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + INVALID_PRIORITY_DESC + PRIORITY_DESC_TASK2;
        EditTaskDescriptor descriptor =
                new EditTaskDescriptorBuilder().withPriority(VALID_PRIORITY_TASK2).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput =
                targetIndex.getOneBased()
                        + INVALID_PRIORITY_DESC
                        + DESCRIPTION_DESC_TASK2
                        + PRIORITY_DESC_TASK2;
        descriptor =
                new EditTaskDescriptorBuilder()
                        .withPriority(VALID_PRIORITY_TASK2)
                        .withDescription(VALID_DESCRIPTION_TASK2)
                        .build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
