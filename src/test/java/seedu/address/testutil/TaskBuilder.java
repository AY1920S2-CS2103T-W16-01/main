package seedu.address.testutil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.InvalidReminderException;
import seedu.address.model.util.SampleDataUtil;

/** A utility class to help with building Task objects. */
public class TaskBuilder {

    public static final String DEFAULT_NAME = "Default homework";
    public static final String DEFAULT_PRIORITY = "1";
    public static final String DEFAULT_DESCRIPTION = "Default Pages 1 and 2";
    public static final String DEFAULT_DONE = "N";

    private Name name;
    private Priority priority;
    private Description description;
    private Done done;
    private Optional<Reminder> reminder = Optional.empty();
    private Set<Tag> tags;
    private Optional<Recurring> recurring = Optional.empty();

    public TaskBuilder() {
        name = new Name(DEFAULT_NAME);
        priority = new Priority(DEFAULT_PRIORITY);
        description = new Description(DEFAULT_DESCRIPTION);
        done = new Done(DEFAULT_DONE);
        tags = new HashSet<>();
    }

    /** Initializes the TaskBuilder with the data of {@code taskToCopy}. */
    public TaskBuilder(Task taskToCopy) {
        name = taskToCopy.getName();
        priority = taskToCopy.getPriority();
        description = taskToCopy.getDescription();
        done = taskToCopy.getDone();
        tags = new HashSet<>(taskToCopy.getTags());
        reminder = taskToCopy.getOptionalReminder();
        recurring = taskToCopy.getOptionalRecurring();
    }

    /** Sets the {@code Name} of the {@code Task} that we are building. */
    public TaskBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Task} that we are
     * building.
     */
    public TaskBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /** Sets the {@code Address} of the {@code Task} that we are building. */
    public TaskBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /** Sets the {@code Priority} of the {@code Task} that we are building. */
    public TaskBuilder withPriority(String priority) {
        this.priority = new Priority(priority);
        return this;
    }

    public TaskBuilder withDone() {
        this.done = new Done(Done.DONE);
        return this;
    }

    public TaskBuilder withReminder(LocalDateTime dateTime) {
        try {
            this.reminder = Optional.of(new Reminder(dateTime));
        } catch (InvalidReminderException e) {
            this.reminder = Optional.empty();
        }
        return this;
    }

    public TaskBuilder withReminder(String dateTime) {
        try {
            this.reminder = Optional.of(new Reminder(dateTime));
        } catch (InvalidReminderException e) {
            this.reminder = Optional.empty();
        }
        return this;
    }

    /** Sets reminder as optional.empty for task builder. */
    public TaskBuilder withReminder() {
        this.reminder = Optional.empty();
        return this;
    }

    public TaskBuilder withRecurring(String recurrStringStorage) {
        try {
            this.recurring = Optional.of(new Recurring(recurrStringStorage));
        } catch (ParseException e) {
            this.recurring = Optional.empty();
        }
        return this;
    }

    public TaskBuilder withRecurring(String recurrString, LocalDateTime referenceDateTime) {
        try {
            this.recurring = Optional.of(new Recurring(recurrString, referenceDateTime));
        } catch (ParseException e) {
            this.recurring = Optional.empty();
        }
        return this;
    }

    public Task build() {
        return new Task(name, priority, description, done, tags, reminder, recurring);
    }
}
