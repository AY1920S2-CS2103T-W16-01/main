package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.TaskBuilder;
import seedu.address.ui.MainWindow;

/**
 * Represents a Task in the task list. Guarantees: details are present and not null, field values
 * are validated, immutable.
 */
public class Task {

    // Identity fields
    private final Name name;
    private final Priority priority;

    // Data fields
    private final Description description;
    private final Done done;
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<Reminder> optionalReminder;
    private final Optional<Recurring> optionalRecurring;

    /** Every field must be present and not null. */
    public Task(
            Name name,
            Priority priority,
            Description description,
            Done done,
            Set<Tag> tags,
            Optional<Reminder> optionalReminder,
            Optional<Recurring> optionalRecurring) {
        requireAllNonNull(name, priority, description, tags);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = done;
        this.tags.addAll(tags);
        this.optionalReminder = optionalReminder;
        this.optionalRecurring = optionalRecurring;
        triggerReminderIfPresent();
    }

    /** With done and no reminder */
    public Task(Name name, Priority priority, Description description, Done done, Set<Tag> tags) {
        requireAllNonNull(name, priority, description, tags);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = done;
        this.tags.addAll(tags);
        this.optionalReminder = Optional.empty();
        this.optionalRecurring = Optional.empty();
    }

    /** With done, no reminder but with recurring */
    public Task(
            Name name,
            Priority priority,
            Description description,
            Done done,
            Set<Tag> tags,
            Optional<Recurring> optionalRecurring) {
        requireAllNonNull(name, priority, description, tags);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = done;
        this.tags.addAll(tags);
        this.optionalReminder = Optional.empty();
        this.optionalRecurring = optionalRecurring;
    }

    // without done provided
    public Task(
            Name name,
            Priority priority,
            Description description,
            Set<Tag> tags,
            Optional<Reminder> optionalReminder,
            Optional<Recurring> optionalRecurring) {
        requireAllNonNull(name, priority, description, tags);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = new Done();
        this.optionalReminder = optionalReminder;
        this.tags.addAll(tags);
        this.optionalRecurring = optionalRecurring;
        triggerReminderIfPresent();
    }

    // without Reminder or done provided
    public Task(Name name, Priority priority, Description description, Set<Tag> tags) {
        requireAllNonNull(name, priority, description, tags);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = new Done();
        this.optionalReminder = Optional.empty();
        this.optionalRecurring = Optional.empty();
        this.tags.addAll(tags);
    }

    /**
     * Gets time delay for first trigger of recurring behaviour.
     *
     * @return time delay in long.
     */
    public long getDelayToFirstTrigger() {
        return this.optionalRecurring.get().getDelayToFirstTrigger();
    }

    /**
     * Gets time delay for recurring based on the type in milliseconds.
     *
     * @return time delay in long.
     */
    public long getRecurPeriod() {
        return this.optionalRecurring.get().getPeriod();
    }

    /**
     * Gets the new version of the task after recurring behaviour, namely with done set to
     * unfinished and reminder updated based on time interval.
     *
     * @return updated version of task.
     */
    public Task getRecurredTask() {
        Recurring recurring = optionalRecurring.get();
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            LocalDateTime newDateTime = recurring.getUpdatedReminderTime(reminder);
            return new TaskBuilder(this).withDone(new Done()).withReminder(newDateTime).build();
        }
        return new TaskBuilder(this).withDone(new Done()).build();
    }

    /** Triggers reminder behaviour if there is a reminder present in the task. */
    public void triggerReminderIfPresent() {
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            if (!reminder.getHasFired()) {
                MainWindow.triggerReminder(reminder, name.toString(), description.toString());
            }
        }
    }

    public Name getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public Description getDescription() {
        return description;
    }

    public Done getDone() {
        return done;
    }

    /** Getter for the optional reminder. */
    public Optional<Reminder> getOptionalReminder() {
        return optionalReminder;
    }

    /** Getter for the optional recurring. */
    public Optional<Recurring> getOptionalRecurring() {
        return optionalRecurring;
    }

    public boolean hasTag(Tag t) {
        return tags.contains(t);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException} if
     * modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both tasks of the same name have at least one other identity field that is
     * the same. This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null && otherTask.getName().equals(getName());
    }

    /**
     * Returns true if both tasks have the same identity and data fields. This defines a stronger
     * notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getName().equals(getName())
                && otherTask.getPriority().equals(getPriority())
                && otherTask.getDescription().equals(getDescription())
                && otherTask.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, priority, description, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName())
                .append(",\nPriority: ")
                .append(getPriority())
                .append(",\nDescription: ")
                .append(getDescription())
                .append(",\nTags: ");
        getTags().forEach(builder::append);
        if (optionalReminder.isPresent()) {
            String reminderString = optionalReminder.get().displayReminder();
            builder.append(",\nReminder: ").append(reminderString);
        }
        if (optionalRecurring.isPresent()) {
            String recurrString = optionalRecurring.get().displayRecurring();
            builder.append(",\nRecurring: ").append(recurrString);
        }
        return builder.toString();
    }
}
