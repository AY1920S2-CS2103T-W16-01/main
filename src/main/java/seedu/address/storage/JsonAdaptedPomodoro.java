package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Pomodoro;
import seedu.address.model.ReadOnlyPomodoro;

@JsonRootName(value = "pomodoro")
class JsonAdaptedPomodoro {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pomodoro's %s field is missing!";

    private String defaultTime;
    private String restTime;
    private String timeLeft;
    private JsonAdaptedTask runningTask;

    /** Constructs a {@code JsonAdaptedTask} with the given task details. */
    @JsonCreator
    public JsonAdaptedPomodoro(
            @JsonProperty("defaultTime") String defaultTime,
            @JsonProperty("restTime") String restTime,
            @JsonProperty("timeLeft") String timeLeft,
            @JsonProperty("runningTask") JsonAdaptedTask runningTask) {
        this.defaultTime = defaultTime;
        this.restTime = restTime;
        this.timeLeft = timeLeft;
        this.runningTask = runningTask;
    }

    /** Converts a given {@code Task} into this class for Jackson use. */
    public JsonAdaptedPomodoro(ReadOnlyPomodoro source) {
        this.defaultTime = source.getDefaultTime();
        this.restTime = source.getRestTime();
        this.timeLeft = source.getTimeLeft();
        if (source.getRunningTask() == null) {
            this.runningTask = null;
        } else {
            this.runningTask = new JsonAdaptedTask(source.getRunningTask());
        }
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *     task.
     */
    public ReadOnlyPomodoro toModelType() throws IllegalValueException {
        if (runningTask == null) return new Pomodoro(defaultTime, restTime, timeLeft, null);
        return new Pomodoro(defaultTime, restTime, timeLeft, runningTask.toModelType());
    }
}
