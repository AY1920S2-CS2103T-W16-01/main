package seedu.address.testutil;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidReminderException;

/** A utility class to help with building EditTaskDescriptor objects. */
public class EditTaskDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditTaskDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditTaskDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /** Returns an {@code EditTaskDescriptor} with fields containing {@code task}'s details */
    public EditTaskDescriptorBuilder(Task task) {
        descriptor = new EditTaskDescriptor();
        descriptor.setName(task.getName());
        descriptor.setPriority(task.getPriority());
        descriptor.setDescription(task.getDescription());
        descriptor.setTags(task.getTags());
    }

    /** Sets the {@code Name} of the {@code EditTaskDescriptor} that we are building. */
    public EditTaskDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /** Sets the {@code Priority} of the {@code EditTaskDescriptor} that we are building. */
    public EditTaskDescriptorBuilder withPriority(String priority) {
        descriptor.setPriority(new Priority(priority));
        return this;
    }

    /** Sets the {@code Address} of the {@code EditTaskDescriptor} that we are building. */
    public EditTaskDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new Description(description));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditTaskDescriptor}
     * that we are building.
     */
    public EditTaskDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditTaskDescriptorBuilder withReminder(String reminder) {
        try {
            descriptor.setReminder(new Reminder(reminder));
        } catch (InvalidReminderException e) {
            return this;
        }
        return this;
    }

    public EditTaskDescriptorBuilder withRecurring(String recurrStringStorage) {
        try {
            descriptor.setRecurring(new Recurring(recurrStringStorage));
        } catch (ParseException e) {
            return this;
        }
        return this;
    }

    public EditTaskDescriptorBuilder withRecurring(
            String recurrString, LocalDateTime referenceDateTime) {
        try {
            descriptor.setRecurring(new Recurring(recurrString, referenceDateTime));
        } catch (ParseException e) {
            return this;
        }
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
