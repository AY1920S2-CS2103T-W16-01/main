package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;

/** Jackson-friendly version of {@link Task}. */
class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    private final String name;
    private final String priority;
    private final String description;
    private final String done;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final String reminder;
    private final String recurring;

    // /** Constructs a {@code JsonAdaptedTask} with the given details. */
    // @JsonCreator
    // public JsonAdaptedTask(
    //         @JsonProperty("name") String name,
    //         @JsonProperty("priority") String priority,
    //         @JsonProperty("description") String description,
    //         @JsonProperty("done") String done,
    //         @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
    //         @JsonProperty("reminder") String reminder) {
    //     this.name = name;
    //     this.priority = priority;
    //     this.description = description;
    //     this.done = done;
    //     if (tagged != null) {
    //         this.tagged.addAll(tagged);
    //     }
    //     this.reminder = reminder;
    // }

    /** Constructs a {@code JsonAdaptedTask} with the given details. */
    @JsonCreator
    public JsonAdaptedTask(
            @JsonProperty("name") String name,
            @JsonProperty("priority") String priority,
            @JsonProperty("description") String description,
            @JsonProperty("done") String done,
            @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
            @JsonProperty("reminder") String reminder,
            @JsonProperty("recurring") String recurring) {
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.done = done;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        this.reminder = reminder;
        this.recurring = recurring;
    }

    /** Converts a given {@code Task} into this class for Jackson use. */
    public JsonAdaptedTask(Task source) {
        name = source.getName().fullName;
        priority = source.getPriority().value;
        description = source.getDescription().value;
        done = source.getDone().toString();
        tagged.addAll(
                source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList()));
        reminder =
                (source.getOptionalReminder().isPresent())
                        ? source.getOptionalReminder().get().toString()
                        : "";
        recurring =
                (source.getOptionalRecurring().isPresent())
                        ? source.getOptionalRecurring().get().toString()
                        : "";
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *     task.
     */
    public Task toModelType() throws IllegalValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (priority == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Priority.class.getSimpleName()));
        }
        if (!Priority.isValidPriority(priority)) {
            throw new IllegalValueException(Priority.MESSAGE_CONSTRAINTS);
        }
        final Priority modelPriority = new Priority(priority);

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        if (!Done.isValidDone(done)) {
            throw new IllegalValueException(Done.MESSAGE_CONSTRAINTS);
        }
        final Done modelDone = new Done(done);

        final Set<Tag> modelTags = new HashSet<>(taskTags);

        Optional<Reminder> optReminder = Optional.empty();
        if (reminder == null || reminder.equals("")) {
            optReminder = Optional.empty();
        } else {
            optReminder = Optional.of(new Reminder(reminder));
        }

        Optional<Recurring> optRecurring = Optional.empty();
        if (recurring == null || recurring.equals("")) {
            optRecurring = Optional.empty();
        } else {
            optRecurring = Optional.of(new Recurring(recurring));
        }

        return new Task(
                modelName,
                modelPriority,
                modelDescription,
                modelDone,
                modelTags,
                optReminder,
                optRecurring);
    }
}
