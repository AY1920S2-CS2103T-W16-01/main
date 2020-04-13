package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.PomDurationData;
import seedu.address.model.dayData.TasksDoneData;

/** Jackson-friendly version of {@link DayData}. */
class JsonAdaptedDayData {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "DayData's %s field is missing!";

    private final String date;
    private final String pomDurationData;
    private final String tasksDoneData;

    /** Constructs a {@code JsonAdaptedDayData} with the given task details. */
    @JsonCreator
    public JsonAdaptedDayData(
            @JsonProperty("date") String date,
            @JsonProperty("pomDurationData") String pomDurationData,
            @JsonProperty("tasksDoneData") String tasksDoneData) {
        this.date = date;
        this.pomDurationData = pomDurationData;
        this.tasksDoneData = tasksDoneData;
    }

    /** Converts a given {@code Task} into this class for Jackson use. */
    public JsonAdaptedDayData(DayData source) {
        date = source.getDate().toString();
        pomDurationData = source.getPomDurationData().toString();
        tasksDoneData = source.getTasksDoneData().toString();
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code DayData} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *     dayData.
     */
    public DayData toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }

        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        final Date modelDate = new Date(date);

        if (pomDurationData == null) {
            throw new IllegalValueException(
                    String.format(
                            MISSING_FIELD_MESSAGE_FORMAT, PomDurationData.class.getSimpleName()));
        }
        if (!PomDurationData.isValidPomDurationData(pomDurationData)) {
            throw new IllegalValueException(PomDurationData.MESSAGE_CONSTRAINTS);
        }
        final PomDurationData modelPomDurationData = new PomDurationData(pomDurationData);

        if (tasksDoneData == null) {
            throw new IllegalValueException(
                    String.format(
                            MISSING_FIELD_MESSAGE_FORMAT, TasksDoneData.class.getSimpleName()));
        }
        if (!TasksDoneData.isValidTasksDoneData(tasksDoneData)) {
            throw new IllegalValueException(TasksDoneData.MESSAGE_CONSTRAINTS);
        }
        final TasksDoneData modelTasksDoneData = new TasksDoneData(tasksDoneData);

        return new DayData(modelDate, modelPomDurationData, modelTasksDoneData);
    }
}
